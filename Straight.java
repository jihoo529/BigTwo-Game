/**
 * This hand consists of five cards with consecutive ranks. For the sake of simplicity, 2 and A can only form a straight with K but not with 3. 
 * The card with the highest rank in a straight is referred to as the top card of this straight. A straight having a top card with a higher rank beats a straight having a top card with a lower rank. 
 * For straights having top cards with the same rank, the one having a top card with a higher suit beats the one having a top card with a lower suit.
 * @author jihoo
 */
public class Straight extends Hand{
	private static final long serialVersionUID = 1L;

	/**
	 * public constructor for straight hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * method for getting the single type of legal combination
	 * @return "Straight" string value represents Straight hand
	 */
	public String getType() {
		return "Straight";
	}
	
	/**
	 * a method for checking whether the type is valid Straight.
	 * @return true if the hand is Straight, return false if it is not.
	 */
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		} else {
			boolean isStraight = true;
			int startRank = this.getCard(0).getRank();
			
			for(int i=0; i<5; i++) {
				if(startRank != this.getCard(i).getRank()) {
					isStraight = false;
					return isStraight;
				}
				startRank ++;
			}
			
			return isStraight;
		}
	}
	
	/**
	 * a method for checking if this hand beats any other straight.
	 * @param hand Hand object, specifically Quad hand 
	 * @return boolean true if player's hand beats the latest hand, false if not.
	 */
	public boolean beats(Hand hand) {
		if(this.getType() != hand.getType()) {
			return false;
		} else {//both Straight
			if(this.getTopCard().getRank() != hand.getTopCard().getRank()){//if top card's rank is different
				return this.getTopCard().getRank() > hand.getTopCard().getRank();//top card with higher rank wins
			}
			else {//if top card's rank is same
				return this.getTopCard().getSuit() > hand.getTopCard().getSuit();//top card with higher suit wins
			}
		}
	}
}
