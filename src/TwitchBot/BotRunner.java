package src.TwitchBot;
import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;


public class BotRunner extends Thread {

    public static final String OAUTH = "oauth:mg6u2wkuylp5j1yagw9yo5jkugvseu";
    public static final String ADDRESS = "irc.twitch.tv.";
    private static final int PORT = 6667;
    public static final String USER = "rpg7777";

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
            bot.sendMessage("#" + USER, "Hello, I am a bot");

    }
}