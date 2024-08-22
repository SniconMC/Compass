package rip.snicon.modules.container.json;

public class ItemCount {

    private int current;
    private int max;

    public ItemCount(int current, int max) {
        this.current = current;
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
