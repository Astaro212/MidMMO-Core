package astaro.midmmo.core.api.stats;

import astaro.midmmo.core.stats.StatsRegistry;

public interface StatsAPI {

    public void getStats();
    public void addStat(StatsRegistry registry);
    public void removeStat(StatsRegistry registry);

}
