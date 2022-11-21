package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.concurrent.BrokenBarrierException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import src.Card;
import src.CardDeck;
import src.CardGame;
import src.Player;

/**
 * Test class for CardGame.
 *
 * @author Daniel Moulton
 * @author James Pilcher
 */
public class TestCardGame {
  public static CardGame game;
  public static int numPlayers;
  public static Card[] cards;

  @Before
  public void setUp() throws InterruptedException, BrokenBarrierException {
    numPlayers = 5;

  }

  @Test
  public void testIsValidPackFileValidFile() {
    String filePath = "test/valid5PlayersPack.txt";
    assertTrue("Valid file not recognised as valid", CardGame.isValidPackFile(
        filePath, numPlayers));
  }

  @Test
  public void testIsValidPackFileNonIntegerCard() {
    String filePath = "test/invalidString5PlayersPack.txt";
    // Console message should read "ERROR: Pack file contains a non-integer"
    assertFalse("Non-integer card not recognised as invalid", CardGame.isValidPackFile(
        filePath, numPlayers));
  }

  @Test
  public void testIsValidPackFileInvalidFilePath() {
    String filePath = "thisIsNotAValidFilePath";
    // Console message should read "ERROR: Pack file does not exist"
    assertFalse("Invalid file path not recognised as invalid", CardGame.isValidPackFile(
        filePath, numPlayers));
  }

  @Test
  public void testIsValidPackFileInvalidNumberOfCards() {
    numPlayers = 3;
    String filePath = "test/valid5PlayersPack.txt";
    // Console message should read "ERROR: There are not 24 cards in the pack file"
    assertFalse("Incorrect number of cards for number of players not recognised as invalid",
        CardGame.isValidPackFile(filePath, numPlayers));
  }

  @Test
  public void testIsValidPackFileNegativeIntegerCard() {
    String filePath = "test/invalidNegative5PlayersPack.txt";
    // Console message should read "ERROR: Pack file contains a non-positive
    // integer"
    assertFalse("Negative integer card not recognised as invalid", CardGame.isValidPackFile(
        filePath, numPlayers));
  }

  @Test
  public void testDealCards() throws FileNotFoundException {
    // Each player and deck should have the cards [1,2,3,4] in that order
    cards = CardGame.readInPack("test/valid5PlayersPackIdenticalHand.txt", numPlayers);
    CardGame game = new CardGame(5, cards);
    game.dealCards();
    for (Player player : game.players) {
      assertEquals(player.getPlayerName() + " dealt wrong cards", "1 2 3 4 ",
          player.handToString());
    }
    for (CardDeck deck : game.decks) {
      assertEquals("Deck " + deck.getDeckNumber() + " dealt wrong cards", "1 2 3 4 ",
          deck.getDeckContentsAsString());
    }
  }

  @Test
  public void testImmediateWinner() throws FileNotFoundException {
    cards = CardGame.readInPack("test/immediateWinner100Players.txt", 100);
    CardGame game = new CardGame(100, cards);
    game.dealCards();
    assertEquals("Player 100 should have won", 100, CardGame.winningPlayer.intValue());
  }

  @After
  public void tearDown() {
    game = null;
  }
}
