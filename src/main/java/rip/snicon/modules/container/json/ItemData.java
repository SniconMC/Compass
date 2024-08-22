package rip.snicon.modules.container.json;

public class ItemData {

    private String function;
    private String page;

    public ItemData(String function, String page) {
        this.function = function;
        this.page = page;
    }

    public String getFunction() {
        if (function == null) {
            return "";
        }
        return function;
    }

    public String getPage() {
        if (page == null){
            return "";
        }
        return page;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
