package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a deck of cards in the game.
 *
 * @author Daniel Moulton
 * @author James Pilcher
 */
public class CardDeck {

  // Queue of the cards in the deck (FIFO)
  private Queue<Card> contents = new ConcurrentLinkedQueue<Card>();
  private final int deckNumber;
  private PrintStream outputter;

  /**
   * Constructor for the CardDeck class.
   *
   * @param deckNumber the number of the deck
   */
  public CardDeck(int deckNumber) {
    contents = new LinkedList<Card>();
    this.deckNumber = deckNumber;
    try {
      this.outputter = new PrintStream(new File(CardGame.gameLocation + "/" 
        + "deck" + deckNumber + "_output.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }


  /** 
   * Getter for the deck number.
   *
   * @return the deck number
   */
  public int getDeckNumber() {
    return deckNumber;
  }


  /** 
   * Inserts a card into the deck.
   *
   * @param card the card to insert
   */
  public void insertCard(Card card) {
    contents.add(card);
  }


  /** 
   * Removes the top card from the deck and returns it.
   *
   * @return the top card from the deck
   */
  public Card drawCard() {
    return contents.remove();
  }


  /** 
   * Getter for the length of the deck.
   *
   * @return number of cards in the deck (length of deck)
   */
  public int getDeckLength() {
    return contents.size();
  }


  /** 
   * Returns the values of the cards in the deck as a string.
   *
   * @return string of card values
   */
  private String getDeckContentsAsString() {
    String output = "";
    for (Card card : contents) { 
      output += card.getCardValue() + " "; 
    }
    return output;
  }

  /**
   * Appends the contents of the deck to the deck's output file.
   */
  public void printContentsToFile() {
    String output = getDeckContentsAsString();
    outputter.println("Deck Contents: " + output);
    outputter.close();
  }
}