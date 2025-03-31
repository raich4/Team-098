import java.util.ArrayList;

public class Table {

    int[] oldestDate = new int[3];
    int[] newestDate = new int[3];
    ArrayList<Item> contents;

    public Table(int oldestYr, int oldestMon, int oldestDay, int newestYr, int newestMon, int newestDay) {
        this.oldestDate[0] = oldestYr;
        this.oldestDate[1] = oldestMon;
        this.oldestDate[2] = oldestDay;
        this.newestDate[0] = newestYr;
        this.newestDate[1] = newestMon;
        this.newestDate[2] = newestDay;
    }



}
