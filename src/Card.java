package src;
public class Card {
    private final int CARD_VALUE;

    // belongs to player? so two people dont own at once.

    public Card(int value) {
        // Initialise the card's value
        CARD_VALUE = value;
    }

    public int getCardValue() {
        // Return the value of the card
        return CARD_VALUE;
    }
}
