package rip.snicon.modules.container.json;

import rip.snicon.utils.json.Text;

import java.util.List;

public class ItemDisplay {

    private List<Text> name;
    private List<List<Text>> lore;
    private Object model_data;
    private boolean glint;
    private String dye_color;
    private boolean show_tooltip;

    public boolean isShow_tooltip() {
        return show_tooltip;
    }

    public List<Text> getName() {
        return name;
    }

    public List<List<Text>> getLore() {
        return lore;
    }

    public Object getModel_data() {
        return model_data;
    }

    public boolean isGlint() {
        return glint;
    }

    public String getDye_color() {
        return dye_color;
    }
}
