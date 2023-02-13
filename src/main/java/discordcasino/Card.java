package discordcasino;

public class Card {

    private String suit;

    private String rank;

    private int pointValue;

    public Card(String cardRank, String cardSuit, int cardPointValue) {
        rank = cardRank;
        suit = cardSuit;
        pointValue = cardPointValue;
    }

    public String suit() {
        return suit;
    }

    public String rank() {
        return rank;
    }

    public int pointValue() {
        return pointValue;
    }

    public String url() {
        return "GameCards/" + rank() + suit() + ".GIF";
    }

    public boolean matches(Card otherCard) {
        return otherCard.suit().equals(this.suit())
                && otherCard.rank().equals(this.rank())
                && otherCard.pointValue() == this.pointValue();
    }

    @Override
    public String toString() {
        return rank + " of " + suit + " (point value = " + pointValue + ")";
    }
}