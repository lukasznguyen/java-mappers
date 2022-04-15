package modelmapper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String fName;
    private String lName;
    private boolean isStudent;
    private int age;
    private String carShortInfo;
}
