package rip.snicon.gandalf;

import com.github.sniconmc.momentum.MomentumMain;
import com.github.sniconmc.momentum.config.MomentumConfig;
import com.github.sniconmc.momentum.data.MomentumConfigHolder;
import com.github.sniconmc.momentum.data.MomentumHologramHolder;
import com.github.sniconmc.momentum.utils.HologramUtils;
import com.github.sniconmc.momentum.utils.LoadMomentum;
import com.github.sniconmc.oblivion.config.OblivionConfig;
import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.github.sniconmc.utils.placeholder.PlaceholderReplacer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.entity.Player;
import rip.snicon.gandalf.config.GandalfConfig;
import rip.snicon.gandalf.utils.TeamUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GandalfManager {

    private static Gson gson = new Gson();

    private static final File dataFolder = new File("resources/gandalf");

    private static Map<String, String> dataFileJSONData;


    public GandalfManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        dataFileJSONData = new LoadMomentum().load(dataFolder);

    }



    public static void initiateGandalf(Player player) {

        for (String fileName : dataFileJSONData.keySet()) {

            try {
                GandalfConfig config = gson.fromJson(dataFileJSONData.get(fileName), GandalfConfig.class);

                Map<String, String> placeholderMap = new HashMap<>();
                placeholderMap.put("username", "");
                placeholderMap.put("player_rank", config.getRankStyle().getFirst());

                PlaceholderManager.addPlaceholdersToPlayer(player, placeholderMap);

                TeamUtils.createTeam(player, config);

            } catch (JsonSyntaxException | JsonIOException e) {
                MomentumMain.logger.error("Error parsing JSON in: {}", fileName);
            } catch (Exception e) {
                MomentumMain.logger.error("Unexpected error in: {}, {}", fileName, e.fillInStackTrace());
            }

        }
    }
}
