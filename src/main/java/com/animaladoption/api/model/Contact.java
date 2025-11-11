package com.animaladoption.api.model;

import com.animaladoption.api.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(schema = "animal_adoption")
public class Contact extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;
    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;
}
