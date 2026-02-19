package com.astaro.midmmo.api.interfaces;

import java.util.Map;

public interface FinalPlayerData {
    int getLevel();
    float getExp();
    String getPlayerRace();
    String getPlayerClass();
    Map<String, Double> getPlayerChar();
}
