/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectFources;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import java.util.Optional;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.paint.Color;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.util.Duration;

// the grid class basically holds a bunch of information about the grid... it also handles the sorting of the grid when gravity is changed
public class Grid {
    int COLUMNS; // num of columns
    int ROWS; // num of rows
    int TILE_SIZE; // tile size (radius of discs)
    Color GridColor; // the color of grid
    Pane discRoot = new Pane(); // a draw pane that draws the discs and there text values
    Disc[][] spaces; // an array of discs for the grid
    String p1; // name of player 1
    String p2; // name of player 2

    public Grid(int c, int r, int ts, Color gc) {
        COLUMNS = c;
        ROWS = r;
        TILE_SIZE = ts;
        GridColor = gc;
        spaces = new Disc[c][r];
    }
    public void setPlayerNames(String p, String n){ // this is called when we hit play
        // we set the current players multiple times over so we dont want this in the contructor.
        p1 = p;
        p2 = n;
    }

    public Shape makeGrid() { // the actual grid shape, like the square with circles in it
        //draw a rectangle
        Shape shape = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);
        
        //cut out circles in the rectange based on radius of discs
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle((TILE_SIZE / 2)-1);
                circle.setCenterX(TILE_SIZE / 2);
                circle.setCenterY(TILE_SIZE / 2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + (TILE_SIZE / 4));
                circle.setTranslateY(y * (TILE_SIZE + 5) + (TILE_SIZE / 4));

