
package ConnectFources;
import java.io.FileOutputStream;
import java.io.*;
import java.util.ArrayList;

//the database object holds and sorts players.
//this database has searching,sorting, and file io

public class Database implements Serializable{
    ArrayList <Player> playerList = new ArrayList (); // list oof players
    
    public Database(){
        
    }
    
   
    public void addPlayer(String s){ // adds new players
      if (playerList.size() == 0){
           playerList.add(new Player(s));
      }
      if (!checkIfNameExists(s) && playerList.size() > 0){ // if the player isnt already in the db, add it
            playerList.add(new Player(s));
      }
      
        
    }

    
    public boolean checkIfNameExists(String s){ // sequentially searches and returns true if it finds 
     boolean playerExists = false; 
        for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i).name.equals(s)){
               playerExists = true;
            }
        }
      return playerExists;
        
    }
    
    public Player playerSearch(String s){ // sequantially searches and returns the player when its found
         for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i).name.equals(s)){
               return playerList.get(i);
            }
        }
        return new Player("tbd");
    }
    
     public void playerUpdateStats(String s, int WinLoseTie){ // updates a players stats
         
         for (int i = 0; i < playerList.size(); i++){
            if (playerList.get(i).name.equals(s)){
            
               if(WinLoseTie == 0){
                 
                  playerList.get(i).wonMatch();
               }else if(WinLoseTie == 1){
                 
                  playerList.get(i).lostMatch();
               }else{
                  playerList.get(i).tiedMatch(); 
               }
            }
        }
      
    }
    
    public void printStats(){ // this is a method that orders the players based on how many matches they have played
         System.out.println("IN ORDER OF MATCHES PLAYED");
         Player[] playerListNew = new Player[playerList.size()]; // 
         
          for (int i = 0; i < playerList.size(); i++){
              playerListNew[i] = playerList.get(i); // copy playerlist and put it in normal array
          }

          
         //insertion sort to put them in order
         for (int i = 0; i < playerListNew.length; i++){
          Player smallestValue = new Player("tbd");
          smallestValue = playerListNew[i];
          int smallestIndex = i;
              for (int j = i+1; j < playerListNew.length; j++){
              if(smallestValue.calcTotalMatches() > playerListNew[j].calcTotalMatches()){
                    smallestValue = (playerListNew[j]);
                    smallestIndex = j;
              }
              playerListNew[smallestIndex] = playerListNew[i];
              playerListNew[i] = smallestValue;
              }
              
          
        }
         

         
         
         //print the array out with their match count
          for (int i = 0; i < playerListNew.length; i++){
              System.out.println(playerListNew[i].name+" | Gamecount: "+playerListNew[i].calcTotalMatches());
          }
    }
    
    	public void writeToFile() throws IOException{ //writes the db to the database.bin file
		@SuppressWarnings("resource")
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("database.bin"));
		objectOutputStream.writeObject(this);
	}
	
	public Database readFile() throws IOException, ClassNotFoundException{ // imports the db off the database. bin file
		@SuppressWarnings("resource")
		Database d = new Database();
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("database.bin"));
		d = (Database) objectInputStream.readObject();
		return d;
	}
	
    
    
}
