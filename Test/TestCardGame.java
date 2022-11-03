package Test;
import org.junit.Before;
import org.junit.Test;

import src.CardGame;

import org.junit.After;
import static org.junit.Assert.*;

import java.io.File;

public class TestCardGame {

    @Before
    public void setUp() {

    }

    @Test
    public void testIsValidPackFileValidFile(){
        int numPlayers=5;
        String filePath="src/Test/valid5PlayersPack.txt";
        assertEquals("Valid file not recognised as valid", CardGame.isValidPackFile(filePath, numPlayers));
    }
}
