package rip.snicon.modules.oblivion.json;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import rip.snicon.modules.oblivion.NPC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Oblivion {

    private Map<String, NPC> oblivions = new HashMap<>();

    private Map<String, List<Entity>> oblivionsName = new HashMap<>();

    public void setOblivionsName(Map<String, List<Entity>> oblivionsName) {
        this.oblivionsName = oblivionsName;
    }


    public Map<String, List<Entity>> getOblivionsName() {
        return oblivionsName;
    }

    public Map<String, NPC> getOblivions() {
        return oblivions;
    }

    public void setOblivions(Map<String, NPC> oblivions) {
        this.oblivions = oblivions;
    }

}
