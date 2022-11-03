package Test;
import org.junit.Before;
import org.junit.Test;

import src.Card;

import org.junit.After;
import static org.junit.Assert.*;

public class TestCard{
    private static Card card;
    @Before
    public void setUp() {
        card = new Card(5);
    }

    @Test
    public void testCardValue() {
        assertEquals("Card values incorrect",5, card.getCardValue());
    }

    @After
    public void tearDown() {
        card = null;
    }
}