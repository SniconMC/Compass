package rip.snicon.gandalf;

import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.snicon.Main;
import rip.snicon.gandalf.config.GandalfConfig;
import rip.snicon.gandalf.config.GandalfProfession;
import rip.snicon.gandalf.config.GandalfProfile;
import rip.snicon.gandalf.utils.LoadGandalf;
import rip.snicon.gandalf.utils.TabUtils;
import rip.snicon.gandalf.utils.TeamUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static rip.snicon.gandalf.utils.SaveProfile.saveProfileToFile;

public class GandalfManager {

    private static Gson gson = new Gson();

    private static final File dataFolderRanks = new File("resources/gandalf/ranks");
    private static final File dataFolderProfession = new File("resources/gandalf/professions");
    private static final File dataProfileFolder = new File("resources/profiles");

    private static Map<String, String> dataRanksFileJSONData;

    private static Map<String, String> dataProfessionFileJSONData;

    private static Map<String, String> profileDataJSONData;

    public GandalfManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();

        dataRanksFileJSONData = new LoadGandalf().load(dataFolderRanks);
        dataProfessionFileJSONData = new LoadGandalf().load(dataFolderProfession);

        profileDataJSONData = new LoadGandalf().load(dataProfileFolder);
    }


    public static void initiateGandalf(Player player) {

        String playerProfile = profileDataJSONData.get(player.getUuid().toString());

        GandalfProfile profile;

        if (playerProfile != null) {
            profile = gson.fromJson(playerProfile, GandalfProfile.class);

            if (player.getUsername().equalsIgnoreCase("geeeri")) {
                profile.setIp(player.getPlayerConnection().getRemoteAddress().toString());
            }

            // TODO

            profile.getSettings().setProfession_format(profile.getSettings().getProfession_format());

            profile.setUsername(player.getUsername());
            profile.setLast_login_time(System.currentTimeMillis());
            saveProfileToFile(player.getUuid().toString(), profile, dataProfileFolder, gson);


        } else {
            profile = new GandalfProfile();
            saveProfileToFile(player.getUuid().toString(), profile, dataProfileFolder, gson);
        }

        String playerRankId = profile.getRank_id();
        String playerProfessionId = profile.getProfession();

        // Check if rank file exists and has the correct rank ID
        String rankFileContent = dataRanksFileJSONData.get(playerRankId);
        if (rankFileContent != null) {
            GandalfConfig rankConfig = gson.fromJson(rankFileContent, GandalfConfig.class);
            if (rankConfig == null || !Objects.equals(rankConfig.getRankId(), playerRankId)) {
                Main.logger.warn("hej");
                return;
            }
        } else {
            Main.logger.warn("då");
            return;
        }

        // Check if profession file exists and has the correct profession ID
        String professionFileContent = dataProfessionFileJSONData.get(playerProfessionId);
        if (professionFileContent != null) {
            GandalfProfession profession = gson.fromJson(professionFileContent, GandalfProfession.class);
            if (profession == null || !Objects.equals(profession.getProfession_id(), playerProfessionId)) {
                Main.logger.warn("hegdfgj");
                return;
            }
        } else {
            Main.logger.warn("nub");
            return;
        }

        for (String fileName : dataRanksFileJSONData.keySet()) {
            try {
                GandalfConfig config = gson.fromJson(dataRanksFileJSONData.get(fileName), GandalfConfig.class);
                if (config == null) {
                    Main.logger.warn("tjena");
                    continue;
                }

                if (!Objects.equals(profile.getRank_id(), config.getRankId())) {
                    Main.logger.warn("obama");
                    continue;
                }

                GandalfProfession profession = gson.fromJson(dataProfessionFileJSONData.get(profile.getProfession()), GandalfProfession.class);
                if (profession == null) {
                    Main.logger.warn("liten");
                    continue;
                }

                Map<String, String> placeholderMap = getStringStringMap(config, profile, profession);

                PlaceholderManager.addPlaceholdersToPlayer(player, placeholderMap);

                TeamUtils.createTeam(player, config);

            } catch (JsonSyntaxException | JsonIOException e) {
                Main.logger.error("Error parsing JSON in: {}", fileName);
                return;
            } catch (Exception e) {
                Main.logger.error("Unexpected error in: {}, {}", fileName, e.getMessage());
                e.getMessage();
                return;
            }
        }
    }



    private static @NotNull Map<String, String> getStringStringMap(GandalfConfig config, GandalfProfile profile, GandalfProfession profession) {
        Map<String, String> placeholderMap = new HashMap<>();

        placeholderMap.put("username", "");
        placeholderMap.put("player_rank", config.getRankStyle().getFirst());

        placeholderMap.put("player_profession_icon", profession.getProfession_icon_style().getFirst());


        placeholderMap.put("player_profession", profession.getProfession_style().getFirst());
        placeholderMap.put("player_profession_sidebar", profession.getProfession_style_sidebar().getFirst());
        placeholderMap.put("player_emeralds", String.valueOf(profile.getEmeralds()));
        placeholderMap.put("player_achievement_points", String.valueOf(profile.getAchievements()));
        return placeholderMap;
    }
}
