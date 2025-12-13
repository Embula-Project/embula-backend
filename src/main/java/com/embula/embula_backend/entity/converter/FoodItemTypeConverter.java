package com.embula.embula_backend.entity.converter;

import com.embula.embula_backend.entity.enums.FoodItemType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FoodItemTypeConverter implements AttributeConverter<FoodItemType, String> {

    @Override
    public String convertToDatabaseColumn(FoodItemType type) {
        if (type == null) {
            return null;
        }
        // Convert enum to database format
        // Non_Veg -> Non-Veg for database storage
        return type.name().replace("_", "-");
    }

    @Override
    public FoodItemType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        // Convert database format to enum
        // "Non-Veg" -> "Non_Veg", "Veg" -> "Veg"
        String enumValue = dbData.replace("-", "_");

        try {
            return FoodItemType.valueOf(enumValue);
        } catch (IllegalArgumentException e) {
            System.err.println("Unknown FoodItemType value: " + dbData + ", defaulting to Veg");
            return FoodItemType.Veg;
        }
    }
}

