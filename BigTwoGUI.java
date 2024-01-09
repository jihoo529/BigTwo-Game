import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

/**
 * This class is used for modeling a user interface for the Big Two card game.
 * 
 * @author jihoo
 */
public class BigTwoGUI implements CardGameUI{
	private final static int MAX_CARD_NUM = 13; // max. no. of cards each player holds
	private BigTwo game = null; // a BigTwo object
	private ArrayList<CardGamePlayer> playerList; // the list of players
	private ArrayList<Hand> handsOnTable; // the list of hands played on the
	private int activePlayer = -1; // the index of the active player
	private boolean[] selected = new boolean[MAX_CARD_NUM]; // selected cards
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private JTextArea chatArea;
	private JTextField chatInput;
	private Image[] portraits;
	private Image[][] cardsImg;
	private JToolBar toolBar;
	private JButton gameButton;
	private JMenuItem restartButton;
	private JMenuItem quitButton;
	private JPopupMenu popupMenu;
	private JPanel topPanel;
	
	/**
	 * Constructor which creates and returns an instance of the BigTwoGUI class.
	 * 
	 * @param game a BigTwo object associated with this GUI
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		playerList = game.getPlayerList();
		handsOnTable = game.getHandsOnTable();
		
		//Create Frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		//Create tool bar
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setPreferredSize(new Dimension(frame.getWidth(), 30));
		toolBar.add(Box.createHorizontalGlue());
		toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//Create game button on the tool bar
		gameButton = new JButton("Game");
		
		//Create restart and quit button 
		restartButton = new JMenuItem("Connect");
		quitButton = new JMenuItem("Quit");
		
		//add game button to the tool bar
		toolBar.add(gameButton);
		
		//Create pop up menu and add restart and quit button
		popupMenu = new JPopupMenu();
		popupMenu.add(restartButton);
		popupMenu.add(quitButton);
		
		//Add event listeners on game, quit, restart button
		gameButton.addActionListener(new GameButtonListener());
		quitButton.addActionListener(new QuitMenuItemListener());
        restartButton.addActionListener(new ConnectMenuItemListener());
        
        //add tool bar to the frame
        frame.add(toolBar, BorderLayout.NORTH);
        
        //Create top panel, where bigTwo panel is on
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setLayout(new BorderLayout());
		bigTwoPanel.setBackground(new Color(0, 100, 0));
		bigTwoPanel.setOpaque(true);
		bigTwoPanel.setVisible(true);
		bigTwoPanel.setLocation(0, 0);
		topPanel.add(bigTwoPanel, BorderLayout.WEST);
		
		
		//Create bottom panel, where PLAY, PASS buttons and chat input area are located
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		//Create Play button
		playButton = new JButton("Play");
		bottomPanel.add(playButton, BorderLayout.WEST);
		
		//Create Pass button
		passButton = new JButton("Pass");
		bottomPanel.add(passButton, BorderLayout.EAST);
		
		//add event listener to bigtwo panel (mouse click) and action listeners to Play and Pass buttons
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		bigTwoPanel.addMouseListener(new BigTwoPanel());
		
		//panel for reulst messages and chat messages at right
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		//panel for result messages
		JPanel msgPanel = new JPanel();
		msgPanel.setLayout(new BorderLayout());
		
		msgArea = new JTextArea("");
		msgArea.setEditable(false);
		JScrollPane scrollMsg = new JScrollPane(msgArea);
		scrollMsg.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollMsg.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		msgPanel.add(scrollMsg, BorderLayout.CENTER);
		
		//Create chat panel
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatArea = new JTextArea("");
		JScrollPane scrollChat = new JScrollPane(chatArea);
		chatArea.setEditable(false);
		scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollChat.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatPanel.add(scrollChat, BorderLayout.CENTER);
		
		//make panel for chat and send buttons
		rightPanel.add(msgPanel);
		rightPanel.add(chatPanel);
		topPanel.add(rightPanel, BorderLayout.CENTER); 
		
		//Label the message input
		JLabel sendLabel = new JLabel("Message: ");
		chatInput = new JTextField(20);
		bottomPanel.add(sendLabel);
		bottomPanel.add(chatInput, BorderLayout.EAST);
		chatInput.addActionListener(new SendEnterListener());
		
		//add two panels to the frame
		frame.add(topPanel);
		frame.add(bottomPanel);
		
		frame.setSize(1000, 800);
		frame.setVisible(true);
	}
		
	/**
	 * getter method to get msgArea
	 * @return msgArea string value in msgArea
	 */
	public JTextArea getTextArea() {
		return msgArea; 
	}
	
