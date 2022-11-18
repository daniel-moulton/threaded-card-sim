package test;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import src.Card;
import src.CardDeck;
import src.Player;

/**
 * Test class for Player.
 *
 * @author Daniel Moulton
 * @author James Pilcher
 */
public class TestPlayer {
  private static Player player;
  private static CardDeck deckDrawnFrom;
  private static CardDeck deckInsertedTo;

  @Before
  public void setUp() {
    deckDrawnFrom = new CardDeck(1);
    deckInsertedTo = new CardDeck(2);
    player = new Player(1, deckDrawnFrom, deckInsertedTo);
  }

  @Test
  public void testFindDiscardables() {
    Card card1 = new Card(1);
    Card card2 = new Card(2);
    Card card3 = new Card(3);
    Card card4 = new Card(4);
    player.initialHand(card1, 0);
    player.initialHand(card2, 1);
    player.initialHand(card3, 2);
    player.initialHand(card4, 3);
    Queue<Card> expectedDiscard = new LinkedList<>();
    expectedDiscard.add(card2);
    expectedDiscard.add(card3);
    expectedDiscard.add(card4);
    Queue<Card> toDiscard = player.getDiscardables();
    assertEquals("Discardable values inccorrect", expectedDiscard, toDiscard);
  }

  @After
  public void tearDown() {
    player = null;
  }
}
