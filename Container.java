import java.util.ArrayList;

public class Container {

    private String type;
    private ArrayList<Item> contents;

    public Container(String type) {
        this.type = type;
    }

    public void addItem(Item item) {
        this.contents.add(item);
    }

    public String getType() {
        return this.type;
    }
    
}
