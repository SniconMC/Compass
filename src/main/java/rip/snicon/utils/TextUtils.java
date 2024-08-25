package rip.snicon.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static Component convertStringToComponent(List<String> minimessages) {
        Component comp = Component.empty();
        MiniMessage miniMessage = MiniMessage.miniMessage();

        for (String text : minimessages) {

            // Deserialize the MiniMessage formatted text
            String newText = "<reset><italic:false><#818181>" + text + "<reset>";
            Component textComponent = miniMessage.deserialize(newText);
            comp = comp.append(textComponent);
        }
        return comp;
    }


    public static List<String> convertComponentToString(Component component) {
        List<String> strings = new ArrayList<>();
        strings.add(MiniMessage.miniMessage().serialize(component));
        return strings;
    }
}
