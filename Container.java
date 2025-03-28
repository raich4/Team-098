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

    public int getRequiredTime() {
        int count = 0;
        for (int i = 0; i < contents.size(); i++) {
            count += contents.get(i).timeToSort;
        }
        return count;
    }
    
}
