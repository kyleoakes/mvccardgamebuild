package buildgame;

import java.util.HashMap;

import javax.swing.*;
import buildgame.Card.Suit;

public class Model
{
   // static variables
   static int MAX_CARDS_PER_HAND = 56;
   private static final int MAX_PLAYERS = 2;
   
   private int numPlayers;
   private int numPacks; // # standard 52-card packs per deck
                         // ignoring jokers or unused cards
   private int numJokersPerPack; // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack; // # cards removed from each pack
   private int numCardsPerHand; // # cards to deal each player
   private int numStacks = 3; // number of playing area stacks. 1 to n.

   private Deck deck; // holds the initial full deck and gets
                      // smaller (usually) during play
   private Hand[] hands; // one Hand for each player
   private Card[] unusedCardsPerPack; // an array holding the cards not used
                                      // in the game. e.g. pinochle does not
                                      // use cards 2-8 of any suit
   private Card[] stacks;
   
   public static int COMPUTER;
   public static int PLAYER;
   
   public int firstPlayer;
   
   private HashMap<Character, Integer> valueMap;
   private boolean playerCanPlay;
   private boolean computerCanPlay;
   private int playerCantPlay;
   private int computerCantPlay;
   private int selectedHumanCardIndex;
   private int selectedComputerCardIndex;
   
   public Model()
   {
      // deal initially so that each hand has cards at the start of the game -- are hands set up?
      cardGameFramework(1, 2, 0, new Card[0], 2, 7);
      generateValueMap();
      deal();
//      printHands();
   }
   
   public int getNumStacks()
   {
      return numStacks;
   }
   
   public int getFirstPlayer()
   {
      return firstPlayer;
   }

   public void setFirstPlayer(int playerIndex)
   {
      this.firstPlayer = playerIndex;
   }

   public boolean getPlayerCanPlay()
   {
      return playerCanPlay;
   }

   public boolean getComputerCanPlay()
   {
      return computerCanPlay;
   }


   public void setPlayerCanPlay(boolean playerCanPlay)
   {
      this.playerCanPlay = playerCanPlay;
   }



   private int generateRand(int min, int max)
   {
      return ((int)Math.floor( Math.random() * max ) + min);
   }
   
   private void randFirstPlayer()
   {
      COMPUTER = generateRand(0, 1);
      PLAYER = (COMPUTER == 0 ? 1 : 0);
   }
   
   public Card getStack(int index)
   {
      if (index >= 0 && index < numStacks) 
      {
         return stacks[index];
      }
      else
      {
         return new Card('E', Suit.SPADES);
      }
   }
   
   public int getSelectedCardIndex(int playerIndex)
   {
      if (playerIndex == PLAYER)
      {
         return getSelectedHumanCardIndex();
      }
      else
      {
         return getSelectedComputerCardIndex();
      }
   }
   
   public boolean setSelectedCardIndex(int playerIndex, int cardIndex)
   {
      return ( (playerIndex == PLAYER)
            ? setSelectedHumanCardIndex(cardIndex)
            : setSelectedComputerCardIndex(cardIndex)
      );
   }
   
   public int getSelectedHumanCardIndex()
   {
      return selectedHumanCardIndex;
   }

   public boolean setSelectedHumanCardIndex(int index)
   {
      if (index >= 0 && index < hands[PLAYER].getNumCards())
      {
         this.selectedHumanCardIndex = index;
         return true;
      }
      else
      {
         return false;
      }
   }

   public int getSelectedComputerCardIndex()
   {
      return selectedComputerCardIndex;
   }
   
   public boolean setSelectedComputerCardIndex(int index)
   {
      if (index >= 0 && index < hands[COMPUTER].getNumCards())
      {
         this.selectedComputerCardIndex = index;
         return true;
      }
      else
      {
         return false;
      }
   }

   public Icon getGuiIcon(int player,int index)
   {
       return GUICard.getIcon(getHand(player).inspectCard(index));
   }
   
   public Icon getGuiIconBack(int player,int index)
   {
      return GUICard.getBackCardIcon();
   }
   
   public Icon getGuiIconBack()
   {
      return GUICard.getBackCardIcon();
   }
   
