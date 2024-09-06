package rip.snicon.gandalf.config;

import java.util.List;

public class GandalfRank {

    private String rank_id;
    private List<String> rank_style;
    private String rank_color;
    private String rank_chat_color;

    private List<String> rank_format;
    private List<String> rank_format_simple;

    private GandalfPermission permissions;

    public String getRankId() {
        return rank_id;
    }

    public List<String> getRankStyle() {
        return rank_style;
    }

    public List<String> getRankFormat() {
        return rank_format;
    }

    public String getRankColor() {
        return rank_color;
    }

    public String getRankChatColor() {
        return rank_chat_color;
    }

    public List<String> getRankFormatSimple() {
        return rank_format_simple;
    }

    public GandalfPermission getPermissions() {
        return permissions;
    }
}
