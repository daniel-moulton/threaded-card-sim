package src;

/**
 * Represents a card in the game.
 *
 * @author Daniel Moulton
 * @author James Pilcher
 */
public class Card {
  private final int cardValue;

  public Card(final int value) {
    // Initialise the card's value
    cardValue = value;
  }


  /**
   * Getter for the value of the card.
   *
   * @return the denomination/value of the card
   */
  public int getCardValue() {
    return cardValue;
  }
}
