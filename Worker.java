import java.util.ArrayList;
import java.util.Random;

public class Worker {

    String name;
    double readErrorRate;

    public Worker(String name, double readErrorRate) {
        this.name = name;
        this.readErrorRate = readErrorRate;
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

}
