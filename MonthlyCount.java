import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class MonthlyCount {

    public static void main(String[] args) {

        LocalDate currentDate = LocalDate.of(2023,5,1);
        int simulationDays = 365; // Total days to simulate
        int dailyShipmentSize = 3; // Placeholder shipment size

        ArrayList<Container> inputRack = new ArrayList<>();

        ArrayList<Container> oldStock = new ArrayList<>();
        for (int i = 0; i < GlobalInfoHelper.Categories.length; i++) {
            oldStock.add(new Container(GlobalInfoHelper.Categories[i]));
        }

        ArrayList<Container> newStock = new ArrayList<>();
        for (int i = 0; i < GlobalInfoHelper.Categories.length; i++) {
            newStock.add(new Container(GlobalInfoHelper.Categories[i]));
        }

        ArrayList<Container> outputRack = new ArrayList<>();

        Container bin = new Container();
        int totalShipmentCount = 0;  // total number of pallets shipped over simulation

        // Monthly counters
        int monthlyShipmentCount = 0;
        int prevBinCount = 0; // bin count at the start of the month

        // Main simulation loop: each iteration is one day
        for (int day = 0; day < simulationDays; day++) {
            System.out.println("Simulation Date: " + currentDate);

            // Vary daily shipment size based on day-of-month (example logic)
            if (currentDate.getMonthValue() == 6 || currentDate.getMonthValue() == 2) {
                dailyShipmentSize = 4;
            } else {
                dailyShipmentSize = 2;
            }

            itemArrives(inputRack, 3, currentDate);
            simulateSortingForDay(inputRack, oldStock, newStock, bin, outputRack);

            int dailyShipments = processDailyShipment(dailyShipmentSize, currentDate, outputRack, bin);
            totalShipmentCount += dailyShipments;
            monthlyShipmentCount += dailyShipments;

            // Move to the next day
            LocalDate nextDate = currentDate.plusDays(1);
            // Check if we are transitioning to a new month
            if (nextDate.getMonthValue() != currentDate.getMonthValue()) {
                int monthlyBinCount = bin.getContents().size() - prevBinCount;
                double monthlyErrorRate = (monthlyShipmentCount > 0) ? ((double)monthlyBinCount / (1000 * monthlyShipmentCount)) * 100 : 0;
                System.out.println("----- Month " + currentDate.getMonth() + " Summary -----");
                System.out.println("Pallets Shipped: " + monthlyShipmentCount);
                System.out.println("Bin Items: " + monthlyBinCount);
                System.out.println("Error Rate: " + monthlyErrorRate + "%");
                System.out.println("----------------------------------------");
                // Reset monthly shipment count and update previous bin count for next month
                monthlyShipmentCount = 0;
                prevBinCount = bin.getContents().size();
            }
            currentDate = nextDate;
        }

        int binCount = bin.getContents().size();
        double totalWaste = ((double)binCount / (1000 * totalShipmentCount)) * 100;
        System.out.println("Total Pallets Shipped: " + totalShipmentCount);
        System.out.println("Total Bin Items: " + binCount);
        System.out.println("Total Waste: " + totalWaste + "%");
    }

    private static void itemArrives(ArrayList<Container> inputRack, int numPallets, LocalDate currentDate) {
        for (int i = 0; i < numPallets; i++) {
            Container pallet = new Container();
            pallet.initializeBatch(1000, currentDate);
            inputRack.add(pallet);
        }
    }

    private static void simulateSortingForDay(ArrayList<Container> inputRack, ArrayList<Container> oldStock,
                                              ArrayList<Container> newStock, Container bin, ArrayList<Container> outputRack) {

        LocalTime timeOfDay = LocalTime.of(9, 30);
        Worker jason = new Worker("Jason", 0.01, 0.01);
        Table oldTable = new Table(true);
        Table cutoffTable = new Table(true);
        Table newTable = new Table(false);
        Container pallet = new Container();

        while (!timeOfDay.isAfter(LocalTime.of(15, 0))) {
            int maxMinutes = 5;
            if (pallet.isEmpty() && !inputRack.isEmpty()) {
                pallet = inputRack.getFirst();
                inputRack.removeFirst();
                //System.out.println("pallet pulled out");
            }

            if (!pallet.isEmpty() && oldTable.isEmpty() && cutoffTable.isEmpty() && newTable.isEmpty()) {
                LocalDate oldestDate = jason.findOldestDate(pallet.getContents());
                LocalDate newestDate = jason.findNewestDate(pallet.getContents());
                int cutoff = (oldestDate.getYear() + newestDate.getYear()) / 2;

                oldTable.setDates(oldestDate.getYear(), oldestDate.getMonthValue(), oldestDate.getDayOfMonth(), cutoff - 1, 12, 31);
                cutoffTable.setDates(cutoff, 1, 1, cutoff, 12, 31);
                newTable.setDates(cutoff + 1, 1, 1, newestDate.getYear(), newestDate.getMonthValue(), newestDate.getDayOfMonth());

                // Separate items by date
                jason.separateDate(oldTable, pallet.getContents());
                jason.separateDate(cutoffTable, pallet.getContents());
                jason.separateDate(newTable, pallet.getContents());

                // Clear the pallet (leftover items remain unsorted)
                pallet.getContents().clear();
                maxMinutes = 30;
                //System.out.println("Put on tables.");
            }

            if (!oldTable.isEmpty() || !cutoffTable.isEmpty() || !newTable.isEmpty()) {
                jason.separateCategory(oldStock, oldTable, outputRack);
                jason.separateCategory(oldStock, cutoffTable, outputRack);
                jason.separateCategory(newStock, newTable, outputRack);

                // Any items left on tables go to bin as waste
                bin.getContents().addAll(oldTable.getContents());
                bin.getContents().addAll(cutoffTable.getContents());
                bin.getContents().addAll(newTable.getContents());
                oldTable.getContents().clear();
                newTable.getContents().clear();
                cutoffTable.getContents().clear();
                maxMinutes = 90;
                //System.out.println("Put in categories.");
            }
            timeOfDay = timeOfDay.plusMinutes(maxMinutes);
        }
    }

    private static int processDailyShipment(int shipmentSize, LocalDate date, ArrayList<Container> outputRack, Container bin) {
        int count = 0, countExpired = 0, countDefective = 0, itemCount = 0;

        if (outputRack.isEmpty()) {
            return 0;
        }

        System.out.println("  Shipping out " + shipmentSize + " pallets on " + date);
        for (int i = 0; i < shipmentSize && !outputRack.isEmpty(); i++) {
            int index = 0;
            LocalDate oldest = outputRack.get(0).getContents().get(0).getExpDate();
            for (int j = 0; j < outputRack.size(); j++) {
                LocalDate candidate = outputRack.get(j).findOldestDate();
                if (candidate.isBefore(oldest)) {
                    oldest = candidate;
                    index = j;
                }
            }

            Container shipped = outputRack.get(index);
            while (!shipped.isEmpty()) {
                if (shipped.getContents().get(0).getExpDate().isBefore(date)) {
                    bin.addItem(shipped.getContents().get(0));
                    countExpired++;
                } else if (shipped.getContents().get(0).isDefective()) {
                    bin.addItem(shipped.getContents().get(0));
                    countDefective++;
                }
                shipped.getContents().remove(0);
                itemCount++;
            }
            outputRack.remove(index);
            count++;
        }
        System.out.println("     Defective: " + countDefective + " Expired: " + countExpired + " TOTAL ITEMS: " + itemCount);
        return count;
    }
}

