package buildgame;

//===================
//   DECK CLASS
//===================
/**
 * Represents the deck of cards in a playing card game.
 */
class Deck
{
   // Static variables
   private static final int NUMBER_OF_CARDS = 56; //includes 4 jokers
   /**
    * The maximum cards a deck can have in a game.
    * 6 packs of cards * 56 cards per deck.
    */
   public static final int MAX_CARDS = 6 * NUMBER_OF_CARDS;
   /**
    * A static starter deck to copy card objects from.
    */
   private static Card[] masterPack;
   
   // Non-static variables
   /**
    * The deck of cards to play with.
    */
   private Card[] cards;
   /**
    * The current card's index at the top of the deck. From 0 to (n-1).
    */
   private int topCard;
   
   /**
    * A constructor to create one deck of cards.
    */
   Deck()
   {
      this(1);
   }
   
   /**
    * Creates the master pack and initializes the playable deck.
    * 
    * @param numPacks - the number of standard decks. 
    */
   Deck(int numPacks)
   {
      allocateMasterPack();
      cards = new Card[numPacks * NUMBER_OF_CARDS];
      init(numPacks);
   }

   /**
    * Initializes the playable deck.
    * 
    * @param numPacks - the number of standard decks.
    */
   public void init(int numPacks)
   {
      if(numPacks > 6 || numPacks <= 0)
      {
         numPacks = 1;
      }
      topCard = -1;
      for(int packCounter = 0; packCounter < numPacks; packCounter++)
      {
         for(int cardCounter = 0; cardCounter < masterPack.length; cardCounter++)
         {
            Card card = masterPack[cardCounter];
            cards[cardCounter + packCounter * masterPack.length] = 
               new Card (card.getValue(), 
                  card.getSuit());
            topCard++;
         }
      } 
   }
   
   /**
    * Adds a card to the deck.
    * 
    * @param card - the card to be added to the deck.
    * @return if the card was successfully added to the deck. True for yes.
    */
   public boolean addCard(Card card)
   {
      for (int element = 0; element < cards.length; element++)
      {
         if(card.equals(cards[element]))
            return false;
      }
      cards[++topCard] = card;
      return true;
   }

   /**
    * Removes a card from the deck.
    * 
    * @param card - the card to be removed.
    * @return If the card was successfully removed. True for yes.
    */
   public boolean removeCard(Card card)
   {
      for (int element = 0; element <= topCard; element++)
      {
         if (card.equals(cards[element]))
         {
            cards[element] = cards[topCard];
            cards[topCard] = new Card('E', Card.Suit.SPADES);
            topCard--;
            return true;
         }
      }
      return false;
   }

   /**
    * Sorts the deck.
    */
   public void sort()
   {
      Card.arraySort(cards,cards.length);
   }

   /**
    * Gets the number of cards in the deck.
    * 
    * @return the number of cards in the deck.
    */
   public int getNumCards()
   {
      return topCard + 1;
   }
   
   /** 
    * Deals one card from the top of the deck.
    * 
    * @return Either a card from the deck or an error card.
    */
   public Card dealCard()
   {
      Card removedCard = new Card('E', Card.Suit.valueOf("SPADES"));
      if(topCard < 0) 
      {
         return removedCard;
      }
      Card dealtCard = new Card(
         cards[topCard].getValue(), cards[topCard].getSuit());
      cards[topCard] = removedCard;
      topCard--;
      return dealtCard;
   }
   
   /**
    * Shuffles the playable deck.
    */
   public void shuffle()
   {
      for(int counter = 0; counter <= topCard; counter++)
         {
            int randomInt = (int) (Math.random() * topCard); 
            Card tempCard = cards[randomInt];
            cards[randomInt] = cards[counter];
            cards[counter] = tempCard;
         }
   }
   
   /** 
    * Shows the kth card in the deck.
    * 
    * @param k - The value of card.
    * @return Either a card from the cards array or an error card.
    */
   public Card inspectCard(int k)
   {
      if(0 <= k && k <= topCard)
      {
         if(cards[k].getErrorFlag() == false)
            return cards[k];
      }
      Card errorCard = new Card('E', Card.Suit.valueOf("SPADES"));
      return errorCard;
   }
   
   /**
    * Gets the top card's position in the deck.
    * 
    * @return the top card's index in the deck.
    */
   public int getTopCard()
   {
      return topCard;
   }
   
   /**
    * Allocates the memory needed for one standard deck of cards.
    */
   private static void allocateMasterPack()
   {
      if (masterPack == null)
      {
         char [] valueArray = 
            {'A','2', '3', '4', '5', 
             '6', '7', '8', '9', 'T', 
             'J', 'Q', 'K'};
         masterPack = new Card[52];
         int masterPackCounter = 0;
         for (int currentSuit = 0; currentSuit < Card.Suit.values().length; 
            currentSuit++)
         {
            Card.Suit enumSuits = Card.Suit.values()[currentSuit];
            for (int valueIndex = 0; valueIndex < valueArray.length; 
               valueIndex++)
            {
               masterPack[masterPackCounter++] = 
                  new Card(valueArray[valueIndex],enumSuits);
            }
         }
      }
   }
}


