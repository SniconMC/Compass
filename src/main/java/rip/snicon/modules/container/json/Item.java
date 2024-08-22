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
        if (data == null) {
            return new ItemData("","");
        }
        return data;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContainer_id(String container_id) {
        this.container_id = container_id;
    }

    public void setCount(ItemCount count) {
        this.count = count;
    }

    public void setDisplay(ItemDisplay display) {
        this.display = display;
    }

    public void setSkin(ItemSkin skin) {
        this.skin = skin;
    }

    public void setData(ItemData data) {
        this.data = data;
    }
}
