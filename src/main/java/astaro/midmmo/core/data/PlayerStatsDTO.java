package astaro.midmmo.core.data;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerStatsDTO {

    private final String schemaVersion = "1.0";
    private final Map<String, Double> stats ;

    //Validate and show that we are creating json for db
    @JsonCreator
    public PlayerStatsDTO(PlayerStatsManager manager) {
        this.stats = new ConcurrentHashMap<>(manager.getStats());
    }

    //Apply to player
    public PlayerStatsManager applyToPlayerStats(PlayerStatsManager manager) {
        if(stats != null){
            manager.setBaseStats(stats);
        }
        return manager;
    }

    //Added validation for further
    public void validateStats(){
        if(stats.containsKey(null) || stats.containsValue(null)){
            throw new IllegalArgumentException("Stats must contain values!");
        }
    }
}
