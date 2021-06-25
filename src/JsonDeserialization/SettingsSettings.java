package src.JsonDeserialization;

public class SettingsSettings {
    public String Username;
    public String OAuth;
    public String WaitTime;
    public boolean HideUsername;
    public boolean ChatWait;
    public SettingsSettings(){}
    public SettingsSettings(String username, boolean hideUsername, String oauth, String waitTime, boolean chatWait){
        this.Username = username;
        this.HideUsername = hideUsername;
        this.OAuth = oauth;
        this.WaitTime = waitTime;
        this.ChatWait = chatWait;
    }
}
