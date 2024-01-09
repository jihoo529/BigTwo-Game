/**
 * This class is a subclass of the CardList class and is used to model a hand of cards.
 * @author jihoo
 */
public abstract class Hand extends CardList{
	private static final long serialVersionUID = 1L;
	private CardGamePlayer player;
	
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public Hand(CardGamePlayer player, CardList cards){
		this.player = player;
		for(int i=0; i<cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
		this.sort();
	}
	
	/**
	 * Getter function for getting the player object
	 * @return player CardGamePlayer object about the player information
	 */
	public CardGamePlayer getPlayer() {
		return player;
		
	}
	
	/**
	 * Getting the topcard of hand, except for some combinations, top card is the last element of the card array
	 * @return topCard the top card of the hand
	 */
	public Card getTopCard() {
		this.sort();
		Card topCard = this.getCard(this.size() - 1);
		return topCard;
	}
	
	/**
	 *Method for checking if this hand beats a specified hand
	 * @param hand specified hand which should be compared with the player's hand
	 * @return true if player's hand beats specified hand, false if not.
	 */
	public boolean beats(Hand hand) {
		if (hand == null || !this.isValid() || !hand.isValid() || this.getType() != hand.getType()) {
			return false;
		}
		
		return (this.getTopCard().compareTo(hand.getTopCard()) > 0); //if this card is higher than opponent's card, return true
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * @return the type of the hand in String
	 */
	public abstract String getType();
	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return true if the hand is valid, false if it is not.
	 */
	public abstract boolean isValid();
}
