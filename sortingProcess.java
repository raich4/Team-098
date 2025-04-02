import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class sortingProcess {

    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);
            DecimalFormat df = new DecimalFormat("#.##");

            System.out.println("Enter number of iterations: ");
            int iterations = scanner.nextInt();
            System.out.println("Enter number of items: ");
            int numItems = scanner.nextInt();

            double error = 0;

            for (int i = 0; i < iterations; i++) {
                    Container pallet = new Container();
                    Worker jason = new Worker("Jason", 0.02, 0.02);


                    pallet.initializeBatch(numItems);
                    //pallet.printContainer();

                    // Three sorting tables
                    Table oldTable = new Table();
                    Table cutoffTable = new Table();
                    Table newTable = new Table();

                    // Finding the oldest and the newest dates
                    int[] oldestDate = jason.findOldestDate(pallet.getContents());
                    int[] newestDate = jason.findNewestDate(pallet.getContents());

                    // System.out.println("Oldest Year: " + oldestDate[0]);
                    // System.out.println("Newest Year: " + newestDate[0]);

                    int cutoff = (oldestDate[0] + newestDate[0]) / 2;

                    oldTable.setDates(oldestDate[0], oldestDate[1], oldestDate[2], cutoff - 1, 12, 31);
                    cutoffTable.setDates(cutoff, 1, 1, cutoff, 12, 31);
                    newTable.setDates(cutoff + 1, 1, 1, newestDate[0], newestDate[1], newestDate[2]);

                    //System.out.println("Cutoff year: " + cutoff);

                    jason.separateDate(oldTable, pallet.getContents());
                    jason.separateDate(cutoffTable, pallet.getContents());
                    jason.separateDate(newTable, pallet.getContents());

                    ArrayList<Container> oldStock = new ArrayList<>();
                    ArrayList<Container> newStock = new ArrayList<>();
                    for (int j = 0; j < GlobalInfoHelper.Categories.length; j++) {
                            oldStock.add(new Container(GlobalInfoHelper.Categories[j]));
                            newStock.add(new Container(GlobalInfoHelper.Categories[j]));
                    }


                    /*System.out.println("OLD: ");
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
                    */

                    jason.separateCategory(oldStock, oldTable.getContents());
                    jason.separateCategory(oldStock, cutoffTable.getContents());

                    jason.separateCategory(newStock, newTable.getContents());

                    int itemsMissed = pallet.getContents().size() + oldTable.getContents().size() + cutoffTable.getContents().size() + newTable.getContents().size();
                    error += ((double)itemsMissed / numItems) * 100;

                    System.out.println("ITERATION #" + i);
                    System.out.println("OUTPUT: " + (numItems - itemsMissed) + " ITEMS");
                    System.out.println("ERROR: " + ((double)itemsMissed / numItems) * 100 );
            }

            System.out.println("Error rate: " + df.format(error / iterations) + "%.");

    }

}
