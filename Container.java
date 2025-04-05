import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.util.ArrayList;

public class Container {

    private String type;
    private ArrayList<Item> contents = new ArrayList<Item>();
    private int maxItems = 1000;
    private boolean isOld;

    public Container() {

    }

    public Container(String type) {
        this.type = type;
    }

    public void initializeBatch(int num, LocalDate currentDate) {
        this.setType("NULL");
        for (int i = 0; i < num; i++) {
            this.addItem(new Item(currentDate));
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

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public boolean isFull() {
        return contents.size() >= maxItems;
    }

    public void setOld(boolean old) {
        this.isOld = old;
    }

    public boolean isOld() {
        return this.isOld;
    }
    
}
