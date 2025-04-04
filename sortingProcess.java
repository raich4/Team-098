import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class sortingProcess {

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.now();
        int simulationDays = 30; // Total days to simulate
        int dailyShipmentSize = 100; // Placeholder shipment size

        ArrayList<Container> inputRack = new ArrayList<>();

        ArrayList<Container> oldStock = new ArrayList<Container>();
        for (int i = 0; i < GlobalInfoHelper.Categories.length; i++) {
            oldStock.add(new Container(GlobalInfoHelper.Categories[i]));
        }

        ArrayList<Container> newStock = new ArrayList<Container>();
        for (int i = 0; i < GlobalInfoHelper.Categories.length; i++) {
            newStock.add(new Container(GlobalInfoHelper.Categories[i]));
        }

        ArrayList<Container> outputRack = new ArrayList<>();

        Container bin = new Container();

        // Main simulation loop: each iteration is one day
        for (int day = 0; day < simulationDays; day++) {
            System.out.println("Simulation Date: " + currentDate);

            itemArrives(inputRack);
            // Simulate the sorting process during the day
            // This can be broken down further into smaller time steps if needed
            simulateSortingForDay(inputRack, oldStock, newStock, bin);

            // Process daily shipment at the end of the day
            processDailyShipment(dailyShipmentSize, currentDate, outputRack, bin);

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }
    }

    private static void itemArrives (ArrayList<Container> inputRack) {
        Container pallet = new Container();
        pallet.initializeBatch(100);
        inputRack.add(pallet);
    }

    private static void simulateSortingForDay(ArrayList<Container> inputRack, ArrayList<Container> oldStock, ArrayList<Container> newStock, Container bin) {

        LocalTime timeOfDay = LocalTime.of(9, 30);

        Worker jason = new Worker("Jason", 0.01, 0.01);

        Table oldTable = new Table();
        Table cutoffTable = new Table();
        Table newTable = new Table();

        Container pallet = new Container();

        while(!timeOfDay.isAfter(LocalTime.of(12,0))) {

            int maxMinutes = 0;
            if (pallet.isEmpty()){
                pallet = inputRack.get(1);
                inputRack.remove(1);

                maxMinutes = 5;
            }

            if ( !pallet.isEmpty() && oldTable.isEmpty() && cutoffTable.isEmpty() && newTable.isEmpty()) {

                LocalDate oldestDate = jason.findOldestDate(pallet.getContents());
                LocalDate newestDate = jason.findNewestDate(pallet.getContents());

                int cutoff = (oldestDate.getYear() + newestDate.getYear()) / 2;

                oldTable.setDates(oldestDate.getYear(), oldestDate.getMonthValue(), oldestDate.getDayOfMonth(), cutoff - 1, 12, 31);
                cutoffTable.setDates(cutoff, 1, 1, cutoff, 12, 31);
                newTable.setDates(cutoff + 1, 1, 1, newestDate.getYear(), newestDate.getMonthValue(), newestDate.getDayOfMonth());

                // Finding the oldest and the newest dates
                jason.separateDate(oldTable, pallet.getContents());
                jason.separateDate(cutoffTable, pallet.getContents());
                jason.separateDate(newTable, pallet.getContents());

                maxMinutes = 30;

            }

            if ( !oldTable.isEmpty() || !cutoffTable.isEmpty() || !newTable.isEmpty() ) {

                jason.separateCategory(oldStock, oldTable.getContents());
                jason.separateCategory(oldStock, cutoffTable.getContents());

                jason.separateCategory(newStock, cutoffTable.getContents());
                maxMinutes = 60;

            }

            timeOfDay = timeOfDay.plusMinutes(maxMinutes);

            // REMEMBER TO IMPLEMENT PUTTING STUFF ON THE RACK
            // REMEMBER TO IMPLEMENT THE FULLNESS OF A CONTAINER

        }

    }

    private static void processDailyShipment(int shipmentSize, LocalDate date, ArrayList<Container> outputRack, Container bin) {
        // Process shipping logic
        System.out.println("  Shipping out " + shipmentSize + " items on " + date);

        Random rand = new Random();
        for (int i = 0; i < shipmentSize; i++) {
            Container shipped = outputRack.get(rand.nextInt());
            while (!shipped.isEmpty()) {
                if (shipped.getContents().get(0).getExpDate().isBefore(date)) {
                    bin.addItem(shipped.getContents().get(0));
                }
                shipped.getContents().remove(0);
            }
        }
    }


}
