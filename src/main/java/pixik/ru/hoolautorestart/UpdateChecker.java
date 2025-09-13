package pixik.ru.hoolautorestart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class UpdateChecker {

    private final HoolAutoRestart plugin;
    private static final String GITHUB_API_URL = "https://api.github.com/repos/yourname/autorestart-plugin/releases/latest";
    private String latestVersion;

    public UpdateChecker(HoolAutoRestart plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdates() {
        try {
            URL url = new URL(GITHUB_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                latestVersion = jsonResponse.getString("tag_name");
                String currentVersion = plugin.getDescription().getVersion();

                if (isNewerVersion(latestVersion, currentVersion)) {
                    String message = String.format(
                            "Доступно обновление! Текущая версия: %s, Новая версия: %s",
                            currentVersion, latestVersion
                    );

                    plugin.getLogger().warning(message);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getOperators().stream().filter(OfflinePlayer::isOnline).forEach(op -> op.getPlayer().sendMessage(ChatColor.GOLD + "[AutoRestart] " +
                                ChatColor.YELLOW + message));
                    });
                } else {
                    plugin.getLogger().info("Обновлений не найдено. Версия актуальна: " + currentVersion);
                }
            } else {
                plugin.getLogger().warning("Не удалось проверить обновления. Код ответа: " + responseCode);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Ошибка при проверке обновлений", e);
        }
    }

    private boolean isNewerVersion(String latest, String current) {
        try {
            String[] latestParts = latest.split("\\.");
            String[] currentParts = current.split("\\.");

            for (int i = 0; i < Math.min(latestParts.length, currentParts.length); i++) {
                int latestNum = Integer.parseInt(latestParts[i]);
                int currentNum = Integer.parseInt(currentParts[i]);

                if (latestNum > currentNum) {
                    return true;
                } else if (latestNum < currentNum) {
                    return false;
                }
            }

            return latestParts.length > currentParts.length;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}