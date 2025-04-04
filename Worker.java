import java.time.LocalDate;
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

    public LocalDate findNewestDate(ArrayList<Item> batch){
        Random rand = new Random();
        LocalDate newest = LocalDate.of(2000, 1, 1);
        for (int i = 0; i < batch.size(); i++) {
            if (rand.nextDouble() < readErrorRate) {
                continue;
            }
            if (batch.get(i).getExpDate().isAfter(newest)) {
                newest = batch.get(i).getExpDate();
            }
        }
        return newest;
    }

    public LocalDate findOldestDate(ArrayList<Item> batch) {
        Random rand = new Random();
        LocalDate oldest = LocalDate.of(2050,1,1);
        for (int i = 0; i < batch.size(); i++) {
            if (rand.nextDouble() < readErrorRate) {
                continue;
            }
            if (batch.get(i).getExpDate().isBefore(oldest)) {
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
                continue;
            }
            if ( ( batch.get(i).getExpDate().isBefore(table.getNewestDate()) ||
                  batch.get(i).getExpDate().equals(table.getNewestDate()))
                  &&
                  (batch.get(i).getExpDate().isAfter(table.getOldestDate()) ||
                  batch.get(i).getExpDate().equals(table.getNewestDate()) ) ) {

                table.addItem(batch.get(i));
                batch.remove(i);
            }
        }
    }

    public void separateCategory(ArrayList<Container> stock, ArrayList<Item> table, ArrayList<Container> outputRack) {

        Random rand = new Random();

        // Note we reverse traverse here due to removing elements changing the index.
        for (int j = 0; j < stock.size(); j++) { // Outer loop for all categories.
            for (int i = table.size() - 1; i >= 0; i--) { // Inner loop for all items on a table
                if (rand.nextDouble() < placeErrorRate) {
                    continue;
                }

                if ( table.get(i).getType().equals( stock.get(j).getType() ) ) {
                    // Transfer the items into the outputRack if it is full.
                    if (stock.get(j).isFull()) {
                        outputRack.add(stock.get(j));
                        stock.get(j).getContents().clear();
                    }

                    stock.get(j).addItem( table.get(i) );
                    table.remove(i);
                }


            }
        }

    }

}
