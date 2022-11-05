package src;

import java.util.LinkedList;
import java.util.Queue;

public class CardDeck{ //inherits WritesToFile

        // Queue of the deck cards (FIFO)
        private Queue<Card> contents = new LinkedList<Card>();
        private final int DECK_NUMBER;

        public CardDeck(int deckNumber) {
            // Initialise the deck's contents
            contents = new LinkedList<Card>();
            DECK_NUMBER=deckNumber;
        }

        public int getDeckNumber(){
                return DECK_NUMBER;
        }

        // folder to print to

        // generate 

        // draw funciton  - remove from top, return value.
        // insert (to bottom) function - remove from bottom, return value. 

        // Write to file, folder 
        public void insertCard(Card card){
                contents.add(card);
        }

        public Card drawCard(){
                return contents.remove();
                // Card card= contents.remove();
                // System.out.println("Card removed: " + card.getCardValue());
        }


        public void outputDeck(){
                for(Card card : contents){
                        System.out.println(card.getCardValue());
                }
        }
}