package buildgame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class View extends JFrame
{
   private static final long serialVersionUID = 1L;
   private JPanel pnlHumanHand;
   private JPanel pnlPlayArea;
   private JPanel pnlComputerHand;
   private JPanel pnlTimer;
   private JPanel pnlTextBox;
   private JButton[] playerCards;
   private JButton[] computerCards;
   private JButton[] playAreaCards;
//   private JLabel[] playAreaText; // These labels not needed for BUILD :)
   private JLabel turnLabel;
   private int playerTopButton;
   private int compTopButton;
   private int playAreaTopButton;
   private int playTextBoxCounter = 0;
   
   private static final int TIMER_PANEL_UPPER = 0; // index in timer panel
   private static final int TIMER_PANEL_LOWER = 1; // index in timer panel
   // the index of the timer text label within the timer panel
   private static final int TIMER_LABEL_INDEX = TIMER_PANEL_UPPER; 
   // the index of the Start/Stop button within lower half of timer panel
   private static final int TIMER_BUTTON_INDEX = 0; 
   // the index of the Start/Stop button within lower half of timer panel
   private static final int CANT_PLAY_BUTTON_INDEX = 1; 
   
   private final int FONT_SIZE_MULT = 3; // make timer font 3x normal size
   private final int numStacks = 3; // BUILD game has 3 stacks :)

   View()
   {
      this("myCardTable", 2, 7);
   }

   View(String title, int numPlayers, int numCardsPerHand)
   {
      if (title != null)
      {
         setTitle(title);
      }
      setLayout(new BorderLayout());
      setSize(800, 600);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      playerTopButton = -1;
      compTopButton = -1;
      playAreaTopButton = -1;

      // set up JFrame starting environment.
      initJPanels(numPlayers);
      initGridLayouts(numCardsPerHand);
      allocButtonArrays(numCardsPerHand);
      allocPlayAreaArrays(numStacks );
      allocButtons(numCardsPerHand);
      allocTimerAndPlayButton();
      initPlayAreaButton(numStacks);
//      initPlayText(numStacks); // These labels not needed for BUILD :)
      initTextBox();
      addButtonsToPanel(numCardsPerHand);
      addCardLabelsToPanel(numStacks);
//      addTextLabelsToPanel(numPlayers); // The labels not needed for BUILD :)
      addPanelsToFrame();
      setTitledBorder();
      setPanelBackground();

      // setVisible initially so that the player has to choose heads or tails
      setVisible(false);
   }
   
//   public JButton[] getPlayAreaCards()
//   {
//      return playAreaCards;
//   }

//   public int getTimerButtonIndex()
//   {
//      return TIMER_BUTTON_INDEX;
//   }

   public int getPlayerTopButton()
   {
      return playerTopButton;
   }

//   public int getCompTopButton()
//   {
//      return compTopButton;
//   }

   public int getPlayAreaTopButton()
   {
      return playAreaTopButton;
   }

   public Component getComputerPanelComponent(int index)
   {
      return pnlComputerHand.getComponent(index);
   }

   public Component getHumanPanelComponent(int index)
   {
      return pnlHumanHand.getComponent(index);
   }

   public Component getPlayAreaComponent(int index)
   {
      return pnlPlayArea.getComponent(index);
   }
   
//   public Component getTimerLabel()
//   {
//      return pnlTimer.getComponent(TIMER_PANEL_UPPER);
//   }
   
   public Component getTimerButton()
   {
      return ((Container) pnlTimer.getComponent(TIMER_PANEL_LOWER))
            .getComponent(TIMER_BUTTON_INDEX);
   }
   
   public Component getCantPlayButton()
   {
      return ((Container) pnlTimer.getComponent(TIMER_PANEL_LOWER))
            .getComponent(CANT_PLAY_BUTTON_INDEX);
   }
   
   public void setTimerDisplay(String timeString)
   {
      ((JLabel) pnlTimer.getComponent(TIMER_LABEL_INDEX)).setText(timeString);
   }

   // All methods below set up JFrame starting environment.
   private void initJPanels(int numPlayers)
   {
      pnlComputerHand = new JPanel();
      pnlTimer = new JPanel();
      pnlPlayArea = new JPanel();
      pnlTextBox = new JPanel();
      pnlHumanHand = new JPanel();
   }

   private void initGridLayouts(int numCardsPerHand)
   {
      pnlComputerHand.setLayout(new GridLayout(1, numCardsPerHand));
      pnlTimer.setLayout(new GridLayout(2, 1)); // for timer and text box
      pnlPlayArea.setLayout(new GridLayout(1, numStacks));
      pnlTextBox.setLayout(new GridLayout(1, 1));
      pnlHumanHand.setLayout(new GridLayout(1, numCardsPerHand));
   }

   private void allocButtonArrays(int numButton)
   {
      computerCards = new JButton[numButton];
      playerCards = new JButton[numButton];
   }

   private void allocPlayAreaArrays(int numLabel)
   {
      playAreaCards = new JButton[numLabel];
//      playAreaText = new JLabel[numLabel];
   }

   private void allocButtons(int numButton)
   {
      for (int index = 0; index < numButton; index++)
      {
         computerCards[index] = new JButton(GUICard.getBackCardIcon());
         computerCards[index].setEnabled(false);
         computerCards[index].setContentAreaFilled(false);
         computerCards[index].setDisabledIcon(GUICard.getBackCardIcon());
         compTopButton++;
         playerCards[index] = new JButton(GUICard.getBackCardIcon());
         playerCards[index].setContentAreaFilled(false);
         playerTopButton++;
      }
   }

   private void allocTimerAndPlayButton()
   {
      // NOTE DO NOT CHANGE THE ORDER THESE ARE ADDED
      pnlTimer.add(new JLabel("0:00", JLabel.CENTER));
      JPanel pnlTimerInner = new JPanel();
      pnlTimerInner.setLayout(new GridLayout(1,2));
      pnlTimer.add(pnlTimerInner);
      pnlTimerInner.add(new JButton("Start/Stop Timer"));
      pnlTimerInner.add(new JButton("I cannot play"));
   }

   private void initPlayAreaButton(int numButton)
   {
      for (int index = 0; index < numButton; index++)
      {
         playAreaCards[index] = new JButton(GUICard.getBackCardIcon());
         playAreaCards[index].setContentAreaFilled(false);
         playAreaTopButton++;
      }
   }

//   private void initPlayText(int numLabel)
//   {
//      String text;
//      for (int index = 0; index < numLabel; index++)
//      {
//         if (index == 0)
//         {
//            text = "COMPUTER";
//         } else
//         {
//            text = "PLAYER" + index;
//         }
//         playAreaText[index] = new JLabel(text, JLabel.CENTER);
//         playAreaText[index].setForeground(new Color(255, 255, 255));
//      }
//   }

   private void initTextBox()
   {
      pnlTextBox.setPreferredSize(new Dimension(220, 295));
      turnLabel = new JLabel("<html> </html>");
      setTurnLabel(" ");
      pnlTextBox.add(turnLabel);
   }

   private void addButtonsToPanel(int numButton)
   {
      for (int index = 0; index < numButton; index++)
      {
         pnlComputerHand.add(computerCards[index]);
         pnlHumanHand.add(playerCards[index]);
      }
   }

   private void addCardLabelsToPanel(int numLabel)
   {
      for (int index = 0; index < numLabel; index++)
      {
         pnlPlayArea.add(playAreaCards[index]);

      }
   }

//   private void addTextLabelsToPanel(int numLabel)
//   {
//      for (int index = 0; index < numLabel; index++)
//      {
//         pnlPlayArea.add(playAreaText[index]);
//      }
//   }

   void setTurnLabel(String addedString)
   {
      String start = "<html>";
      String end = "</html>";
      playTextBoxCounter++;
      String s;
      if (playTextBoxCounter > 9)
      {
         turnLabel.setText(start + addedString + end);
         playTextBoxCounter = 0;
      } else
      {
         s = turnLabel.getText().substring(6, turnLabel.getText().length() - 7);
         turnLabel.setText(start + s + "<br>" + addedString + end);
      }
   }

   private void addPanelsToFrame()
   {
      add(pnlComputerHand, BorderLayout.NORTH);
      add(pnlTimer, BorderLayout.WEST);
      add(pnlPlayArea, BorderLayout.CENTER);
      add(pnlTextBox, BorderLayout.EAST);
      add(pnlHumanHand, BorderLayout.SOUTH);
   }

   private void setTitledBorder()
   {
      TitledBorder computerBorder = new TitledBorder("Computer Hand");
      TitledBorder timerBorder = new TitledBorder("Timer");
      TitledBorder playAreaBorder = new TitledBorder("Play Area");
      TitledBorder textBoxBorder = new TitledBorder("Game Log");
      TitledBorder humanBorder = new TitledBorder("Your Hand");

      computerBorder.setTitleColor(new Color(255, 255, 255));
      timerBorder.setTitleColor(new Color(255, 255, 255));
      playAreaBorder.setTitleColor(new Color(255, 255, 255));
      textBoxBorder.setTitleColor(new Color(255, 255, 255));
      humanBorder.setTitleColor(new Color(255, 255, 255));

      pnlComputerHand.setBorder(computerBorder);
      pnlTimer.setBorder(timerBorder);
      pnlPlayArea.setBorder(playAreaBorder);
      pnlTextBox.setBorder(textBoxBorder);
      pnlHumanHand.setBorder(humanBorder);
      
      JLabel timeLabel = (JLabel) (pnlTimer.getComponent(TIMER_LABEL_INDEX));
      timeLabel.setForeground(new Color(255, 255, 255));
      timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, timeLabel.getFont().getSize()*FONT_SIZE_MULT));
      
      JLabel turnLabel = (JLabel) (pnlTextBox.getComponent(0));
      turnLabel.setForeground(new Color(255, 255, 255));
   }

   private void setPanelBackground()
   {
      pnlComputerHand.setBackground(new Color(39, 119, 20));
      pnlTimer.setBackground(new Color(39, 119, 20));
      pnlPlayArea.setBackground(new Color(39, 119, 20));
      pnlTextBox.setBackground(new Color(39, 119, 20));
      pnlHumanHand.setBackground(new Color(39, 119, 20));
   }
   
   public void enablePlayerButtons()
   {
      for (int index = 0; index < playerCards.length; index++)
      {
         playerCards[index].setEnabled(true);
      }
   }
   
   public void disablePlayerButtons()
   {
      for (int index = 0; index < playerCards.length; index++)
      {
         playerCards[index].setEnabled(false);
      }
   }
   
   public void enableStacks()
   {
      for (int index = 0; index < playAreaCards.length; index++)
      {
         playAreaCards[index].setEnabled(true);
      }
   }
   
   public void disableStacks()
   {
      for (int index = 0; index < playAreaCards.length; index++)
      {
         playAreaCards[index].setEnabled(false);
      }
   }

   public boolean setVisibility(boolean visible)
   {
      setVisible(visible);
      return true;
   }
}