   // Generates a global hashmap.
   private boolean generateValueMap()
   {
      if(valueMap == null)
      {
         valueMap = new HashMap<Character, Integer>();
         valueMap.put('A', 0);
         valueMap.put('2', 1);
         valueMap.put('3', 2);
         valueMap.put('4', 3);
         valueMap.put('5', 4);
         valueMap.put('6', 5);
         valueMap.put('7', 6);
         valueMap.put('8', 7);
         valueMap.put('9', 8);
         valueMap.put('T', 9);
         valueMap.put('J', 10);
         valueMap.put('Q', 11);
         valueMap.put('K', 12);
         valueMap.put('X', 13);
      
         // valueMap was created.
         return true;
      }
      
      // fall through -- valueMap already exists, no need to create again
      return false;
   }
   
   // Compare to cards to see if the selected card can be place on to the stack choosen.
   boolean compareCards(Card selectedCard, Card stackCard)
   {
      int selectedCardIndex = valueMap.get(selectedCard.getValue());
      int stackCardIndex = valueMap.get(stackCard.getValue());
      
      int valueDistance = selectedCardIndex - stackCardIndex;
      
      if((valueDistance == -1) || (valueDistance == 1) 
            || (valueDistance == -13) || valueDistance == 13)
      {
         // stack can be played on.
         return true;
      }
      
      // fail case -- stack can not be played on.
      return false;
   }
   
   public void cardGameFramework(int numPacks, int numJokersPerPack,
         int numUnusedCardsPerPack, Card[] unusedCardsPerPack, int numPlayers,
         int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) // > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if (numCardsPerHand < 1 || numCardsPerHand > numPacks
            * (52 - numUnusedCardsPerPack) / numPlayers)
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      randFirstPlayer();
      
      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hands = new Hand[numPlayers];
      this.stacks = new Card[numStacks];
      for (k = 0; k < numPlayers; k++)
         this.hands[k] = new Hand();
      for (k = 0; k < numStacks; k++)
         this.stacks[k] = new Card();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
      
      this.playerCanPlay = true;
      this.computerCanPlay = true;
      this.playerCantPlay = 0;
      this.computerCantPlay = 0;


      // prepare deck and shuffle
      newGame();
   }

   // overload/default for game like bridge
   public void cardGameFramework()
   {
      cardGameFramework(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hands[k];
   }

   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hands[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard(unusedCardsPerPack[k]);

      // add jokers
      for (k = 0; k < numPacks; k++)
         for (j = 0; j < numJokersPerPack; j++)
            deck.addCard(new Card('X', Card.Suit.values()[j]));

      // shuffle the cards
      deck.shuffle();
      
      // Deal a card to each stack
      for (k = 0; k < numStacks; k++)
      {
         stacks[k] = deck.dealCard();
      }
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hands[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hands[j].takeCard(deck.dealCard());
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hands[k].sort();
   }

   Card playCard(int playerIndex, int cardIndex)
   {
      // returns bad card if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1 || cardIndex < 0
            || cardIndex > numCardsPerHand - 1)
      {
         // Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);
      }

      // return the card played
      return hands[playerIndex].playCard(cardIndex);
   }
   
   // moves a card into the stacks array index given.
   public boolean playOnStack(Card selectedCard, int stackIndex)
   {
      if((stackIndex > 0) || (stackIndex < numStacks))
      {
         System.out.println("Playing " + selectedCard + " on " + stacks[stackIndex]);
         Card copyCard = new Card(selectedCard.getValue(), selectedCard.getSuit());
         this.stacks[stackIndex] = copyCard;
         
         // card added to stack
         return true;
      }
      
      // fail case - stack index out of bounds.
      return false;
   }

   boolean takeCard(int playerIndex)
   {
      // returns false if either argument is bad
      if (playerIndex < 0 || playerIndex > numPlayers - 1)
         return false;

      // Are there enough Cards?
      if (deck.getNumCards() <= 0)
         return false;

      return hands[playerIndex].takeCard(deck.dealCard());
   }
   
   boolean dealToStacks()
   {
      // Are there enough Cards?
      if (deck.getNumCards() <= numStacks)
         return false;
      
      // deal a card to each stack
      for (int i = 0; i < numStacks; i++)
      {
         stacks[i] = deck.dealCard();
      }
      return true;
   }
   
   public void incrementPlayerCantPlay()
   {
      ++playerCantPlay;
   }
   
   public void incrementComputerCantPlay()
   {
      ++computerCantPlay;
   }

   public void setComputerCanPlay(boolean b)
   {
      computerCanPlay = b;
   }

   public int getPlayerCantPlay()
   {
      return playerCantPlay;
   }
   
   public int getComputerCantPlay()
   {
      return computerCantPlay;
   }
}




