package buildgame;

import java.awt.event.ActionListener;

class Timer extends Thread
{
   private boolean running;
   private int minutes;
   private int seconds;
   
   ActionListener actionListener;
   
   static final int PAUSE = 1000; // milliseconds
   
   public boolean isRunning()
   {
      return running;
   }
   
   public void pauseRunning()
   {
      running = false;
   }
   
   public void resumeRunning()
   {
      running = true;
   }
   
   public Timer()
   {
      super();
      this.running = true;
      this.minutes = 0;
      this.seconds = -1;
   }
   
   // Returns the current time elapsed as a String of the form: #:##
   public String timeElapsed()
   {
      String timeString = ""; // ""
      
      timeString += minutes; // "0"
      timeString += ":"; // "0:"
      if (seconds < 10)
      {
         timeString += "0"; // "0:0"
      }
      timeString += seconds; // "0:00"
      
      return timeString;
   }
   
   private void incrementTimer()
   {
      ++seconds;
      if (seconds > 59)
      {
         ++minutes;
         seconds = 0;
      }
   }
   
   @Override
   public void run() 
   {
      while(true)
      {
         if (running)
         {
            incrementTimer();
            actionListener.actionPerformed(null);
            doNothing(PAUSE);
         }
         else
         {
            doNothing(PAUSE);
         }
      }
   }

   public void doNothing(int milliseconds)
   {
      try
      {
         Thread.sleep(milliseconds);
      }
      catch(InterruptedException e)
      {
         System.out.println("Timer.doNothing(int): Thread.sleep() interrupted");
         System.exit(0);
      }
   }

   public void addActionListener(ActionListener actionListener)
   {
      this.actionListener = actionListener;
   }

}


