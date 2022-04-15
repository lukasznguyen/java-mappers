package modelmapper.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private String brand;
    private String model;
    private int year;
}
