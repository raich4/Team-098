import java.util.ArrayList;

public class sortingProcess {

    public static void main(String[] args) {

        Container pallet = new Container();
        Worker jason = new Worker("Jason", 0.01, 0.01);

        pallet.initializeBatch(10);
        pallet.printContainer();

        // Three sorting tables
        Table oldTable = new Table();
        Table cutoffTable = new Table();
        Table newTable = new Table();

        // Finding the oldest and the newest dates
        int[] oldestDate = jason.findOldestDate(pallet.getContents());
        int[] newestDate = jason.findNewestDate(pallet.getContents());

        System.out.println("Oldest Year: " + oldestDate[0]);
        System.out.println("Newest Year: " + newestDate[0]);

        int cutoff = (oldestDate[0] + newestDate[0]) / 2;

        oldTable.setDates(oldestDate[0], oldestDate[1], oldestDate[2], cutoff - 1, 12, 31);
        cutoffTable.setDates(cutoff, 1, 1, cutoff, 12, 31);
        newTable.setDates(cutoff + 1, 1, 1, newestDate[0], newestDate[1], newestDate[2]);

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

    }

}
