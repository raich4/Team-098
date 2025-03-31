import java.util.ArrayList;
import java.util.Random;

public class Worker {

    String name;
    double readErrorRate;
    double placeErrorRate;

    public Worker(String name, double readErrorRate, double placeErrorRate) {
        this.name = name;
        this.readErrorRate = readErrorRate;
        this.placeErrorRate = placeErrorRate;
    }

    // Compares two dates inputted and returns the number of the newest one.
    public int compareDate(int[] date1, int[] date2) {
        if (date1[0] > date2[0]) {
            return 1;
        }
        else if (date1[0] < date2[0]) {
            return 2;
        }
        if (date1[1] > date2[1]) {
            return 1;
        }
        else if (date1[1] < date2[1]) {
            return 2;
        }
        if (date1[2] > date2[2]) {
            return 1;
        }
        else if (date1[2] < date2[2]) {
            return 2;
        }
        return 0;
    }

    public int[] findNewestDate(ArrayList<Item> batch){
        Random rand = new Random();
        int[] newest = {2000, 0, 0};
        for (int i = 0; i < batch.size(); i++) {
            if (rand.nextDouble() < readErrorRate) {
                continue;
            }
            if (compareDate(batch.get(i).getExpDate(), newest) == 1) {
                newest = batch.get(i).getExpDate();
            }
        }
        return newest;
    }

    public int[] findOldestDate(ArrayList<Item> batch) {
        Random rand = new Random();
        int[] oldest = {2050, 0, 0};
        for (int i = 0; i < batch.size(); i++) {
            if (rand.nextDouble() < readErrorRate) {
                continue;
            }
            if (compareDate(batch.get(i).getExpDate(), oldest) == 0) {
                oldest = batch.get(i).getExpDate();
            }
        }
        return oldest;
    }

    public void separateDate(Table table, ArrayList<Item> batch) {
        Random rand = new Random();
        for (int i = batch.size() - 1; i >= 0; i--) {

            // Due to potential error, volunteer randomly puts an item onto the table.
            // This item may not necessarily belong to this table.
            if (rand.nextDouble() < placeErrorRate) {
                table.addItem(batch.get(i));
                batch.remove(i);
                continue;
            }
            if (compareDate(batch.get(i).getExpDate(), table.getNewestDate()) == 2 &&
                    compareDate(batch.get(i).getExpDate(), table.getOldestDate()) == 1) {
                table.addItem(batch.get(i));
                batch.remove(i);
            }
        }
    }

    public void separateCategory() {

    }

}
