package rip.snicon.modules.oblivion;

import net.minestom.server.coordinate.Pos;
import rip.snicon.modules.oblivion.json.OblivionPosition;
import rip.snicon.modules.oblivion.json.OblivionSkin;
import rip.snicon.utils.json.Text;

import java.util.List;

public class OblivionConfig {

    private List<List<Text>> name;
    private String uuid;
    private String entity_type;
    private OblivionPosition position;

    private OblivionSkin skin;

    private boolean in_tab;
    private String world;

    public List<List<Text>> getName() {
        return name;
    }

    public boolean isIn_tab() {
        return in_tab;
    }

    public OblivionSkin getSkin() {
        return skin;
    }

    public OblivionPosition getPosition() {
        return position;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public String getUuid() {
        return uuid;
    }

    public String getWorld(){
        return world;
    }
}
