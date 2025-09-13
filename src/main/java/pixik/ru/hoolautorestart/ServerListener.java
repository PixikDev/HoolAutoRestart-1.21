package pixik.ru.hoolautorestart;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

public class ServerListener implements Listener {

    private final HoolAutoRestart plugin;

    public ServerListener(HoolAutoRestart plugin) {
        this.plugin = plugin;
    }

    public ServerListener(HoolAutoRestart autoRestartPlugin, HoolAutoRestart plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String command = event.getCommand().toLowerCase();

        // Перехват команд остановки сервера
        if (command.equals("stop") || command.equals("restart") ||
                command.equals("reload") || command.startsWith("stop ") ||
                command.startsWith("restart ")) {

            event.setCancelled(true);
            plugin.getLogger().info("Обнаружена команда остановки, планируем перезагрузку...");

            // Планируем перезагрузку через 5 секунд
            plugin.getRestartManager().scheduleRestart(100L); // 5 секунд = 100 тиков
        }
    }
}