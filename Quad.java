/**
 * This hand consists of five cards, with four having the same rank. The card in the quadruplet with the highest suit in a quad is referred to as the top card of this quad.
 *  A quad always beats any straights, flushes, and full houses. A quad having a top card with a higher rank beats a quad having a top card with a lower rank.
 *  @author jihoo
 */
public class Quad extends Hand{
	private static final long serialVersionUID = 1L;
	
	/**
	 * a public constructor for Quad hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for getting the quad type of legal combination
	 * @return "Quad" String object which represents Quad hand
	 */
	public String getType() {
		return "Quad";
	}
	
	/**
	 * a method for checking whether the type is valid Quad.
	 * @return true if the hand is Quad, return false if it is not.
	 */
	public boolean isValid() {
		if(this.size() != 5) {
			return false;
		} else {
			int rank0 = this.getCard(0).getRank();
			int rank1 = this.getCard(1).getRank();
			
			if(this.getCard(0).getRank() == this.getCard(1).getRank()) {
				// if quad hand looks like @@@@#
				for(int i=0; i<4; i++) {
					if(rank0 != this.getCard(i).getRank()) {
						return false;
					}
				}
			}
			else {
				//if quad hand looks like #@@@@
				for(int i=1; i<5; i++) {
					if(rank1 != this.getCard(i).getRank()) {
						return false;
					}
				}
			}
			return true;	
		}
	}
	
	/**
	 * Method of getting the top card of Quad hand. The card in the quadruplet with the highest suit in a quad is referred to as the top card of this quad.
	 * @return topCard top card of the Quad hand.
	 */
	public Card getTopCard() {
		Card topCard;
		if(this.getCard(0).getRank() == this.getCard(1).getRank()) { //for example, 3 3 3 3 8 => 3 in index 3 is the topcard
			topCard = this.getCard(3);
		}
		else {//else if hand looks like 8 3 3 3 3 => 3 in index 4 is the top card
			topCard = this.getCard(4);		
		}
		return topCard;
	}
	
	/**
	 * a method for checking if this hand beats Straight, Flush or FullHouse
	 * @param hand Hand object, specifically Quad hand 
	 * @return boolean true if player's hand beats the latest hand, false if not.
	 */
	public boolean beats(Hand hand) {
		if(this.getType() != hand.getType()) {
			if(hand.getType()=="Straight" || hand.getType()=="Flush" || hand.getType()=="FullHouse") {
				return true;
			} else {
				return false;
			}
		} 
		else {//both Quad
			return(this.getTopCard().getRank() > hand.getTopCard().getRank());
		}
	}
}
