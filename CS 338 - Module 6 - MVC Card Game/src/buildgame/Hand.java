package buildgame;

//===================
//    HAND CLASS
//===================
/**
 * Represents a hand of cards in a playing card game.
 */
class Hand
{
   // Static variables
   /**
    * The max number of cards a hand can hold.
    */
   public static final int MAX_CARDS = 100;

   // Non-static variables
   /**
    * The container for the cards in this playing hand.
    */
   private Card[] myCards;
   /**
    * The number of cards in this playing hand. From 0 to n.
    */
   private int numCards;

   /**
    * Creates an array of Card objects representing this playing hand.
    */
   Hand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   /**
    * Empties this playing hand. Creating an array of new references to null
    * card objects.
    */
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   /**
    * Takes a card from the playable deck.
    * 
    * @param card - The card to be placed in the hand.
    * @return true if card exists and can be placed in the hand. Otherwise
    *         false.
    */
   public boolean takeCard(Card card)
   {
      if (card != null && numCards < MAX_CARDS)
      {
         myCards[numCards++] = new Card(card.getValue(), card.getSuit());
         return true;
      }
      return false;
   }

   /**
    * Plays a card from the hand.
    * 
    * @return Either the card to play, or an error card.
    */
   public Card playCard()
   {
      Card removedCard = new Card('E', Card.Suit.valueOf("SPADES"));
      if (numCards <= 0)
      {
         return removedCard;
      }
      Card playedCard = new Card(myCards[numCards - 1].getValue(),
            myCards[numCards - 1].getSuit());
      myCards[numCards - 1] = removedCard;
      numCards--;
      return playedCard;
   }

   /**
    * Plays a card from a given index in the hand.
    * 
    * @return Either the card to be played, or an error card.
    */
   public Card playCard(int cardIndex)
   {
      if (numCards == 0) // error
      {
         // Creates a card that does not work
         return new Card('E', Card.Suit.SPADES);
      }
      // Save the current card
      Card card = myCards[cardIndex];
      // Decreases numCards.
      numCards--;
      for (int i = cardIndex; i < numCards; i++)
      {
         myCards[i] = myCards[i + 1];
      }

      myCards[numCards] = null;

      return card;
   }
   
   /**
    * Sorts the playing hand array named myCards.
    */
   public void sort()
   {
      Card.arraySort(myCards, myCards.length);
   }

   /**
    * Gets the number of cards in this hand. From 0 to n cards.
    * 
    * @return the number of cards in this hand.
    */
   public int getNumCards()
   {
      return numCards;
   }

   /**
    * Shows the kth card in a hand.
    * 
    * @param k - The value of card.
    * @return Either a card from the myCards array or an error card.
    */
   public Card inspectCard(int k)
   {
      if (0 <= k && k < numCards && myCards[k] != null && 
            myCards[k].getErrorFlag() == true)
      {
            return myCards[k];
      }
      return new Card('E', Card.Suit.valueOf("SPADES"));
   }

   /**
    * Creates a string from the Hand.
    */
   public String toString()
   {
      StringBuilder sBuilder = new StringBuilder();
      sBuilder.append("Hand: ");
      sBuilder.append("\n");
      if (numCards == 0)
      {
         sBuilder.append("Out of Cards");
         return sBuilder.toString();
      }
      for (int i = 0; i < numCards; i++)
      {
         if (myCards[i] != null)
         {
            if (i != numCards - 1)
            {
               if ((i + 1) % 10 == 0 && i != 0)
               {
                  sBuilder.append(myCards[i].toString());
                  sBuilder.append("\n");
               } else
               {
                  {
                     sBuilder.append(myCards[i].toString());
                     sBuilder.append(", ");
                  }
               }

            } else
            {
               sBuilder.append(myCards[i].toString());
            }
         }
      }
      return sBuilder.toString();
   }
}


