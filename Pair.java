/**
 * This class is for pair hand, This hand consists of two cards with the same rank. The card with a higher suit in a pair is referred to as the top card of this pair. 
 * A pair with a higher rank beats a pair with a lower rank. 
 * For pairs with the same rank, the one containing the highest suit beats the other.
 * @author jihoo
 */
public class Pair extends Hand{
	private static final long serialVersionUID = 1L;

	/**
	 * public constructor for pair hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * method for getting the pair type of legal combination
	 * @return "Pair" String object which represents Pair hand
	 */
	public String getType() {
		return "Pair";
	}
	
	/**
	 * method for checking whether the hand is valid
	 * @return true if the hand is valid pair, false if it is not
	 */
	public boolean isValid() {
		if (this.size() != 2) {
			return false;
		} else {
			Card card0 = this.getCard(0);
			Card card1 = this.getCard(1);
			return (card0.getRank() == card1.getRank());
  		}
	}
}

