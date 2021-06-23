package src;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.JsonDeserialization.*;
import src.TerminalConnect4.*;
import src.TwitchBot.BotRunner;

public class GUI extends JFrame{
	private JPanel menu;
	private static JPanel container;
	private GUIGame game; 
	private static CardLayout cards = new CardLayout();
	private JButton goToGame,goToTwitchGame,settings;
	private JLabel menuLabel;
	private SettingsMenu settingsMenu;
	
	public GUI() {

		container = new JPanel();
		container.setLayout(cards);
		
		menu = new JPanel();
		menu.setLayout(new BoxLayout(menu,BoxLayout.Y_AXIS));
		menuLabel = new JLabel("Welcome To Connect 4");
		menu.add(menuLabel);
		goToGame = new JButton("Play Connect 4");
		menu.add(goToGame);
		goToTwitchGame = new JButton("Play Connect 4 With Twitch Bot");
		menu.add(goToTwitchGame);
		settings = new JButton("Settings");
		menu.add(settings);


		menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		goToGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		goToGame.setAlignmentY(Component.CENTER_ALIGNMENT);
		goToTwitchGame.setAlignmentX(Component.CENTER_ALIGNMENT);
		goToTwitchGame.setAlignmentY(Component.CENTER_ALIGNMENT);
		settings.setAlignmentX(Component.CENTER_ALIGNMENT);
		settings.setAlignmentY(Component.CENTER_ALIGNMENT);

		container.add(menu, "menu");
		game = new GUIGame();
		container.add(game, "game");
		settingsMenu = new SettingsMenu();
		container.add(settingsMenu,"settings");
		cards.show(container,"menu");

		goToGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cards.show(container, "game");
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

class SettingsMenu extends JPanel{
	private JLabel chNameL, oauthTokL, timeL;
	private JTextField timeCollecting;
	private JPasswordField chName, oauthTok;
	private JButton b, g, back;
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

	SettingsMenu(){
		chNameL = new JLabel("Enter your channel name:");
		chName = new JPasswordField(25);
		chNameCB = new JCheckBox("Show Text");
		oauthTokL = new JLabel("Enter your OAuth Token");
		oauthTok = new JPasswordField(30);
		oauthTokCB = new JCheckBox("Show Text");
		b = new JButton("Get Oauth Token");
		g = new JButton("ConnectBot(DEBUG)");
		back = new JButton("Back To Menu");
		waitToCollect = new JCheckBox("Start Timer After First Valid Chat Response Instead Of After Streamer's Move");
		timeL = new JLabel("Time spent waiting on chat to respond");
		timeCollecting = new JTextField(15);
		timeCollecting.setText("0");
		try {
			SettingsSettings settings = new JsonManager().readJSON("settings.json");
			chName.setText(settings.Username);
			oauthTok.setText(settings.OAuth);
			timeCollecting.setText(settings.WaitTime);
			if(settings.ChatWait){
				waitToCollect.doClick();
				waitClicked = true;
			}
			if(settings.HideUsername){
				chNameCB.doClick();
				userClicked = true;
			}
		}catch (Exception e){e.printStackTrace();}


		timeCollecting.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//Have run on text change
				if(parseIntOrNull(timeCollecting.getText())==null){
					timeCollecting.setText("0");
				}
				//Have run on text change ^
			}
		});


		chNameCB.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if (e.getStateChange()==1){
					userClicked = true;
					chName.setEchoChar((char) 0);
				}
				else{
					userClicked = false;
					chName.setEchoChar('*');
				}
			}
		});
		oauthTokCB.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if (e.getStateChange()==1){
					oauthTok.setEchoChar((char) 0);
				}
				else{
					oauthTok.setEchoChar('*');
				}
			}
		});
		waitToCollect.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange()==1){
					waitClicked = true;
				}
				else{
					waitClicked = false;
				}
			}
		});

		urlString = "https://twitchapps.com/tmi/";
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					Desktop.getDesktop().browse(java.net.URI.create(urlString));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		g.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BotRunner botRunner = new BotRunner();
				botRunner.run();
			}
		});
		back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JsonManager jsonManager = new JsonManager();
				try {
					jsonManager.writeJSON(new SettingsSettings(new String(chName.getPassword()), userClicked, new String(oauthTok.getPassword()), timeCollecting.getText(), waitClicked), "settings.json");
				}catch (Exception j){j.printStackTrace();}
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
		add(g);
		add(back);
		add(timeL);
		add(timeCollecting);
		add(waitToCollect);


	}
}

