package rip.snicon.utils.json;

public class Text {

    private String text;
    private String color;
    private boolean bold;
    private boolean italic;

    public Text(String text, String color, boolean bold, boolean italic) {
        this.text = text;
        this.color = color;
        this.bold = bold;
        this.italic = italic;
    }

    public String getText() {
        return text;
    }

    public String getColor() {
        return color;
    }

    public boolean isBold() {
        return bold;
    }

    public boolean isItalic() {
        return italic;
    }
}
