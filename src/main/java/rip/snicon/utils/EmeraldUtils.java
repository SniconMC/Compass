package rip.snicon.utils;

import net.minestom.server.entity.Player;
import rip.snicon.modules.placeholders.PlaceholderManager;
import rip.snicon.modules.sidebar.SidebarCreator;

import java.util.List;

public class EmeraldUtils {


    public static void addEmeralds(Player player, String amount){

        float oldAmount = Float.parseFloat(PlaceholderManager.getPlaceholderForPlayer(player, "player_emeralds"));

        float newAmount = oldAmount + Float.parseFloat(amount);
        PlaceholderManager.setPlaceholderToPlayer(player, "player_emeralds", String.valueOf(newAmount));
        player.sendMessage(TextUtils.convertStringToComponent(List.of("You Obtained " + amount + " more <green>Emeralds</green>")));
        SidebarCreator.updateSidebar(player);

    }

}
