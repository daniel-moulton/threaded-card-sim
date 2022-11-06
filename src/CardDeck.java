package src;

import java.util.LinkedList;
import java.util.Queue;

public class CardDeck{

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

        public void insertCard(Card card){
                contents.add(card);
        }

        public Card drawCard(){
                return contents.remove();
        }

        public int getDeckLength(){
                return contents.size();
        }
}