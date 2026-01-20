package com.animaladoption.api.enums;

import com.animaladoption.api.exception.NotFoundException;
import lombok.Getter;

@Getter
public enum StatusAnimal {

    PUBLISHED(1L, "Publicado"),
    NOT_PUBLISHED(2L, "Não publicado"),
    REPUBLISHED(3L, "Republicado"),
    DESPUBLICADO(4L, "Despublicado, por algum motivo.");

    private final Long id;
    private final String name;

    StatusAnimal(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public static StatusAnimal fromId(Long id) {
        if (id == null) return null;

        for (StatusAnimal status : values()) {
            if (status.id.equals(id)) {
                return status;
            }
        }
        throw new NotFoundException("StatusAnimal id não encontrado: " + id);
    }

    public static Long toId(StatusAnimal status) {
        return status != null ? status.id : null;
    }

    public static StatusAnimal fromName(String name) {
        if (name == null) return null;

        for (StatusAnimal status : values()) {
            if (status.name.equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new NotFoundException("StatusAnimal nome não encontrado: " + name);
    }

    public static String toName(StatusAnimal status) {
        return status != null ? status.name : null;
    }
}
