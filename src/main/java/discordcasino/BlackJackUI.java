import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;
import javafx.beans.binding.Bindings;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

public class BlackJackUI extends Application {

    private static final String[] RANKS = { "ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen",
            "king" };

    private static final String[] SUITS = { "spades", "hearts", "diamonds", "clubs" };

    private static final int[] POINT_VALUES = { 11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10 };

    private int count = 2;
    private int count2 = 2;

    private Stage stage;
    private Scene scene;
    private VBox root;

    // top
    private HBox topRow;
    private Label dealerTotal;

    // dealer cards
    private HBox dealerCardRow;
    private ImageView dealerCardHolder[] = new ImageView[5];

    // your cards
    private HBox yourCardsRow;
    private Label yourCards;
    private Label winLoose;

    // where cards go
    private HBox cardRow;
    private ImageView cardHolder[] = new ImageView[5];

    // total and buttons
    private HBox bottomRow;
    private Label spacer;
    private Label total;
    private Button stand;
    private Button hit;

    private Deck deck = new Deck(RANKS, SUITS, POINT_VALUES);
    private Player one = new Player("Brett", 1);
    private Player dealer = new Player("", 0);
    private List<Card> oneCards = new ArrayList<Card>();
    private List<Card> dealerCards = new ArrayList<Card>();

    public BlackJackUI() {
        this.stage = null;
        this.scene = null;
        this.root = new VBox(3);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BlackJackUI app = new BlackJackUI();
        System.out.println(deck);
        this.stage = stage;
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

        // topRow
        topRow = new HBox(4);
        dealerTotal = new Label("Dealers Cards");
        dealerTotal.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        topRow.getChildren().addAll(dealerTotal);

        // dealer cards
        dealerCardRow = new HBox(4);
        for (int i = 0; i < dealerCardHolder.length; i++)
            dealerCardHolder[i] = new ImageView("GameCards/white.GIF");
        dealerCardRow.setPadding(new Insets(20));
        dealerCardHolder[0] = new ImageView(dealerCards.get(0).url());
        dealerCardHolder[1] = new ImageView("GameCards/back1.GIF");
        for (int i = 0; i < dealerCardHolder.length; i++)
            dealerCardRow.getChildren().add(dealerCardHolder[i]);

        // your cards
        yourCardsRow = new HBox(4);
        yourCards = new Label("Your Cards");
        yourCards.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        winLoose = new Label("");
        winLoose.setPadding(new Insets(0, 0, 0, 30));
        winLoose.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        yourCardsRow.getChildren().addAll(yourCards, winLoose);

        // test
        for (int i = 0; i < cardHolder.length; i++)
            cardHolder[i] = new ImageView("GameCards/white.GIF");
        cardHolder[0] = new ImageView(oneCards.get(0).url());
        cardHolder[1] = new ImageView(oneCards.get(1).url());

        // cardRow
        cardRow = new HBox(4);
        cardRow.setPadding(new Insets(20));
        for (int i = 0; i < cardHolder.length; i++)
            cardRow.getChildren().add(cardHolder[i]);

        // bottomRow
        bottomRow = new HBox(4);
        total = new Label("Total = " + one.total());
        total.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        Label spacer2 = new Label("");
        spacer2.setPadding(new Insets(0, 30, 0, 15));
        stand = new Button("Stand");
        stand.setOnAction(this::stand);
        stand.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        stand.setPadding(new Insets(15));
        spacer = new Label("");
        spacer.setPadding(new Insets(0, 30, 0, 15));
        hit = new Button("Hit");
        hit.setOnAction(this::hit);
        hit.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        hit.setPadding(new Insets(15));
        bottomRow.getChildren().addAll(total, spacer2, stand, spacer, hit);

        if (one.total() == 21) {
            ActionEvent a = new ActionEvent();
            stand(a);
        }

        // adding all to root
        root.getChildren().addAll(topRow, dealerCardRow, yourCardsRow, cardRow, bottomRow);
        root.setSpacing(0);
        // setting scene
        this.scene = new Scene(this.root);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("Casino");
        this.stage.setScene(this.scene);
        this.stage.setMinWidth(500);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    private void stand(ActionEvent a) {
        dealerCardHolder[1].setImage(new Image(dealerCards.get(1).url()));
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
            dealerCardHolder[count2].setImage(new Image(dealt.url()));
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
        cardHolder[count].setImage(new Image(dealt.url()));
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
