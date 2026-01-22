package com.animaladoption.api.dto.cat;

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
public class CatFilterDTO {
    private String name;
    private String breed;
    private Integer age;
    private String valueContact;
    private String nameContact;
}
