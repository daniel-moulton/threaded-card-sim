package Test;
import org.junit.Before;
import org.junit.Test;

import src.CardDeck;
import src.Card;

import org.junit.After;
import static org.junit.Assert.*;

public class TestCardDeck{
    private static CardDeck deck;
    @Before
    public void setUp() {
        deck = new CardDeck();
    }

    @Test
    public void testCardDeckInsert() {
        deck.insertCard(new Card(5));
        assertEquals("Card values incorrect",5, deck.removeCard().getCardValue());
    }

    @Test
    public void testCardDeckRemove() {
        deck.insertCard(new Card(5));
        deck.insertCard(new Card(6));
        deck.insertCard(new Card(7));
        deck.insertCard(new Card(8));
        Card removed=deck.removeCard();
        assertEquals("Card values incorrect",5, removed.getCardValue());
    }
    @After
    public void tearDown() {
        deck = null;
    }
}