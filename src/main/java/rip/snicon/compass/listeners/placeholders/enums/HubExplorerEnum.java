package rip.snicon.compass.listeners.placeholders.enums;

import java.util.Random;

public enum HubExplorerEnum {

    TEXT1("Enjoy your stay!"),
    TEXT2("Hope to see you again!"),
    TEXT3("Have a look around!"),
    TEXT4("Invite your friends!");

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
