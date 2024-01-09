import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * This class is used to model a Big Two game client that is responsible for establishing a connection and communicating with the Big Two game server.
 * @author jihoo
 */
public class BigTwoClient implements NetworkGame{
	
	private BigTwo game;
	private BigTwoGUI gui;
	private Socket sock;
	private ObjectOutputStream oos;
	private int playerID;
	private String playerName;
	private String serverIP;
	private int serverPort;
	
	/**
	 * Public constructor of BigTwoClient class
	 * @param game Big Two game object
	 * @param gui BigTwoGUI object
	 */
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		this.game = game;
		this.gui = gui;
	}
	
	@Override
	/**
	 *  a method for getting the playerID (i.e., index) of the local player
	 *  @return playerID
	 */
	public int getPlayerID() {
		return this.playerID;
	}

	@Override
	/**
	 *  method for setting the playerID (i.e., index) of the local player. This method should be called from the parseMessage() method when a message of the type PLAYER_LIST is received from the game server.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	@Override
	/**
	 * a method for getting the name of the local player.
	 * @return playerName
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	@Override
	/**
	 * a method for setting the name of the local player.
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	/**
	 * a method for getting the IP address of the game server.
	 * @return serverIP
	 */
	public String getServerIP() {
		return this.serverIP;
	}

	@Override
	/**
	 * a method for setting the IP address of the game server.
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	@Override
	/**
	 * a method for getting the TCP port of the game server.
	 * @return serverPort number
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	@Override
	/**
	 * a method for setting the TCP port of the game server.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	/**
	 * a method for making a socket connection with the game server.
	 */
	public synchronized void connect() {
		setServerIP("127.0.0.1");
		setServerPort(2396);
		
		String userName = JOptionPane.showInputDialog("Name: ");
		setPlayerName(userName);

		try {
			System.out.println("trying to connect");
			this.sock = new Socket(getServerIP(),getServerPort());
			this.oos = new ObjectOutputStream(sock.getOutputStream());
			Thread msgThread = new Thread(new ServerHandler());
			msgThread.start();
			System.out.println("connection established");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * a method for parsing the messages received from the game server.
	 * @param message GameMessage object
	 */
	public synchronized void parseMessage(GameMessage message) {
		CardGameMessage gameMsg;
		
		if (message.getType() == CardGameMessage.PLAYER_LIST) {
			setPlayerID(message.getPlayerID());
			//update the name of player

			String[] names = ((String[])message.getData());
			for(int i=0; i<4; i++) {
				game.getPlayerList().get(i).setName(names[i]);
			}
			
			sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName));
		}
		else if (message.getType() == CardGameMessage.JOIN) {
			if(this.playerID == message.getPlayerID()) {
				System.out.println(this.getPlayerID()+" joined\n");
				gameMsg = new CardGameMessage(CardGameMessage.READY, -1, null); //READY, playerID, null
				sendMessage(gameMsg);
			}
			else {
				gui.printMsg((String) message.getData()+" joined the game\n");
			}
			
			game.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			gui.repaint();
		}
		else if (message.getType() == CardGameMessage.FULL) {
			gui.printMsg("Server is FULL, cannot join the game\n");
		}
		else if (message.getType() == CardGameMessage.QUIT) {
			game.getPlayerList().get(message.getPlayerID()).setName("");
			gameMsg = new CardGameMessage(CardGameMessage.READY, -1, null);
			sendMessage(gameMsg);
			gui.repaint();
		}
		else if (message.getType() == CardGameMessage.READY) {
			String name = game.getPlayerList().get(message.getPlayerID()).getName();
			String msg = String.format("Player %s is READY\n", name);
			gui.printMsg(msg);
		}
		else if (message.getType() == CardGameMessage.START) {
			//gui.disable();
			game.start((BigTwoDeck)message.getData());
			gui.repaint();
		}
		else if (message.getType() == CardGameMessage.MOVE) {
			game.checkMove(message.getPlayerID(), (int[])message.getData());
			gui.repaint();
		}
		else if (message.getType() == CardGameMessage.MSG) {
			gui.printChat((String) message.getData());
		}
	}

	@Override
	/**
	 * a method for sending the specified message to the game server.
	 * @param message containing data
	 */
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
			oos.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//Inner classes
	/**
	 * This class is an inner class that implements the Runnable interface.
	 * @author jihoo
	 */
	public class ServerHandler implements Runnable{
		private ObjectInputStream oiStream;
		/**
		 * public constructor of ServerHandler class
		 */
		public ServerHandler() {
			try {
				oiStream = new ObjectInputStream(sock.getInputStream());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		@Override
		/**
		 * Method keep parsing the messages
		 */
		public void run() {
			CardGameMessage message;
			try {
				while ((message = (CardGameMessage) oiStream.readObject()) != null) {
					parseMessage(message);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	

}
