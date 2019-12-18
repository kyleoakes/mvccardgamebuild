package buildgame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Controller
{
   Model model;
   View view;
   Timer timer;
   GUICard guiCard;
   
   public Controller(Model model, View view, Timer timer, GUICard guiCard)
   {
      this.guiCard = guiCard;
      this.model = model;
      this.view = view;
      this.timer = timer;
      
      this.setupActionListeners();
      updateIcons();
      view.setVisibility(true);
   }
   
   private void setupActionListeners()
   {
      // Set up the Player button listeners
      for (int index = 0; index <= view.getPlayerTopButton(); index++)
      {
         JButton button = ((JButton) view.getHumanPanelComponent(index));
         button.setActionCommand("" + index);
         button.addActionListener(
               new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent actionEvent)
                  {
                     // index is the index of the button
                     int index = Integer.parseInt(button.getActionCommand());
                     System.out.println("You clicked Button index " 
                           + Integer.parseInt(button.getActionCommand())
                     );
                     model.setSelectedHumanCardIndex(index);
                  }
               }
         );
      }
      
      // Set up the play area listeners
      for (int index = 0; index <= view.getPlayAreaTopButton(); index++)
      {
         JButton button = ((JButton) view.getPlayAreaComponent(index));
         button.setActionCommand("" + index);
         button.addActionListener(
               new ActionListener()
               {
                  @Override
                  public void actionPerformed(ActionEvent actionEvent)
                  {
                     // index is the index of the button
                     int stackIndex = Integer.parseInt(button.getActionCommand());
                     System.out.println("You clicked Play Area Button index " 
                           + stackIndex);
                     // Indicate stack has been selected
                     stackSelected(Model.PLAYER, stackIndex);
                  }
               }
         );
      }
      
      // Set up the timer (this actionPerformed will be called by Timer once
      // every second. This is how the timer updates the view via the controller
      timer.addActionListener(
            new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent actionEvent)
               {
                  view.setTimerDisplay(timer.timeElapsed());
               }
            }
      );
      
      // Set up the timer button
      JButton timerButton = ((JButton) view.getTimerButton());
      timerButton.addActionListener(
            new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent actionEvent)
               {
                  if (timer.isRunning())
                  {
                     timer.pauseRunning();
                  }
                  else
                  {
                     timer.resumeRunning();
                  }
               }
            }
      );
      
      // Set up the "I cannot play" button
      JButton cantPlayButton = ((JButton) view.getCantPlayButton());
      cantPlayButton.addActionListener(
            new ActionListener()
            {
               @Override
               public void actionPerformed(ActionEvent actionEvent)
               {
                  indicateCantPlay(Model.PLAYER);
               }
            }
      );
   }
   
   private void indicateCantPlay(int playerIndex)
   {
      if (playerIndex == Model.PLAYER)
      {
         view.setTurnLabel("Player couldn't play.");
         changePlayerStatus();
      }
      else
      {
         view.setTurnLabel("Computer couldn't play.");
         changeComputerStatus();
      }
      checkStatus();
   }
   
   private String plrIdToStr(int playerIndex)
   {
      return ((playerIndex == model.COMPUTER)?"Computer":"You");
   }
   
   // Called at end of the ActionListener when player selects a stack
   private void stackSelected(int callingPlayer, int stackIndex) 
   { 
      // validate that the card can be played onto the stack
      Card selectedCard = model.getHand(callingPlayer)
            .inspectCard(model.getSelectedCardIndex(callingPlayer));
      Card stackCard = model.getStack(stackIndex);
      if (model.compareCards(selectedCard, stackCard))
      {
         // Play the valid card
         model.playOnStack(selectedCard, stackIndex);
         model.playCard(callingPlayer, model.getSelectedCardIndex(callingPlayer));
         updateIcons();
         view.setTurnLabel(plrIdToStr(callingPlayer) + " played " + selectedCard 
               + " on " + stackCard );
         model.setPlayerCanPlay(true);
         model.setComputerCanPlay(true);
         // Draw a card
         if (cardsLeft())
         {
            model.takeCard(callingPlayer);
         }
         else
         {
            endGame();
         }
         // Update the View
         updateIcons();
         // Enable second player to play
         if (callingPlayer == model.PLAYER)
         {
            computerPlay();
         }
         else
         {
            view.enablePlayerButtons();
         }
      }
      else
      {
         view.setTurnLabel(selectedCard + " can't be played on " + stackCard );
      }
   }
   
   private void endGame()
   {
      updateIcons();
      view.getCantPlayButton().setEnabled(false);
      view.disableStacks();
      view.disablePlayerButtons();
      int playerScore = model.getPlayerCantPlay();
      int computerScore = model.getComputerCantPlay();
      int winnerIndex = ((playerScore < computerScore) ? Model.PLAYER : Model.COMPUTER);
      String winnerString = (winnerIndex == model.PLAYER) 
            ? "The Human Player! Congrats :D" 
            : "The Computer... Better luck next time!";
      System.out.println("The winner is... " + winnerString);
      view.setTurnLabel("The winner is... " + winnerString);
      view.setTurnLabel(plrIdToStr(winnerIndex) + " won in " + timer.timeElapsed() + ".");
      timer.pauseRunning();

   }

   private boolean computerPlay()
   {
      for(int cardIndex = 0; cardIndex < model.getHand(model.COMPUTER).getNumCards(); cardIndex++)
      {
         Card currentCard = model.getHand(model.COMPUTER).inspectCard(cardIndex);
         for(int stack = 0; stack < model.getNumStacks(); stack++)
         {
            System.out.println("Computer comparing... " + currentCard + " and " 
                  + model.getStack(stack));
            System.out.println(model.compareCards(currentCard, model.getStack(stack)));
            if(model.compareCards(currentCard, model.getStack(stack)))
            {
               model.setSelectedComputerCardIndex(cardIndex);
               stackSelected(model.COMPUTER, stack);
               return true;
            }
         }
      }
      indicateCantPlay(model.COMPUTER);
      return false;
   }
   
   private void updateIcons()
   {
      // Update the stack icons
      for(int stack = 0; stack < model.getNumStacks(); stack++)
      {
         ((JButton) view.getPlayAreaComponent(stack)).setIcon(GUICard
               .getIcon(model.getStack(stack)));
      }
//      
//      // try again because that one fails maybe?? ^ i don't even know right now
//      for (int i = 0; i < view.getPlayAreaCards().length; i++)
//      {
//         view.setPlayAreaCardIcon(i, GUICard.getIcon(model.getStack(i)));
//      }
      
      // Update the Player hand icons
      Hand hand = model.getHand(Model.PLAYER);
      for (int i = 0; i < hand.getNumCards(); i++)
      {
         ((JButton) view.getHumanPanelComponent(i)).setIcon(GUICard
               .getIcon(hand.inspectCard(i)));
      }
   }
   
   void checkStatus()
   {
      if((!model.getPlayerCanPlay()) && model.getComputerCanPlay())
      {
         computerPlay();
      }
      
      if(model.getPlayerCanPlay() && (!model.getComputerCanPlay()))
      {
         view.enablePlayerButtons();
      }
      
      if((!model.getPlayerCanPlay()) && (!model.getComputerCanPlay()))
      {
         // Neither player can play!!
         if (cardsLeft())
         {
            // Deal a card to each Stack from the Deck
            changePlayerStatus();
            changeComputerStatus();
            if (!model.dealToStacks()) // if deck is empty, end game
               endGame();
            updateIcons(); 
         }
         else
         {
            endGame();
         }
      }
   }

   void changePlayerStatus()
   {
      if(model.getPlayerCanPlay())
      {
         model.setPlayerCanPlay(false);
         model.incrementPlayerCantPlay();
      }
      else
      {
         model.setPlayerCanPlay(true);
      }
   }

   
   void changeComputerStatus()
   {
      if(model.getComputerCanPlay())
      {
         model.setComputerCanPlay(false);
         model.incrementComputerCantPlay();
      }
      else
      {
         model.setComputerCanPlay(true);
      }
   }

   
   // Checks whether a given card can be played on the card from a stack
   private boolean validatePlay(Card cardSelected, Card topOfStack)
   {
      // validate card and topOfStack values
      int cardValue = cardSelected.getInt();
      int stackValue = topOfStack.getInt();
      if (cardValue != -1 && stackValue != -1) // validate input
      {
         // Check if card value is 1 lower or 1 higher
         return (Math.abs(cardValue - stackValue) == 1);
      }
      else
      {
         // if we get to this return, Card.getInt() must have returned -1
         return false;  
      }
   }
   
   // Checks if there are at least 2 cards left in the deck
   private boolean cardsLeft()
   {
      return (model.getNumCardsRemainingInDeck() >= 2);
   }
}



