import java.util.*;
import java.lang.*;
public class GameRunner extends Thread{
  private Board board;
  public GameRunner(){
    board = new Board();
  }

  public void run(){
    Scanner scanner = new Scanner(System.in);
     int rnd = new Random().nextInt(2);
    boolean player = (rnd==1?false:true);
    while(!hasWon()){
      String s = "a";
      int times = 0;

      //Get Move
      System.out.println(board);
      while(!isInteger(s)){
        if(times > 1){
          System.out.println("Player: "+(player?1:2)+" please make a move "+ InsultMaker.createInsult());
        }
        else{
          System.out.println("Player: "+(player?1:2)+" please make a move");
        }
        s = scanner.nextLine();
        if(isInteger(s)){
          if(!(Integer.parseInt(s) < 8) || !(Integer.parseInt(s) > 0) ){
            s = "a";
          }
        }
        times++;
      }


      //Act on move
      int location = Integer.parseInt(s);
      if(board.dropthechild((player?1:2),location-1)){
        player = !player;
      }
      else{
        continue;
      }
      times = 0;
    }
    System.out.println("Someonewon, whoopdedoo");
  }

  public boolean isInteger( String input ) {
    try {
        Integer.parseInt( input );
        return true;
    }
    catch( Exception e ) {
        return false;
    }
}

  public boolean hasWon(){
    return board.theySayChildrenAreTheChickenOfTheOrphanarium();
  }
}