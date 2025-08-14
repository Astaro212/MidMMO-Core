package astaro.midmmo.core.attributes.stats;

public class BaseStats {

    private double value;

    BaseStats(PlayerStatsManager stats) {
        calculatePhysPower(stats);
        calculateBaseMana(stats);
        calculateBaseHealth(stats);
        calculateHealthRegen(stats);
        calculateManaRegen(stats);
        calculateArmor(stats);
        calculateCritChanceAndDamage(stats);
        calculateMagicResist(stats);
    }

    private void calculateBaseHealth(PlayerStatsManager stats) {
        this.value = stats.getStat("rec") * 2.0D;
        stats.setStat("health", value);
    }

    private void calculateBaseMana(PlayerStatsManager stats) {
        this.value = stats.getStat("wis") * 12.0D;
        stats.setStat("mana", value);
    }

    private void calculatePhysPower(PlayerStatsManager stats) {
        this.value = stats.getStat("str") * 0.08D;
        stats.setStat("physical_damage", value);
    }

    private void calculateHealthRegen(PlayerStatsManager stats) {
        this.value = stats.getStat("regen") + stats.getStat("str") * 0.1;
        stats.setStat("regen", value);
    }

    private void calculateManaRegen(PlayerStatsManager stats) {
        this.value = stats.getStat("mana_regen") + stats.getStat("int") * 0.5;
        stats.setStat("mana_regen", value);
    }

    private void calculateArmor(PlayerStatsManager stats) {
        this.value = stats.getStat("armor") + stats.getStat("rec") * 10;
        stats.setStat("armor", value);
    }

    private void calculateCritChanceAndDamage(PlayerStatsManager stats) {
        double luck = stats.getStat("luck");
        stats.setStat("critical_damage", stats.getStat("critical_damage") + luck * 0.5);
        stats.setStat("critical_chance", stats.getStat("critical_chance") + luck * 0.25);
    }

    private void calculateMagicResist(PlayerStatsManager stats) {
        this.value = stats.getStat("magic_resist") + stats.getStat("spirit") * 12;
        stats.setStat("magic_resist", value);
    }

}
