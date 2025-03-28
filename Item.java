public class Item {
    String name;
    String type;
    int[] expiryDate = new int[3];
    boolean isDefective;
    int timeToSort = 3; // This is the default time (in seconds) that is needed to sort each item.
                        // If required, we can do research and put this in.

    public Item(String name, String type, int expYr, int expMon, int expDay, boolean isDefective) {
        this.name = name;
        this.type = type;
        this.expiryDate[0] = expYr;
        this.expiryDate[1] = expMon;
        this.expiryDate[2] = expDay;
        this.isDefective = isDefective;
    }

}


