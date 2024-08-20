package rip.snicon.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import rip.snicon.modules.sidebar.Text;

import java.util.List;

import static rip.snicon.utils.ColorUtils.StringToTextColor;

public class TextUtils {

    public static Component convertToComponent(List<Text> textList) {
        if ((textList == null) || (textList.isEmpty())) {
            return Component.text("Default Name");
        }
        Component combinedComponent = Component.empty();

        for (Text object : textList) {
            String text = object.getText();
            String colorString = object.getColor();
            boolean bold = object.isBold();
            boolean italic = object.isItalic();
            //boolean underline = displayName.isUnderline();
            //boolean strikethrough = displayName.isStrikethrough();



            // Convert color string to TextColor
            TextColor color = StringToTextColor(colorString);

            // Create individual component
            Component component = Component.text(text)
                    .color(color)
                    .decoration(TextDecoration.BOLD, bold)
                    .decoration(TextDecoration.ITALIC, italic);
            //.decoration(TextDecoration.UNDERLINED, underline)
            //.decoration(TextDecoration.STRIKETHROUGH, strikethrough);

            // Append to the combined component
            combinedComponent = combinedComponent.append(component);
        }
        // Create new Component with the converted color
        return combinedComponent;
    }

}
