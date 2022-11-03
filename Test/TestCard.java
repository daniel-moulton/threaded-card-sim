package Test;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import src.cardsim.Card;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

public class TestCard{
    private static Card card;
    @Before
    public void setUp() {
        Card card = new Card(5);
    }

    @Test
    public void testCardValue() {
        assertEquals("Card values incorrect",5, card.getCardValue());
    }
}