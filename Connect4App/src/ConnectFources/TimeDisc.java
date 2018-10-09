/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectFources;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TimeDisc extends Disc{
  
   public TimeDisc (boolean red , int ts){
       super(red,ts);
       this.red = red;
       setCenterX(ts/2);
       setCenterY(ts/2);
       this.Life = 21;
       this.Type = 1;
     //  this.setFill(new ImagePattern(im));
        text.setFont(Font.font ("Arial Black", 30));
        text.setFill(Color.WHITE);   
        text.setTextAlignment(TextAlignment.CENTER);
       color2 =(red ? Color.rgb(156,61,43) : Color.rgb(156,124,43));
           
       
       
   }
   
   @Override
    public void turnPassed(){
       this.Life -=1;
       this.text.setText(""+this.Life);
       if (Life -1 == 0){
           this.setFill(color2);
       }
   }
   
  
    
}
