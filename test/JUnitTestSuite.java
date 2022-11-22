package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   TestPlayer.class,
   TestCard.class,
   TestCardDeck.class,
   TestCardGame.class
})

public class JUnitTestSuite {
    
}
