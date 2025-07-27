package astaro.midmmo.core.attributes.stats;

public class StatCalculator {

    private double value;

    public void calculateBaseHealth(PlayerStatsManager stats){
        this.value = stats.getStat("rec") * 2.0D;
        stats.setStat("health", value);
    }

    public void calculateBaseMana(PlayerStatsManager stats){
        this.value = stats.getStat("wis")* 12.0D;
        stats.setStat("mana", value);
    }
}
