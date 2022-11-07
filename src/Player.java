package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

public class Player implements Runnable{

    // current hand

    private final int PLAYER_NUMBER;

    private final String PLAYER_NAME;
    private Card[] hand;

    private PrintStream outputter;

    private Queue<Card> discardables = new LinkedList<Card>();
    // deck to draw from
    private CardDeck deckDrawnFrom;
    // deck to insert into
    private CardDeck deckInsertedTo;

    public Player(int playerNumber, CardDeck deckDrawnFrom, CardDeck deckInsertedTo, String gameLocation){
        // Initialise the player's number
        this.PLAYER_NUMBER = playerNumber;

        this.PLAYER_NAME = "player" + PLAYER_NUMBER;
        // Initialise the player's hand
        this.hand = new Card[4];
        // Initialise the deck to draw from
        this.deckDrawnFrom = deckDrawnFrom;
        // Initialise the deck to insert into
        this.deckInsertedTo = deckInsertedTo;

        try {
            this.outputter = new PrintStream(new File(gameLocation + "/" + PLAYER_NAME + "_output.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialHand(Card card, int handPosition){
        hand[handPosition] = card;
        if (handPosition == 3){
            appendToFile("Player " + PLAYER_NUMBER + " initial hand is " + handToString());
            findDiscardables();
        }
    }

    public void findDiscardables(){
        for (int i = 0; i < hand.length; i++) {
            if (hand[i].getCardValue() != PLAYER_NUMBER){
                discardables.add(hand[i]);
            }
        }
    }

    public void appendToFile(String output){
        outputter.println(output);
    }

    public String handToString(){
        String stringHand = "";
        for (Card card : hand){
            if (card != null){
                stringHand += card.getCardValue() + " ";
            }
        }
        return stringHand;
    }

    public Card drawCard(){
        Card card = deckDrawnFrom.drawCard();
        appendToFile("Player " + PLAYER_NUMBER + " draws a " + card.getCardValue() + " from deck " + deckDrawnFrom.getDeckNumber());
        return card;
    }

    //   from discardables remove front of queue, remove from hand (empty slot 0), place into next deck,
    public void removeMostDiscardable(){
        // Remove the card from the discardables
        Card card = discardables.remove();
        // Remove the card from the hand
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == card){
                hand[i] = null;
            }
        }
        deckInsertedTo.insertCard(card);
        appendToFile("Player " + PLAYER_NUMBER + " discards a " + card.getCardValue() + " to deck " + deckInsertedTo.getDeckNumber());
    }   

    public void updateHand(Card drawnCard) {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null){
                hand[i] = drawnCard;
                if (drawnCard.getCardValue() != PLAYER_NUMBER){
                    discardables.add(drawnCard);
                }
                break;
            }
        }
        appendToFile("Player " + PLAYER_NUMBER + " current hand is " + handToString());
    }

    public Boolean hasWon(){
        for (Card card : hand) {
            if (card.getCardValue() != (hand[0].getCardValue()))
                return false;
        }
        return true;
    }

    public String getPlayerName() {
        return PLAYER_NAME;
    }

    public void gameWon(){
        CardGame.winningPlayer.set(PLAYER_NUMBER);
        System.out.println("Player " + PLAYER_NUMBER + " wins!");
    }
    @Override
    public void run(){
        while (CardGame.winningPlayer.get()==0)
        {
            if (hasWon()){
                gameWon();
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
                Card drawnCard = drawCard();
                removeMostDiscardable();
                updateHand(drawnCard);

                if (hasWon()){
                    gameWon();
                }
                synchronized (this){
                    this.notifyAll();
                }
            }
        }
        int winnerNumber = CardGame.winningPlayer.get();
        if (winnerNumber == PLAYER_NUMBER){
            appendToFile("Player " + PLAYER_NUMBER + " wins");
        }
        else{
            appendToFile("Player " + winnerNumber + " has informed Player " + PLAYER_NUMBER + " that Player " + winnerNumber + " has won");
        }
        appendToFile("Player " + PLAYER_NUMBER + " exits");

        if (winnerNumber == PLAYER_NUMBER){
            appendToFile("Player " + PLAYER_NUMBER + " final hand: " + handToString());
        }
        else{
            appendToFile("Player " + PLAYER_NUMBER + " hand: " + handToString());
        }
        deckDrawnFrom.getDeckContentsAsString();
        deckInsertedTo.getDeckContentsAsString();
        outputter.close();
    }
}
