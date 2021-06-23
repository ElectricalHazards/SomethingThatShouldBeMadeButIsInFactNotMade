package src.TwitchBot;
import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;


public class BotRunner extends Thread {

    public static final String OAUTH = "";
    public static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    public static final String USER = "";

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