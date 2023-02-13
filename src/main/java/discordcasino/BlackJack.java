package discordcasino;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;

import java.util.ArrayList;

import net.dv8tion.jda.api.utils.FileUpload;

public class BlackJack {

    private static final String[] RANKS = { "ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen",
            "king" };

    private static final String[] SUITS = { "spades", "hearts", "diamonds", "clubs" };

    private static final int[] POINT_VALUES = { 11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10 };

    private int count = 2;
    private int count2 = 2;

    private Label dealerTotal;
    private FileUpload dealerCardHolder[] = new FileUpload[5];
    private Label total;
    private Label winLoose;
    private FileUpload cardHolder[] = new FileUpload[5];
    private Button stand;
    private Button hit;

    private Deck deck = new Deck(RANKS, SUITS, POINT_VALUES);
    private Player one = new Player("Brett", 1);
    private Player dealer = new Player("", 0);
    private List<Card> oneCards = new ArrayList<Card>();
    private List<Card> dealerCards = new ArrayList<Card>();

    @Override
    public void startGame() {
        System.out.println(deck);
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

        // dealer cards
        for (int i = 0; i < dealerCardHolder.length; i++) {
            // InputStream is =
            // getClass().getClassLoader().getResourceAsStream("GameCards/white.GIF");
            File file = new File("GameCards/white.GIF");
            FileUpload upload = FileUpload.fromData(file);
            dealerCardHolder[i] = upload;
        }
        File file = new File(dealerCards.get(0).url());
        FileUpload upload = FileUpload.fromData(file);
        dealerCardHolder[0] = upload;
        file = new File("GameCards/back1.GIF");
        upload = FileUpload.fromData(file);
        dealerCardHolder[1] = upload;

        // filling blanks
        for (int i = 0; i < cardHolder.length; i++) {
            file = new File("GameCards/white.GIF");
            upload = FileUpload.fromData(file);
            cardHolder[i] = upload;
        }
        file = new File(oneCards.get(0).url());
        upload = FileUpload.fromData(file);
        cardHolder[0] = upload;
        file = new File(oneCards.get(1).url());
        upload = FileUpload.fromData(file);
        cardHolder[1] = upload;

        if (one.total() == 21) {
            ActionEvent a = new ActionEvent();
            stand(a);
        }
    }

    private void stand(ActionEvent a) {
        File file = new File(dealerCards.get(1).url());
        FileUpload upload = FileUpload.fromData(file);
        dealerCardHolder[1] = upload;
        System.out.println(deck);
        hit.setDisable(true);
        stand.setDisable(true);
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
            dealerCardHolder[count2] = upload;
            count2++;
            if (dealt.pointValue() == 11)
                dealer.addAce();
        }
        if ((one.total() > dealer.total()) || (dealer.total() > 21)) {
            winLoose.setText("YOU WIN");
            winLoose.setTextFill(Color.color(0, 1, 0));
            System.out.println("YOU WIN");
            System.out.println("Dealer Total = " + dealer.total());
        } else if (one.total() == dealer.total()) {
            winLoose.setText("TIE");
            winLoose.setTextFill(Color.color(1, .5, 0));
            System.out.println("TIE");
            System.out.println("Dealer Total = " + dealer.total());
        } else {
            winLoose.setText("YOU LOOSE");
            winLoose.setTextFill(Color.color(1, 0, 0));
            System.out.println("YOU LOOSE");
            System.out.println("Dealer Total = " + dealer.total());
        }
    }

    private void hit(ActionEvent a) {
        System.out.println(deck);
        Card dealt = deck.deal();
        one.add(dealt.pointValue());
        total.setText("Total = " + one.total());
        oneCards.add(dealt);
        File file = new File(dealt.url());
        FileUpload upload = FileUpload.fromData(file);
        cardHolder[count] = upload;
        count++;
        if (dealt.pointValue() == 11)
            one.addAce();
        if (one.total() > 21 && one.getAce() > 0) {
            one.subTotal(10);
            one.subAce();
            total.setText("Total = " + one.total());
        } else if (one.total() > 21) {
            winLoose.setText("YOU LOOSE");
            winLoose.setTextFill(Color.color(1, 0, 0));
            System.out.println("Total = " + one.total());
            System.out.println("BUST");
            System.out.println("YOU LOOSE");
            hit.setDisable(true);
            stand.setDisable(true);
            // System.exit(1);
        }
    }
}
