package src.TwitchBot;
import org.jibble.pircbot.*;

import java.util.*;

import src.TerminalConnect4.Board;
import src.TwitchBot.PircBot;
import src.gui.GUIGameGrid;

public class TwitchBot extends PircBot {

    private final String requestedNick;


    public boolean isInvalid = false;

    private String realNick;
    private String realServer;

    private HashMap<Integer, ArrayList<String>> CollectedVotes= new HashMap<Integer, ArrayList<String>>();
    private boolean isCollectingMessages = false;
    private int CollectionTime;
    private boolean WaitForConnect;
    private boolean isWaitingForMessage;
    private Board board;
    private GUIGameGrid guiGameGrid;
    private ArrayList<Integer> one, two;

    public TwitchBot(String nick, int connectionTime, boolean waitColledt) {
        this.requestedNick = nick;
        this.CollectionTime = connectionTime;
        this.WaitForConnect = waitColledt;

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
        if(line.contains(":tmi.twitch.tv NOTICE * :Improperly formatted auth")||line.contains(":tmi.twitch.tv NOTICE * :Invalid NICK")||line.contains(":tmi.twitch.tv NOTICE * :Login authentication failed")){
            isInvalid = true;
            this.disconnect();
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
        if(isCollectingMessages){
            if(isNumeric(message.toLowerCase(Locale.ROOT))){
                int vote = Integer.parseInt(message.toLowerCase(Locale.ROOT));
                if(vote<=7&&vote>=1){
                    if (CollectedVotes.containsKey(vote)) {
                            for(ArrayList<String> i : CollectedVotes.values().stream().toList()){
                                CollectedVotes.values().stream().toList().get(CollectedVotes.values().stream().toList().indexOf(i)).remove(sender);
                            }
                            CollectedVotes.get(vote).add(sender);
                    } else {
                        ArrayList<String> a = new ArrayList<>();
                        a.add(sender);
                        for(ArrayList<String> i : CollectedVotes.values().stream().toList()){
                            CollectedVotes.values().stream().toList().get(CollectedVotes.values().stream().toList().indexOf(i)).remove(sender);
                        }
                        CollectedVotes.put(vote, a);
                    }
                }
            }
        }
        else if(isWaitingForMessage){
            if(isNumeric(message.toLowerCase(Locale.ROOT))){
                int vote = Integer.parseInt(message.toLowerCase(Locale.ROOT));
                if(vote<=7&&vote>=1){
                    if (CollectedVotes.containsKey(vote)) {
                            for(ArrayList<String> i : CollectedVotes.values().stream().toList()){
                                CollectedVotes.values().stream().toList().get(CollectedVotes.values().stream().toList().indexOf(i)).remove(sender);
                            }
                            CollectedVotes.get(vote).add(sender);
                        isWaitingForMessage = false;
                    } else {
                        ArrayList<String> a = new ArrayList<>();
                        a.add(sender);
                        for(ArrayList<String> i : CollectedVotes.values().stream().toList()){
                            CollectedVotes.values().stream().toList().get(CollectedVotes.values().stream().toList().indexOf(i)).remove(sender);
                        }
                        CollectedVotes.put(vote, a);
                    }
                    isWaitingForMessage = false;
                }
            }
        }
        else{
            if(message.equalsIgnoreCase("s")){
                empty();
            }
        }
    }

    public void empty(){
        if(WaitForConnect){
            sendMessage("#"+requestedNick,"Waiting for valid vote to start collecting.");
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    isWaitingForMessage = true;
                    while (isWaitingForMessage){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    startCollectingMessages();
                    try {
                        Thread.sleep(CollectionTime*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stopCollecting();
                }
            });
            t1.start();
        }
        else {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    startCollectingMessages();
                    try {
                        Thread.sleep(CollectionTime*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stopCollecting();
                }
            });
            t1.start();
        }
    }


    public void startCollectingMessages(){
        isCollectingMessages = true;
        String output = "";
        if (WaitForConnect){
            output += "First vote collected. ";
        }
        output += "Collecting... "+CollectionTime+" seconds remain until votes are finalized.";
        sendMessage("#" + requestedNick, output);
    }
    public void stopCollecting(){
        int x = stopCollectingMessages();
        int y = stopCollectingMessages(true);
        CollectedVotes.clear();
        if(x==-1||y==-1){
            sendMessage("#"+requestedNick, "Error collecting messages, please try again");
            empty();
            return;
        }
        if(one != null || two != null){
            int index = new Random().nextInt(one.size());
            x = one.get(index);
            y = two.get(index);
            System.out.println(one);
            System.out.println(two);
            one = null;
            two = null;
        }
        sendMessage("#"+requestedNick, x+" wins with "+y+" votes.");
        if(board.dropthechild(1,x - 1)) guiGameGrid.afterBot();
        else {
            sendMessage("#"+requestedNick,"Column already full.");
            empty();
        }
    }
    public int stopCollectingMessages(){
        isCollectingMessages = false;
        int max = -1;
        int maxi = -1;
        System.out.println(CollectedVotes);
        for(int i = 0; i < CollectedVotes.values().size(); i++){
            if(CollectedVotes.values().stream().toList().get(i).size()>max){
                max = CollectedVotes.values().stream().toList().get(i).size();
                maxi = i;
                if(one != null){
                    one = null;
                }
            }
            else if(CollectedVotes.values().stream().toList().get(i).size()==max){
                if(one == null){
                    one = new ArrayList<Integer>();
                    one.add(CollectedVotes.keySet().stream().toList().get(maxi));
                }
                one.add(CollectedVotes.keySet().stream().toList().get(i));
            }
        }
        if(maxi == -1){
            return -1;
        }
        return CollectedVotes.keySet().stream().toList().get(maxi);
    }
    public int stopCollectingMessages(boolean isSpecial){
        isCollectingMessages = false;
        int max = -1;
        int maxi = -1;
        for(int i = 0; i < CollectedVotes.values().size(); i++){
            if(CollectedVotes.values().stream().toList().get(i).size()>max){
                max = CollectedVotes.values().stream().toList().get(i).size();
                maxi = i;
                if(two != null){
                    two = null;
                }
            }
            else if(CollectedVotes.values().stream().toList().get(i).size()==max){
                if(two == null){
                    two = new ArrayList<Integer>();
                    two.add(max);
                }
                two.add(CollectedVotes.values().stream().toList().get(i).size());
            }
        }
        if(max == -1){
            return -1;
        }
        return CollectedVotes.values().stream().toList().get(maxi).size();
    }
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[-+]?\\d*\\.?\\d+");
    }

    public void giveBoard(Board b){
        this.board = b;
    }
    public void giveGUI(GUIGameGrid g){
        this.guiGameGrid = g;
    }
}