	/**
	 * getter method to get chatArea
	 * @return chatArea string value in msgArea
	 */
	public JTextArea getChatArea() {
		return chatArea;
	}
	
	/**
	 * Sets the index of the active player.
	 * 
	 * @param activePlayer the index of the active player (i.e., the player who can
	 *                     make a move)
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= playerList.size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
			
		}
	}

	/**
	 * Redraws the GUI.
	 */
	public void repaint() {
		//if game is finished, do not output Player's turn
		
		if(!game.endOfGame()) {
			String name = playerList.get(activePlayer).getName();
			String msg = String.format("%s's turn:\n", name);
			getTextArea().append(msg);
		}
		if(game.getBigTwoClient().getPlayerID() == this.activePlayer) {
			enable();
		}
		else {
			disable();
		}
		frame.repaint();
	}

	/**
	 * Prints the specified string to the UI.
	 * 
	 * @param msg the string to be printed to the UI
	 */
	public void printMsg(String msg) {
		getTextArea().append(msg);
		getTextArea().setCaretPosition(getTextArea().getDocument().getLength());
	}
	
	public void printChat(String msg) {
		//String chat = chatInput.getText();
		chatArea.append(msg);
		chatArea.append("\n");
		chatInput.setText("");
	}
	/**
	 * Clears the message area of the UI.
	 */
	public void clearMsgArea() {
		// not used in non-graphical UI
		msgArea.setText("");
	}

	/**
	 * Resets the UI.
	 */
	public void reset() {
		// not used in non-graphical UI
		resetSelected();
		clearMsgArea();
		enable();
		
	}

	/**
	 * Enables user interactions.
	 */
	public void enable() {
		
		this.playButton.setEnabled(true);
		this.passButton.setEnabled(true);
		this.bigTwoPanel.setEnabled(true);
	}

	/**
	 * Disables user interactions.
	 */
	public void disable() {
		// not used in non-graphical UI
		this.playButton.setEnabled(false);
		this.passButton.setEnabled(false);
		this.bigTwoPanel.setEnabled(false);
	}

	/**
	 * Prompts active player to select cards and make his/her move.
	 */
	public void promptActivePlayer() {
		int[] cardIdx = getSelected();
		resetSelected();
		game.makeMove(activePlayer, cardIdx);
	}
	
