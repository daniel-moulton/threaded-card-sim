package src;

import java.util.LinkedList;
import java.util.Queue;

public class Player extends WritesToFile {

    // current hand

    private final int PLAYER_NUMBER;

    private final String playerName;
    private Card[] hand;

    private Card drawnCard;

    private Queue<Card> discardables = new LinkedList<Card>();
    // deck to draw from
    private CardDeck deckDrawnFrom;
    // deck to insert into
    private CardDeck deckInsertedTo;
    // pass in deck into constructor


    // player number (determined by name)
    // player name 
    // folder to print to



    public Player(int playerNumber, CardDeck deckDrawnFrom, CardDeck deckInsertedTo){
        // Initialise the player's number
        this.PLAYER_NUMBER = playerNumber;

        this.playerName = "player " + PLAYER_NUMBER;
        // Initialise the player's hand
        this.hand = new Card[4];
        // Initialise the deck to draw from
        this.deckDrawnFrom = deckDrawnFrom;
        // Initialise the deck to insert into
        this.deckInsertedTo = deckInsertedTo;
    }
    // in constructor, 
    // current folder
    // create a file for them. (using their name)



    public void initialiseDiscardables(Card[] hand){
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
    public void removeCard(Queue<Card> discardables, Card[] hand, CardDeck deckInsertedTo){
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

    public void placeCardInHand(Card drawncard) {
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] == null){
                hand[i] = drawncard;
                if (drawncard.getCardValue() != PLAYER_NUMBER){
                    discardables.add(drawncard);
                }
            }
        }
    }

    public Boolean checkWinCondition(Card[] hand){
        for (Card card : hand) {
            if (!card.equals(hand[0]))
                return false;
        }
        return true;
    }

    // close file

    // function: writes deck contents files


    // method that checks if their hand wins

}
