package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CardDeck{

        // Queue of the deck cards (FIFO)
        private Queue<Card> contents = new ConcurrentLinkedQueue<Card>();
        private final int DECK_NUMBER;

        private PrintStream outputter;

        
        public CardDeck(int deckNumber) {
            // Initialise the deck's contents
            contents = new LinkedList<Card>();
            DECK_NUMBER=deckNumber;
            try {
                this.outputter = new PrintStream(new File(CardGame.gameLocation + "/" + "deck" + DECK_NUMBER + "_output.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        
        /** 
         * @return int
         */
        public int getDeckNumber(){
                return DECK_NUMBER;
        }

        
        /** 
         * @param card
         */
        public void insertCard(Card card){
                contents.add(card);
        }

        
        /** 
         * @return Card
         */
        public Card drawCard(){
                return contents.remove();
        }

        
        /** 
         * @return int
         */
        public int getDeckLength(){
                return contents.size();
        }

        
        /** 
         * @return String
         */
        private String getDeckContentsAsString(){
                String output = "";
                for(Card card : contents) { 
                        output += card.getCardValue() + " "; 
                      }
                return output;
        }

        public void printContentsToFile(){
                String output = getDeckContentsAsString();
                outputter.println("Deck Contents: " + output);
                outputter.close();
        }
}