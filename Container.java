import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class Container {

    private String type;
    private ArrayList<Item> contents = new ArrayList<Item>();

    public Container() {

    }

    public Container(String type) {
        this.type = type;
    }

    public void initializeBatch(int num) {
        this.setType("NULL");
        for (int i = 0; i < num; i++) {
            this.addItem(new Item());
        }
    }

    public void printContainer() {
        for (int i = 0; i < this.contents.size(); i++) {
            this.contents.get(i).printItem();
        }
    }

    public void addItem(Item item) {
        this.contents.add(item);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Item> getContents() {
        return this.contents;
    }
    
}
