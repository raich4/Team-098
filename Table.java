import java.time.LocalDate;
import java.util.ArrayList;

public class Table {

    private LocalDate oldestDate;
    private LocalDate newestDate;
    private ArrayList<Item> contents = new ArrayList<>();

    // No argument constructor
    public Table() {

    }

    public Table(int oldestYr, int oldestMon, int oldestDay, int newestYr, int newestMon, int newestDay) {
        this.oldestDate = LocalDate.of(oldestYr, oldestMon, oldestDay);
        this.newestDate = LocalDate.of(newestYr, newestMon, newestDay);
    }

    public void setDates(int oldestYr, int oldestMon, int oldestDay, int newestYr, int newestMon, int newestDay) {
        this.oldestDate = LocalDate.of(oldestYr, oldestMon, oldestDay);
        this.newestDate = LocalDate.of(newestYr, newestMon, newestDay);
    }

    public LocalDate getOldestDate() {
        return this.oldestDate;
    }

    public LocalDate getNewestDate() {
        return this.newestDate;
    }

    public void addItem(Item item) {
        this.contents.add(item);
    }

    public ArrayList<Item> getContents() {
        return this.contents;
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

}
