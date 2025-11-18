package com.animaladoption.api.model.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UUIDListConverter implements AttributeConverter<List<UUID>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<UUID> uuids) {
        if (uuids == null || uuids.isEmpty()) return "";
        return uuids.stream().map(UUID::toString).collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<UUID> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return new ArrayList<>();
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }
}
