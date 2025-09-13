package pixik.ru.hoolautorestart;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class RestartManager {

    private final HoolAutoRestart plugin;
    private boolean restartScheduled = false;

    public RestartManager(HoolAutoRestart plugin) {
        this.plugin = plugin;
    }

    public void scheduleRestart(long delayTicks) {
        if (restartScheduled) {
            return;
        }

        restartScheduled = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                performRestart();
            }
        }.runTaskLater(plugin, delayTicks);

        plugin.getLogger().info("Перезагрузка сервера запланирована");
    }

    public void performRestart() {
        plugin.getLogger().info("Запуск процесса перезагрузки сервера...");

        Bukkit.getOnlinePlayers().forEach(player ->
                player.kickPlayer("§cСервер перезагружается..."));

        Bukkit.savePlayers();
        Bukkit.getWorlds().forEach(world -> world.save());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Thread.sleep(3000);

                createRestartScript();

                Bukkit.shutdown();

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Ошибка при перезагрузке сервера", e);
            }
        });
    }

    private void createRestartScript() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        Path scriptPath;
        String scriptContent;

        if (os.contains("win")) {
            scriptPath = Paths.get("restart.bat");
            scriptContent = "@echo off\n"
                    + "echo Перезапуск сервера...\n"
                    + "timeout /t 5 /nobreak\n"
                    + "java -jar spigot-1.21.jar nogui\n"
                    + "pause";
        } else {
            scriptPath = Paths.get("restart.sh");
            scriptContent = "#!/bin/bash\n"
                    + "echo \"Перезапуск сервера...\"\n"
                    + "sleep 5\n"
                    + "java -jar spigot-1.21.jar nogui";
        }

        Files.write(scriptPath, scriptContent.getBytes());

        if (!os.contains("win")) {
            Runtime.getRuntime().exec("chmod +x " + scriptPath.toString());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (os.contains("win")) {
                    Runtime.getRuntime().exec("cmd /c start " + scriptPath.toString());
                } else {
                    Runtime.getRuntime().exec("./" + scriptPath.toString());
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Не удалось запустить скрипт перезагрузки", e);
            }
        }));
    }

    public boolean isRestartScheduled() {
        return restartScheduled;
    }
}