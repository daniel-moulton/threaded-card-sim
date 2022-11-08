package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;

public class Player implements Runnable {

    // current hand

    private final int PLAYER_NUMBER;

    private final String PLAYER_NAME;
    private Card[] hand;

    private PrintStream outputter;

    private static final Object OBJECT_LOCK = new Object();

    private Queue<Card> discardables = new LinkedList<Card>();
    // deck to draw from
    private CardDeck deckDrawnFrom;
    // deck to insert into
    private CardDeck deckInsertedTo;

    public Player(int playerNumber, CardDeck deckDrawnFrom, CardDeck deckInsertedTo) {
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
            this.outputter = new PrintStream(new File(CardGame.gameLocation + "/" + PLAYER_NAME + "_output.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param card
     * @param handPosition
     */
    public void initialHand(Card card, int handPosition) {
        hand[handPosition] = card;
        if (handPosition == 3) {
            appendToFile("Player " + PLAYER_NUMBER + " initial hand is " + handToString());
            findDiscardables();
        }
    }

    private void findDiscardables() {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i].getCardValue() != PLAYER_NUMBER) {
                discardables.add(hand[i]);
            }
        }
    }

    /**
     * @param output
     */
    private void appendToFile(String output) {
        outputter.println(output);
    }

    /**
     * @return String
     */
    private String handToString() {
        String stringHand = "";
        for (Card card : hand) {
            if (card != null) {
                stringHand += card.getCardValue() + " ";
            }
        }
        return stringHand;
    }

    
    /** 
     * @return Card
     */
    public Card drawCard() {
        Card card = deckDrawnFrom.drawCard();
        appendToFile("Player " + PLAYER_NUMBER + " draws a " + card.getCardValue() + " from deck "
                + deckDrawnFrom.getDeckNumber());
        return card;
    }

    /**
     * @param card
     */
    public void removeMostDiscardable() {
        // Remove the card from the discardables
        Card card = discardables.remove();
        // Remove the card from the hand
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == card) {
                hand[i] = null;
            }
        }
        deckInsertedTo.insertCard(card);
        appendToFile("Player " + PLAYER_NUMBER + " discards a " + card.getCardValue() + " to deck "
                + deckInsertedTo.getDeckNumber());
    }

    /**
     * @param drawnCard
     */
    private void updateHand(Card drawnCard) {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null) {
                hand[i] = drawnCard;
                if (drawnCard.getCardValue() != PLAYER_NUMBER) {
                    discardables.add(drawnCard);
                }
                break;
            }
        }
        appendToFile("Player " + PLAYER_NUMBER + " current hand is " + handToString());
    }

    /**
     * @return Boolean
     */
    private Boolean hasWon() {
        for (Card card : hand) {
            if (card.getCardValue() != (hand[0].getCardValue()))
                return false;
        }
        return true;
    }

    /**
     * @return String
     */
    public String getPlayerName() {
        return PLAYER_NAME;
    }

    public void gameWon() {
        CardGame.winningPlayer.set(PLAYER_NUMBER);
        synchronized (OBJECT_LOCK) {
            OBJECT_LOCK.notifyAll();
        }
        System.out.println("Player " + PLAYER_NUMBER + " wins!");
    }

    @Override
    public void run() {
        try {
            CardGame.barrier.await();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (BrokenBarrierException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        while (CardGame.winningPlayer.get() == 0) {
            // deckDrawnFrom.printContentsToFile();
            if (hasWon()) {
                gameWon();
                break;
            }

            while (deckDrawnFrom.getDeckLength() == 0 && CardGame.winningPlayer.get() == 0) {
                try {
                    synchronized (OBJECT_LOCK) {
                        System.out.println("Player " + PLAYER_NUMBER + " waiting for deck "
                                + deckDrawnFrom.getDeckNumber() + " to be filled");
                        OBJECT_LOCK.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
            if (CardGame.winningPlayer.get() != 0) { // dont bother waiting for new cards if its over
                break;
            }
            Card drawnCard = drawCard();
            removeMostDiscardable();
            updateHand(drawnCard);

            if (hasWon()) {
                gameWon();
                break;
            }
            synchronized (OBJECT_LOCK) {
                OBJECT_LOCK.notifyAll();
            }
        }
        System.out.println("player " + PLAYER_NUMBER + " EXITS");
        int winnerNumber = CardGame.winningPlayer.get();
        if (winnerNumber == PLAYER_NUMBER) {
            appendToFile("Player " + PLAYER_NUMBER + " wins");
        } else {
            appendToFile("Player " + winnerNumber + " has informed Player " + PLAYER_NUMBER + " that Player "
                    + winnerNumber + " has won");
        }
        appendToFile("Player " + PLAYER_NUMBER + " exits");

        if (winnerNumber == PLAYER_NUMBER) {
            appendToFile("Player " + PLAYER_NUMBER + " final hand: " + handToString());
        } else {
            appendToFile("Player " + PLAYER_NUMBER + " hand: " + handToString());
        }
        deckDrawnFrom.printContentsToFile();
        outputter.close();
    }
}
