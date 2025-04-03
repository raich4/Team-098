import java.time.LocalDate;
import java.util.Random;

public class Item {
    private String type;
    private LocalDate expiryDate; // Using LocalDate instead of int[] for the expiry date
    private boolean isDefective;

    // No argument constructor for an item. This randomizes every value in a reasonable way.
    public Item() {
        Random rand = new Random();

        // Assign item a random type, from the GlobalInfoHelper class.
        // We are only taking into account the items with an expiry date.
        this.type = GlobalInfoHelper.Categories[rand.nextInt(39)];

        // Assigning each item a random expiry date between 2024 and 2026.
        int year = rand.nextInt(3) + 2024;         // 2024, 2025, or 2026
        int month = rand.nextInt(12) + 1;            // 1 to 12
        // Create a temporary date to determine the maximum day in that month.
        int maxDay = LocalDate.of(year, month, 1).lengthOfMonth();
        int day = rand.nextInt(maxDay) + 1;          // 1 to maxDay

        this.expiryDate = LocalDate.of(year, month, day);

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
