package astaro.midmmo.api.stats;


import java.util.Map;

public interface StatsAPI {

    Map<String, Double> getStats();

    // Получить конкретный стат
    Double getStat(String stat);

    // Изменить конкретный стат
    void setStat(String stat, double value);

    // Добавить к стату значение
    void addStat(String stat, double value);

    //Убрать значение от стата (например спал эффект баффа)
    void removeStat(String stat, double value);
}
