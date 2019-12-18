package buildgame;

//===================
//    CARD CLASS
//===================
/**
 * Represents a playing card from a standard playing card deck.
 */
class Card
{
   /**
    * Represents the card suits in a playing card game (CLUBS, DIAMONDS, HEARTS,
    * SPADES).
    */
   public enum Suit
   {
      CLUBS, DIAMONDS, HEARTS, SPADES;
   }

   // Static variables
   /**
    * An array of card char values.
    */
   public static char[] valuRanks;

   // Non-static variables
   /**
    * The value of this Card.
    */
   private char value;
   /**
    * The suit of this Card.
    */
   private Suit suit;
   /**
    * The legality of this Card.
    */
   private boolean errorFlag;

   /**
    * A constructor that instantiates a Card to the Ace of Spades ('A' of
    * "SPADES") and an errorFlag to false.
    */
   Card()
   {
      this('A', Suit.valueOf("SPADES"));
   }

   /**
    * A constructor that instantiates a Card using the set method and an
    * errorFlag to false.
    */
   Card(char value, Suit suit)
   {
      valuRanks();
      set(value, suit);
   }

   /**
    * The string representation is "VALUE of SUIT". Where VALUE is the face
    * value of the card and SUIT is the suit name of the card.
    * 
    * @return "**Invalid Card**" or the value and suit of the card as "VALUE of
    *         SUIT". (i.e. "A of SPADES").
    */
   public String toString()
   {
      if (errorFlag == false)
         return "**Invalid Card**";
      return getValue() + " of " + getSuit();
   }

   /**
    * Sets the face value, suit, and legality of a Card. A return value of false
    * indicates a legal card.
    * 
    * @param value - The face value of a Card.
    * @param suit  - The suit of a Card.
    * @return The legality of a Card.
    */
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit;
         errorFlag = true;
         return errorFlag;
      }
      this.value = 'E';
      this.suit = suit;
      errorFlag = false;
      return errorFlag;
   }

   /**
    * Creates an array of Card char values. (A - K, plus X as the Joker)
    * 
    * @return A char array of Card values.
    */
   public static char[] valuRanks()
   {
      if (valuRanks == null)
      {
         valuRanks = new char[]
         { 'A', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K',
               'X' };
         return valuRanks;
      }
      return valuRanks;
   }
   
   public int getInt()
   {
      char[] valueRanks = valuRanks();
      for (int i = 0; i < valueRanks.length; i++)
      {
         if (this.value == valueRanks[i])
         {
            return i;
         }
      }
      return -1;
   }

   /**
    * Gets the Card value.
    * 
    * @return the value of this Card.
    */
   public char getValue()
   {
      return value;
   }

   /**
    * Gets the Card suit.
    * 
    * @return the suit of this Card.
    */
   public Suit getSuit()
   {
      return suit;
   }

   /**
    * Gets the Card's legality.
    * 
    * @return this Cards legality.
    */
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   /**
    * Determines if two Cards are identical. A return value of true indicates
    * two cards are identical.
    * 
    * @param card - The Card to compare.
    * @return true if two Cards are identical.
    */
   public boolean equals(Card card)
   {
      if (card != null && this.getClass() == card.getClass()
            && this.value == card.value && this.suit.equals(card.suit)
            && this.errorFlag == card.errorFlag)
      {
         return true;
      }
      return false;
   }

   /**
    * Determines if a Card has a legal face value & suit. A return value of true
    * indicates a Card has a legal face value and suit.
    * 
    * @param value - The value of a Card.
    * @param suit  - The suit of a Card.
    * @return The legality of a Card's face value and suit.
    */
   private boolean isValid(char value, Suit suit)
   {
      if (value == 'A' || value == '2' || value == '3' || value == '4'
            || value == '5' || value == '6' || value == '7' || value == '8'
            || value == '9' || value == 'T' || value == 'J' || value == 'Q'
            || value == 'K' || value == 'X')
      {
         return true;
      }
      return false;
   }

   /**
    * Will sort the incoming array of cards using a bubble sort routine.
    * 
    * @param arr       - A Card array.
    * @param arraySize - The size of the Card array.
    */
   public static void arraySort(Card[] arr, int arraySize)
   {
      for (int i = 0; i < arraySize - 1; i++)
      {
         for (int j = 0; j < arraySize - i - 1; j++)
         {
            int val1 = arr[j].getValue();
            int val2 = arr[j + 1].getValue();
            if (val1 > val2)
            {
               Card temp = arr[j];
               arr[j] = arr[j + 1];
               arr[j + 1] = temp;
            }
         }
      }
   }

   public static int getIntValue(char value2)
   {
      if(value2 > 90)
      {
         return (int) (value2 - 32);
      }
      return -1;
   }
}


