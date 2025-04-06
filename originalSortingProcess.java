import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class originalSortingProcess {

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.of(2023,6,1);
        int simulationDays = 365; // Total days to simulate
        int dailyShipmentSize = 3; // Placeholder shipment size

        ArrayList<Container> inputRack = new ArrayList<>();

        ArrayList<Container> stock = new ArrayList<Container>();
        for (int i = 0; i < GlobalInfoHelper.Categories.length; i++) {
            stock.add(new Container(GlobalInfoHelper.Categories[i]));
        }

        ArrayList<Container> outputRack = new ArrayList<>();

        Container bin = new Container();
        int count = 0;

        // Main simulation loop: each iteration is one day
        for (int day = 0; day < simulationDays; day++) {
            System.out.println("Simulation Date: " + currentDate);

            if (currentDate.getDayOfMonth() == 6 || currentDate.getDayOfMonth() == 2) {
                dailyShipmentSize = 4;
            }
            else {
                dailyShipmentSize = 2;
            }

            itemArrives(inputRack, 4, currentDate);
            // Simulate the sorting process during the day
            // This can be broken down further into smaller time steps if needed
            simulateSortingForDay(inputRack, stock, bin, outputRack);

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

    private static void simulateSortingForDay(ArrayList<Container> inputRack, ArrayList<Container> stock, Container bin, ArrayList<Container> outputRack) {

        LocalTime timeOfDay = LocalTime.of(9, 30);

        Worker jason = new Worker("Jason", 0.01, 0.01);

        Table oldTable = new Table();
        Table cutoffTable = new Table();
        Table newTable = new Table();

        Container pallet = new Container();

        while(!timeOfDay.isAfter(LocalTime.of(15,0))) {

            int maxMinutes = 5;
            if (pallet.isEmpty() && !inputRack.isEmpty()){
                pallet = inputRack.getFirst();
                inputRack.removeFirst();

                maxMinutes = 5;

                System.out.println("pallet pulled out");
            }

            if ( !stock.isEmpty() ) {

                // This moves all the items into the stocks
                // output rack is there because if the stock is full then its put to the outputRack.
                jason.separateCategory(stock, pallet.getContents(), outputRack);


                // Volunteer may make mistakes, this will result in the table having leftover items
                // These items are put into the waste bin for later counting.
                System.out.println(pallet.getContents().size() + " ITEMS PUT IN TO THE BIN!!! WASTEFUL!");
                bin.getContents().addAll(pallet.getContents());

                pallet.getContents().clear();

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

        Random rand = new Random();

        if (outputRack.isEmpty()) {
            return 0;
        }

        // Process shipping logic
        System.out.println("  Shipping out " + shipmentSize + " pallets on " + date);

        for (int i = 0; i < shipmentSize && !outputRack.isEmpty(); i++) {
            Container shipped = outputRack.get(rand.nextInt(outputRack.size()));
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

            outputRack.remove(shipped);
            count++;
        }

        System.out.println("     Defective: " + countDefective + " Expired: " + countExpired + " TOTAL ITEMS: " + itemCount);
        return count;
    }

}
