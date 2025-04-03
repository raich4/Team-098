import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class sortingProcess {

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.now();
        int simulationDays = 30; // Total days to simulate
        int dailyShipmentSize = 100; // Placeholder shipment size

        Worker jason = new Worker("Jason", 0.01, 0.01);
        ArrayList<Container> inputRack = new ArrayList<>();

        // Main simulation loop: each iteration is one day
        for (int day = 0; day < simulationDays; day++) {
            System.out.println("Simulation Date: " + currentDate);

            itemArrives(inputRack);
            // Simulate the sorting process during the day
            // This can be broken down further into smaller time steps if needed
            simulateSortingForDay(inputRack);

            // Process daily shipment at the end of the day
            processDailyShipment(dailyShipmentSize, currentDate);

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }
    }

    private static void itemArrives (ArrayList<Container> inputRack) {
        Container pallet = new Container();
        pallet.initializeBatch(100);
        inputRack.add(pallet);
    }

    private static void simulateSortingForDay(ArrayList<Container> inputRack) {

        LocalTime timeOfDay = LocalTime.of(9, 30);

        Table oldTable = new Table();
        Table cutoffTable = new Table();
        Table newTable = new Table();

        Container pallet = new Container();

        while(!timeOfDay.isAfter(LocalTime.of(12,0))) {

            if (pallet.isEmpty()){
                pallet = inputRack.get(1);
                inputRack.remove(1);

                timeOfDay.plusMinutes(5);
                continue;
            }

        }





        // Example of breaking the day into smaller intervals
        int timeStepsInDay = 6; // e.g., one time step per hour
        for (int step = 0; step < timeStepsInDay; step++) {
            // Update volunteer actions, item sorting, etc.
            System.out.println("  Time step " + step + ": Processing sorting tasks.");

            LocalDate oldestDate = jason.findOldestDate(pallet.getContents());
            LocalDate newestDate = jason.findNewestDate(pallet.getContents());



        }
    }

    private static void processDailyShipment(int shipmentSize, LocalDate date) {
        // Process shipping logic
        System.out.println("  Shipping out " + shipmentSize + " items on " + date);
    }



/*
        pallet.initializeBatch(10);
        pallet.printContainer();

        // Three sorting tables


        // Finding the oldest and the newest dates


        int cutoff = (oldestDate.getYear() + newestDate.getYear()) / 2;

        oldTable.setDates(oldestDate.getYear(), oldestDate.getMonthValue(), oldestDate.getDayOfMonth(), cutoff - 1, 12, 31);
        cutoffTable.setDates(cutoff, 1, 1, cutoff, 12, 31);
        newTable.setDates(cutoff + 1, 1, 1, newestDate.getYear(), newestDate.getMonthValue(), newestDate.getDayOfMonth());

        System.out.println("Cutoff year: " + cutoff);

        jason.separateDate(oldTable, pallet.getContents());
        jason.separateDate(cutoffTable, pallet.getContents());
        jason.separateDate(newTable, pallet.getContents());

        ArrayList<Container> stock = new ArrayList<Container>();
        for (int i = 0; i < GlobalInfoHelper.Categories.length; i++) {
            stock.add(new Container(GlobalInfoHelper.Categories[i]));
        }


        System.out.println("OLD: ");
        for (int i = 0; i < oldTable.getContents().size(); i++) {
            oldTable.getContents().get(i).printItem();
        }

        System.out.println("Cutoff: ");
        for (int i = 0; i < cutoffTable.getContents().size(); i++) {
            cutoffTable.getContents().get(i).printItem();
        }

        System.out.println("New: ");
        for (int i = 0; i < newTable.getContents().size(); i++) {
            newTable.getContents().get(i).printItem();
        }

        jason.separateCategory(stock, oldTable.getContents());
        jason.separateCategory(stock, cutoffTable.getContents());
*/
}
