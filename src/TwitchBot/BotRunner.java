package src.TwitchBot;
import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import src.JsonDeserialization.JsonManager;
import src.JsonDeserialization.SettingsSettings;


public class BotRunner extends Thread {

    public static String OAUTH = "";
    public static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    public static String USER = "";
    private static int CollectionTime;
    private static boolean ChatWait;

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
            TwitchBot bot = new TwitchBot(USER);
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
}