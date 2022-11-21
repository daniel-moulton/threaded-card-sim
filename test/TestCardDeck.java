package test;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import src.Card;
import src.CardDeck;
import src.CardGame;

/**
 * Test class for CardDeck.
 *
 * @author Daniel Moulton
 * @author James Pilcher
 */
public class TestCardDeck {
  private static CardDeck deck;

  @Before
  public void setUp() throws FileNotFoundException {
    CardGame.setGameLocation();
    deck = new CardDeck(1);
  }

  @Test
  public void testGetDeckLength() {
    assertEquals("Deck not empty", 0, deck.getDeckLength());
  }

  @Test
  public void testCardDeckInsert() {
    deck.insertCard(new Card(5));
    assertEquals("Card values incorrect", 5, deck.drawCard().getCardValue());
  }

  @Test
  public void testCardDeckRemove() {
    deck.insertCard(new Card(5));
    deck.insertCard(new Card(6));
    deck.insertCard(new Card(7));
    deck.insertCard(new Card(8));
    Card removed = deck.drawCard();
    assertEquals("Card values incorrect", 5, removed.getCardValue());
  }

  @After
  public void tearDown() {
    deck = null;

  }
}