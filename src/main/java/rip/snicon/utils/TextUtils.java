package rip.snicon.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import rip.snicon.utils.json.Text;

import java.util.ArrayList;
import java.util.List;

import static rip.snicon.utils.ColorUtils.StringToTextColor;

public class TextUtils {

    public static Component convertToComponentWithPlaceholders(List<Text> textList, Player player) {
        if ((textList == null) || (textList.isEmpty())) {
            return Component.text("Default Name");
        }
        Component combinedComponent = Component.empty();

        for (Text object : textList) {
            String text = PlaceholderReplacer.replacePlaceholders(player, object.getText());
            String colorString = PlaceholderReplacer.replacePlaceholders(player,object.getColor());
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


    // Reverse method without placeholders
    public static List<Text> convertComponentToTextList(Component component) {
        List<Text> textList = new ArrayList<>();

        if (component instanceof TextComponent) {
            TextComponent textComponent = (TextComponent) component;
            extractTextComponent(textComponent, textList);
        }

        return textList;
    }

    // Helper method to extract data from TextComponent
    private static void extractTextComponent(TextComponent component, List<Text> textList) {
        String text = component.content();
        TextColor color = component.color();
        String colorString = color != null ? color.asHexString() : "#ffffff"; // Default to white if no color
        boolean bold = component.hasDecoration(TextDecoration.BOLD);
        boolean italic = component.hasDecoration(TextDecoration.ITALIC);

        Text textObject = new Text(text, colorString, bold, italic);
        textList.add(textObject);

        // If there are more children, recursively process them
        for (Component child : component.children()) {
            if (child instanceof TextComponent) {
                extractTextComponent((TextComponent) child, textList);
            }
        }
    }

    public static Component componentFormatMinecraft(String text, String color) {
        Component textComponent;

        if (text.startsWith("&")) {
            char formatChar = text.charAt(1);
            String remainingText = text.substring(2);

            switch (formatChar) {
                case 'k' ->
                        textComponent = Component.text(remainingText).decorate(TextDecoration.OBFUSCATED);
                case 'l' ->
                        textComponent = Component.text(remainingText).decorate(TextDecoration.BOLD);
                case 'm' ->
                        textComponent = Component.text(remainingText).decorate(TextDecoration.STRIKETHROUGH);
                case 'n' ->
                        textComponent = Component.text(remainingText).decorate(TextDecoration.UNDERLINED);
                case 'o' ->
                        textComponent = Component.text(remainingText).decorate(TextDecoration.ITALIC);
                default ->
                        textComponent = Component.text(text);
            }
        } else {
            textComponent = Component.text(text);
        }

        return textComponent.color(TextColor.fromHexString(color));
    }


}

