package rip.snicon.modules.oblivion.json;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Oblivion {

    private Map<String, Player> oblibions = new HashMap<>();

    private Map<String, List<Entity>> oblibionsName = new HashMap<>();

    public void setOblibionsName(Map<String, List<Entity>> oblibionsName) {
        this.oblibionsName = oblibionsName;
    }


    public Map<String, List<Entity>> getOblibionsName() {
        return oblibionsName;
    }

    public Map<String, Player> getOblibions() {
        return oblibions;
    }

    public void setOblibions(Map<String, Player> oblibions) {
        this.oblibions = oblibions;
    }

}
