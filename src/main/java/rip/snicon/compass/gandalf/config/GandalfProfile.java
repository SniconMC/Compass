package rip.snicon.compass.gandalf.config;

public class GandalfProfile {

    private String username;
    private String rank_id;
    private String profession;
    private String profession_icon;
    private double emeralds;
    private double achievements;
    private double profession_total_xp;
    private GandalfProfileSettings settings;
    private long last_login_time;
    private String ip;

    public GandalfProfile() {
        this.username = "";
        this.rank_id = "villager";
        this.profession = "nitwit";
        this.profession_icon = "?";
        this.emeralds = 0.0;
        this.achievements = 0.0;
        this.profession_total_xp = 0.0;
        this.settings = new GandalfProfileSettings();
        this.last_login_time = System.currentTimeMillis();
        this.ip = "";
    }

    public long getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(long last_login_time) {
        this.last_login_time = last_login_time;
    }

    public GandalfProfileSettings getSettings() {
        return settings;
    }

    public void setSettings(GandalfProfileSettings settings) {
        this.settings = settings;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getProfession_total_xp() {
        return profession_total_xp;
    }

    public void setProfession_total_xp(double profession_total_xp) {
        this.profession_total_xp = profession_total_xp;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRank_id() {
        return rank_id;
    }

    public void setRank_id(String rank_id) {
        this.rank_id = rank_id;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfession_icon() {
        return profession_icon;
    }

    public void setProfession_icon(String profession_icon) {
        this.profession_icon = profession_icon;
    }

    public double getEmeralds() {
        return emeralds;
    }

    public void setEmeralds(double emeralds) {
        this.emeralds = emeralds;
    }

    public double getAchievements() {
        return achievements;
    }

    public void setAchievements(double achievements) {
        this.achievements = achievements;
    }
}
