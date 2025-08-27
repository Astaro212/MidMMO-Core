package astaro.midmmo.core.data.cache;


import astaro.midmmo.core.data.PlayerData;
import com.google.gson.Gson;
import net.neoforged.fml.loading.FMLEnvironment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//Added for future
public class CacheBackup {

    private static final Gson gson = new Gson();
    private static final Path BACKUP_DIR = Path.of("backup/playerdata");
    private static final Map<UUID,Boolean> backup_progress = new ConcurrentHashMap<>();

    static{
        try{
            Files.createDirectories(BACKUP_DIR);
        } catch (IOException e) {
            Logger.getLogger(CacheBackup.class.getName()).log(Level.SEVERE, "Failed to create backup directory", e);
        }
    }

    private static boolean serverSide(){
        return FMLEnvironment.dist.isDedicatedServer();
    }

    public static void backupPlayerData(UUID uuid, PlayerData data){
        if(serverSide()) return;

        synchronized (backup_progress) {
            if (backup_progress.containsKey(uuid)) return;
            backup_progress.put(uuid, true);
        }

        try{
            Path backupFile = BACKUP_DIR.resolve(uuid.toString()+".json");
            String jsonData = gson.toJson(data);

            Files.writeString(backupFile, jsonData,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            Logger.getLogger(CacheBackup.class.getName()).log(Level.WARNING,
                    "Failed to backup data for player: " + uuid, e);
        } finally {
            synchronized (backup_progress) {
                backup_progress.remove(uuid);
            }
        }
    }

    public static PlayerData restorePlayerData(UUID playerId) {
        if (serverSide()) {
            return null;
        }

        try {
            Path backupFile = BACKUP_DIR.resolve(playerId.toString() + ".json");
            if (Files.exists(backupFile)) {
                String jsonData = Files.readString(backupFile);
                return gson.fromJson(jsonData, PlayerData.class);
            }
        } catch (IOException e) {
            Logger.getLogger(CacheBackup.class.getName()).log(Level.WARNING,
                    "Failed to restore data for player: " + playerId, e);
        }
        return null;
    }

    public static void removeBackup(UUID playerId) {
        if (serverSide()) {
            return;
        }

        try {
            Path backupFile = BACKUP_DIR.resolve(playerId.toString() + ".json");
            Files.deleteIfExists(backupFile);
        } catch (IOException e) {
            Logger.getLogger(CacheBackup.class.getName()).log(Level.WARNING,
                    "Failed to remove backup for player: " + playerId, e);
        }
    }


    public static void emergencyBackupAll() {
        if (serverSide()) {
            return;
        }

        int backedUp = 0;
        for (Map.Entry<UUID, PlayerData> entry : PlayerDataCache.getAllData().entrySet()) {
            if (backedUp++ > 1000) break; // ← Защита от бесконечных циклов
            backupPlayerData(entry.getKey(), entry.getValue());
        }
    }

}
