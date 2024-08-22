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
        if (dye_color == null){
            return "";
        }
        return dye_color;
    }


    public void setName(List<Text> name) {
        this.name = name;
    }

    public void setLore(List<List<Text>> lore) {
        this.lore = lore;
    }

    public void setGlint(boolean glint) {
        this.glint = glint;
    }

    public void setModel_data(Object model_data) {
        this.model_data = model_data;
    }

    public void setDye_color(String dye_color) {
        this.dye_color = dye_color;
    }

    public void setShow_tooltip(boolean show_tooltip) {
        this.show_tooltip = show_tooltip;
    }
}
