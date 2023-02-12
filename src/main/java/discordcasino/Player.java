package discordcasino;

public class Player {

    private static String name = "";

    private int cardTotal = 0;

    private int ace = 0;

    // private int bet; // for when i add "money" bets

    // private int money; // ^^^^^

    public Player(String n, int key) { // key == 0 is dealer
        if (key == 0) {
            name = "Dealer";
        } else {
            name = n;
        }
    }

    public void add(int points) {
        cardTotal += points;
    }

    public int total() {
        return cardTotal;
    }

    public void subTotal(int n) {
        cardTotal -= n;
    }

    public static String getName() {
        return name;
    }

    public void addAce() {
        ace += 1;
    }

    public void subAce() {
        ace -= 1;
    }

    public int getAce() {
        return ace;
    }
}
