package src.gui;

import src.GUI;
import src.TerminalConnect4.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIGame extends JPanel {
    private GUIGameDetails d;
    private GUIGameGrid g;
    public Board board;
    private JDialog dialog;
    private JLabel dialogL;
    private JButton toMenu, again;
    private boolean isTwitchGame;

    public GUIGame() {
        this(false);
    }

    public GUIGame(boolean isTwitchGame) {
        super(new BorderLayout());

        this.isTwitchGame = isTwitchGame;
        this.board = new Board();
        d = new GUIGameDetails(isTwitchGame);
        g = new GUIGameGrid(board, isTwitchGame);

        dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Game Over");
        dialogL = new JLabel();
        dialogL.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        toMenu = new JButton("Exit To Main Menu");
        toMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                g.reset();
                GUI.backToMenu();
            }
        });
        again = new JButton("New Game");
        again.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                g.reset();
            }
        });


        dialog.add(dialogL);
        dialog.add(toMenu);
        dialog.add(again);


        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.setVisible(false);
                g.reset();
                GUI.backToMenu();
            }
        });
        dialog.setLayout(new GridLayout(7, 1));
        dialog.setSize(300, 300);


        add(d, BorderLayout.NORTH);
        add(g, BorderLayout.CENTER);


    }

    public void gameOver(boolean isRunning, boolean turn, Board board) {
        if (board.isDraw) {
            dialogL.setText("Draw!");
        } else if (!isRunning) {
            dialogL.setText((!turn ? (isTwitchGame ? "Twitch" : "Red") : "Yellow") + " Won!");
        } else {
            dialogL.setText((turn ? (isTwitchGame ? "Twitch" : "Red") : "Yellow") + "'s Turn");
        }
        dialog.setVisible(true);
    }

    public void updateLabel(boolean isRunning, boolean turn, Board board) {
        d.updateLabel(isRunning, turn, board);
    }

}
