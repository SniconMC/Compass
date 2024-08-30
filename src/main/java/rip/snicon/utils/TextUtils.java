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

/**
 * Utility class for handling text components and MiniMessage serialization/deserialization.
 *
 * <p>This class provides methods for converting between lists of MiniMessage-formatted strings
 * and Adventure {@link Component} objects. This is particularly useful in Minecraft server
 * development, where text is often formatted in a custom way.</p>
 *
 * @see Component
 * @see MiniMessage
 * @see TextComponent
 * @see <a href="https://docs.adventure.kyori.net/">Adventure Documentation</a>
 *
 * @author znopp
 * @author Wi1helm
 */
public class TextUtils {


    /**
     * Converts a list of MiniMessage-formatted strings to a single {@link Component}.
     *
     * <p>This method processes each string in the provided list, deserializing it from
     * MiniMessage format to an Adventure {@link Component}. All components are then combined
     * into a single component.</p>
     *
     * <p>The method applies a default format of reset, non-italic, and gray color to each text component.</p>
     *
     * @param minimessages A list of strings formatted using MiniMessage syntax.
     * @return A {@link Component} representing the combined text.
     *
     * @author znopp
     */
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

    /**
     * Converts an Adventure {@link Component} to a list of MiniMessage-formatted strings.
     *
     * <p>This method serializes the provided {@link Component} to a string using MiniMessage
     * syntax and adds it to a list. This is useful for converting complex text components
     * back into a format that can be easily stored or transmitted as plain text.</p>
     *
     * @param component The {@link Component} to convert to a string.
     * @return A list containing the MiniMessage representation of the component.
     *
     * @author znopp
     * @author Wi1helm
     */
    public static List<String> convertComponentToString(Component component) {
        List<String> strings = new ArrayList<>();
        strings.add(MiniMessage.miniMessage().serialize(component));
        return strings;
    }
}