	/**
	 * Returns an array of indices of the cards selected through the UI.
	 * 
	 * @return an array of indices of the cards selected, or null if no valid cards
	 *         have been selected
	 */
	private int[] getSelected() {
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}

		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}

	/**
	 * Resets the list of selected cards to an empty list.
	 */
	private void resetSelected() {
		for (int j = 0; j < selected.length; j++) {
			selected[j] = false;
		}
	}
	
	
	// Inner classes
	/**
	 *  This class is an inner class that extends the JPanel class and implements the MouseListener interface.
	 *  Overrides the paintComponent() method inherited from the JPanel class to draw the card game table. 
	 *  Implements the mouseReleased() method from the MouseListener interface to handle mouse click events.
	 *  @author jihoo
	 */
	public class BigTwoPanel extends JPanel implements MouseListener{
		private static final long serialVersionUID = 1L;
		private Image cardBack;
		/**
		 * Constructor of BigTwoPanel inner class
		 */
		public BigTwoPanel() {
			portraits = new Image[4];
			portraits[0] = new ImageIcon("img/avatars/pikachu.png").getImage();
			portraits[1] = new ImageIcon("img/avatars/charmander.png").getImage();
			portraits[2] = new ImageIcon("img/avatars/squirtle.png").getImage();
			portraits[3] = new ImageIcon("img/avatars/bulbasaur.png").getImage();
			
			//Store images to cardsImg array
			cardsImg = new Image[4][13];
			cardBack = new ImageIcon("img/cards/b.gif").getImage();
			//put card image into [4][13] array
			String[] suitNames = {"d", "c", "h", "s"};
			String[] rankNames = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
			
			for(int suitIdx = 0; suitIdx<suitNames.length; suitIdx++) {
				for(int rankIdx=0; rankIdx<rankNames.length; rankIdx++) {
					String imagePath = "img/cards/" + rankNames[rankIdx] + suitNames[suitIdx] + ".gif";
					cardsImg[suitIdx][rankIdx] = new ImageIcon(imagePath).getImage();
				}
			}
		}
		
		@Override
		/**
		 * This method draws the card game table.
		 * @param g Graphic component
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			bigTwoPanel.setOpaque(true);
			
			int imageWidth = 60;
			int imageHeight = 60;
			
			int rowHeight = getHeight() / 5;
			
			Font labelFont = new Font("Arial", Font.BOLD, 12); // Define the font for the labels
			int labelOffsetY = 15;
			
			for (int i = 0; i < portraits.length; i++) {
		        int y = 35 + i * rowHeight; // Calculate the y-coordinate for each row
		        
		        // Draw the image at the center of the row
		        int x = 0; // Align the image to the left
		        
		        String playerName = game.getPlayerList().get(i).getName();

		        if (game.getBigTwoClient() != null && playerName != null) {
		        	g.drawImage(portraits[i], x, y, imageWidth, imageHeight, null);
			        String label = "";

			        if (i==game.getBigTwoClient().getPlayerID()) {
			        	label = "You";
			        }
			        else {
			        	label = playerName;
			        }
		        	

			        int labelWidth = g.getFontMetrics(labelFont).stringWidth(label); // Calculate the width of the label
			        int labelX = x + (imageWidth - labelWidth) / 2; // Calculate the x-coordinate for centering the label
			        int labelY = y + imageHeight + labelOffsetY; // Calculate the y-coordinate for the label
			        g.setFont(labelFont);
			        g.drawString(label, labelX, labelY);
		        }
		        
			}
			
			//print the name of LastPlayer on the Table
	        Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);
			if (lastHandOnTable != null) {
				String lastHandLabel = lastHandOnTable.getPlayer().getName();
				int labelWidth = g.getFontMetrics(labelFont).stringWidth(lastHandLabel); // Calculate the width of the label
		        int labelX = 0 + (imageWidth - labelWidth) / 2; // Calculate the x-coordinate for centering the label
		        int labelY = 35 + 4  * rowHeight ; // Calculate the y-coordinate for the label
		        g.setFont(labelFont);
		        g.drawString("<Table>", labelX, labelY);
		        labelX += 15;
		        labelY += 15;
		        g.drawString(lastHandLabel, labelX - 10, labelY);
			}
			
			//Draw cards for each player
			for (int i = 0; i < 4; i++) {
		        // Draw the image at the center of the row
		        int x = 0; // Align the image to the left
				int y=this.getHeight()/5;
				
		        if (i==game.getBigTwoClient().getPlayerID()) {
		    		
		        	CardList cardsInHand = game.getPlayerList().get(game.getBigTwoClient().getPlayerID()).getCardsInHand();
		        	
		        	for(int j=0; j<cardsInHand.size(); j++) {
		        		Card card = cardsInHand.getCard(j);
		        		
		        		if (selected[j]) { //if card is selected one, make it little bit higher
		        			g.drawImage(cardsImg[card.getSuit()][card.getRank()], this.getWidth()/6+x, 30+y*i-25, this);
		        			x+=this.getWidth()/30;
		        		}
		        		else { //initially draw cards
		        			g.drawImage(cardsImg[card.getSuit()][card.getRank()], this.getWidth()/6+x, 30+y*i,  this);
							x+=this.getWidth()/30;
		        		}
		        	}
		        }
		        else { //draw other player's cards
		        	CardList cardsInHand = game.getPlayerList().get(i).getCardsInHand();
		        	for (int k=0; k< cardsInHand.size();k++) {
						g.drawImage(cardBack, this.getWidth()/6+x, 30+y*i, this);
						x+=this.getWidth()/30;
					}
		        }
		        
			}
			
			
			//draw table cards
			if (game.getHandsOnTable().size()!=0) {
				int x =0; 
				Hand handsOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);		
				
				for (int i=0; i<handsOnTable.size();i++) {
					Card card = handsOnTable.getCard(i);
					g.drawImage(cardsImg[card.getSuit()][card.getRank()], this.getWidth()/6+x, 10+4*(this.getHeight()/5),this);
					x+=this.getWidth()/30;
				}
			}
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			//ystem.out.println("clicked");
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		/**
		 * This method handles mouse click on each card of active player
		 * @param MouseEvent
		 */
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			int mouseX = e.getX();
			int mouseY = e.getY();
			int cardHeight = 97;
		    int cardWidth = 73;
		    int xGap = bigTwoPanel.getWidth() / 30; //distance between overlapped card

		    for (int i = 0; i < 4; i++) {
		    	int playerCardY = 30 + activePlayer * (bigTwoPanel.getHeight() / 5);
		    	
		    	if(mouseY >= playerCardY && mouseY <= playerCardY + cardHeight) {
		    		CardList cardsInHand = game.getPlayerList().get(activePlayer).getCardsInHand();
			    	for (int j=0; j<cardsInHand.size(); j++) {
			    		int cardX = bigTwoPanel.getWidth() / 6 + j * (bigTwoPanel.getWidth()/30); //calculate the x-coordinate of card
			    		
			    		//handle clicking the last card of player
			    		if(j== cardsInHand.size() - 1) {
			    			if (mouseX >= cardX && mouseX <= cardX + cardWidth) {
			    				//if card is clicked already, unclick it
				    			selected[j] = !selected[j];
				    			bigTwoPanel.repaint();
				    			return;
			    			}
			    		}
			    		//handle clicking non-last card of player
			    		if (mouseX >= cardX && mouseX <= cardX + xGap) {
			    			selected[j] = !selected[j];
			    			bigTwoPanel.repaint();
			    			return;
			    		}
			    	}
		    	}
		    }
			        
		}
			

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * This class is an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Play” button. 
	 * When the “Play” button is clicked, you should call the makeMove() method of your BigTwo object to make a move.
	 * @author jihoo
	 */
	public class PlayButtonListener implements ActionListener{
		@Override
		/**
		 * This method handle button-click events for the “Play” button. Calls promptActivePlayer and promptActivePlayer will call checkMove()
		 * If player tries to click play without choosing any cards, print select card to play.
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(getSelected()!=null ) {
				promptActivePlayer();
				repaint();
			} else {
				String msg = "Select card(s) to play!\n";
				printMsg(msg);
				repaint();
			}
			
		}
	}
	
	/**
	 * This class is an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Pass” button.
	 * When the “Pass” button is clicked, you should call the makeMove() method of your BigTwo object to make a move.
	 * @author jihoo
	 */
	public class PassButtonListener implements ActionListener{
		@Override
		/**
		 * This method handle button-click events for the “Pass” button.
		 * @param ActionEvent e
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			//if cards are not selected and press Pass
			if(getSelected() == null) {
				promptActivePlayer();
				repaint();
			}
			else {
				//if cards are selected and press Pass, reset clicked cards
				for(int i=0; i<selected.length; i++) {
					selected[i] = false;
				}
				promptActivePlayer();
				repaint();
			}
		}
	}
	
	/**
	 * This class is an inner class that implements the ActionListener interface.
	 * Implements the actionPerformed method from ActionListener interface to handle button-click events for "Game" button on the toolbar.
	 * When game button is clicked, it shows the pop-up menu.
	 * @author jihoo
	 */
	public class GameButtonListener implements ActionListener{
		@Override
		/**
		 * This method handle button-click events for the "Game" button
		 * @param ActionEvent e
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			popupMenu.show(gameButton, 0, gameButton.getHeight());
			
		}
		
	}
	
	/**
	 * This class is an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Restart” menu item. 
	 * When the “Restart” menu item is selected, you should (i) create a new BigTwoDeck object and call its shuffle() method; and (ii) call the start() method of your BigTwo object with the BigTwoDeck object as an argument.
	 * @author jihoo
	 */
	public class RestartMenuItemListener implements ActionListener{
		@Override
		/**
		 * This method handle menu-item-click events for the “Restart” menu item. It clears the message area, shuffle the deck, and start the game again.
		 * @param ActionEvent e
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			clearMsgArea();
			BigTwoDeck deck = new BigTwoDeck();
			deck.shuffle();
			game.start(deck);
		}
		
	}
	
	public class ConnectMenuItemListener implements ActionListener{
		@Override
		/**
		 * This method handle menu-item-click events for the “Restart” menu item. It clears the message area, shuffle the deck, and start the game again.
		 * @param ActionEvent e
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			game.getBigTwoClient().connect();
		}
		
	}
	
	/**
	 * This class is an inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Quit” menu item. 
	 * When the “Quit” menu item is selected, you should terminate your application. (You may use System.exit() to terminate your application.)
	 * @author jihoo
	 */
	public class QuitMenuItemListener implements ActionListener{
		@Override
		/**
		 * This method handles menu-item-click events for the “Quit” menu item. 
		 * @param ActionEvent e
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.exit(0);
		}
		
	}
	
	/**
	 * This class is an inner class that implements the ActionListener interface.
	 * Implements the actionPerformed() method from the ActionListener interface to handle enter key press for the chatting area.
	 * @author jihoo
	 */
	public class SendEnterListener implements ActionListener{
		@Override
		/**
		 * This method handles enter key event to add chat message in the chat area.
		 * @param ActionEvent e
		 */
		public synchronized void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//String chat = chatInput.getText();
			//chatArea.append("Player " + activePlayer + ": " + chat + "\n");
			//chatInput.setText("");
			CardGameMessage msg = new CardGameMessage(CardGameMessage.MSG, -1, chatInput.getText());
			game.getBigTwoClient().sendMessage(msg);
		}
	}
}
