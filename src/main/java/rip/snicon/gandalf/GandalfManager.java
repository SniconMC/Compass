package rip.snicon.gandalf;

import com.github.sniconmc.utils.placeholder.PlaceholderManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import rip.snicon.Main;
import rip.snicon.gandalf.config.GandalfRank;
import rip.snicon.gandalf.config.GandalfProfession;
import rip.snicon.gandalf.config.GandalfProfile;
import rip.snicon.gandalf.data.PlayerProfile;
import rip.snicon.gandalf.utils.LoadGandalf;
import rip.snicon.gandalf.utils.TeamUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static rip.snicon.gandalf.utils.SaveProfile.saveProfileToFile;

public class GandalfManager {

    private static Gson gson = new Gson();

    private static File dataFolderRanks = new File("resources/gandalf/ranks");
    private static File dataFolderProfession = new File("resources/gandalf/professions");
    private static File dataProfileFolder = new File("resources/profiles");

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

        PlayerProfile playerProfile = (PlayerProfile) player;

