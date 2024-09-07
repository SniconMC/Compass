package rip.snicon.compass.gandalf;

import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.entity.Player;
import rip.snicon.compass.gandalf.config.GandalfProfession;
import rip.snicon.compass.gandalf.config.GandalfProfile;
import rip.snicon.compass.gandalf.config.GandalfProfileSettings;
import rip.snicon.compass.gandalf.config.GandalfRank;
import rip.snicon.compass.gandalf.utils.LoadGandalf;
import rip.snicon.compass.gandalf.utils.SaveProfile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GandalfManager {

    private static Gson gson = new Gson();

    private static File dataFolderRanks = new File("resources/gandalf/ranks");
    private static File dataFolderProfession = new File("resources/gandalf/professions");
    private static File dataProfileFolder = new File("resources/profiles");

    private static Map<String, String> rankFileData;

    private static Map<String, String> professionFileData;

    private static Map<String, String> profileData;


    private static Map<String, GandalfRank> rankMap = new HashMap<>();
    private static Map<String, GandalfProfession> professionMap = new HashMap<>();

    private static Map<String, GandalfProfile> playerProfiles = new HashMap<>();

    public GandalfManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();

        rankFileData = new LoadGandalf().load(dataFolderRanks);

        professionFileData = new LoadGandalf().load(dataFolderProfession);

        profileData = new LoadGandalf().load(dataProfileFolder);

        getConstantConfigFiles();
    }

    public static void getConstantConfigFiles(){
        rankFileData.forEach((fileName, content) -> {

            try {
                GandalfRank rankConfig = gson.fromJson(content, GandalfRank.class);
                rankMap.put(fileName, rankConfig);
            } catch (JsonSyntaxException | JsonIOException e) {
                GandalfMain.logger.error("Error parsing JSON in: {}", fileName);
            } catch (Exception e) {
                GandalfMain.logger.error("Unexpected error in: {}, {}", fileName, e.fillInStackTrace());
            }

        });

        professionFileData.forEach((fileName, content) -> {
            try {
                GandalfProfession professionConfig = gson.fromJson(content, GandalfProfession.class);
                professionMap.put(fileName, professionConfig);
            } catch (JsonSyntaxException | JsonIOException e) {
                GandalfMain.logger.error("Error parsing JSON in: {}", fileName);
            } catch (Exception e) {
                GandalfMain.logger.error("Unexpected error in: {}, {}", fileName, e.fillInStackTrace());
            }
        });
    }


    public static void initiateGandalf(Player player) {



        String playerUUID = player.getUuid().toString();

        if (!profileData.containsKey(player.getUuid().toString())){

            GandalfProfile newProfile = new GandalfProfile();
            saveProfileToFile(playerUUID, newProfile);
            return;
        }

        String content = profileData.get(playerUUID);

        try{

            GandalfProfile profile = gson.fromJson(content, GandalfProfile.class);

            setPlaceholders(player, profile);

            playerProfiles.put(playerUUID, profile);

        } catch (JsonSyntaxException | JsonIOException e) {
            GandalfMain.logger.error("Error parsing JSON in profile for player: {}", player.getUsername());
        } catch (Exception e) {
            GandalfMain.logger.error("Unexpected error in profile for player: {}", player.getUsername());
        }

    }

    public static GandalfProfile getProfiles(Player player){
        return playerProfiles.get(player.getUuid().toString());
    }

    public static GandalfRank getRank(String id){

        for (GandalfRank config : rankMap.values()) {
            if (Objects.equals(config.getRankId(), id)) {
                return config;
            }
        }

        return null;
    }

    public static GandalfProfession getProfession(String id){

        for (GandalfProfession config : professionMap.values()) {
            if (Objects.equals(config.getProfession_id(), id)) {
                return config;
            }
        }
        return null;
    }

    public static void saveProfileToFile(String uuid, GandalfProfile profile){
        SaveProfile.saveProfileToFile(uuid, profile, dataProfileFolder, gson);
    }

    private static void setPlaceholders(Player player, GandalfProfile profile){
        Map<String, String> placeholders = new HashMap<>();

        String username = player.getUsername();
        String rank = profile.getRank_id();
        String profession = profile.getProfession();
        double emeralds = profile.getEmeralds();
        double achivements = profile.getAchievements();

        GandalfProfileSettings setting = profile.getSettings();
        String icon_format = setting.getProfession_format();

        GandalfRank rankConfig = rankMap.get(rank);
        GandalfProfession professionConfig = professionMap.get(profession);

        String rankStyle = rankConfig.getRankStyle();
        String professionStyle = professionConfig.getProfession_style();
        String professionStyleSidebar = professionConfig.getProfession_style_sidebar();
        String professionIconStyle = professionConfig.getProfession_icon_style();

        placeholders.put("username",username);
        placeholders.put("player_rank", rankStyle);
        placeholders.put("player_profession", professionStyle);
        placeholders.put("player_profession_sidebar", professionStyleSidebar);
        placeholders.put("player_profession_icon", professionIconStyle);
        placeholders.put("player_emeralds", Double.toString(emeralds));
        placeholders.put("player_achievement_points", Double.toString(achivements));

        placeholders.put("profession_format_state", icon_format);

        PlaceholderManager.addPlaceholdersToPlayer(player, placeholders);
    }
}

