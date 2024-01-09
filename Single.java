/**
 * This hand consists of only one single card. The only card in a single is referred to as the top card of this single. 
 * A single with a higher rank beats a single with a lower rank. 
 * For singles with the same rank, the one with a higher suit beats the one with a lower suit.
 * @author jihoo
 */
public class Single extends Hand{
	private static final long serialVersionUID = 1L;

	/**
	 * public constructor for single hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * method for getting the single type of legal combination
	 * @return "Single" string value represents Single hand
	 */
	public String getType() {
		return "Single";
	}
	
	/**
	 * method for checking whether thee type is valid hand
	 * @return true if the hand is valid single, false if it is not
	 */
	public boolean isValid() {
		if (this.size() != 1) {
			return false;
		} else {
			return true; 
		}
	}
}
