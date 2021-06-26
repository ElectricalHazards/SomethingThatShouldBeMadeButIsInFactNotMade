package src.gui;

import src.GUI;
import src.TerminalConnect4.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIGame extends JPanel {
    private GUIGameDetails d;
    private GUIGameGrid g;
    public Board board;
    private JDialog dialog;
    private JLabel dialogL;
    private JButton toMenu, again;
    private boolean isTwitchGame;
    private AbstractAction backToMenu, reset, escape;

    public GUIGame() {
        this(false);
    }

    public GUIGame(boolean isTwitchGame) {
        super(new BorderLayout());


        backToMenu = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                g.reset();
                GUI.backToMenu();
            }
        };
        reset = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                g.reset();
            }
        };
        escape = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(! board.isRunning()) return;
                if (g.isTwitchsTurn()) return;
                dialogL.setText("Paused");
                dialogIsVisible(true);
            }
        };

        this.isTwitchGame = isTwitchGame;
        this.board = new Board();
        d = new GUIGameDetails(isTwitchGame);
        g = new GUIGameGrid(board, isTwitchGame);

        dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Game Over");
        dialogL = new JLabel();
        dialogL.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        toMenu = new JButton("Exit To Main Menu");
        toMenu.addActionListener(backToMenu);
        again = new JButton("New Game");
        again.addActionListener(reset);





        dialog.add(dialogL);
        dialog.add(toMenu);
        dialog.add(again);


        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(board.isRunning()){
                    return;
                }
                dialog.setVisible(false);
                g.reset();
                GUI.backToMenu();
            }
        });

        dialog.setLayout(new GridLayout(7, 1));
        dialog.setSize(300, 300);


        add(d, BorderLayout.NORTH);
        add(g, BorderLayout.CENTER);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"),"escape");
        getActionMap().put("escape", escape);

    }

    public void gameOver(boolean isRunning, boolean turn, Board board) {
        if (board.isDraw) {
            dialogL.setText("Draw!");
        } else if (!isRunning) {
            dialogL.setText((!turn ? (isTwitchGame ? "Twitch" : "Red") : (isTwitchGame ? "Streamer" : "Yellow")) + " Won!");
        } else {
            dialogL.setText((turn ? (isTwitchGame ? "Twitch" : "Red") : (isTwitchGame ? "Streamer" : "Yellow")) + "'s Turn");
        }
        dialogIsVisible(true);
    }

    public void updateLabel(boolean isRunning, boolean turn, Board board) {
        d.updateLabel(isRunning, turn, board);
    }

    public void dialogIsVisible(boolean isVisible){
        if (isVisible()){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension parentSize = getSize();
            Dimension childSize = dialog.getSize();
            Point parentLocationOnScreen = getLocationOnScreen();
            int x = (parentSize.width - childSize.width) / 2;
            int y = (parentSize.height - childSize.height) / 2;
            x += parentLocationOnScreen.x;
            y += parentLocationOnScreen.y;
            dialog.setLocation(x,y);

            dialog.setVisible(true);

        }
        else{
            dialog.setVisible(false);
        }
    }

}
