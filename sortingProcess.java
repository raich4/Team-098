import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class sortingProcess {

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.of(2024,1,1);
        int simulationDays = 30; // Total days to simulate
        int dailyShipmentSize = 9; // Placeholder shipment size

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

            itemArrives(inputRack, 9);
            // Simulate the sorting process during the day
            // This can be broken down further into smaller time steps if needed
            simulateSortingForDay(inputRack, oldStock, newStock, bin, outputRack);

            // Process daily shipment at the end of the day
            processDailyShipment(dailyShipmentSize, currentDate, outputRack, bin);

            // Move to the next day
            currentDate = currentDate.plusDays(1);

        }

        int binCount = bin.getContents().size();
        double waste = ((double)binCount / (simulationDays * 1000 * dailyShipmentSize) ) * 100;

        System.out.println("Waste: " + waste + "%");
    }

    private static void itemArrives (ArrayList<Container> inputRack, int numPallets) {
        for (int i = 0; i < numPallets; i++){
            Container pallet = new Container();
            pallet.initializeBatch(1000);
            inputRack.add(pallet);
        }
    }

    private static void simulateSortingForDay(ArrayList<Container> inputRack, ArrayList<Container> oldStock, ArrayList<Container> newStock, Container bin, ArrayList<Container> outputRack) {

        LocalTime timeOfDay = LocalTime.of(9, 30);

        Worker jason = new Worker("Jason", 0.01, 0.01);

        Table oldTable = new Table();
        Table cutoffTable = new Table();
        Table newTable = new Table();

        Container pallet = new Container();

        while(!timeOfDay.isAfter(LocalTime.of(15,0))) {

            int maxMinutes = 0;
            if (pallet.isEmpty() && !inputRack.isEmpty()){
                pallet = inputRack.getFirst();
                inputRack.removeFirst();

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

                bin.getContents().addAll(pallet.getContents());

                pallet.getContents().clear();

                maxMinutes = 30;

            }

            if ( !oldTable.isEmpty() || !cutoffTable.isEmpty() || !newTable.isEmpty() ) {

                jason.separateCategory(oldStock, oldTable.getContents(), outputRack);
                jason.separateCategory(oldStock, cutoffTable.getContents(), outputRack);

                jason.separateCategory(newStock, newTable.getContents(), outputRack);

                bin.getContents().addAll(oldTable.getContents());
                bin.getContents().addAll(cutoffTable.getContents());
                bin.getContents().addAll(newTable.getContents());

                oldTable.getContents().clear();
                newTable.getContents().clear();
                cutoffTable.getContents().clear();

                maxMinutes = 60;

            }

            timeOfDay = timeOfDay.plusMinutes(maxMinutes);

        }

    }

    private static void processDailyShipment(int shipmentSize, LocalDate date, ArrayList<Container> outputRack, Container bin) {
        if (outputRack.isEmpty()) {
            return;
        }

        // Process shipping logic
        System.out.println("  Shipping out " + shipmentSize + " pallets on " + date);

        Random rand = new Random();
        for (int i = 0; i < shipmentSize && !outputRack.isEmpty(); i++) {
            Container shipped = outputRack.getFirst();
            while (!shipped.isEmpty()) {
                if (shipped.getContents().getFirst().getExpDate().isBefore(date)) {
                    bin.addItem(shipped.getContents().getFirst());
                }
                shipped.getContents().removeFirst();
            }

            outputRack.removeFirst();
        }
    }


}