class GUIGame extends JPanel{
	private GUIGameDetails d;
	private GUIGameGrid g;
	private Board board;
	private static JDialog dialog;
	private static JLabel dialogL;
	private static JButton toMenu, again;

	
	GUIGame(){
		super(new BorderLayout());
		
		
		this.board = new Board();
		d = new GUIGameDetails();
		g = new GUIGameGrid(board);

		dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Game Over");
		dialogL = new JLabel();
		dialogL.setHorizontalAlignment((int) CENTER_ALIGNMENT);
		toMenu = new JButton("Exit To Main Menu");
		toMenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.setVisible(false);
				GUI.backToMenu();
			}
		});
		again = new JButton("New Game");

		dialog.add(dialogL);
		dialog.add(toMenu);
		dialog.add(again);


		dialog.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				dialog.setVisible(false);
				GUI.backToMenu();				
			}
		});
		dialog.setLayout(new GridLayout(7,1));
		dialog.setSize(300,300);

		
		add(d, BorderLayout.NORTH);
		add(g, BorderLayout.CENTER);
		

		
	}

	public static void gameOver(boolean isRunning, boolean turn, Board board){
		if(board.isDraw){
			dialogL.setText("Draw!");
			return;
		}
		if (!isRunning) {
			dialogL.setText((!turn ? "Red" : "Yellow") + " Won!");
		}
		else {
			dialogL.setText((turn ? "Red" : "Yellow") + "'s Turn" );
		}
		dialog.setVisible(true);
	}
	
}

class GUIGameDetails extends JPanel{
	private static JLabel l;
	
	GUIGameDetails(){
		l = new JLabel("Yellow's Turn");
		add(l);
	}
	
	public static void updateLabel(boolean isRunning, boolean turn, Board board) {
		if(board.isDraw){
			l.setText("Draw!");
			GUIGame.gameOver(isRunning, turn, board);
			return;
		}
		if (!isRunning) {
			l.setText((!turn ? "Red" : "Yellow") + " Won!");
			GUIGame.gameOver(isRunning, turn, board);
		}
		else {
			l.setText((turn ? "Red" : "Yellow") + "'s Turn" );
		}
	}
}

class GUIGameGrid extends JPanel{
	private JButton[] buttons;
	private JLabel[][] grid;
	private int lastClicked = -1;
	private int turnCount = 0;
	private int turnCountLast = 0;
	private Board board;
	private boolean player;

	  GUIGameGrid(Board board){
		this.board = board;
	    buttons = new JButton[7];
	    for (int i = 0; i < buttons.length; i++){
	      ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("arrow.png")); // load the image to a imageIcon
	        Image image = imageIcon.getImage(); // transform it 
	        Image newimg = image.getScaledInstance(55,55,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	      ImageIcon newImageIcon = new ImageIcon(newimg);
	      buttons[i] = new JButton(new StretchIcon(newimg));
	      //buttons[i].setPreferredSize(new Dimension(newImageIcon.getIconWidth(),newImageIcon.getIconHeight()));
	      buttons[i].addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	          int index = 0;
	          for (int whatEver = 0; whatEver < buttons.length; whatEver++){
	            if(e.getSource() == buttons[whatEver]){
	              index = whatEver;
	              break;
	            }
	          }
	          if(board.isRunning()){
	            if(board.dropthechild((player?1:2),index)){
					board.theySayChildrenAreTheChickenOfTheOrphanarium();
					player = !player;
					updateBoard();
					GUIGameDetails.updateLabel(board.isRunning(), player,board);
				}
	          }

	        }
	      }
	      );
	    }



	    for (JButton b : buttons){
	      add(b);
	    }

	    grid = new JLabel[6][7];

	    for (int r = 0; r < grid.length; r ++){
	      for (int c = 0; c < grid[r].length; c++){
	        ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("empty.png")); // load the image to a imageIcon
	        Image image = imageIcon.getImage(); // transform it 
	        Image newimg = image.getScaledInstance(55,55,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
	        grid[r][c] = new JLabel(new StretchIcon(newimg));
	        add(grid[r][c]);
	      }
	    }

	    
	    setLayout(new GridLayout(7,7));
	    //setSize(245,266);


	    /*
	    DisplayGraphics g = new DisplayGraphics();
	    this.add(g);
	    */
	  }

	  public void updateBoard(){
	    String[] conversions = {"empty.png", "red.png", "yellow.png"};
	    int[][] boardBoard = board.getBoard();
	    for (int r = 0; r < grid.length; r++){
	      for(int c = 0; c < grid[r].length; c++){
	        ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource(conversions[boardBoard[r][c]])); // load the image to a imageIcon
	        Image image = imageIcon.getImage(); // transform it 
	        Image newimg = image.getScaledInstance(55,55,  java.awt.Image.SCALE_SMOOTH);
	        grid[r][c].setIcon(new StretchIcon(newimg));
	      }
	    }
	  }
	  public void end(){
	      //The use of !player is because the check win function runs at the very end of the turn and so pressing the button has already changed it to the next player (the loser) 's turn'
	      board.setIsRunning(false);
	  }
}