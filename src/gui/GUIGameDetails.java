package src.gui;

import src.gui.GUIGame;
import src.TerminalConnect4.Board;

import javax.swing.*;

class GUIGameDetails extends JPanel {
    private JLabel l;
    private boolean isTwitchGame;

    GUIGameDetails(boolean isTwitchGame) {
        this.isTwitchGame = isTwitchGame;
        l = new JLabel("Yellow's Turn");
        add(l);
    }

    public void updateLabel(boolean isRunning, boolean turn, Board board) {
        if (board.isDraw) {
            l.setText("Draw!");
            ((GUIGame) getParent()).gameOver(isRunning, turn, board);
            return;
        }
        if (!isRunning) {
            l.setText((!turn ? (isTwitchGame ? "Twitch" : "Red") : "Yellow") + " Won!");
            ((GUIGame) getParent()).gameOver(isRunning, turn, board);
        } else {
            l.setText((turn ? (isTwitchGame ? "Twitch" : "Red") : "Yellow") + "'s Turn");
        }
    }
}
