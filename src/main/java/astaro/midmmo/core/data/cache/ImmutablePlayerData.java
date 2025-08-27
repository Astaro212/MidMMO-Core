package astaro.midmmo.core.data.cache;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.PlayerData;

public class ImmutablePlayerData implements Cloneable {
    private PlayerData original;


    public ImmutablePlayerData(PlayerData original) {
        if (original == null) {
            throw new NullPointerException("PlayerData cannot be null");
        }
        this.original = original;
    }

    public PlayerData getOriginal() {
        throw new UnsupportedOperationException("Immutable data cannot by modified");
    }

    public int getLevel() {
        return original.getPlayerLvl();
    }

    public float getExp() {
        return original.getPlayerExp();
    }

    public PlayerStatsManager getPlayerChar() {
        return original.getPlayerChar();
    }

    public String getPlayerRace() {
        return original.getPlayerRace();
    }

    public String getPlayerClass() {
        return original.getPlayerClass();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return this.original = (PlayerData) super.clone();
    }

}
