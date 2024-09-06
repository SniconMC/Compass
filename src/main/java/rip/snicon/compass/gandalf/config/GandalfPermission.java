package rip.snicon.compass.gandalf.config;

import java.util.List;

public class GandalfPermission {

    private String parent;
    private List<String> rank_specific;


    public String getParent() {
        return parent;
    }

    public List<String> getRank_specific() {
        return rank_specific;
    }
}
