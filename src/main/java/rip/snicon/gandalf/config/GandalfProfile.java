package rip.snicon.gandalf.config;

public class GandalfProfile {

    private String rank_id;
    private String profession;
    private String profession_icon;
    private double emeralds;
    private double achievements;

    public GandalfProfile() {
        this.rank_id = "villager";
        this.profession = "nitwit";
        this.profession_icon = "?";
        this.emeralds = 0.0;
        this.achievements = 0.0;
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
