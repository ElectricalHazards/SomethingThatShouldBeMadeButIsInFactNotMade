package src.TwitchBot;
import org.jibble.pircbot.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

import src.TwitchBot.PircBot;

public class TwitchBot extends PircBot {

    private final String requestedNick;

    private String realNick;
    private String realServer;

    private HashMap<Integer,Integer> CollectedVotes= new HashMap<Integer, Integer>();
    private boolean isCollectingMessages = false;

    public TwitchBot(String nick) {
        this.requestedNick = nick;

        setName(this.requestedNick);
        setLogin(this.requestedNick);
    }

    @Override
    protected void onConnect() {
        super.onConnect();
        System.out.println("Connected!");

        // Sending special capabilities.
        sendRawLine("CAP REQ :twitch.tv/membership");
        sendRawLine("CAP REQ :twitch.tv/commands");
        sendRawLine("CAP REQ :twitch.tv/tags");
    }

    @Override
    protected void handleLine(String line) {
        super.handleLine(line);

        if (line.startsWith(":")) {
            String[] recvLines = line.split(" ");

            // First message is 001, extract logged in information.
            if (recvLines[1].equals("001")) {
                this.realServer = recvLines[0].substring(1);
                this.realNick = recvLines[2];
                System.out.println("realServer: " + this.realServer);
                System.out.println("realNick: " + this.realNick);
            }
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        super.onJoin(channel, sender, login, hostname);
        if (sender.equals(this.realNick)){
            System.out.println("Successfully joined: " + channel);
        }
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        System.out.println(sender+" "+message+" ");
        if(isCollectingMessages){
            if(isNumeric(message.toLowerCase(Locale.ROOT))){
                int vote = Integer.parseInt(message.toLowerCase(Locale.ROOT));
                boolean flag = false;
                if(CollectedVotes.containsKey(vote)){
                        CollectedVotes.put(vote, CollectedVotes.get(vote)+1);
                }
                else{
                    CollectedVotes.put(vote, 1);
                }
            }
            else{
                if(message.equalsIgnoreCase("s")){
                    sendMessage(channel, "Winner = "+stopCollectingMessages());
                }
            }
        }
        else{
            if(message.equalsIgnoreCase("c")){
                System.out.println("HIII");
                startCollectingMessages();
                sendMessage(channel,"Now collecting messages");
            }
        }
    }

    public void startCollectingMessages(){
        CollectedVotes.clear();
        isCollectingMessages = true;
    }
    public int stopCollectingMessages(){
        isCollectingMessages = false;
        int max = -1;
        System.out.println(CollectedVotes);
        for(int i = 0; i < CollectedVotes.keySet().size(); i++){
            if(CollectedVotes.keySet().stream().toList().get(i)>max){
                max = i;
            }
        }
        return max;
    }
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }
}