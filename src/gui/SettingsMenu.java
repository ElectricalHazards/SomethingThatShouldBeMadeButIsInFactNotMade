package src.gui;

import src.GUI;
import src.TwitchBot.BotRunner;
import src.gui.GUIGameGrid;
import src.JsonDeserialization.JsonManager;
import src.JsonDeserialization.SettingsSettings;

import src.gui.GUIGameGrid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SettingsMenu extends JPanel {
    private JLabel chNameL, oauthTokL, timeL;
    private JTextField timeCollecting;
    private JPasswordField chName, oauthTok;
    private JButton b, back, t;
    private JCheckBox chNameCB, oauthTokCB, waitToCollect;
    private boolean userClicked, waitClicked;
    //Wait untill valid answer is sent to start collecting answers button
    //Length of time collecting answers button
    String urlString;

    public Integer parseIntOrNull(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public SettingsMenu() {
        chNameL = new JLabel("Enter your channel name:");
        chName = new JPasswordField(25);
        chNameCB = new JCheckBox("Show Text");
        oauthTokL = new JLabel("Enter your OAuth Token");
        oauthTok = new JPasswordField(30);
        oauthTokCB = new JCheckBox("Show Text");
        b = new JButton("Get Oauth Token");
        back = new JButton("Back To Menu");
        t = new JButton("Test Bot Connection");
        waitToCollect = new JCheckBox("Start Timer After First Valid Chat Response Instead Of After Streamer's Move");
        timeL = new JLabel("Time spent collecting turn votes from chat: (Seconds)");
        timeCollecting = new JTextField(15);
        try {
            SettingsSettings settings = new JsonManager().readJSON("settings.json");
            chName.setText(settings.Username);
            oauthTok.setText(settings.OAuth);
            timeCollecting.setText(settings.WaitTime);
            if (settings.ChatWait) {
                waitToCollect.doClick();
                waitClicked = true;
            }
            if (!settings.HideUsername) {
                chNameCB.doClick();
                userClicked = true;
                chName.setEchoChar((char) 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        timeCollecting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Have run on text change
                if (parseIntOrNull(timeCollecting.getText()) == null) {
                    timeCollecting.setText("0");
                }
                //Have run on text change ^
            }
        });


        chNameCB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    userClicked = true;
                    chName.setEchoChar((char) 0);
                } else {
                    userClicked = false;
                    chName.setEchoChar('*');
                }
            }
        });
        oauthTokCB.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    oauthTok.setEchoChar((char) 0);
                } else {
                    oauthTok.setEchoChar('*');
                }
            }
        });
        waitToCollect.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    waitClicked = true;
                } else {
                    waitClicked = false;
                }
            }
        });

        urlString = "https://twitchapps.com/tmi/";
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(java.net.URI.create(urlString));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                t.setText("Testing connection...");
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JsonManager jsonManager = new JsonManager();
                        try {
                            jsonManager.writeJSON(new SettingsSettings(new String(chName.getPassword()), userClicked, new String(oauthTok.getPassword()), timeCollecting.getText(), waitClicked), "settings.json");
                        } catch (Exception j) {
                            j.printStackTrace();
                        }
                        GUIGameGrid.botRunner = new BotRunner();
                        GUIGameGrid.botRunner.run();
                        if(!GUIGameGrid.botRunner.checkInvalid()){
                            t.setText("Connection successful!");
                            t.setBackground(Color.green);
                            GUIGameGrid.botRunner.dispose();
                            GUIGameGrid.botRunner = null;
                        }
                        else{
                            t.setText("Error connecting");
                            GUIGameGrid.botRunner.dispose();
                            GUIGameGrid.botRunner = null;
                            t.setBackground(Color.red);
                        }
                    }
                });
                t1.start();
            }
        });
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JsonManager jsonManager = new JsonManager();
                try {
                    jsonManager.writeJSON(new SettingsSettings(new String(chName.getPassword()), userClicked, new String(oauthTok.getPassword()), timeCollecting.getText(), waitClicked), "settings.json");
                } catch (Exception j) {
                    j.printStackTrace();
                }
                if(GUIGameGrid.botRunner!=null) {
                    GUIGameGrid.botRunner.dispose();
                    GUIGameGrid.botRunner = null;
                }
                t.setText("Test Bot Connection");
                t.setBackground(back.getBackground());
                GUIGameGrid.isTwitchConnected = false;
                GUI.backToMenu();
            }
        });

        add(chNameL);
        add(chName);
        add(chNameCB);
        add(oauthTokL);
        add(oauthTok);
        add(oauthTokCB);
        add(b);
        add(t);
        add(back);
        add(timeL);
        add(timeCollecting);
        add(waitToCollect);


    }
}
