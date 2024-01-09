/**
 * This hand consists of five cards with consecutive ranks and the same suit. 
 * For the sake of simplicity, 2 and A can only form a straight flush with K but not with 3.
 * @author jihoo
 */
public class StraightFlush extends Hand{
	private static final long serialVersionUID = 1L;
	
	/**
	 * a public constructor for StraightFlush hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for getting the quad type of legal combination
	 * @return "StraightFlush" String object represents StraightFlush hand
	 */
	public String getType() {
		return "StraightFlush";
	}
	
	/**
	 * a method for checking whether the type is valid StraightFlush.
	 * @return true if the hand is StraightFlush, return false if it is not.
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
			
			boolean isStraight = true;
			int startRank = this.getCard(0).getRank();
			
			for(int i=0; i<5; i++) {
				if(startRank != this.getCard(i).getRank()) {
					isStraight = false;
					return isStraight;
				}
				startRank ++;
			}
			
			return isFlush==true && isStraight==true;
		}
	}
	
	/**
	 * a method for checking if this hand beats Straight, Flush,FullHouse or Quad
	 * @param hand Hand object, specifically StraightFlush hand 
	 * @return boolean true if player's hand beats the latest hand, false if not.
	 */
	public boolean beats(Hand hand) {
		if(this.getType() != hand.getType()) {
			if(hand.getType()=="Straight" || hand.getType()=="Flush" || hand.getType()=="FullHouse" || hand.getType()=="Quad") {
				return true;
			} else {
				return false;
			}
		} 
		else {//both straightflush
			if(this.getTopCard().getRank() != hand.getTopCard().getRank()){
				return this.getTopCard().getRank() > hand.getTopCard().getRank();
			}
			else {
				return this.getTopCard().getSuit() > hand.getTopCard().getSuit();
			}
		}
	}
}
