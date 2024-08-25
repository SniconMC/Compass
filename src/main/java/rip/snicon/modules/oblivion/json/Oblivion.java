package rip.snicon.modules.oblivion.json;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Oblivion {

    private Map<String, Player> oblivions = new HashMap<>();

    private Map<String, List<Entity>> oblivionsName = new HashMap<>();

    public void setOblivionsName(Map<String, List<Entity>> oblivionsName) {
        this.oblivionsName = oblivionsName;
    }


    public Map<String, List<Entity>> getOblivionsName() {
        return oblivionsName;
    }

    public Map<String, Player> getOblivions() {
        return oblivions;
    }

    public void setOblivions(Map<String, Player> oblivions) {
        this.oblivions = oblivions;
    }

}
