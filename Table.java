import java.util.ArrayList;

public class Table {

    private int[] oldestDate = new int[3];
    private int[] newestDate = new int[3];
    private ArrayList<Item> contents;

    public Table(int oldestYr, int oldestMon, int oldestDay, int newestYr, int newestMon, int newestDay) {
        this.oldestDate[0] = oldestYr;
        this.oldestDate[1] = oldestMon;
        this.oldestDate[2] = oldestDay;
        this.newestDate[0] = newestYr;
        this.newestDate[1] = newestMon;
        this.newestDate[2] = newestDay;
    }

    public int[] getOldestDate() {
        return this.oldestDate;
    }

    public int[] getNewestDate() {
        return this.newestDate;
    }

    public void addItem(Item item) {
        this.contents.add(item);
    }

}
