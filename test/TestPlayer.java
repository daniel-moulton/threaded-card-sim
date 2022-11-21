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
import src.CardGame;

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
  private static Card card1;
  private static Card card2;
  private static Card card3;
  private static Card card4;

  @Before
  public void setUp() {
    deckDrawnFrom = new CardDeck(1);
    deckInsertedTo = new CardDeck(2);
    CardGame.setGameLocation();
    player = new Player(1, deckDrawnFrom, deckInsertedTo);
    card1 = new Card(1);
    card2 = new Card(2);
    card3 = new Card(3);
    card4 = new Card(4);
  }

  @Test
  public void testFindDiscardables() {
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

  @Test
  public void testRemoveMostDiscardable() {
    player.initialHand(card1, 0);
    player.initialHand(card2, 1);
    player.initialHand(card3, 2);
    player.initialHand(card4, 3);
    player.removeMostDiscardable();
    System.out.println(player.handToString());
    assertEquals("The most discardable card was not removed",
        "1 3 4 ", player.handToString());
  }

  @Test
  public void testDrawCard() {
    deckDrawnFrom.insertCard(card1);
    deckDrawnFrom.insertCard(card2);
    deckDrawnFrom.insertCard(card3);
    deckDrawnFrom.insertCard(card4);
    Card drawnCard = player.drawCard();
    assertEquals("Incorrect card drawn", 1, drawnCard.getCardValue());
  }

  @Test
  public void updateHandDiscardableCard(){
    player.initialHand(card1,0);
    player.initialHand(new Card(100),1);
    player.initialHand(card3,2);
    player.initialHand(card4,3);
    player.removeMostDiscardable();
    deckDrawnFrom.insertCard(card2);
    player.updateHand(player.drawCard());
    assertEquals("Card not inserted in correct slot", "1 2 3 4 ", player.handToString());
  }

  @After
  public void tearDown() {
    player = null;
  }
}
