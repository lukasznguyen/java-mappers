package modelmapper;

import modelmapper.models.Car;
import modelmapper.models.Customer;
import modelmapper.models.CustomerDTO;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public class ModelMapperMain {
    public static void main(String[] args) {
        ModelMapper mapper = new ModelMapper();

        TypeMap<Customer, CustomerDTO> customerToCustomerDtoPropMapper = mapper.createTypeMap(Customer.class, CustomerDTO.class);
        customerToCustomerDtoPropMapper.addMapping(Customer::getFirstName, CustomerDTO::setFName);
        customerToCustomerDtoPropMapper.addMapping(Customer::getLastName, CustomerDTO::setLName);
        customerToCustomerDtoPropMapper.addMappings(
                mapperinio -> mapperinio.skip(CustomerDTO::setId)
        );
        Converter<Car, String> carToCarShortInfoConverter = ctx -> {
            if (ctx.getSource() == null) {
                return null;
            } else {
                String sb = ctx.getSource().getBrand() +
                        " " +
                        ctx.getSource().getModel() +
                        " " +
                        ctx.getSource().getYear();
                return sb;
            }
        };
        customerToCustomerDtoPropMapper.addMappings(
                mapperinio -> mapperinio.using(carToCarShortInfoConverter).map(Customer::getCar, CustomerDTO::setCarShortInfo)
        );
        Condition<Customer, CustomerDTO> sourceNotEmpty = ctx -> ctx.getSource() != null;
        customerToCustomerDtoPropMapper.addMappings(
                mapperinio -> mapperinio.when(sourceNotEmpty).skip(Customer::getId, CustomerDTO::setId)
        );

        TypeMap<CustomerDTO, Customer> customerDtoToCustomerPropMapper = mapper.createTypeMap(CustomerDTO.class, Customer.class);
        customerDtoToCustomerPropMapper.addMapping(CustomerDTO::getFName, Customer::setFirstName);
        customerDtoToCustomerPropMapper.addMapping(CustomerDTO::getLName, Customer::setLastName);
        Converter<String, Car> carShortInfoToCarConverter = ctx -> {
            String carShortInfo = ctx.getSource();
            if (carShortInfo == null) {
                return null;
            } else {
                String[] carDetails = carShortInfo.split(" ");
                Car car = Car.builder()
                        .brand(carDetails[0])
                        .model(carDetails[1])
                        .year(Integer.parseInt(carDetails[2]))
                        .build();
                return car;
            }
        };
        customerDtoToCustomerPropMapper.addMappings(
                mapperinio -> mapperinio.using(carShortInfoToCarConverter).map(CustomerDTO::getCarShortInfo, Customer::setCar)
        );

        Customer customer = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .age(22)
                .isStudent(true)
                .car(Car.builder()
                        .brand("Toyota")
                        .model("Avensis")
                        .year(2013)
                        .build())
                .build();

        CustomerDTO customerDTO = mapper.map(customer, CustomerDTO.class);
        Customer customer2 = mapper.map(customerDTO, Customer.class);
        System.out.println(customer);
        System.out.println(customerDTO);
        System.out.println(customer2);


    }
}
