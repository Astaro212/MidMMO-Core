package com.astaro.midmmo.server.controllers;

import com.astaro.midmmo.api.data.StatType;
import com.astaro.midmmo.server.database.SQLWorker;
import com.astaro.midmmo.server.database.enums.PlayerQueries;
import com.astaro.midmmo.server.items.ItemRecord;
import com.astaro.midmmo.server.managers.PlayerStatsManager;
import com.astaro.midmmo.server.player.PlayerProfile;

import java.util.List;
import java.util.Map;

public class InventoryController {

    public void loadEquipment(PlayerProfile profile) {
        SQLWorker worker = new SQLWorker();
        worker.queryList(PlayerQueries.GET_EQUIPMENT.get(), ItemRecord.class, profile.getUUID())
                .thenAcceptAsync(itemList -> {
                    PlayerStatsManager manager = profile.getStatsManager();

                    profile.getEquipment().clear();
                    manager.getBonusStats().clear();


                    for(ItemRecord item : itemList){
                        Map<StatType, Double> bonuses = item.bonuses();

                        bonuses.forEach(manager::addBonus);

                        profile.getEquipment().put(item.slotType(), bonuses);
                    }
                    manager.updateFinalStats();

                    this.syncEquipment(profile, itemList);
                }).exceptionally(
                        ex -> {
                            ex.printStackTrace();
                            return null;
                        }
                );
    }

    public void syncEquipment(PlayerProfile profile, List<ItemRecord> itemList){

    }
}
