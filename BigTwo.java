import java.util.ArrayList;

/**
 * This class implements the CardGame interface and is used to model Big Two card game.
 * @author jihoo
 */
public class BigTwo implements CardGame{
	private int numOfPlayers;
	private int currentPlayerIdx;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private BigTwoGUI ui;
	private BigTwoClient client;
	
	/**
	 * Constructor for creating Big Two card game.
	 */
	public BigTwo() {
		numOfPlayers = 4;
		playerList = new ArrayList<CardGamePlayer>();
		for(int i=0; i<numOfPlayers; i++) {
			CardGamePlayer player = new CardGamePlayer();
			playerList.add(player);
			playerList.get(i).setName(null);
		}
		handsOnTable = new ArrayList<Hand>();
		ui = new BigTwoGUI(this);
		client = new BigTwoClient(this, ui);
		client.connect();
	}
	
	/**
	 * 
	 * @return Big Two Client
	 */
	public BigTwoClient getBigTwoClient() {
		return client;
	}
	
	
	/**
	 * Getter function for retrieving the number of players
	 * @return number of players
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	
	/**
	 * Getter function for retrieving the deck of cards being used
	 * @return deck Deck object which represents deck of cards
	 */
	public Deck getDeck() {
		return deck;
	}
	
	/**
	 * Getter function for retrieving the list of players
	 * @return ArrayList of CardGamePlayer
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}
	
	/**
	 * Getter function for retrieving the list of hands played on the table
	 * @return ArrayList of hands
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}
	
	/**
	 * Getter function for retrieving the index of the current player
	 * @return index of the current player
	 */
	public int getCurrentPlayerIdx() {
		return currentPlayerIdx;
	}
	
	/**
	 * Method for starting the game with given shuffled deck of cards
	 * @param deck Deck of card
	 */
	public synchronized void start(Deck deck) {
		ui.reset();
		ui.disable();
		//remove all the cards from the players as well as from the table
		for(int i=0; i<4; i++) {
			playerList.get(i).removeAllCards();
		}
		handsOnTable.clear();
		
		//distribute the cards to the players
		for(int i=0; i<4; i++) {
			for(int j=0; j<13; j++) {
				playerList.get(i).addCard(deck.getCard(i*13+j));
			}
		}
		
		for(int i=0; i<4; i++) {
			playerList.get(i).sortCardsInHand();
		}
		
		//identify the player who holds the Three of Diamonds
		CardGamePlayer diamondThreePlayer = new CardGamePlayer();
		BigTwoCard threeOfDiamonds = new BigTwoCard(0,2); 
		
		for(int i=0; i<4; i++) {
			if(playerList.get(i).getCardsInHand().contains(threeOfDiamonds)) {
				diamondThreePlayer = playerList.get(i);
			}
		}
		//set both the currentPlayerIdx of the BigTwo object and the activePlayer of the BigTwoUI object to the index of the player who holds the Three of Diamonds;
		currentPlayerIdx = playerList.indexOf(diamondThreePlayer);
		ui.setActivePlayer(currentPlayerIdx);
		ui.repaint();
		//client.connect();
	}
	
	/**
	 * Method for printing Not a legal move
	 */
	public void illegalMove() {
		String msg = "Not a legal move !!\n";
		ui.printMsg(msg);
		return;
	}
	
	/**
	 * Method for making a move by a player with the specified index using the cards specified by the list of indices.
	 * @param playerIdx index number of the current player
	 * @param cardIdx array of index number(s) of card(s) that current player selected
	 */
	public synchronized void makeMove(int playerIdx, int[] cardIdx) {
		client.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
		//ui.enable();
		//checkMove(playerIdx, cardIdx);
	}
	
