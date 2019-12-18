package buildgame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class GameTimerGUI extends JFrame implements ActionListener
{
   private static final long serialVersionUID = 1L;
   public static final int WIDTH = 300;
   public static final int HEIGHT = 200;
   public static final int PAUSE = 1000; //milliseconds
   public static final int i = 10;

   private JPanel box;
   
   Timer timerThread;

   public JPanel getBox()
   {
      return box;
   }

   public GameTimerGUI()
   {
      setSize(WIDTH, HEIGHT);
      setTitle("Threaded Timer");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      setLayout(new BorderLayout());

      box = new JPanel();
      add(box, "Center");

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new FlowLayout());
      JButton startButton = new JButton("Start/Stop");
      startButton.addActionListener(this);
      buttonPanel.add(startButton);

      add(buttonPanel, "East");
   }
   
   public void actionPerformed(ActionEvent e)
   {
      if (timerThread.isRunning())
      {
         timerThread.pauseRunning();
      }
      else
      {
         timerThread.resumeRunning();
      }
   }

   public void restart() 
   {
      
   }
   
   public static void main(String[] args) 
   {
      GameTimerGUI gui = new GameTimerGUI();
      gui.timerThread = new Timer();
      gui.timerThread.start(); // calls ThreadedTimer's run()
      gui.setVisible(true);
   }

}






