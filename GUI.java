import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GUI extends JFrame{

  JLabel turn;
  JButton[] buttons;
  Board board;
  JLabel[][] grid;
  int lastClicked = -1;
  int turnCount = 0;
  int turnCountLast = 0;
  boolean player;
  boolean isRunning = true;

  public GUI(Board board){
    this.board = board;
    buttons = new JButton[7];
    for (int i = 0; i < buttons.length; i++){
      ImageIcon imageIcon = new ImageIcon("Connect4Assets/arrow.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(55,55,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
      buttons[i] = new JButton(new ImageIcon(newimg));
      buttons[i].addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          int index = 0;
          for (int whatEver = 0; whatEver < buttons.length; whatEver++){
            if(e.getSource() == buttons[whatEver]){
              index = whatEver;
              break;
            }
          }
          if(isRunning){
            board.dropthechild((player?1:2),index);
            player = !player;
            updateBoard();
          }

        }
      }
      );
    }



    for (JButton b : buttons){
      add(b);
    }

    grid = new JLabel[6][7];

    for (int r = 0; r < grid.length; r ++){
      for (int c = 0; c < grid[r].length; c++){
        ImageIcon imageIcon = new ImageIcon("Connect4Assets/empty.png"); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(55,55,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        grid[r][c] = new JLabel(new ImageIcon(newimg));
        add(grid[r][c]);
      }
    }

    
    setLayout(new GridLayout(7,7));
    //setSize(245,266);
    setSize(400,400);
    setResizable(false);
    setVisible(true);
    setTitle("Connect 4: " + (player?"Red":"Yellow") + "'s Turn");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    /*
    DisplayGraphics g = new DisplayGraphics();
    this.add(g);
    */
  }

  public void updateBoard(){
    String[] conversions = {"Connect4Assets/empty.png","Connect4Assets/red.png","Connect4Assets/yellow.png"};
    int[][] boardBoard = board.getBoard();
    for (int r = 0; r < grid.length; r++){
      for(int c = 0; c < grid[r].length; c++){
        ImageIcon imageIcon = new ImageIcon(conversions[boardBoard[r][c]]); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(55,55,  java.awt.Image.SCALE_SMOOTH);
        grid[r][c].setIcon(new ImageIcon(newimg));
      }
    }
    setTitle("Connect 4: " + (player?"Red":"Yellow") + "'s Turn");
  }
  public void end(){
      //The use of !player is because the check win function runs at the very end of the turn and so pressing the button has already changed it to the next player (the loser) 's turn'
      setTitle("Connect 4: " + (!player?"Red":"Yellow") + " Has Won");
      isRunning = false;
  }

}