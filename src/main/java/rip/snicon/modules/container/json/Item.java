package rip.snicon.modules.container.json;

public class Item {

    private int slot;
    private String id;
    private String container_id;
    private ItemCount count;
    private ItemDisplay display;
    private ItemSkin skin;
    private ItemData data;

    public String getContainerId() {
        return container_id;
    }

    public void setContainerId(String container_id) {
        this.container_id = container_id;
    }

    public int getSlot() {
        return slot;
    }

    public String getId() {
        return id;
    }

    public ItemDisplay getDisplay() {
        return display;
    }

    public ItemCount getCount() {
        return count;
    }

    public ItemSkin getSkin() {
        return skin;
    }

    public ItemData getData() {
        return data;
    }
}
