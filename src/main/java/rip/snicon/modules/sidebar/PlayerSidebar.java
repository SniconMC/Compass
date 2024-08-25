package rip.snicon.modules.sidebar;

import java.util.List;

public class PlayerSidebar {

    private boolean blankNumberFormat;
    private List<String> title;
    private List<List<String>> layout;

    public boolean isBlankNumberFormat() {
        return blankNumberFormat;
    }

    public List<String> getTitle() {
        return title;
    }

    public List<List<String>> getLayout() {
        return layout;
    }
}
