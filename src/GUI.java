package src;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.TwitchBot.BotRunner;
import src.gui.FixAuth;
import src.gui.GUIGame;
import src.gui.GUIGameGrid;
import src.gui.SettingsMenu;

public class GUI extends JFrame{
	private JPanel menu;
	private static JPanel container;
	private GUIGame game, twitchGame;
	private static CardLayout cards = new CardLayout();
	private JButton goToGame,goToTwitchGame,settings;
	private JLabel menuLabel;
	private Font menuLabelFont;
	private SettingsMenu settingsMenu;
	private GUIGameGrid GuiGameGrid;
	private FixAuth fixAuth;

	public GUI() {

		container = new JPanel();
		container.setLayout(cards);
		
		menu = new JPanel();
		menu.setLayout(new GridLayout(7,1));
		menuLabelFont = new Font(Font.SANS_SERIF,Font.BOLD,20);
		menuLabel = new JLabel("Welcome To Connect 4");
		menuLabel.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		menuLabel.setFont(menuLabelFont);

		menu.add(menuLabel);
		goToGame = new JButton("Play Connect 4");
		menu.add(goToGame);
		goToTwitchGame = new JButton("Play Connect 4 With Twitch Bot");
		menu.add(goToTwitchGame);
		settings = new JButton("Settings");
		menu.add(settings);



		container.add(menu, "menu");
		game = new GUIGame();
		container.add(game, "game");
		twitchGame = new GUIGame(true);
		container.add(twitchGame,"twitchGame");
		settingsMenu = new SettingsMenu();
		container.add(settingsMenu,"settings");
		fixAuth = new FixAuth();
		container.add(fixAuth, "fixAuth");
		cards.show(container,"menu");

		goToGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cards.show(container, "game");
			}
		});
		goToTwitchGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!GuiGameGrid.isTwitchConnected) {
					GuiGameGrid.botRunner = new BotRunner();
					GuiGameGrid.botRunner.run();
					if(!GuiGameGrid.botRunner.checkInvalid()){
						GuiGameGrid.isTwitchConnected = true;
						//setTitle("Connect 4 For Twitch");
						cards.show(container,"twitchGame");
					}
					else{
						GUIGameGrid.botRunner.dispose();
						GUIGameGrid.botRunner = null;
						cards.show(container, "fixAuth");
					}
				}
			}
		});
		settings.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cards.show(container, "settings");
			}
		});		

		add(container);

		pack();
		setMinimumSize(new Dimension(400,400));
		setSize(600,600);
	    //setResizable(false);
	    setVisible(true);
	    setTitle("Connect 4");
	    setIconImage(new ImageIcon("Connect4Assets/icon.png").getImage());
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void backToMenu(){
		cards.show(container, "menu");
	}
	
}

