package src.TwitchBot;
import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import src.JsonDeserialization.JsonManager;
import src.JsonDeserialization.SettingsSettings;
import src.TerminalConnect4.Board;
import src.gui.GUIGameGrid;


public class BotRunner extends Thread {

    public static String OAUTH = "";
    public static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    public static String USER = "";
    private static int CollectionTime;
    private static boolean ChatWait;
    private TwitchBot bot;

    public BotRunner(){
        try {
            SettingsSettings settings = new JsonManager().readJSON("settings.json");
            this.USER = settings.Username;
            this.OAUTH = settings.OAuth;
            this.CollectionTime = Integer.parseInt(settings.WaitTime);
            if(settings.ChatWait) {
                this.ChatWait = settings.ChatWait;
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void run() {
             bot = new TwitchBot(USER, CollectionTime, ChatWait);
            bot.setVerbose(true);
            try {
                bot.connect(ADDRESS, PORT, OAUTH);
            } catch (IOException e) {
                System.exit(4);
            } catch (IrcException e) {
                System.exit(4);
            }
            bot.joinChannel("#" + USER);
            bot.sendMessage("#" + USER, "Twitch plays Connect 4 is now active");

    }
    public void dispose(){
        bot.disconnect();
        bot.dispose();
        bot = null;
    }

    public boolean checkInvalid(){
        return bot.isInvalid;
    }


    public void empty(){
        bot.empty();
    }
    public void giveBoard(Board b){
        bot.giveBoard(b);
    }
    public void giveGUI(GUIGameGrid g){
        bot.giveGUI(g);
    }
}