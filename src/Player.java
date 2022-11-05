package src;

import java.util.LinkedList;
import java.util.Queue;

public class Player {

    // current hand


    private Card[] hand;

    private Queue<Card> discardables = new LinkedList<Card>();


    // deck to draw from
    private CardDeck deckDrawnFrom;
    // deck to insert into
    private CardDeck deckInsertedTo;
    // pass in deck into constructor


    // player number (determined by name)
    private final int PLAYER_NUMBER;
    // player name 
    // folder to print to



    public Player(int playerNumber, CardDeck deckDrawnFrom, CardDeck deckInsertedTo){
        // Initialise the player's number
        PLAYER_NUMBER = playerNumber;
        // Initialise the player's hand
        hand = new Card[4];
        // Initialise the deck to draw from
        this.deckDrawnFrom = deckDrawnFrom;
        // Initialise the deck to insert into
        this.deckInsertedTo = deckInsertedTo;
    }
    // in constructor, 
    // current folder
    // create a file for them. (using their name)


    // function: append string to their output file

    // chooseDiscardables


    // drawCard
    //     alter drawn card variable, picking up from deck

    // removeCard:
    //   from discardables remove front of queue, remove from hand (empty slot 0), place into next deck,

    // place drawncard:
    //   add drawncard to empty slot in hand, if does not match desired number add to discardables


    // close file

    // function: writes deck contents files


    // method that checks if their hand wins

}
