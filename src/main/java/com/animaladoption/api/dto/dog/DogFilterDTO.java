package com.animaladoption.api.dto.dog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogFilterDTO {
    private String name;
    private String breed;
    private Integer age;
}
