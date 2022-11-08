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

public class CardGame {

    // folder for each game with date time?
    // list of players
    // list of decks
    public static String gameLocation;
    public static Scanner scanner = new Scanner(System.in);
    public final int NUMBER_OF_PLAYERS;
    public Card[] cards;
    public Player[] players;
    public CardDeck[] decks;
    public static AtomicInteger winningPlayer = new AtomicInteger();
    public static CyclicBarrier barrier;

    /**
     * @param cards
     * @param players
     * @param decks
     */
    public void dealCards(Card[] cards, Player[] players, CardDeck[] decks) {
        // Index of which card we're dealing
        int cardIndex = 0;
        // For each loop of dealing a card
        for (int cardIndexInHand = 0; cardIndexInHand < 4; cardIndexInHand++) {
            // For each player in the game
            for (Player player : players) {
                player.initialHand(cards[cardIndex], cardIndexInHand);
                cardIndex++;
            }
        }
        for (int i = 0; i < 4; i++) { // 4 cards in each deck (8n cards)
            // For each deck in the game
            for (CardDeck deck : decks) {
                deck.insertCard(cards[cardIndex]);
                cardIndex++;
            }
        }
    }

    /**
     * @param args
     * @throws InterruptedException
     * @throws BrokenBarrierException
     */
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        System.out.println("Welcome to the Card Game!");
        System.out.println(new File(".").getAbsolutePath());
        int numPlayers = getNumberOfPlayers();
        barrier = new CyclicBarrier(numPlayers + 1);
        CardGame game = new CardGame(numPlayers, getInputPack(numPlayers));
        System.out.println("Dealing cards...");
        game.dealCards(game.cards, game.players, game.decks);
        for (Player player : game.players) {
            new Thread(player).start(); // because thread1 will run first, it wont see that thread 3 has an immediate
                                        // winner. therefore it will do its whole go. this is where we need interrupt }
        }
        barrier.await();
    }

    public CardGame(int numPlayers, Card[] cards) {
        // Initialise the number of players
        NUMBER_OF_PLAYERS = numPlayers;
        this.cards = cards;
        String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        gameLocation = "./games/" + time;

        new File(gameLocation).mkdirs();
        // Initialise the decks
        decks = new CardDeck[NUMBER_OF_PLAYERS];
        for (int i = 0; i < decks.length; i++) {
            decks[i] = new CardDeck(i + 1);
        }
        players = new Player[NUMBER_OF_PLAYERS];
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player(i + 1, decks[i], decks[(i + 1) % NUMBER_OF_PLAYERS]);
        }
    }

    /**
     * @return int
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
     * @param numPlayers
     * @return Card[]
     */
    public static Card[] getInputPack(int numPlayers) {
        String packLocation;
        Card[] pack = new Card[numPlayers];
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
     * @param packLocation
     * @param numPlayers
     * @return Card[]
     */
    public static Card[] readInPack(String packLocation, int numPlayers) {
        Card[] pack = new Card[numPlayers * 8];
        // Read in the contents of the file at packLocation into the array pack
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
     * @param packLocation
     * @param numPlayers
     * @return boolean
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
}