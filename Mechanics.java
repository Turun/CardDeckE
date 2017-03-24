import java.util.Random;

public class Mechanics extends Thread
{
    
    boolean interrupted = false;
    boolean end = false;
    int[] deck1;
    int[] deck2;
    int match;
    int total;
    
    public Mechanics(int deckLength){
        this.deck1 = new int[deckLength];
        this.deck2 = new int[deckLength];
        this.match = 0;
        this.total = 0;
        
        for(int i = 0; i<deckLength; i++){
            deck1[i] = i;
            deck2[i] = i;
        }
    }
    
    public void run(){
        while(!interrupted){
            mixDeck();
            match += checkDeck();
            total++;
        }
        while(end){} //wait till values are copied
    }
    
    private void mixDeck(){
        int r = 0;
        int temp = 0;
        Random rand = new Random();
        
        for(int i = deck1.length-1; i>0; i--){
            r = rand.nextInt(i+1);
            temp     = deck1[i];
            deck1[i] = deck1[r];
            deck1[r] = temp;
        }
        
        rand = new Random();
        
        for(int i = deck2.length-1; i>0; i--){
            r = rand.nextInt(i+1);
            temp     = deck2[i];
            deck2[i] = deck2[r];
            deck2[r] = temp;
        }
    }
    
    private int checkDeck(){
        for(int i = 0; i<deck1.length; i++){
            if(deck1[i] == deck2[i]){
                return 1;
            }
        }
        return 0;
    }
    
    public void setTotal(int total){this.total = total;}
    public void setMatch(int match){this.match = match;}
    
}
