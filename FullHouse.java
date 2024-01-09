/**
 * This hand consists of five cards, with two having the same rank and three having another same rank. 
 * The card in the triplet with the highest suit in a full house is referred to as the top card of this full house. 
 * A full house always beats any straights and flushes. 
 * A full house having a top card with a higher rank beats a full house having a top card with a lower rank.
 * @author jihoo
 */
public class FullHouse extends Hand{
	private static final long serialVersionUID = 1L;
	
	/**
	 * a public constructor for FullHouse hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * a method for getting the fullhouse type of legal combination
	 * @return "FullHosue" String object represents FullHouse hand
	 */
	public String getType() {
		return "FullHouse";
	}
	
	/**
	 * a method for checking whether the type is valid FullHouse.
	 * @return true if the hand is FullHouse, return false if it is not.
	 */
	public boolean isValid() {
		if(this.size() != 5) {
			return false;
		} else {
			int rank0 = this.getCard(0).getRank();
			int rank1 = this.getCard(1).getRank();
			int rank2 = this.getCard(2).getRank();
			int rank3 = this.getCard(3).getRank();
			int rank4 = this.getCard(4).getRank();
			
			if(rank0==rank1 && rank1==rank2 && rank3 == rank4 || rank0==rank1 && rank2==rank3 && rank3==rank4) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * Method of getting the top card of FullHouse hand. The card in the triplet with the highest suit in a full house is referred to as the top card of this full house. 
	 * @return topCard top card of the FullHouse hand.
	 */
	public Card getTopCard() {
		Card topCard;
		if(this.getCard(0).getRank() == this.getCard(2).getRank()) {
			topCard = this.getCard(2);
		}
		else {
			topCard = this.getCard(4);		
		}
		return topCard;
	}
	
	/**
	 * a method for checking if this hand beats Straight or Flush
	 * @param hand Hand object, specifically FullHouse hand 
	 * @return boolean true if player's hand beats the latest hand, false if not.
	 */
	public boolean beats(Hand hand) {
		if(this.getType() != hand.getType()) {
			if(hand.getType()=="Straight" || hand.getType()=="Flush") {
				return true;
			} else {
				return false;
			}
		} 
		else {//both fullhouse
			return(this.getTopCard().getRank() > hand.getTopCard().getRank());
		}
	}
}
