import java.util.ArrayList;
import java.util.List;
/**
 * This class is a subclass of the Card class and is used to model a card used in a Big Two card game.
 * @author jihoo
 */
public class BigTwoCard extends Card{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for building a card with the specified suit and rank.
	 * @param suit integer value 0-3 which represents suit of the card, {Diamond, Club, Heart, Spade} respectively
	 * @param rank integer value 0-12 wihch represents rank of the card
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * Method for comparing the order of this card with the specified card.
	 * @param card Card object which represents the card.
	 * @return Returns a negative integer, zero, or a positive integer when this card is less than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card) {
		ArrayList<Integer> bigTwoRank = new ArrayList<Integer>(List.of(2,3,4,5,6,7,8,9,10,11,12,0,1));//low to high
		
		if(bigTwoRank.indexOf(this.rank) > bigTwoRank.indexOf(card.getRank())) {
			return 1;
		}
		else if(bigTwoRank.indexOf(this.rank) < bigTwoRank.indexOf(card.getRank())) {
			return -1;
		}
		if(this.suit > card.getSuit()) {
			return 1;
		}
		else if(this.suit < card.getSuit()){
			return -1;
		}
		else {
			return 0;
		}
	}
}
