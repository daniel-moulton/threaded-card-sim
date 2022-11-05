package src;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Player extends WritesToFile implements Runnable{

    // current hand

    private final int PLAYER_NUMBER;

    private final String PLAYER_NAME;
    private Card[] hand;

    private Card drawnCard;

    private File outputFile;

    private Queue<Card> discardables = new LinkedList<Card>();
    // deck to draw from
    private CardDeck deckDrawnFrom;
    // deck to insert into
    private CardDeck deckInsertedTo;
    // pass in deck into constructor


    // player number (determined by name)
    // player name 
    // folder to print to



    public Player(int playerNumber, CardDeck deckDrawnFrom, CardDeck deckInsertedTo, String gameLocation){
        // Initialise the player's number
        this.PLAYER_NUMBER = playerNumber;

        this.PLAYER_NAME = "player " + PLAYER_NUMBER;
        // Initialise the player's hand
        this.hand = new Card[4];
        // Initialise the deck to draw from
        this.deckDrawnFrom = deckDrawnFrom;
        // Initialise the deck to insert into
        this.deckInsertedTo = deckInsertedTo;

        this.outputFile = new File(gameLocation + "/" + PLAYER_NAME + ".txt");  //io exceptions here?
    }
    // in constructor, 
    // current folder
    // create a file for them. (using their name)

    public void dealCard(Card card){
        hand[0] = (card);
    }

    public void appendToFile(){
        // outputFile.write()
    }


    public void initialiseDiscardables(){
        for (Card card : hand) {
            if (card.getCardValue() != PLAYER_NUMBER){
                discardables.add(card);
            }
        }
    }


    public void drawCard(){
        drawnCard = deckDrawnFrom.drawCard();
    }

    // removeCard:
    //   from discardables remove front of queue, remove from hand (empty slot 0), place into next deck,
    public void removeCard(){
        // Remove the card from the discardables
        Card card = discardables.remove();
        // Remove the card from the hand
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == card){
                hand[i] = null;
            }
        }
        deckInsertedTo.insertCard(card);
    }   

    public void placeCardInHand(Card drawnCard) {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null){
                hand[i] = drawnCard;
                if (drawnCard.getCardValue() != PLAYER_NUMBER){
                    discardables.add(drawnCard);
                }
                break;
            }
        }
    }

    public Boolean checkWinCondition(){
        for (Card card : hand) {
            if (card.getCardValue() != (hand[0].getCardValue()))
                return false;
        }
        return true;
    }

    public void showCards(){
        // for (Card card : hand) {
        //     System.out.print(card.getCardValue());
        // }
        // Print the value of each card in hand in one single print statement
        System.out.print(PLAYER_NAME + "'s cards: \n " + hand[0].getCardValue() + "," + hand[1].getCardValue() + "," + hand[2].getCardValue() + "," + hand[3].getCardValue() + "\n");
    }



    public String getPlayerName() {
        return PLAYER_NAME;
    }

    @Override
    public void run(){
        while (CardGame.winningPlayer.get()==0)
        {
            if (checkWinCondition()){
                CardGame.winningPlayer.set(PLAYER_NUMBER);
                System.out.println("Player " + PLAYER_NUMBER + " wins!");
            }
            // if the deck drawn from is empty the thread waits until it is notified by the previous player
            else if (deckDrawnFrom.getDeckLength() == 0){
                synchronized (this){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                showCards();
                System.out.println("Drawing card for " + PLAYER_NAME);
                drawCard();
                removeCard();
                placeCardInHand(drawnCard);
                showCards();
                System.out.println("Player " + PLAYER_NUMBER + " TURN OVER");
                synchronized (this){
                    this.notifyAll();
                }
            }
        }
        // showCards();
        // drawCard();
        // removeCard();
        // placeCardInHand(drawnCard);
        // showCards();
    }

    // close file

    // function: writes deck contents files


    // method that checks if their hand wins

}
