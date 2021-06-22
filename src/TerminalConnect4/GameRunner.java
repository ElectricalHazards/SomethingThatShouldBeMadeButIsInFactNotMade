package src.TerminalConnect4;
import src.*;

import java.lang.*;
public class GameRunner extends Thread{
  public Board board;
//  public boolean player;
  public GameRunner(){
    board = new Board();
  }

  public void run(){
    GUI gui = new GUI(board);
  //  Scanner scanner = new Scanner(System.in);
  //  int rnd = new Random().nextInt(2);
  //  boolean player = (rnd==1?false:true);
    while(!hasWon()){

    }
    //System.out.println(board);
    gui.updateBoard();
    System.out.println("Someonewon, congration, someonedoneit");
    gui.end();
  }
  public boolean hasWon(){
    return board.theySayChildrenAreTheChickenOfTheOrphanarium();
  }
}