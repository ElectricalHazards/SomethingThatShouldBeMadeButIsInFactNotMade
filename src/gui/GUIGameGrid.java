package src.gui;

import src.StretchIcon;
import src.TerminalConnect4.Board;
import src.TwitchBot.BotRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIGameGrid extends JPanel {
    private JButton[] buttons;
    private JLabel[][] grid;
    private int lastClicked = -1;
    private int turnCount = 0;
    private int turnCountLast = 0;
    private Board board;
    private boolean player;
    private GUIGameDetails parent;
    public static boolean isTwitchConnected = false;
    public static BotRunner botRunner;
    public boolean doesHaveMe = false;
    public GUIGameGrid self;

    GUIGameGrid(Board board, boolean isTwitchGame) {
        if (!isTwitchGame) {
            //parent = (GUIGame) getParent();
            // System.out.println(parent.getName());
            this.board = board;
            buttons = new JButton[7];
            for (int i = 0; i < buttons.length; i++) {
                ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("arrow"+(i+1)+".png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(55, 55, Image.SCALE_SMOOTH); // scale it the smooth way
                ImageIcon newImageIcon = new ImageIcon(newimg);
                buttons[i] = new JButton(new StretchIcon(newimg));
                //buttons[i].setPreferredSize(new Dimension(newImageIcon.getIconWidth(),newImageIcon.getIconHeight()));
                buttons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int index = 0;
                        for (int whatEver = 0; whatEver < buttons.length; whatEver++) {
                            if (e.getSource() == buttons[whatEver]) {
                                index = whatEver;
                                break;
                            }
                        }
                        if (board.isRunning()) {
                            if (board.dropthechild((player ? 1 : 2), index)) {
                                board.theySayChildrenAreTheChickenOfTheOrphanarium();
                                player = !player;
                                updateBoard();
                                ((GUIGame) getParent()).updateLabel(board.isRunning(), player, board);
                            }
                        }
                    }
                });
            }
        } else {
            this.board = board;
            self = this;
            buttons = new JButton[7];
            for (int i = 0; i < buttons.length; i++) {
                ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("arrow.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(55, 55, Image.SCALE_SMOOTH); // scale it the smooth way
                ImageIcon newImageIcon = new ImageIcon(newimg);
                buttons[i] = new JButton(new StretchIcon(newimg));
                //buttons[i].setPreferredSize(new Dimension(newImageIcon.getIconWidth(),newImageIcon.getIconHeight()));
                buttons[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(!doesHaveMe){
                            botRunner.giveGUI(self);
                            botRunner.giveBoard(board);
                            doesHaveMe = true;
                        }
                        int index = 0;
                        for (int whatEver = 0; whatEver < buttons.length; whatEver++) {
                            if (e.getSource() == buttons[whatEver]) {
                                index = whatEver;
                                break;
                            }
                        }
                        if (board.isRunning()) {
                            if (!player) {
                                if (board.dropthechild(2, index)) {
                                    board.theySayChildrenAreTheChickenOfTheOrphanarium();
                                    player = !player;
                                    updateBoard();
                                    ((GUIGame) getParent()).updateLabel(board.isRunning(), player, board);
                                    if(board.isRunning()){
                                        botRunner.empty();
                                    }
                                }
                            } else if (player) {
                                /*if (board.dropthechild(2, index)) {
                                    board.theySayChildrenAreTheChickenOfTheOrphanarium();
                                    player = !player;
                                    updateBoard();
                                    ((GUIGame) getParent()).updateLabel(board.isRunning(), player, board);
                                }*/
                            }
                        }
                    }
                });
            }
        }


        for (JButton b : buttons) {
            add(b);
        }

        grid = new JLabel[6][7];

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("empty.png")); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(55, 55, Image.SCALE_SMOOTH); // scale it the smooth way
                grid[r][c] = new JLabel(new StretchIcon(newimg));
                add(grid[r][c]);
            }
        }


        setLayout(new GridLayout(7, 7));
        //setSize(245,266);


	    /*
	    DisplayGraphics g = new DisplayGraphics();
	    this.add(g);
	    */
    }

    public void updateBoard() {
        String[] conversions = {"empty.png", "red.png", "yellow.png"};
        int[][] boardBoard = board.getBoard();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource(conversions[boardBoard[r][c]])); // load the image to a imageIcon
                Image image = imageIcon.getImage(); // transform it
                Image newimg = image.getScaledInstance(55, 55, Image.SCALE_SMOOTH);
                grid[r][c].setIcon(new StretchIcon(newimg));
            }
        }
    }

    public void reset() {
        player = false;
        board.reset();
        ((GUIGame) getParent()).updateLabel(board.isRunning(), player, board);
        updateBoard();
    }

    public void afterBot(){
        this.board.theySayChildrenAreTheChickenOfTheOrphanarium();
        player = !player;
        updateBoard();
        ((GUIGame) getParent()).updateLabel(board.isRunning(), player, board);
    }

    public void end() {
        //The use of !player is because the check win function runs at the very end of the turn and so pressing the button has already changed it to the next player (the loser) 's turn'
        board.setIsRunning(false);
    }
}
