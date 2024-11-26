package rip.snicon.compass.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public class TextUtils {


    public static Component convertStringToComponent(String messages) {
        MiniMessage miniMessage = MiniMessage.miniMessage();


        // Deserialize the MiniMessage formatted text
        String newText = "<reset><italic:false><#818181>" + messages + "<reset>";

        return miniMessage.deserialize(newText);
    }

}
