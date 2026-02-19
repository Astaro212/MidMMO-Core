package com.astaro.midmmo.server.database.enums;

public enum PlayerQueries {

    GET_FULL_PROFILE("""
            SELECT p.*, s.*, ss.*
            FROM players p
            LEFT JOIN player_stats s ON p.player_id = s.player_id
            LEFT JOIN player_secondary_stats ss ON p.player_id = ss.player_id
            WHERE p.player_id = ?
            """),
    INSERT_PLAYER("""
            WITH new_player AS(
                INSERT INTO players(player_id, username, race_id, class_id)
                VALUES (?,?,?,?)
                RETURNING *
            ),
            init_stats AS(
                INSERT INTO player_stats(player_id, strength, dexterity, intelligence, recovery, luck, wisdom, available_points)
                SELECT player_id,?,?,?,?,?,?,? FROM new_player
            ),
            init_ss AS (INSERT INTO player_secondary_stats(player_id, health, max_health, mana, max_mana, physical_attack, magic_attack,
            defence, resistance, critical_chance, critical_damage, evasion, accuracy)
            SELECT player_id, ?,?,?,?,?,?,?,?,?,?,?,?,? FROM new_player)
            SELECT * FROM new_player;
            """),
    UPDATE_PROFILE("""
            UPDATE players SET level = ?, experience = ?, gold = ?, vouchers = ?, crystals = ? WHERE player_id = ?
            """),
    UPDATE_ECONOMY("""
            UPDATE players SET gold = ?, vouchers = ?, crystals = ? WHERE player_id = ?
            """),
    UPDATE_GOLD("UPDATE players SET gold = ? WHERE player_id = ?"),
    UPDATE_VOUCHERS("UPDATE players SET vouchers = ? WHERE player_id = ?"),
    UPDATE_CRYSTALS("UPDATE players SET crystals = ? WHERE player_id = ?"),
    UPDATE_LEVEL("UPDATE players SET level = ? WHERE player_id = ?"),
    CHANGE_RACE("UPDATE players SET race_id = ? WHERE player_id = ?"),
    CHANGE_CLASS("UPDATE players SET class_id = ? WHERE player_id = ?"),
    UPDATE_LAST_LOGIN("UPDATE players SET last_login = CURRENT_TIMESTAMP WHERE player_id = ?" ),
    UPDATE_STATS("""
            UPDATE player_stats SET strength = ?, dexterity = ?, intelligence = ?,
            recovery = ?, luck = ?, wisdom = ?, available_points = ? WHERE player_id = ?
            """),
    UPDATE_SECONDARY_STATS("""
            UPDATE player_secondary_stats SET health = ?, max_health = ?, mana = ?, max_mana = ?,
            physical_attack = ?, magic_attack = ?,defence = ?,resistance = ?, critical_chance = ?,
            critical_damage = ?, evasion = ?, accuracy = ?, critical_resistance = ? WHERE player_id = ?
            """),
    JOIN_GUILD("UPDATE players SET guild_id = ? WHERE player_id = ?"),
    LEFT_GUILD("UPDATE players SET guild_id = NULL WHERE player_id = ?"),
    CHANGE_USERNAME("UPDATE players SET username = ? WHERE player_id = ?"),

    GET_EQUIPMENT("""
        SELECT e.slot_type, i.item_id, i.item_name, i.item_type as category, i.bonuses
        FROM player_equipment e
        JOIN player_inventory inv ON e.inventory_id = inv.inventory_id
        JOIN items i ON inv.item_id = i.item_id
        WHERE e.player_id = ?
    """);

    private final String sql;

    PlayerQueries(String sql) {
        this.sql = sql;
    }

    public String get() {
        return sql;
    }
    }
