package astaro.midmmo.core.attributes.stats;

import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber
public class BaseStats {


    @SubscribeEvent
    public static void checkHandAndStats(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        double currentDamage;

        PlayerData playerData = PlayerDataCache.get(player.getUUID());
        if (playerData == null) return;
        PlayerStatsManager stats = playerData.getPlayerChar();
        if (stats == null) return;

        if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            currentDamage = 1.0D;
        } else {
            currentDamage = getBaseWeaponDamage(player.getItemInHand(InteractionHand.MAIN_HAND));
        }
        double physPower = calculatePhysPower(stats, currentDamage);
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(stats.getStat("health"));
        player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(physPower);
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
            case TridentItem ignored -> 9.0f; // Урон трезубца
            default -> 1.0f;
        };
    }

    public static void calcStats(PlayerStatsManager stats) {
        calculateBaseMana(stats);
        calculateBaseHealth(stats);
        calculateHealthRegen(stats);
        calculateManaRegen(stats);
        calculateArmor(stats);
        calculateCritChanceAndDamage(stats);
        calculateMagicResist(stats);
        calculateMagicDamage(stats);
    }

    private static void calculateBaseHealth(PlayerStatsManager stats) {
        double value =  stats.getStat("rec") * 2.0D;
        stats.addStatModifier("health", value);
    }

    private static void calculateBaseMana(PlayerStatsManager stats) {
        double value = stats.getStat("wis") * 12.0D;
        stats.addStatModifier("mana", value);
    }

    private static double calculatePhysPower(PlayerStatsManager stats, double currentDamage) {
        double value = currentDamage + (stats.getStat("str") * 0.08D);
        stats.addStatModifier("physical_damage", value);
        return stats.getStat("physical_damage");
    }

    private static void calculateHealthRegen(PlayerStatsManager stats) {
        double value =  stats.getStat("str") * 0.1;
        stats.addStatModifier("regen", value);
    }

    private static void calculateManaRegen(PlayerStatsManager stats) {
        double value = stats.getStat("int") * 0.5;
        stats.addStatModifier("mana_regen", value);
    }

    private static void calculateMagicDamage(PlayerStatsManager stats){
        double value = stats.getStat("int") * 1.2;
        stats.addStatModifier("magic_damage", value);
    }

    private static void calculateArmor(PlayerStatsManager stats) {
        double value =  stats.getStat("rec") * 10;
        stats.addStatModifier("armor", value);
    }

    private static void calculateCritChanceAndDamage(PlayerStatsManager stats) {
        double luck = stats.getStat("luck");
        stats.addStatModifier("critical_damage", luck * 0.5);
        stats.addStatModifier("critical_chance", luck * 0.25);
    }

    private static void calculateMagicResist(PlayerStatsManager stats) {
        double value = stats.getStat("spirit") * 2;
        stats.addStatModifier("magic_resist", value);
    }

}

