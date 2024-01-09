/**
 * This hand consists of three cards with the same rank. The card with the highest suit in a triple is referred to as the top card of this triple. 
 * A triple with a higher rank beats a triple with a lower rank.
 * @author jihoo
 */
public class Triple extends Hand{
	private static final long serialVersionUID = 1L;

	/**
	 * public constructor for triple hand
	 * @param player CardGamePlayer object which stores the information of player
	 * @param cards CardList object which the player trying to compose as a hand
	 */
	public Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * method for getting the single type of legal combination
	 * @return "Triple" string object which represents Triple hand
	 */
	public String getType() {
		return "Triple";
	}
	
	/**
	 * method for checking whether the type is valid hand Triple
	 * @return boolean true if the hand is valid Triple, false if it is not.
	 */
	public boolean isValid() {
		if (this.size() != 3) {
			return false;
		} else {
			Card card0 = this.getCard(0);
			Card card1 = this.getCard(1);
			Card card2 = this.getCard(2);
			
			return (card0.getRank()==card1.getRank() && card1.getRank() == card2.getRank());
		}
	}
}
