package src.TerminalConnect4;

import java.io.File;
import java.util.*;
import java.lang.*;
import src.*;
import src.JsonDeserialization.JsonManager;
import src.JsonDeserialization.SettingsSettings;

public class GameRunner extends Thread{
//  public boolean player;
  public GameRunner(){ }

  public void run(){
    File f = new File(new JsonManager().getSettingsPath()+"settings.json");
    if(!f.exists()){
      try {
        new JsonManager().writeJSON(new SettingsSettings(), "settings.json");
      }catch (Exception e){
        e.printStackTrace();
      }
    }
    GUI gui = new GUI();
  }
}
  //  Scanner scanner = new Scanner(System.in);
  //  int rnd = new Random().nextInt(2);
  //  boolean player = (rnd==1?false:true);
    /*
    while(!hasWon()){}
    //System.out.println(board);
    gui.updateBoard();
    System.out.println("Someonewon, congration, someonedoneit");
    gui.end();
  }
  public boolean hasWon(){
    return board.theySayChildrenAreTheChickenOfTheOrphanarium();
  }
}
    */