import java.time.LocalDate;
import java.util.Random;
import java.time.temporal.ChronoUnit;

public class Item {
    private String type;
    private LocalDate expiryDate; // Using LocalDate instead of int[] for the expiry date
    private boolean isDefective;

    // No argument constructor for an item. This randomizes every value in a reasonable way.
    public Item(LocalDate currentDate) {
        Random rand = new Random();

        // Assign item a random type, from the GlobalInfoHelper class.
        // We are only taking into account the items with an expiry date.
        this.type = GlobalInfoHelper.Categories[rand.nextInt(39)];

        // Define the range: from tomorrow until three years from currentDate
        LocalDate start = currentDate.plusDays(1);
        LocalDate end = currentDate.plusYears(3);

        // Calculate the number of days between start and end.
        long daysBetween = ChronoUnit.DAYS.between(start, end);

        // Pick a random number of days in that interval.
        long randomOffset = rand.nextInt((int) daysBetween + 1);

        // Set the expiry date.
        this.expiryDate = start.plusDays(randomOffset);

        // Assume every item has a 1% chance to be defective.
        this.isDefective = (rand.nextInt(100) == 0);
    }

    public Item(String type, int expYr, int expMon, int expDay, boolean isDefective) {
        this.type = type;
        this.expiryDate = LocalDate.of(expYr, expMon, expDay);
        this.isDefective = isDefective;
    }

    public LocalDate getExpDate() {
        return this.expiryDate;
    }

    public String getType() {
        return this.type;
    }

    public boolean isDefective() {
        return this.isDefective;
    }

    public void printItem() {
        System.out.println("<" + this.type + ", EXP: " + expiryDate + ", isDefective: " + isDefective + ">");
    }
}
