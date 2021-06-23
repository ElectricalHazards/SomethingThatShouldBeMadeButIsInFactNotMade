package src.JsonDeserialization;

public class SettingsSettings {
    public SettingsSettings(){}
    public SettingsSettings(String username, String oauth, boolean hideUsername){
        this.Username = username;
        this.OAuth = oauth;
        this.hideUsername = hideUsername;
    }
    public String Username;
    public String OAuth;
    public boolean hideUsername;
}
