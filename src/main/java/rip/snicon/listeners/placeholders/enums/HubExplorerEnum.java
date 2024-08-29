package rip.snicon.listeners.placeholders.enums;

import java.util.Random;

public enum HubExplorerEnum {

    A("Enjoy your stay!"),
    B("Hope to see you again!"),
    C("In development!"),
    D("Crafted with love"),
    E("Of Swedish origin!"),
    F("Balls"),
    G("balls");

    private final String text;

    HubExplorerEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getRandomText() {
        HubExplorerEnum[] texts = HubExplorerEnum.values(); // Get all enum values
        int randomIndex = new Random().nextInt(texts.length); // Generate a random index
        return texts[randomIndex].getText(); // Return the randomly selected color
    }

}
