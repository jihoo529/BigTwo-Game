/**
 * This hand consists of five cards with the same suit. The card with the highest rank in a flush is referred to as the top card of this flush. A flush always beats any straights.
 * A flush with a higher suit beats a flush with a lower suit. For flushes with the same suit, the one having a top card with a higher rank beats the one having a top card with a lower rank.
 * @author jihoo
 */
public class Flush extends Hand{
	private static final long serialVersionUID = 1L;

	/**
	 * a public constructor for flush hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for getting the flush type of legal combination
	 * @return "Flush" String object represents Flush hand
	 */
	public String getType() {
		return "Flush";
	}
	
	/**
	 * a method for checking whether the type is valid Flush
	 * @return isFlush boolean value, true if the hand is Flush, false if the hand is not Flush
	 */
	public boolean isValid() {
		if(this.size() != 5) {
			return false;
		} else {
			int suit0 = this.getCard(0).getSuit();
			boolean isFlush = true;
			
			for(int i=0; i<5; i++) {
				if(suit0 != this.getCard(i).getSuit()) {
					isFlush = false;
					return isFlush;
				}
			}
			return isFlush;
		}
	}
	
	/**
	 * a method for checking if this hand beats Straight
	 * @param hand Hand object, specifically Flush hand 
	 * @return boolean true if player's hand beats the latest hand, false if not.
	 */
	public boolean beats(Hand hand) {
		if(this.getType() != hand.getType()) {
			if(hand.getType()=="Straight") {
				return true;
			} else {
				return false;
			}
		} 
		else {//both flush
			if(this.getTopCard().getSuit() == hand.getTopCard().getSuit()) {//both same suit
				return this.getTopCard().getRank() > hand.getTopCard().getRank(); //return true if this's rank is higher, return false if hand's rank is higher
			} 
			else {
				return this.getTopCard().getSuit() > hand.getTopCard().getRank();
			}
		}
	}
}

