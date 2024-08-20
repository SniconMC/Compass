package rip.snicon.modules.sidebar;

import java.util.List;

public class PlayerSidebar {

    private boolean blankNumberFormat;
    private List<Text> title;
    private List<List<Text>> layout;

    public boolean isBlankNumberFormat() {
        return blankNumberFormat;
    }

    public List<Text> getTitle() {
        return title;
    }

    public List<List<Text>> getLayout() {
        return layout;
    }
}
