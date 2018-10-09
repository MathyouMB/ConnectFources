package ConnectFources;


import java.io.IOException;
import javafx.geometry.*;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.Parent;
import javafx.scene.shape.*;
import javafx.animation.*;
import javafx.scene.shape.*;
import javafx.animation.Animation.*;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.*;
//Matthew M-B
//06/11/2018
//This is a connect 4 game made using javafx. It will only run with an ide with the correct javafx imports.
//The diffference between this and normal connect 4 is that this game lets you change gravity, push discs, and discs break after 20 turns.

public class ConnectFources extends Application {
    Stage window; // the main stage, aka the screen
    Scene scene1, scene2; //title screen, and game screen
    private static final int TILE_SIZE = 60; // radius of the circle
    private static final int COLUMNS = 7; //num of columns
    private static final int ROWS = 6; //num of rows
    private int turnCount = 0; // counts turns
    private static final Color GridColor = Color.rgb(0, 136, 170); //the color of the grid
    private Grid grid = new Grid(COLUMNS, ROWS, TILE_SIZE, GridColor); //the grid
    public boolean player1Move = true; //a boolean that tracks the players turn
    public int gravityAngle = 270; //the direction of gravity, 270 means down,90 means up
    Boolean AIMODE = false; //a testing ai I made that you shouldnt turn on cus it will crash things
    private final int SCENE_WIDTH = 800; //window width
    private final int SCENE_HEIGHT = 600; //window height
    Image bk = new Image("https://i.imgur.com/yRX2Mlq.png", false); //the background image
    Image gbk = new Image("https://i.imgur.com/u7uC6x5.png", false); //the game screen background image
    Image title = new Image("https://i.imgur.com/8w173Gq.png", false); //the title image
    Database gameDatabase = new Database(); // the database
    String currentPlayer1; //the current player 1s name
    String currentPlayer2; //the current player 2s name

    public void start(Stage primaryStage) throws Exception {

       // gameDatabase.writeToFile();
        gameDatabase = gameDatabase.readFile(); //load the database
        window = primaryStage;
        primaryStage.setTitle("Connect Fources");
        scene1 = titleScene();
        scene2 = mainScene();
        primaryStage.setScene(scene1); //show title screen.
        primaryStage.show(); //display window

    }

    public static void main(String[] args) {
        launch(args);

    }
    private Scene titleScene() { //this is the gui setup for the first scene

        ImageView backgroundImageView = new ImageView(bk); //display background
        ImageView titleImageView = new ImageView(title); //display title
        
        //GUI OBJECT DECLARATION
        TextField field = new TextField(); 
        field.relocate(400 - 75, 400);
        TextField field2 = new TextField();
        field2.relocate(400 - 75, 450);
        Button btn = new Button();
        btn.setText("Play");
        btn.relocate(400 - 25, 500);
        Text t = new Text();
        t.relocate(400 - 75, 380);
        t.setText("Enter the names of 2 players");
        
        //when you hit the play button
        btn.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                if (!(field.getText() == null || field.getText().trim().isEmpty())) { // if player 1 name box is empty
                    if (!(field2.getText() == null || field2.getText().trim().isEmpty())) { // if player 2 box is empty
                        if (!(field.getText().equals(field2.getText()))) { //if they are not the same name
                            currentPlayer1 = field.getText(); //set current player 1
                            currentPlayer2 = field2.getText(); //set current player 2
                            gameDatabase.addPlayer(field.getText()); // add this player to the db
                            gameDatabase.addPlayer(field2.getText()); // add this player to the db
                            window.setScene(mainScene()); //switch to game scene
                            grid.setPlayerNames(field.getText(), field2.getText()); //set player info in grid

                            try {
                                gameDatabase.writeToFile(); //update the db bin file
                            } catch (IOException ex) {
                                Logger.getLogger(ConnectFources.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            System.out.println("ERROR: ENTER TWO DIFFERENT NAMES"); //print if names are same
                        }
                    } else {
                        System.out.println("ERROR: ENTER TWO NAMES."); // print if empty
                    }
                } else {
                    System.out.println("ERROR: ENTER TWO NAMES."); // prnt if empty
                }

 
            }
        });

