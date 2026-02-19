package com.astaro.midmmo.server.database.mappers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.RecordComponent;
import java.sql.ResultSet;
import java.util.Arrays;

public class GenericMapper {

    private static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .create();

    public static <T> T map(ResultSet rs, Class<T> clazz) throws Exception {
        if (clazz.isRecord()) {
            var components = clazz.getRecordComponents();
            Object[] values = new Object[components.length];

            for (int i = 0; i < components.length; i++) {
                String name = toSnakeCase(components[i].getName());
                Object val = rs.getObject(name);

                if (val instanceof String str && (str.startsWith("{") || str.startsWith("["))) {
                    values[i] = GSON.fromJson(str, components[i].getGenericType());
                } else {
                    values[i] = val;
                }
            }
            return clazz.getConstructor(getTypes(components)).newInstance(values);
        }
        return null;
    }

    private static String toSnakeCase(String name) {
        return name.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    private static Class<?>[] getTypes(RecordComponent[] components) {
        return Arrays.stream(components)
                .map(RecordComponent::getType)
                .toArray(Class<?>[]::new);
    }
}
