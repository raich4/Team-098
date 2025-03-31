import java.util.Random;

public class Item {
    private String type;
    private int[] expiryDate = new int[3]; // Date format is in: {Year, Month, Day}
    private boolean isDefective;

    // No argument constructor for an item. This randomizes every value in a reasonable way.
    // NEEDS CONFIRMATION
    public Item() {
        Random rand = new Random();

        // Assign item a random type, from the GlobalInfoHelper class.
        // We are only taking into account the items with an expiry date.
        this.type = GlobalInfoHelper.Categories[rand.nextInt(39)];

        // Assigning each item a random expiry date between 2024 and 2026.
        // This also takes into account the number of days in a month, including leap years.
        // 2024 is a leap year.
        this.expiryDate[0] = rand.nextInt(3) + 2024;
        this.expiryDate[1] = rand.nextInt(12) + 1;
        if (this.expiryDate[1] == 2) {
            if (this.expiryDate[0] == 2024) {
                this.expiryDate[2] = rand.nextInt(29) + 1;
            }
            else {
                this.expiryDate[2] = rand.nextInt(28) + 1;
            }
        }
        else if (this.expiryDate[1] == 4 || this.expiryDate[1] == 6 || this.expiryDate[1] == 9 || this.expiryDate[1] == 11) {
            this.expiryDate[2] = rand.nextInt(30) + 1;
        }
        else {
            this.expiryDate[2] = rand.nextInt(31) + 1;
        }

        //Assume every item has a 1% chance to be defective (NEEDS CONFIRMATION)
        this.isDefective = (rand.nextInt(100) == 0);
    }

    public Item(String type, int expYr, int expMon, int expDay, boolean isDefective) {
        this.type = type;
        this.expiryDate[0] = expYr;
        this.expiryDate[1] = expMon;
        this.expiryDate[2] = expDay;
        this.isDefective = isDefective;
    }

    public int[] getExpDate(){
        return this.expiryDate;
    }

    public String getType() {
        return this.type;
    }

    public boolean isDefective() {
        return this.isDefective;
    }

}