        Button btn2 = new Button();
        btn2.setText("LeaderBoard");
        btn2.relocate(400 - 25, 530);
        btn2.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                if (gameDatabase.playerList.size() > 0) {
                    gameDatabase.printStats(); //prints out leaderboard
                } else {
                    System.out.println("Database is empty."); //if no one is in the db then dont print
                }
            }

        });



        //add all the stuff to the gui
        Pane but = new Pane();
        but.getChildren().add(btn);
        but.getChildren().add(btn2);
        but.getChildren().add(field);
        but.getChildren().add(field2);
        but.getChildren().add(t);
        Pane main = new Pane();
        main.getChildren().add(backgroundImageView);
        main.getChildren().add(titleImageView);
        main.getChildren().add(but);


        //return title scene
        Scene scene = new Scene(main, SCENE_WIDTH, SCENE_HEIGHT);
        return scene;
    }
    private Scene mainScene() {
        //GUI OBJECT SETUP STuff
        ImageView backgroundImageView = new ImageView(gbk);
        Pane p1p = gameDatabase.playerSearch(currentPlayer1).playerInfo(); // players have a pane that puts there info in a setup
        Pane p2p = gameDatabase.playerSearch(currentPlayer2).playerInfo(); // players have a pane that puts there info in a setup
        p1p.relocate(30, 300);
        p2p.relocate(650, 300);
       
        //PUSH DISC s RIGHT
        Button btnR = new Button();
        btnR.setText("Right");
        btnR.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                grid.reArrangeGrid(1);
                turnUpdate();
                if (!AIMODE) {
                    player1Move = !player1Move;
                }

            }
        });

        
        //PUSH DISC s LEFT
        Button btnL = new Button();
        btnL.setText("Left");
        btnL.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                //gravityAngle = 180;
                grid.reArrangeGrid(3);
                turnUpdate();
                if (!AIMODE) {
                    player1Move = !player1Move;
                }

            }
        });
        btnL.relocate(0, 25);

        //CHANGE GRAVITY TO UP
        Button btnD = new Button();
        btnD.setText("Down");
        btnD.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                gravityAngle = 270;
                grid.reArrangeGrid(2);
                turnUpdate();
                if (!AIMODE) {
                    player1Move = !player1Move;
                }

            }
        });
        btnD.relocate(50, 25);
        
        //CHANGE GRAVITY TO DOWN
        Button btnU = new Button();
        btnU.setText("Up");
        btnU.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                gravityAngle = 90;
                grid.reArrangeGrid(4);
                turnUpdate();
                if (!AIMODE) {
                    player1Move = !player1Move;
                }

            }
        });
        btnU.relocate(50, 0);


        //PRINTS THE GRID OUT (this is for testing purposes)
        Button btn2 = new Button();
        btn2.setText("grid");
        btn2.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {


                grid.printGridTest();
            }
        });
        
        //RESETS THE GRID
        Button btnReset = new Button();
        btnReset.setText("clear");
        btnReset.setOnAction(new EventHandler < ActionEvent > () {
            @Override
            public void handle(ActionEvent event) {
                resetGame();
                grid.resetGrid();
            }
        });
        btnReset.relocate(0, 25);

        //ADD EVERYTHING TO THE GUI
        Pane but = new Pane();
        but.getChildren().add(btnR);
        but.getChildren().add(btnL);
        but.getChildren().add(btnD);
        but.getChildren().add(btnU);

        Pane but2 = new Pane();
        but2.getChildren().add(btn2);
        but2.relocate(0, 75);
        but2.getChildren().add(btnReset);

        Pane drawGrid = gridPaneColumns();
        drawGrid.relocate(150, 75);

        Pane main = new Pane();
        main.getChildren().add(backgroundImageView);
        main.getChildren().add(drawGrid);
        main.getChildren().add(but);
        main.getChildren().add(but2);
        main.getChildren().add(p1p);
        main.getChildren().add(p2p);

        Scene scene = new Scene(main, 800, 600);
        return scene;
    }
    public void resetGame() { // this is called when a winner is found
        System.out.println("NEW GAME--------------------------------");
        player1Move = true;
        turnCount = 0;
        gravityAngle = 270;
       // grid.resetGrid();

    }
    private Pane gridPaneColumns() { //this pain draws the grid, discs, and columns

        Pane root = new Pane();
        root.getChildren().add(grid.discRoot);
        Shape gridShape = grid.makeGrid();
        root.getChildren().add(gridShape);
        root.getChildren().addAll(makeColumns());
        return root;
    }
  
    private List < Rectangle > makeColumns() { // this is the draw code for those rectangles that appear when you hover your mouse
        List < Rectangle > list = new ArrayList < > ();

        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE + 5) + (TILE_SIZE / 4));
            rect.setFill(Color.TRANSPARENT);
            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(255, 255, 255, 0.3))); // if youur mouse is inside it, the color is white
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT)); //if your mouse is outside its invisible
            list.add(rect);
            final int column = x;
            rect.setOnMouseClicked(e -> placeDisc(new TimeDisc(player1Move, TILE_SIZE), column)); //if you click the white rectangle, place a disc there


        }

        return list;

    }

    private void turnUpdate() { //called when turn is switched
        System.out.println(turnCount + " ---------------" + " Turn: " + (player1Move ? "Red" : "Yellow")); //test console output
       
        checkwin(); // check if someone won
        
        if (AIMODE == false) { //if your not playing ai... just update the discs

            grid.updateTimeDisks(gravityAngle);
            turnCount++;

        } else { // if your playing the ai... let the AI move
            grid.updateTimeDisks(gravityAngle);
            
            
            //i'm not gonna overly explain the ai because its not actually an ai.
            //it just picks a random number and does a certain move based on which number it picked.
            //the following code is basically just .. generate a number... if its that number... play in that column
            
            Random ran = new Random();
            int x = 0;
            if (turnCount >= 2) {
                x = ran.nextInt(11); //7+4 8-1 9-2 10-3 11-4
            } else {
                x = ran.nextInt(7);
            }
            boolean didit = false;

            //  Duration.seconds(2);
            if (x >= 7) {

                if (gravityAngle == 90 && x - 6 == 4) {
                    x = 8;
                }
                if (gravityAngle == 270 && x - 6 == 2) {
                    x = 10;
                }
                if (x - 6 == 4) {
                    gravityAngle = 90;
                } else if (x - 6 == 2) {
                    gravityAngle = 270;
                }
                grid.reArrangeGrid(x - 6);
            } else {
                if (didit == false) {
                    placeDiscAi(new TimeDisc(false, TILE_SIZE), x);
                    grid.updateTimeDisks(gravityAngle);
                    didit = true;
                }
            }
            turnCount += 2;
        }


    }

    private void placeDisc(Disc disc, int column) { //the code for players placing discs
        if (gravityAngle == 270) { //if the disc has to move down
            int row = ROWS - 1;
            do {
                if (!getDisc(column, row).isPresent()) // if its pretty then break the loop
                    break;
                row--; 
            } while (row >= 0); //check if its empty till you find the lowest possible row to place the disc in
            if (row < 0) {
                return;
            }

            grid.spaces[column][row] = disc; // place the disc in this spot
            
            //set the discs internal location
            disc.gridX = column;
            disc.gridY = row;
            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4); // move it to its spot on the gui

            grid.discRoot.getChildren().add(disc); // add the disc to the disc pane root (this is what lets it be drawn).
           
            if (disc.Life >= 10) { //so... if the number drawn on the disc is less then 10 we draw it differently to make sure its still centered. The # 10 takes up more space then 9
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7 * (TILE_SIZE / 16)));
            } else {
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75 * (TILE_SIZE / 16)));
            }
            
            grid.discRoot.getChildren().add(disc.text); //add the discs text to the draw
            
            //ANIMATE IT MOVING
            TranslateTransition animation = new TranslateTransition(Duration.millis(250), disc);
            TranslateTransition animation2 = new TranslateTransition(Duration.millis(250), disc.text);
            animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation2.setToY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + (1.25 * (TILE_SIZE / 2)));
            final int currentrow = row;
            animation.setOnFinished(e -> {
                //WHEN ITS DONE MOVING CHECK FOR A GAME WIN
                if (grid.gameEnded(column, currentrow, player1Move)) { // IF SOMEONE WON
                    System.out.println("Winner:" + (player1Move ? (currentPlayer1) : (currentPlayer2))); //PRINT WINNER
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer1) : (currentPlayer2)), 0); //UPDATE PLAYER 1's stats
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer2) : (currentPlayer1)), 1); //UPDATE PLAYER 2's stats
                    resetGame(); //RESET GAME
                    //window.setScene(scene1);//GO TO TITLE

                }
                if (!AIMODE) {
                    player1Move = !player1Move; //if your not playing agaist the ai, then switch those players turn it is
                }
            });
            //play animations
            animation.play(); 
            animation2.play();
            turnUpdate(); // update the turn
        
        } else { //if gravity is going up... we have to handle the placing slighty differently
            
       
            int row = 0;
            disc.setTranslateY(500);
            disc.text.setTranslateY(500);
            do {
                if (!getDisc(column, row).isPresent()) // if there is a disc then break and use the row below that
                    break;
                row++; 
            } while (row <= ROWS - 1);//start from the lowest row and go up... 
            if (row > ROWS - 1) {
                return;
            }

            ///I'm not gonna recomment code it's the same as the other place basically... 
              
            grid.spaces[column][row] = disc;
            disc.gridX = column;
            disc.gridY = row;

            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

            grid.discRoot.getChildren().add(disc);
            if (disc.Life >= 10) {
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7 * (TILE_SIZE / 16)));
            } else {
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75 * (TILE_SIZE / 16)));
            }
            grid.discRoot.getChildren().add(disc.text);

            TranslateTransition animation = new TranslateTransition(Duration.millis(250), disc);
            TranslateTransition animation2 = new TranslateTransition(Duration.millis(250), disc.text);
            animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation2.setToY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + (1.25 * (TILE_SIZE / 2)));
            final int currentrow = row;
            animation.setOnFinished(e -> {
                if (grid.gameEnded(column, currentrow, player1Move)) {
                    System.out.println("Winner:" + (player1Move ? (currentPlayer1) : (currentPlayer2)));
                    resetGame();
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer1) : (currentPlayer2)), 0);
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer2) : (currentPlayer1)), 1);
                    //window.setScene(scene1);
                }
                if (!AIMODE) {
                    player1Move = !player1Move;
                }
            });

            animation.play();
            animation2.play();
            turnUpdate();
        }

    }

    private void placeDiscAi(Disc disc, int column) { //Not comment this code either as its almost the same as the player place (open to see the difference)
        
        //the difference between this method and the one the player uses is that this one handles the turn update differently.
        
        if (gravityAngle == 270) {
            int row = ROWS - 1;
            do {
                if (!getDisc(column, row).isPresent())
                    break;
                row--;
            } while (row >= 0);
            if (row < 0) {
                return;
            }

      
            Text text = new Text("" + turnCount);
            disc.text.setText("" + disc.Life);
            StackPane stack = new StackPane();
            disc.text.setBoundsType(TextBoundsType.VISUAL);


            grid.spaces[column][row] = disc;
            disc.gridX = column;
            disc.gridY = row;
            //System.out.println(disc);
            stack.getChildren().add(disc);
            stack.getChildren().add(text);

            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);
            //disc.setTranslateY(row *(TILE_SIZE + 5)+ TILE_SIZE/4);

            grid.discRoot.getChildren().add(disc);
            if (disc.Life >= 10) {
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7 * (TILE_SIZE / 16)));
            } else {
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75 * (TILE_SIZE / 16)));
            }
            grid.discRoot.getChildren().add(disc.text);


            // grid.discRoot.getChildren().add(stack);

            TranslateTransition animation = new TranslateTransition(Duration.millis(250), disc);
            TranslateTransition animation2 = new TranslateTransition(Duration.millis(250), disc.text);
            animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation2.setToY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + (1.25 * (TILE_SIZE / 2)));
            final int currentrow = row;
            animation.setOnFinished(e -> {
                if (gameEnded(column, currentrow)) {
                    System.out.println("Winner:" + (player1Move ? (currentPlayer1) : (currentPlayer2)));
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer1) : (currentPlayer2)), 0);
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer2) : (currentPlayer1)), 1);
                    resetGame();
                    //window.setScene(scene1);
                }
            });

            animation.play();
            animation2.play();
            // turnUpdate();
        } else {
            int row = 0;
            disc.setTranslateY(500);
            disc.text.setTranslateY(500);
            do {
                if (!getDisc(column, row).isPresent()) // if its pretty then break the loop
                    break;
                row++; // if its empty take it down another row
            } while (row <= ROWS - 1);
            if (row > ROWS - 1) {
                return;
            }

            // Circle circle = new Circle();
            Text text = new Text("" + turnCount);
            disc.text.setText("" + disc.Life);

            StackPane stack = new StackPane();
            disc.text.setBoundsType(TextBoundsType.VISUAL);


            grid.spaces[column][row] = disc;
            disc.gridX = column;
            disc.gridY = row;

            disc.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);
            //disc.setTranslateY(row *(TILE_SIZE + 5)+ TILE_SIZE/4);

            grid.discRoot.getChildren().add(disc);
            if (disc.Life >= 10) {
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (3.7 * (TILE_SIZE / 16)));
            } else {
                disc.text.setTranslateX((column * (TILE_SIZE + 5) + TILE_SIZE / 4) + (6.75 * (TILE_SIZE / 16)));
            }
            grid.discRoot.getChildren().add(disc.text);


            // grid.discRoot.getChildren().add(stack);

            TranslateTransition animation = new TranslateTransition(Duration.millis(250), disc);
            TranslateTransition animation2 = new TranslateTransition(Duration.millis(250), disc.text);
            animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
            animation2.setToY((row * (TILE_SIZE + 5) + TILE_SIZE / 4) + (1.25 * (TILE_SIZE / 2)));
            final int currentrow = row;
            animation.setOnFinished(e -> {
                if (gameEnded(column, currentrow)) {
                    System.out.println("Winner:" + (player1Move ? (currentPlayer1) : (currentPlayer2)));
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer1) : (currentPlayer2)), 0);
                    gameDatabase.playerUpdateStats((player1Move ? (currentPlayer2) : (currentPlayer1)), 1);
                    resetGame();
                    //window.setScene(scene1);
                }
            });

            animation.play();
            animation2.play();
            //  turnUpdate();
        }

    }

    private Optional < Disc > getDisc(int column, int row) { //method that returns if a disc is in a certain location
        if (column < 0 || column >= COLUMNS || row < 0 || row >= ROWS)
            return Optional.empty();

        return Optional.ofNullable(grid.spaces[column][row]);
    }

    private boolean gameEnded(int column, int row) {
        
        //so... to check if someone won we need to check if any other 3 discs are in a line next to where we just played
        // column and row are the location of the last played disc
        
        List < Point2D > vertical = IntStream.rangeClosed(row - 3, row + 3) // check vertically up and down 3 discs
            .mapToObj(r -> new Point2D(column, r))
            .collect(Collectors.toList());
        List < Point2D > horizontal = IntStream.rangeClosed(column - 3, column + 3) //check horizontally left and right 3 discs
            .mapToObj(c -> new Point2D(c, row))
            .collect(Collectors.toList());

        Point2D topLeft = new Point2D(column - 3, row - 3); // check  on diagonals that go down
        List < Point2D > diagonal1 = IntStream.rangeClosed(0, 6)
            .mapToObj(i -> topLeft.add(i, i))
            .collect(Collectors.toList());

        Point2D botLeft = new Point2D(column - 3, row + 3); //check diagonals that go up
        List < Point2D > diagonal2 = IntStream.rangeClosed(0, 6)
            .mapToObj(i -> botLeft.add(i, -i))
            .collect(Collectors.toList());

        return checkRange(vertical) || checkRange(horizontal) || checkRange(diagonal1) || checkRange(diagonal2); // if any of these return a line of 4 then there is a win.
    }

    private boolean checkRange(List < Point2D > points) { //checks a possible line for a connect 4
        int chain = 0;
        for (Point2D p: points) {
            int column = (int) p.getX();
            int row = (int) p.getY();

            Disc disc = getDisc(column, row).orElse(new Disc(!player1Move, TILE_SIZE));

            if (disc.red == player1Move) {  //if the disc we are check is the same color as the current players color...

                chain++; // we have a disc in a chain
              
                if (chain == 4) { // if we hit 4 discs in a line...
                    return true; // we have a win
                }
            } else {
                chain = 0; // else there was no chain
            }
        }
        return false;
    }
    
      public void checkwin(){
        boolean hit = false; // if we detect a
          for (int y = 0; y <= ROWS - 1; y++) {
           
                        for (int x = 0; x <= COLUMNS - 1; x++) {
                                if(grid.gameEnded(x,y,true)){
                                    System.out.println("Winner: "+currentPlayer1);                                   
                                        gameDatabase.playerUpdateStats(currentPlayer1, 0); //UPDATE PLAYER 1's stats
                                        gameDatabase.playerUpdateStats(currentPlayer2, 1); //UPDATE PLAYER 2's stats
                                        resetGame(); //RESET GAME
                                        //window.setScene(scene1);//GO TO TITLE
                                    hit = true;
                                    break;
                               }
                              

                }
                          if (hit){
                                    break;
                                }
            }
                   for (int y = 0; y <= ROWS - 1; y++) {
                        for (int x = 0; x <= COLUMNS - 1; x++) {
                                if(grid.gameEnded(x,y,false)){
                                    if(!hit){
                                    System.out.println("Winner: "+currentPlayer2);
                                      gameDatabase.playerUpdateStats(currentPlayer2, 0); //UPDATE PLAYER 1's stats
                                        gameDatabase.playerUpdateStats(currentPlayer1, 1); //UPDATE PLAYER 2's stats
                                        resetGame(); //RESET GAME
                                        //window.setScene(scene1);//GO TO TITLE
                                    }else{
                                     System.out.println("Draw");   
                                       gameDatabase.playerUpdateStats(currentPlayer1, 2); //UPDATE PLAYER 1's stats
                                        gameDatabase.playerUpdateStats(currentPlayer2, 2); //UPDATE PLAYER 2's stats
                                        resetGame(); //RESET GAME
                                        //window.setScene(scene1);//GO TO TITLE
                                    }
                                    hit = true;
                                    break;
                               }
                               

                }
                         if (hit){
                                    break;
                                }
            }
    }
   



}