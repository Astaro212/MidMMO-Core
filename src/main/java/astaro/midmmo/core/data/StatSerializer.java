package astaro.midmmo.core.data;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import com.google.gson.Gson;

public class StatSerializer {

    private static final Gson gson = new Gson();

    public static String serialize(PlayerStatsManager manager) {
        PlayerStatsDTO playerStatsDTO = new PlayerStatsDTO(manager);
        return gson.toJson(playerStatsDTO);
    }

    public static PlayerStatsManager deserizalize(String json) {
        PlayerStatsDTO playerStats = gson.fromJson(json, PlayerStatsDTO.class);
        return playerStats.applyToPlayerStats(new PlayerStatsManager());
    }
}