	/**
	 * Method for checking a move made by a player. This method should be called from the makeMove() method.
	 * @param playerIdx index number of the current player
	 * @param cardIdx array of index number(s) of card(s) that current player selected
	 */
	public synchronized void checkMove(int playerIdx, int[] cardIdx) {
		CardGamePlayer currPlayer = playerList.get(playerIdx);
		CardList playerCard = new CardList();
		Card diaThree = new Card(0, 2);
		
		//if the current player is beginner of the game
		if(getHandsOnTable().isEmpty()) {
			//Beginning player cannot PASS his/her turn
			if(cardIdx == null){ 
				illegalMove();
				return;
			}
			else {
				//get cards which current player chose from cardIdx and store in playerCard
				for(int i : cardIdx) {
					playerCard.addCard(currPlayer.getCardsInHand().getCard(i));
				}
			}
			
			//beginning player must choose a hand containing diamond three
			if(playerCard.contains(diaThree)) {
				Hand playerHand = composeHand(currPlayer, playerCard);
				//beginning player can't take empty hand i.e., cannot pass
				if(playerHand != null) {
					String msg = String.format("{%s} %s\n",playerHand.getType(), playerHand);
					ui.printMsg(msg);
										
					handsOnTable.add(playerHand);
					currPlayer.removeCards(playerCard);
					
					currentPlayerIdx = (currentPlayerIdx + 1) % 4;
					ui.setActivePlayer(currentPlayerIdx);
					return;

				} else {
					//if beginning player tries to pass
					illegalMove();
					return;
				}
			} else {
				//if beginning player does not have diamond three card
				illegalMove();
				return;
			}
		}
		else { 
			//current player is not the begginer of game..
			Hand prevHand = getHandsOnTable().get(handsOnTable.size() - 1); //get handsontable
		
			boolean isLastHand = prevHand.getPlayer().getName().contains(playerList.get(currentPlayerIdx).getName());
			
			//current player wants to PASS
			if(cardIdx == null) {
				//current player can only PASS if he/she is not the last hand played
				if(!isLastHand) {
					String msg = String.format("{PASS}\n");
					ui.printMsg(msg);
					
					currentPlayerIdx = (currentPlayerIdx + 1) % 4;
					ui.setActivePlayer(currentPlayerIdx);
					return;
				} else {
					//if current player is the last hand played, cannot PASS
					illegalMove();
					return;
				}
			}
			else {
				//store chosen hand into playerCard
				for(int i : cardIdx) {
					playerCard.addCard(currPlayer.getCardsInHand().getCard(i));
				}
			}
		
			Hand playerHand = composeHand(currPlayer, playerCard);
			
			//if the player is the last hand played (player can have any kinds of hands), or player hand is valid and beats the previous hand
			if((isLastHand && playerHand!=null) || (playerHand!=null && playerHand.beats(prevHand))) {
				String msg = String.format("{%s} %s\n",playerHand.getType(), playerHand);
				ui.printMsg(msg);
				
				handsOnTable.add(playerHand);
				
				//remove card's which player used
				currPlayer.removeCards(playerCard);
				
				//check whether game ended
				if(endOfGame() == true) {
					msg = "Game ends.\n";
					ui.printMsg(msg);
					for(CardGamePlayer player : playerList) {
						if(player.getCardsInHand().size() == 0) {
							msg = String.format("%s wins the game.\n", player.getName());
							ui.printMsg(msg);	
						}
						else {		
							msg = String.format("%s has %d cards in hand.\n", player.getName(), player.getNumOfCards());
							ui.printMsg(msg);
						}
					}
                    client.sendMessage(new CardGameMessage(CardGameMessage.QUIT, -1, null));
					return;
				}
				
				currentPlayerIdx = (currentPlayerIdx + 1) % 4;
				ui.setActivePlayer(currentPlayerIdx);
				return;
			}
			//if selected player's hand is not valid or cannot beat hand on table.
			else if(playerHand==null || !playerHand.beats(prevHand)){
				illegalMove();
				return;
			}
		}
	}
	
	/**
	 * Method for checking if the game ends.
	 * @return boolean true if player's hand is empty false if it is not
	 */
	public synchronized boolean endOfGame() {
		for (CardGamePlayer player : playerList) {
			if (player.getCardsInHand().isEmpty()) {
				ui.disable();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method for starting a Big Two card game.
	 * @param args String array
	 */
	public static void main(String[] args) {
		//create Big Two card game
		BigTwo game = new BigTwo();
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player. 
	 * @param player CardGamePlayer object about player information
	 * @param cards CardList object about the hand of cards that the player trying to compose
	 * @return hand if player's hand is valid, return null if it is invalid.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand hand;
		if (cards.size() == 1) {
			hand = new Single(player, cards);
			if(hand.isValid()) {
				return hand;
			}
		}
		else if (cards.size() == 2) {
			hand = new Pair(player, cards);
			if(hand.isValid()) {
				return hand;
			}
		}
		else if (cards.size() == 3) {
			hand = new Triple(player, cards);
			if(hand.isValid()) {
				return hand;
			}
		}
		else if (cards.size() == 5){
			hand = new StraightFlush(player, cards);
			
			if (hand.isValid()) {
				return hand;
			}
			hand = new Quad(player, cards);
			
			if (hand.isValid()) {
				return hand;
			}
			hand = new FullHouse(player, cards);
			
			if (hand.isValid()) {
				return hand;
			}
			hand = new Flush(player, cards);
			
			if (hand.isValid()) {
				return hand;
			}
			hand = new Straight(player, cards);
			
			if (hand.isValid()) {
				return hand;
			}
		}
		return null;
	}
}