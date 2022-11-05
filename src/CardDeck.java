package src;

import java.util.LinkedList;
import java.util.Queue;

public class CardDeck{ //inherits WritesToFile

        // Queue of the deck cards (FIFO)
        private Queue<Card> contents = new LinkedList<Card>();

        public CardDeck() {
            // Initialise the deck's contents
            contents = new LinkedList<Card>();
        }
        // folder to print to

        // generate 

        // draw funciton  - remove from top, return value.
        // insert (to bottom) function - remove from bottom, return value. 

        // Write to file, folder 
        public void insertCard(int value){
                contents.add(new Card(value));
        }

        public Card removeCard(){
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