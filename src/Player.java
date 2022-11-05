package src;
public class Player {
    // current hand
    private CardDeck hand;
    // player number (determined by name)
    private final int PLAYER_NUMBER;
    // player name 
    // folder to print to

    // deck to draw from
    private CardDeck deckDrawnFrom;
    // deck to insert into
    private CardDeck deckInsertedTo;
    // pass in deck into constructor

    public Player(int playerNumber, CardDeck deckDrawnFrom, CardDeck deckInsertedTo){
        // Initialise the player's number
        PLAYER_NUMBER = playerNumber;
        // Initialise the player's hand
        hand = new CardDeck();
        // Initialise the deck to draw from
        this.deckDrawnFrom = deckDrawnFrom;
        // Initialise the deck to insert into
        this.deckInsertedTo = deckInsertedTo;
    }
    // in constructor, 
    // current folder
    // create a file for them. (using their name)


    // function: append string to their output file


    // close file


}
