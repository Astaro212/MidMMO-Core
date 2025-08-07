package astaro.midmmo.core.attributes.stats;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.system.linux.Stat;

public class StatCalculator {

    private double value;


    StatCalculator(PlayerStatsManager stats){
        calculatePhysPower(stats);
        calculateBaseMana(stats);
        calculateBaseHealth(stats);
    }


    public void calculateBaseHealth(PlayerStatsManager stats) {
        this.value = stats.getStat("rec") * 2.0D;
        stats.setStat("health", value);
    }

    public void calculateBaseMana(PlayerStatsManager stats) {
        this.value = stats.getStat("wis") * 12.0D;
        stats.setStat("mana", value);
    }

    public void calculatePhysPower(PlayerStatsManager stats) {
        this.value = stats.getStat("str") * 0.08D;
        stats.setStat("physical_damage", value);
    }
}
