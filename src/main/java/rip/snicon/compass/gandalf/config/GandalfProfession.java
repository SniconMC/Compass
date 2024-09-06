package rip.snicon.compass.gandalf.config;

import java.util.List;

public class GandalfProfession {

    private String profession_id;
    private List<String> profession_style;
    private List<String> profession_style_sidebar;
    private String profession_icon;
    private List<String> profession_icon_style;

    public String getProfession_id() {
        return profession_id;
    }

    public List<String> getProfession_style() {
        return profession_style;
    }

    public List<String> getProfession_style_sidebar() {
        return profession_style_sidebar;
    }

    public String getProfession_icon() {
        return profession_icon;
    }

    public List<String> getProfession_icon_style() {
        return profession_icon_style;
    }
}
