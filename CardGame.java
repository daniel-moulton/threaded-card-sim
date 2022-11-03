import java.util.Scanner;

public class CardGame{

    public final int NUMBER_OF_PLAYERS;
    
    public CardGame(int numPlayers)
    {
        // Initialise the number of players
        NUMBER_OF_PLAYERS = numPlayers;
    }
    
    public static int getNumberOfPlayers(){
        int numPlayers=0;
        Scanner scanner = new Scanner(System.in);
        // Asks the user for an input and keeps asking until the user enters an integer greater than 1
        // The input must be an integer otherwise they are asked to enter again
        while(numPlayers<2){
            System.out.println("Enter the number of players (must be greater than 1): ");
            if(scanner.hasNextInt()){
                numPlayers = scanner.nextInt();
            }else{
                System.out.println("ERROR: Input must be an integer greater than 1");
                scanner.next();
            }
        }
        getInputPack(numPlayers);
        scanner.close();
        return numPlayers;
    }
    
    public static int[] getInputPack(int numPlayers){
        boolean isValid;
        String packLocation="";
        int[] pack;
        Scanner scanner = new Scanner(System.in);
        
        return pack;
    }
    public static void main(String[] args){
        Card card = new Card(20);
        System.out.println(card.getCardValue());
        System.out.println(getNumberOfPlayers());
    }
}