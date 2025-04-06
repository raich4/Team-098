import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class sortingProcess {

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.of(2022,6,1);
        int simulationDays = 600; // Total days to simulate
        int dailyShipmentSize = 4; // Placeholder shipment size

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
        int count = 0;

        // Main simulation loop: each iteration is one day
        for (int day = 0; day < simulationDays; day++) {
            System.out.println("Simulation Date: " + currentDate);

            itemArrives(inputRack, 4, currentDate);
            // Simulate the sorting process during the day
            // This can be broken down further into smaller time steps if needed
            simulateSortingForDay(inputRack, oldStock, newStock, bin, outputRack);

            // Process daily shipment at the end of the day
            count += processDailyShipment(dailyShipmentSize, currentDate, outputRack, bin);

            System.out.println("Input rack size: " + inputRack.size());
            System.out.println("Output rack size: " + outputRack.size());

            // Move to the next day
            currentDate = currentDate.plusDays(1);

        }

        int binCount = bin.getContents().size();
        double waste = ((double)binCount / (1000 * count) ) * 100;

        System.out.println("Waste: " + waste + "%");
    }

    private static void itemArrives (ArrayList<Container> inputRack, int numPallets, LocalDate currentDate) {
        for (int i = 0; i < numPallets; i++){
            Container pallet = new Container();
            pallet.initializeBatch(1000, currentDate);
            inputRack.add(pallet);
        }
    }

    private static void simulateSortingForDay(ArrayList<Container> inputRack, ArrayList<Container> oldStock, ArrayList<Container> newStock, Container bin, ArrayList<Container> outputRack) {

        LocalTime timeOfDay = LocalTime.of(9, 30);

        Worker jason = new Worker("Jason", 0.01, 0.01);

        Table oldTable = new Table(true);
        Table cutoffTable = new Table(true);
        Table newTable = new Table(false);

        Container pallet = new Container();

        while(!timeOfDay.isAfter(LocalTime.of(15,0))) {

            int maxMinutes = 0;
            if (pallet.isEmpty() && !inputRack.isEmpty()){
                pallet = inputRack.getFirst();
                inputRack.removeFirst();

                maxMinutes = 5;

                System.out.println("pallet pulled out");
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

                // Volunteer may make mistakes, this will result in the pallet having leftover items
                // These items are put into the waste bin for later counting.
                //System.out.println(pallet.getContents().size() + " ITEMS PUT IN TO THE BIN!!! WASTEFUL!");
                //bin.getContents().addAll(pallet.getContents());

                // Clear the pallet
                pallet.getContents().clear();

                maxMinutes = 30;

                System.out.println("Put on tables.");

            }

            if ( !oldTable.isEmpty() || !cutoffTable.isEmpty() || !newTable.isEmpty() ) {

                // This moves all the items into the stocks
                // output rack is there because if the stock is full then its put to the outputRack.
                jason.separateCategory(oldStock, oldTable, outputRack);
                jason.separateCategory(oldStock, cutoffTable, outputRack);

                jason.separateCategory(newStock, newTable, outputRack);

                // Volunteer may make mistakes, this will result in the table having leftover items
                // These items are put into the waste bin for later counting.
                bin.getContents().addAll(oldTable.getContents());
                bin.getContents().addAll(cutoffTable.getContents());
                bin.getContents().addAll(newTable.getContents());

                System.out.println(oldTable.getContents().size() + " ITEMS PUT IN TO THE BIN!!! WASTEFUL!");
                System.out.println(cutoffTable.getContents().size() + " ITEMS PUT IN TO THE BIN!!! WASTEFUL!");
                System.out.println(newTable.getContents().size() + " ITEMS PUT IN TO THE BIN!!! WASTEFUL!");

                oldTable.getContents().clear();
                newTable.getContents().clear();
                cutoffTable.getContents().clear();

                maxMinutes = 90;

                System.out.println("Put in categories.");

            }

            timeOfDay = timeOfDay.plusMinutes(maxMinutes);

        }

    }

    private static int processDailyShipment(int shipmentSize, LocalDate date, ArrayList<Container> outputRack, Container bin) {
        int count = 0;
        int countExpired = 0;
        int countDefective = 0;
        int itemCount = 0;

        if (outputRack.isEmpty()) {
            return 0;
        }

        // Process shipping logic
        System.out.println("  Shipping out " + shipmentSize + " pallets on " + date);

        for (int i = 0; i < shipmentSize && !outputRack.isEmpty(); i++) {
            int index = 0;
            LocalDate oldest = outputRack.getFirst().getContents().getFirst().getExpDate();
            for (int j = 0; j < outputRack.size(); j++) {
                if (outputRack.get(j).findOldestDate().isBefore(oldest)) {
                    oldest = outputRack.get(j).findOldestDate();
                    index = j;
                }
            }

            Container shipped = outputRack.get(index);

            while (!shipped.isEmpty()) {
                if (shipped.getContents().getFirst().getExpDate().isBefore(date)) {
                    bin.addItem(shipped.getContents().getFirst());
                    countExpired++;
                }
                else if (shipped.getContents().getFirst().isDefective()) {
                    bin.addItem(shipped.getContents().getFirst());
                    countDefective++;
                }
                shipped.getContents().removeFirst();
                itemCount++;
            }

            outputRack.remove(index);
            count++;
        }

        System.out.println("     Defective: " + countDefective + " Expired: " + countExpired + " TOTAL ITEMS: " + itemCount);
        return count;
    }


}
