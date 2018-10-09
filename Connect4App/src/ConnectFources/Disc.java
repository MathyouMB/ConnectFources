/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectFources;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.image.*;
import javafx.scene.paint.ImagePattern;

//the disc is simply the playing piece
// it inherents from the built in circle class

public class Disc extends Circle{
    
   boolean red; // red or yellow
   int gridX; //x location in grid
   int gridY;//y locations in grid
   int Type; // 0 if normal, 1 if time disc
   int Life; // life .. only used by time disc
   Color color2;
   Text text = new Text(""); // shows life
   
   public Disc(){
       super(60/2,Color.rgb(255,255,255)); // use circle contructor
       setCenterX(60/2); // 
       setCenterY(60/2);
       
   }
   
   public Disc (boolean red , int ts){
       super(ts/2, red ? Color.rgb(255,100,71) : Color.rgb(255,204,71)); // use circle contructor
       this.red = red;
       Type = 0; // this is a normal disc
       setCenterX(ts/2);
       setCenterY(ts/2);

   }
   
   @Override
   public String toString(){
       return("["+this.gridX+","+this.gridY+"]");
   }
   
   public void turnPassed(){
       this.Life -=1; // if a turn passes your one round closer to breaking
        this.text.setText(""+this.Life); // update life disc
   }
   
    public void CheckIfDead(Grid grid){
       if (Life == 0){ // if the disc is dead
           
           //remove it and its even from the grid
           grid.discRoot.getChildren().remove(this);
           grid.discRoot.getChildren().remove(this.text);
           grid.spaces[this.gridX][this.gridY] = null;
           System.out.println("dead:"+this);
       }
       
   }
    
}
