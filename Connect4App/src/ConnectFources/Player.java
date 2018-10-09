
package ConnectFources;

import java.io.Serializable;
import java.text.DecimalFormat;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import static jdk.nashorn.internal.objects.NativeMath.round;

//players are profiles for the "players"
//they hold game stats

public class Player implements Serializable{
    boolean player1;
    int wins;
    int loses;
    int ties;
    String name;
   
    public Player(String n){
        name = n;
        wins = 0;
        loses = 0;
        ties = 0;
    }
    
    public Pane playerInfo(){ //the gui formatting for the player on the game screen
        Pane p = new Pane();
        DecimalFormat df = new DecimalFormat("#.#");
     
         Text t = new Text();
        t.relocate(0,0);
        t.setText(name);
        
        Text wr = new Text();
        wr.relocate(0,12);
        wr.setText("winrate: "+( df.format(calcWinRate()*100))+"%");
        
        Text gp = new Text();
        gp.relocate(0,24);
        gp.setText("Game Count: "+calcTotalMatches());
        
        p.getChildren().add(t);
        p.getChildren().add(wr);
        p.getChildren().add(gp);
        
        return p;
    }
    
    public void wonMatch(){
        wins++;
    }
    
    public void lostMatch(){
        loses++;
    }
    
    public void tiedMatch(){
        ties++;
    }
    
    public double calcWinRate(){ // calcuates winrate
        return wins/calcTotalMatches();
    }
    
    public double calcTotalMatches(){ // caculates your total match count
        return(wins+loses+ties);
    }
    
    public String toString(){
        return(name+" | Winrate: "+calcWinRate()+"%");
    }

}