                shape = Shape.subtract(shape, circle); // cut the circles out
            }
        }
   
        shape.setFill(GridColor); // make the remaining shape blue
        return shape; // send back shape
    }


    private Optional < Disc > getDisc(int column, int row) { // method that checks if a spot in the grid is empty
        if (column < 0 || column >= COLUMNS || row < 0 || row >= ROWS)
            return Optional.empty();

        return Optional.ofNullable(this.spaces[column][row]);
    }

    public void reArrangeGrid(int direction) {
        
        //directions 
        //1=right
        //2=down
        //3=right
        //4=up
        
        if (direction == 1) { // go right
            for (int y = 0; y <= ROWS - 1; y++) {
                for (int x = COLUMNS-1; x >= 0; x--) {
                    if (!isNull(spaces[x][y])) {
                        if((spaces[x][y]).gridX != 6){ // dont try to move any disc that is in the far right column
                            if(isNull(spaces[x+1][y])){ // if this disc isnt null
                             moveDiscRight(spaces[x][y], y); // move it right
                         }
                        }
                     
                    }

                }

            }
        }
        
        if (direction == 2) {
         for (int x = 0; x <= COLUMNS -1; x++) {
            for (int y = ROWS -1; y >= 0; y--) {
              if (!isNull(spaces[x][y])) {
                if((spaces[x][y]).gridY != 5){ // dont try to move any disc that is in the lowest row
                  if(isNull(spaces[x][y+1])){ // if the space isnt null
                  moveDiscDown(spaces[x][y], x); // move that disc down
                }
                }
              }
        }
        }
        }
        
        
        if (direction == 3) {//go left
            for (int y = 0; y <= ROWS - 1; y++) { 
                for (int x = 0; x <= COLUMNS -1; x++) {
                    if (!isNull(spaces[x][y])) { // if this space isnt null
                        if((spaces[x][y]).gridX != 0){ // if its not the far left column
                            if(isNull(spaces[x-1][y])){ // if its not null
                             moveDiscLeft(spaces[x][y], y); // move it left
                         }
                        }
                     
                    }

                }

            }
        }
        
         if (direction == 4) {
         for (int x = 0; x <= COLUMNS -1; x++) {
            for (int y = 0; y <= ROWS-1; y++) {
              if (!isNull(spaces[x][y])) { // if isnt not null
                if((spaces[x][y]).gridY != 0){ // if its not the top row
                  if(isNull(spaces[x][y-1])){ // if its not null
                  moveDiscUp(spaces[x][y], x); // move it up
                }
                }
              }
        }
        }
        }
        
      
    }

    public void printGridTest() { //testing method that prints the grid in console

        for (int y = 0; y <= ROWS - 1; y++) {
            for (int x = 0; x <= COLUMNS - 1; x++) {
                if (!isNull(spaces[x][y])) {
                    if((spaces[x][y]).red){
                    System.out.print("[1]");
                    }else{
                    System.out.print("[2]");
                    }

                } else {
                    System.out.print("[0]");
                }

            }
            System.out.println();
        }

    }
    
    public void resetGrid() { // makes the grid blank

        for (int y = 0; y <= ROWS - 1; y++) {
            for (int x = 0; x <= COLUMNS - 1; x++) {
                if (!isNull(spaces[x][y])) { // if there is a disc here...
                   
                      this.discRoot.getChildren().remove(spaces[x][y]); // remove disc from gui
                      this.discRoot.getChildren().remove(spaces[x][y].text); // remove disc text from gui
                      this.spaces[x][y] = null; // make it null cus its gone now.

                } 

            }
        }

    }

    private boolean isNull(Object obj) { // returns if an option is null or not
        return obj == null;
    }
    public void updateTimeDisks(int gravity) { // method that is called each turn to lower life of discs.. and move discs if they break
        int hit = -1; // this method gets turned to the num of the column if a discs breaks
        for (int y = 0; y <= ROWS - 1; y++) {
            for (int x = 0; x <= COLUMNS - 1; x++) {
                if (!isNull(spaces[x][y])) { // if they are not null
                    if (spaces[x][y].Type == 1) { // if they are time discs
                        spaces[x][y].turnPassed(); // turn passed lowers a discs life
                          if(spaces[x][y].Life >=10){// this is for centering the text
                            spaces[x][y].text.setTranslateX((x * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7*(TILE_SIZE / 16)));
                            }else{
                            spaces[x][y].text.setTranslateX((x * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75*(TILE_SIZE / 16)));   
                            }
                        if ((spaces[x][y].Life - 1) < 0) { // if they are gonna die....
                            hit = x; // this is the column that needs to move
                        }
                        spaces[x][y].CheckIfDead(this);
                      
                    }

                }



            }

        }
        
        if (hit != -1){ // if a disc broke
            if(gravity == 270){ // if the grvaity is down
             for (int y = ROWS -1; y >= 0; y--) {
              if (!isNull(spaces[hit][y])) {
                 if((spaces[hit][y]).gridY != 5){
                  if(isNull(spaces[hit][y+1])){
                  moveDiscDown(spaces[hit][y], hit); // move the row with the broken disc down
                  }
                 }
              }
             }
            }else if(gravity == 90){ // if gravity is up
                for (int y = 0; y <= ROWS-1; y++) {
                   if (!isNull(spaces[hit][y])) {
                    if((spaces[hit][y]).gridY != 0){
                      if(isNull(spaces[hit][y-1])){
                  moveDiscUp(spaces[hit][y], hit); // move that column up
                  }
                 }
              }
             }
            }
        }

    }

    private void moveDiscDown(Disc disc, int column) {
        int row = ROWS - 1;

        do {
            if (!getDisc(column, row).isPresent()) // if its pretty then break the loop
                break;
            row--; // if its empty take it down another row
        } while (row >= 0);
        if (row < 0) {
            return;
        } 
        this.spaces[disc.gridX][disc.gridY] = null;
        this.spaces[column][row] = disc;
        disc.gridX = column;
        disc.gridY = row;
        disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

      if(disc.Life >=10){
            disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7*(TILE_SIZE / 16)));
            }else{
            disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75*(TILE_SIZE / 16)));    
            }
       disc.text.setTranslateY((row *(TILE_SIZE + 5)+ TILE_SIZE/4)+(TILE_SIZE/2));
       
        TranslateTransition animation = new TranslateTransition(Duration.millis(250),disc);
        TranslateTransition animation2 = new TranslateTransition(Duration.millis(250),disc.text);
       animation.setToY(row *(TILE_SIZE + 5) + TILE_SIZE /4);
        animation2.setToY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + (1.25*(TILE_SIZE / 2)));
     
        animation.setOnFinished(e -> {
               //  checkwin();
	});
        animation.play(); 
        animation2.play();
        
       

    } 
    private void moveDiscUp(Disc disc, int column) {
        int row = 0;

        do {
            if (!getDisc(column, row).isPresent()) // if its pretty then break the loop
                break;
            row++; // if its empty take it down another row
        } while (row <= ROWS-1);
        if (row > ROWS-1) {
            return;
        } 
        this.spaces[disc.gridX][disc.gridY] = null;
        this.spaces[column][row] = disc;
        disc.gridX = column;
        disc.gridY = row;
       // System.out.println(disc);
        disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);
       // disc.setTranslateY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        if(disc.Life >=10){
            disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7*(TILE_SIZE / 16)));
            }else{
            disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75*(TILE_SIZE / 16)));    
            }
       disc.text.setTranslateY((row *(TILE_SIZE + 5)+ TILE_SIZE/4)+(TILE_SIZE/2));
       
        TranslateTransition animation = new TranslateTransition(Duration.millis(250),disc);
        TranslateTransition animation2 = new TranslateTransition(Duration.millis(250),disc.text);
       animation.setToY(row *(TILE_SIZE + 5) + TILE_SIZE /4);
       animation2.setToY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + (1.25*(TILE_SIZE / 2)));
        animation.setOnFinished(e -> {
            //   checkwin();
	});
        animation.play(); 
        animation2.play();
       

    }   
    
    private void moveDiscRight(Disc disc, int row) {
        int column = COLUMNS - 1;

        do {
            if (!getDisc(column, row).isPresent()) // if its pretty then break the loop
                break;
            column--; // if its empty take it down another row
        } while (column >= 0);
        if (column < 0) {
            return;
        } 
        this.spaces[disc.gridX][disc.gridY] = null;
        this.spaces[column][row] = disc;
        disc.gridX = column;
        disc.gridY = row;
       // System.out.println(disc);
       // disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);
        disc.setTranslateY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        //disc.text.setTranslateX((column *(TILE_SIZE + 5)+ TILE_SIZE/4)+(TILE_SIZE/2));
       disc.text.setTranslateY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + (1.25*(TILE_SIZE / 2)));
       
        TranslateTransition animation = new TranslateTransition(Duration.millis(250),disc);
        TranslateTransition animation2 = new TranslateTransition(Duration.millis(250),disc.text);
       animation.setToX(column *(TILE_SIZE + 5) + TILE_SIZE /4);
       if(disc.Life >=10){
       animation2.setToX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7*(TILE_SIZE / 16)));
       }else{
       animation2.setToX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75*(TILE_SIZE / 16)));   
       }
        animation.setOnFinished(e -> {
             //  checkwin();
	});
        animation.play(); 
        animation2.play();
       

    }   
    private void moveDiscLeft(Disc disc, int row) {
        int column = 0;

        do {
            if (!getDisc(column, row).isPresent()) // if its pretty then break the loop
                break;
            column++; // if its empty take it down another row
        } while (column <= COLUMNS-1);
        if (column > COLUMNS-1) {
            return;
        } 
        this.spaces[disc.gridX][disc.gridY] = null;
        this.spaces[column][row] = disc;
        disc.gridX = column;
        disc.gridY = row;
       // System.out.println(disc);
       // disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);
        disc.setTranslateY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        //disc.text.setTranslateX((column *(TILE_SIZE + 5)+ TILE_SIZE/4)+(TILE_SIZE/2));
      disc.text.setTranslateY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + + (1.25*(TILE_SIZE / 2)));
       
        TranslateTransition animation = new TranslateTransition(Duration.millis(250),disc);
        TranslateTransition animation2 = new TranslateTransition(Duration.millis(250),disc.text);
       animation.setToX(column *(TILE_SIZE + 5) + TILE_SIZE /4);
      if(disc.Life >=10){
       animation2.setToX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7*(TILE_SIZE / 16)));
       }else{
       animation2.setToX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75*(TILE_SIZE / 16)));  
       }
      
        animation.setOnFinished(e -> {
             //   checkwin();
	});
        animation.play(); 
        animation2.play();
       

    }
      
      public boolean gameEnded (int column, int row, boolean player1Move){ // this method parrellels the one found in the main, the only difference is it takes the players turn
        List<Point2D> vertical = IntStream.rangeClosed(row-3,row+3)
                .mapToObj(r -> new Point2D(column,r))
                .collect(Collectors.toList());
        List<Point2D> horizontal = IntStream.rangeClosed(column-3, column+3)
                .mapToObj(c -> new Point2D(c,row))
                .collect(Collectors.toList());
        
        Point2D topLeft = new Point2D(column -3,row -3);
        List<Point2D> diagonal1 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> topLeft.add(i,i))
                .collect(Collectors.toList());
        
        Point2D botLeft = new Point2D(column -3,row +3);
        List<Point2D> diagonal2 = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> botLeft.add(i,-i))
                .collect(Collectors.toList());
        
        return checkRange(vertical,player1Move) || checkRange(horizontal,player1Move) || checkRange(diagonal1,player1Move) || checkRange (diagonal2,player1Move);
    }
    
    private boolean checkRange(List <Point2D> points, boolean player1Move){ // also the same as the one in main
        int chain = 0;
        for(Point2D p: points){
            int column = (int) p.getX();
            int row = (int) p.getY();
            
            Disc disc = getDisc(column, row).orElse(new Disc(!player1Move,TILE_SIZE));
           
            if(disc.red == player1Move){
                
                chain++;
               if (chain ==3){
                 //  System.out.println("3");
               }
                if(chain ==4){
                    return true;
                }
            }else{
                chain = 0;
            }
        }
        return false;
    }



}