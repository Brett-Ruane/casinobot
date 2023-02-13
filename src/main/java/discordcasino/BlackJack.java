package discordcasino;

import java.io.File;
import java.util.List;

import java.util.ArrayList;

import net.dv8tion.jda.api.utils.FileUpload;

public class BlackJack {

    private static final String[] RANKS = { "ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen",
            "king" };

    private static final String[] SUITS = { "spades", "hearts", "diamonds", "clubs" };

    private static final int[] POINT_VALUES = { 11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10 };

    // private Label dealerTotal;
    private List<FileUpload> dealerCardHolder = new ArrayList<FileUpload>();
    // private Label total;
    // private Label winLoose;
    private List<FileUpload> cardHolder = new ArrayList<FileUpload>();
    // private Button stand;
    // private Button hit;

    private Deck deck = new Deck(RANKS, SUITS, POINT_VALUES);
    private Player one = new Player("Brett", 1);
    private Player dealer = new Player("", 0);
    private List<Card> oneCards = new ArrayList<Card>();
    private List<Card> dealerCards = new ArrayList<Card>();

    public BlackJack() {
        startGame();
    }

    public void startGame() {
        // System.out.println(deck);
        Card dealt = deck.deal();
        oneCards.add(dealt);
        if (dealt.pointValue() == 11)
            one.addAce();
        one.add(dealt.pointValue());
        dealt = deck.deal();
        dealerCards.add(dealt);
        if (dealt.pointValue() == 11)
            dealer.addAce();
        dealer.add(dealt.pointValue());
        dealt = deck.deal();
        oneCards.add(dealt);
        if (dealt.pointValue() == 11)
            one.addAce();
        one.add(dealt.pointValue());
        dealt = deck.deal();
        dealerCards.add(dealt);
        if (dealt.pointValue() == 11)
            dealer.addAce();
        dealer.add(dealt.pointValue());

        File file = new File(dealerCards.get(0).url());
        FileUpload upload = FileUpload.fromData(file);
        dealerCardHolder.add(upload);
        file = new File("GameCards/back1.GIF");
        upload = FileUpload.fromData(file);
        dealerCardHolder.add(upload);

        file = new File(oneCards.get(0).url());
        upload = FileUpload.fromData(file);
        cardHolder.add(upload);
        file = new File(oneCards.get(1).url());
        upload = FileUpload.fromData(file);
        cardHolder.add(upload);

        if (one.total() == 21) {
            stand();
        }
    }

    public String stand() {
        File file = new File(dealerCards.get(1).url());
        FileUpload upload = FileUpload.fromData(file);
        dealerCardHolder.set(1, upload);
        while ((dealer.total() < 17) || (dealer.total() > 17 && dealer.getAce() != 0)) {
            if (dealer.total() > 17 && dealer.getAce() > 0) {
                dealer.subTotal(10);
                dealer.subAce();
            }
            Card dealt = deck.deal();
            dealer.add(dealt.pointValue());
            dealerCards.add(dealt);
            file = new File(dealt.url());
            upload = FileUpload.fromData(file);
            dealerCardHolder.add(upload);
            if (dealt.pointValue() == 11)
                dealer.addAce();
        }
        if ((one.total() > dealer.total()) || (dealer.total() > 21)) {
            // you win
            System.out.println("YOU WIN");
            System.out.println("Dealer Total = " + dealer.total());
            return "win";
        } else if (one.total() == dealer.total()) {
            // tie
            System.out.println("TIE");
            System.out.println("Dealer Total = " + dealer.total());
            return "tie";
        } else {
            // you loose
            System.out.println("YOU LOOSE");
            System.out.println("Dealer Total = " + dealer.total());
            return "lose";
        }
    }

    public String hit() {
        // System.out.println(deck);
        Card dealt = deck.deal();
        one.add(dealt.pointValue());
        oneCards.add(dealt);
        File file = new File(dealt.url());
        FileUpload upload = FileUpload.fromData(file);
        cardHolder.add(upload);
        if (dealt.pointValue() == 11)
            one.addAce();
        if (one.total() > 21 && one.getAce() > 0) {
            one.subTotal(10);
            one.subAce();
        } else if (one.total() > 21) {
            // you loose
            System.out.println("Total = " + one.total());
            System.out.println("BUST");
            System.out.println("YOU LOOSE");
            return "lose";
            // System.exit(1);
        }
        return "";
    }

    public Player getOne() {
        return one;
    }

    public Player getDealer() {
        return dealer;
    }

    public List<FileUpload> getCardsUploads() {
        return cardHolder;
    }

    public List<FileUpload> getDealerCardsUploads() {
        return dealerCardHolder;
    }

    public List<Card> getDealerCards() {
        return dealerCards;
    }
}
