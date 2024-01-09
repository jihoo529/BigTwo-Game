/**
 * This class is a subclass of the Deck class and is used to model a deck of cards used in a Big Two card game.
 * @author jihoo
 */
public class BigTwoDeck extends Deck{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Method for initializing a deck of Big Two cards. It should remove all cards from the deck, create 52 Big Two cards and add them to the deck.
	 */
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard bigTwoCard = new BigTwoCard(i, j);
				addCard(bigTwoCard);
			}
		}	
	}
}
