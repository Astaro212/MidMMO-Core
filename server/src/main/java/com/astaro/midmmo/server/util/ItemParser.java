package com.astaro.midmmo.server.util;

import com.astaro.midmmo.api.data.StatType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.EnumMap;
import java.util.Map;

public class ItemParser {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeReference<EnumMap<StatType, Double>> STAT_MAP_TYPE =
            new TypeReference<>() {};

    public static Map<StatType, Double> parseBonuses(String jsonb) {
        if (jsonb == null || jsonb.isEmpty() || jsonb.equals("{}")) {
            return new EnumMap<>(StatType.class);
        }
        try {
            return mapper.readValue(jsonb, STAT_MAP_TYPE);
        } catch (Exception e) {
            return new EnumMap<>(StatType.class);
        }
    }
}
