package rip.snicon.compass.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.stream.Collectors;

public class TextUtils {


    public static Component convertStringToComponent(String messages) {
        MiniMessage miniMessage = MiniMessage.miniMessage();


        // Deserialize the MiniMessage formatted text
        String newText = "<reset><italic:false><#818181>" + messages + "<reset>";

        return miniMessage.deserialize(newText);
    }

    public static List<Component> convertStringToComponent(List<String> messages) {
        MiniMessage miniMessage = MiniMessage.miniMessage();

        // Map each message string to a formatted Component using MiniMessage
        return messages.stream()
                .map(message -> miniMessage.deserialize("<reset><italic:false><#818181>" + message + "<reset>"))
                .collect(Collectors.toList());
    }


    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input; // Return as is for null or empty strings
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

}
