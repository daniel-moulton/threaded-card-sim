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
        String filePath="Test/valid5PlayersPack.txt";
        System.out.println(new File(".").getAbsolutePath());
        assertTrue("Valid file not recognised as valid", CardGame.isValidPackFile(filePath, numPlayers));
    }

    @Test
    public void testIsValidPackFileNonIntegerCard(){
        int numPlayers=5;
        String filePath="Test/invalidString5PlayersPack.txt";
        // Console message should read "ERROR: Pack file contains a non-integer"
        assertFalse("Non-integer card not recognised as invalid", CardGame.isValidPackFile(filePath, numPlayers));
    }

    @Test
    public void testIsValidPackFileInvalidFilePath(){
        int numPlayers=5;
        String filePath="thisIsNotAValidFilePath";
        // Console message should read "ERROR: Pack file does not exist"
        assertFalse("Invalid file path not recognised as invalid", CardGame.isValidPackFile(filePath, numPlayers));
    }

    @Test
    public void testIsValidPackFileInvalidNumberOfCards(){
        int numPlayers=3;
        String filePath="Test/valid5PlayersPack.txt";
        // Console message should read "ERROR: There are not 24 cards in the pack file"        
        assertFalse("Incorrect number of cards for number of players not recognised as invalid", CardGame.isValidPackFile(filePath, numPlayers));
    }

    @Test
    public void testIsValidPackFileNegativeIntegerCard(){
        int numPlayers=5;
        String filePath="Test/invalidNegative5PlayersPack.txt";
        // Console message should read "ERROR: Pack file contains a non-positive integer"
        assertFalse("Negative integer card not recognised as invalid", CardGame.isValidPackFile(filePath, numPlayers));
    }
}
