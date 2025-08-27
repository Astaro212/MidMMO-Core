package astaro.midmmo.core.data.cache;

import astaro.midmmo.api.FinalPlayerData;
import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.PlayerData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ImmutablePlayerData implements FinalPlayerData {
    private final int level;
    private final float exp;
    private final String playerRace;
    private final String playerClass;
    private final Map<String, Double> stats;


    public ImmutablePlayerData(PlayerData original) {
        if (original == null) {
            throw new NullPointerException("PlayerData cannot be null");
        }
        this.level = original.getPlayerLvl();
        this.exp = original.getPlayerExp();
        this.playerRace = original.getPlayerRace();
        this.playerClass = original.getPlayerClass();

        // Защитная копия Map!
        this.stats = Collections.unmodifiableMap(new HashMap<>(original.getPlayerChar().getStats()));
    }

    public PlayerData getOriginal() {
        throw new UnsupportedOperationException("Immutable data cannot by modified");
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public float getExp() {
        return exp;
    }

    @Override
    public String getPlayerRace() {
        return playerRace;
    }

    @Override
    public String getPlayerClass() {
        return playerClass;
    }

    @Override
    public Map<String, Double> getPlayerChar() {
        return stats;
    }


}
