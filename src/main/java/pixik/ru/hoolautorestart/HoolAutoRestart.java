package pixik.ru.hoolautorestart;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Level;

public class HoolAutoRestart extends JavaPlugin {

    private RestartManager restartManager;
    private UpdateChecker updateChecker;
    private BukkitTask updateCheckTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.restartManager = new RestartManager(this);
        this.updateChecker = new UpdateChecker(this);

        getServer().getPluginManager().registerEvents(new ServerListener(this), this);

        startUpdateChecker();

        getLogger().info("AutoRestartPlugin успешно запущен!");
    }

    @Override
    public void onDisable() {
        if (updateCheckTask != null) {
            updateCheckTask.cancel();
        }

        getLogger().info("AutoRestartPlugin отключен");
    }

    private void startUpdateChecker() {
        updateCheckTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                updateChecker.checkForUpdates();
            } catch (Exception e) {
                getLogger().log(Level.WARNING, "Ошибка при проверке обновлений", e);
            }
        }, 0L, 36000L);
    }

    public RestartManager getRestartManager() {
        return restartManager;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }
}