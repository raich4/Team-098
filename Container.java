import java.util.ArrayList;

public class Container {

    String type;
    ArrayList<Item> contents;

    public Container(String type) {
        this.type = type;
    }

    public void addItem(Item item) {
        this.contents.add(item);
    }


    
}
