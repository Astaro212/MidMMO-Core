package astaro.midmmo.core.attributes.stats;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@EventBusSubscriber
public class BaseStats {

    private double value;
    private static double currentDamage;

    @SubscribeEvent
    public static void checkHandAndStats(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        PlayerStatsManager stats = new PlayerStatsManager(player);
        if(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            currentDamage = 1.0D;
        } else {
            currentDamage = getBaseWeaponDamage(player.getItemInHand(InteractionHand.MAIN_HAND));
        }

        Logger.getLogger("DAMAGE").log(Level.INFO, "Weapon damage = " + currentDamage);
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(stats.getStat("health"));
        player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(stats.getStat("physical_damage"));
        player.getAttribute(Attributes.ARMOR).setBaseValue(stats.getStat("armor"));
        player.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.0D);

        if (player.getHealth() != player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    private static float getBaseWeaponDamage(ItemStack itemStack) {

        Item item = itemStack.getItem();
        // Базовый урон для ванильных предметов
        return switch (item) {
            case SwordItem sword -> sword.getDamage(itemStack) + 1.0f; // +1 потому что базовый урон мечей на 1 меньше
            case AxeItem axe -> axe.getDamage(itemStack) + 1.0f;
            case TridentItem trident -> 9.0f; // Урон трезубца
            default -> 1.0f;
        };
    }

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
        this.value = (currentDamage + stats.getStat("str")) * 0.08D;
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

