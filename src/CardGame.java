package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents the game.
 *
 * @author Daniel Moulton
 * @author James Pilcher
 */
public class CardGame {

  public static String gameLocation;
  public static Scanner scanner = new Scanner(System.in);
  public final int numPlayers;
  public Card[] cards;
  public Player[] players;
  public CardDeck[] decks;
  public static AtomicInteger winningPlayer = new AtomicInteger();
  // public static CyclicBarrier barrier;

  /**
   * Constructor for the CardGame class.
   *
   * @param numPlayers the number of players in the game
   */
  public CardGame(int numPlayers, Card[] cards) {
    this.numPlayers = numPlayers;
    this.cards = cards;
    // Gets the current date and time in specified format to use as folder for
    // output files.
    String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    gameLocation = "./games/" + time;
    new File(gameLocation).mkdirs();

    decks = new CardDeck[numPlayers];
    for (int i = 0; i < decks.length; i++) {
      decks[i] = new CardDeck(i + 1);
    }
    players = new Player[numPlayers];
    for (int i = 0; i < players.length; i++) {
      players[i] = new Player(i + 1, decks[i], decks[(i + 1) % numPlayers]);
    }
  }

  /**
   * Deals all of the cards from the pack to players and then decks in round-robin
   * fashion.
   */
  public void dealCards() {
    // Index of which card we're dealing
    int cardIndex = 0;
    // For each loop of giving each player a card
    for (int cardIndexInHand = 0; cardIndexInHand < 4; cardIndexInHand++) {
      // For each player in the game
      for (Player player : players) {
        player.initialHand(cards[cardIndex], cardIndexInHand);
        cardIndex++;
      }
    }
    for (int cardIndexInDeck = 0; cardIndexInDeck < 4; cardIndexInDeck++) {
      // For each deck in the game
      for (CardDeck deck : decks) {
        deck.insertCard(cards[cardIndex]);
        cardIndex++;
      }
    }
  }

  /**
   * Gets the number of players to play the game from the user
   * looping until an integer above 1 is entered.
   *
   * @return the number of players playing the game
   */
  public static int getNumberOfPlayers() {
    int numPlayers = 0;
    while (numPlayers < 2) {
      System.out.println("Enter the number of players (must be greater than 1): ");
      if (scanner.hasNextInt()) {
        numPlayers = scanner.nextInt();
        if (numPlayers < 2) {
          System.out.println("ERROR: Number of players must be greater than 1");
        }
      } else {
        System.out.println("ERROR: Input must be an integer greater than 1");
        scanner.next();
      }
    }
    return numPlayers;
  }

  /**
   * Gets the pack location from the user, looping until a valid pack is found.
   *
   * @param numPlayers number of players in the game
   * @return array of cards
   */
  public static Card[] getInputPack(int numPlayers) {
    String packLocation;
    Card[] pack;
    scanner.nextLine();
    while (true) {
      System.out.println("Enter the location of the pack file: ");
      if (scanner.hasNextLine()) {
        packLocation = scanner.nextLine();
        if (packLocation.endsWith(".txt") && isValidPackFile(packLocation, numPlayers)) {
          pack = readInPack(packLocation, numPlayers);
          break;
        } else if (!packLocation.endsWith(".txt")) {
          System.out.println("ERROR: File must be a .txt file");
        }
      }
    }
    scanner.close();
    return pack;
  }

  /**
   * Reads in the pack file at the specified location and returns an array of
   * cards.
   *
   * @param packLocation location of the pack file
   * @param numPlayers   number of players in the game
   * @return array of cards
   */
  public static Card[] readInPack(String packLocation, int numPlayers) {
    // Each game has 8n cards, where n is the number of players
    Card[] pack = new Card[numPlayers * 8];
    try {
      File file = new File(packLocation);
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line;
      int i = 0;
      while ((line = br.readLine()) != null) {
        pack[i] = new Card(Integer.parseInt(line));
        i++;
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return pack;
  }

  /**
   * Takes in a file location and checks if it is a valid pack file.
   *
   * @param packLocation location of the pack file
   * @param numPlayers   number of players in the game
   * @return true if the file is valid, false otherwise
   */
  public static boolean isValidPackFile(String packLocation, int numPlayers) {
    File file = new File(packLocation);
    if (file.exists() && file.isFile()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(packLocation))) {
        String line;
        int lineCount = 0;
        while ((line = reader.readLine()) != null) {
          try {
            int num = Integer.parseInt(line);
            if (num <= 0) {
              System.out.println("ERROR: Pack file contains a non-positive integer");
              return false;
            }
            lineCount++;
          } catch (NumberFormatException e) {
            System.out.println("ERROR: Pack file contains a non-integer");
            return false;
          }
        }
        if (lineCount == numPlayers * 8) {
          return true;
        } else {
          System.out.println("ERROR: There are not " + 8 * numPlayers + " cards in the pack file");
          return false;
        }
      } catch (IOException e) {
        System.out.println("ERROR: Could not read pack file");
        return false;
      }
    } else {
      System.out.println("ERROR: Pack file does not exist");
      return false;
    }
  }

  /**
   * Starts the threads of each player.
   */
  public void startPlayerThreads() {
    for (Player player : players) {
      new Thread(player).start();
    }
  }

  // /**
  //  * Called once every player has called await on the barrier, releasing the barrier.
  //  *
  //  * @throws InterruptedException
  //  * @throws BrokenBarrierException
  //  */
  // public void releaseBarrier() throws InterruptedException,
  //     BrokenBarrierException {
  //   barrier.await();
  // }

  /**
   * Main method called when starting the game.
   *
   * @param args the command line arguments
   * @throws InterruptedException   if the thread is interrupted
   * @throws BrokenBarrierException if the barrier is broken
   */
  public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
    System.out.println("Welcome to the Card Game!");
    int numPlayers = getNumberOfPlayers();
    // barrier = new CyclicBarrier(numPlayers + 1);
    Card[] cards = getInputPack(numPlayers);
    CardGame game = new CardGame(numPlayers, cards);
    System.out.println("Dealing cards...");
    game.dealCards();
    for (Player player : game.players) {
      new Thread(player).start();
      System.out.println(player.getPlayerName() + " has been dealt cards " + player.handToString());
    }
    // game.releaseBarrier();  
  }
}