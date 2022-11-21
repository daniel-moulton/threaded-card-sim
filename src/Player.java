package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;

/**
 * Represents a player in the game.
 *
 * @author Daniel Moulton
 * @author James Pilcher
 */
public class Player implements Runnable {

  private final int playerNumber;
  private final String playerName;
  private Card[] hand;
  private PrintStream outputter;
  private static final Object OBJECT_LOCK = new Object();
  private Queue<Card> discardables = new LinkedList<Card>();
  private CardDeck deckDrawnFrom;
  private CardDeck deckInsertedTo;

  /**
   * Constructor for the Player class.
   *
   * @param playerNumber   the number of the player
   * @param deckDrawnFrom  the deck the player draws from
   * @param deckInsertedTo the deck the player inserts into
   * @throws FileNotFoundException
   */
  public Player(int playerNumber, CardDeck deckDrawnFrom, CardDeck deckInsertedTo) throws FileNotFoundException {
    this.playerNumber = playerNumber;
    this.playerName = "Player " + playerNumber;
    this.hand = new Card[4];
    this.deckDrawnFrom = deckDrawnFrom;
    this.deckInsertedTo = deckInsertedTo;
    this.outputter = new PrintStream(new File(CardGame.gameLocation
      + "/player" + playerNumber + "_output.txt"));
  }

  /**
   * Inserts the starting cards into the player's hand.
   *
   * @param card         the card to add to the player's hand
   * @param handPosition the position in the hand to add the card
   */
  public void initialHand(Card card, int handPosition) {
    hand[handPosition] = card;
    if (handPosition == 3) {
      appendToFile(playerName + " initial hand is " + handToString());
      // Check if the player has won initially.
      if (hasWon()) {
        playerWon();
      } else {
        findDiscardables();
      }
    }
  }

  /**
   * Checks each card in the hand and if the denomination is not equal to the
   * player's number
   * the card is added to the queue of discardables which contains all cards in
   * the hand that can
   * be discarded in future turns.
   */
  public void findDiscardables() {
    for (int i = 0; i < hand.length; i++) {
      if (hand[i].getCardValue() != playerNumber) {
        discardables.add(hand[i]);
      }
    }
  }

  public Queue<Card> getDiscardables() {
    return discardables;
  }

  /**
   * Appends the given string to the player's output file.
   *
   * @param output string to append
   */
  public void appendToFile(String output) {
    outputter.println(output);
  }

  /**
   * Returns the player's hand as a string of card values.
   *
   * @return string of card values
   */
  public String handToString() {
    String stringHand = "";
    for (Card card : hand) {
      if (card != null) {
        stringHand += card.getCardValue() + " ";
      }
    }
    return stringHand;
  }

  /**
   * Draws the card at the top of the previous deck and appends to the player's
   * output file
   * the card they drew and from which deck.
   *
   * @return the card that was drawn
   */
  public Card drawCard() {
    Card card = deckDrawnFrom.drawCard();
    appendToFile(playerName + " draws a " + card.getCardValue() + " from deck "
        + deckDrawnFrom.getDeckNumber());
    return card;
  }

  /**
   * Gets the next card to discard from the queue of discardables and removes that
   * card from
   * the player's hand. Then inserts the removed card into the next deck and
   * appends to the
   * player's output file the card they discarded and into which deck.
   */
  public void removeMostDiscardable() {
    Card card = discardables.remove();
    for (int i = 0; i < hand.length; i++) {
      if (hand[i] == card) {
        hand[i] = null;
      }
    }
    deckInsertedTo.insertCard(card);
    appendToFile(playerName + " discards a " + card.getCardValue() + " to deck "
        + deckInsertedTo.getDeckNumber());
  }

  /**
   * Loops through the player's hand and inserts the card drawn into the first
   * empty slot.
   * Then checks if the card can be discarded or not in future turns.
   * And finally appends the player's current hand to their output file.
   *
   * @param drawnCard the drawn card to be inserted into the hand
   */
  public void updateHand(Card drawnCard) {
    for (int i = 0; i < hand.length; i++) {
      if (hand[i] == null) {
        hand[i] = drawnCard;
        if (drawnCard.getCardValue() != playerNumber) {
          discardables.add(drawnCard);
        }
        break;
      }
    }
    appendToFile(playerName + " current hand is " + handToString());
  }

  /**
   * Checks a player's hand to see if they have won by having four cards of the
   * same value.
   *
   * @return true if the player has won, false otherwise
   */
  private Boolean hasWon() {
    for (Card card : hand) {
      if (card.getCardValue() != (hand[0].getCardValue())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Getter for the name of the player.
   *
   * @return the name of the player
   */
  public String getPlayerName() {
    return playerName;
  }

  /**
   * When they win the game, sets the atomic variable winning player to winning
   * player's number.
   * Then wakes up all threads as the game is now over and prints to console which
   * player won.
   */
  public void playerWon() {
    CardGame.winningPlayer.set(playerNumber);
    synchronized (OBJECT_LOCK) {
      OBJECT_LOCK.notifyAll();
    }
    System.out.println(playerName + " wins!");
  }

  @Override
  public void run() {
    // try {
    // CardGame.barrier.await();
    // } catch (InterruptedException e1) {
    // e1.printStackTrace();
    // } catch (BrokenBarrierException e1) {
    // e1.printStackTrace();
    // }
    while (CardGame.winningPlayer.get() == 0) {
      if (hasWon()) {
        playerWon();
        break;
      }
      // Player can go as long as the deck they draw from is not empty and no one has
      // won the game.
      while (deckDrawnFrom.getDeckLength() == 0 && CardGame.winningPlayer.get() == 0) {
        try {
          synchronized (OBJECT_LOCK) {
            System.out.println(playerName + " waiting for deck "
                + deckDrawnFrom.getDeckNumber() + " to be filled");
            OBJECT_LOCK.wait();
          }
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      if (CardGame.winningPlayer.get() != 0) {
        break;
      }
      Card drawnCard = drawCard();
      removeMostDiscardable();
      updateHand(drawnCard);
      if (hasWon()) {
        playerWon();
        break;
      }
      synchronized (OBJECT_LOCK) {
        OBJECT_LOCK.notifyAll();
      }
    }
    int winnerNumber = CardGame.winningPlayer.get();
    if (winnerNumber == playerNumber) {
      appendToFile(playerName + " wins");
    } else {
      appendToFile("Player " + winnerNumber + " has informed Player " + playerNumber
          + " that Player " + winnerNumber + " has won");
    }
    appendToFile(playerName + " exits");

    if (winnerNumber == playerNumber) {
      appendToFile(playerName + " final hand: " + handToString());
    } else {
      appendToFile(playerName + " hand: " + handToString());
    }
    deckDrawnFrom.printContentsToFile();
    outputter.close();
  }
}
