package buildgame;

public class Assig6Main
{
   static Model model;
   static View view;
   static Controller controller;
   static Timer timer;
   static GUICard guiCard;
   
   public static void main(String[] args)
   {
      guiCard = new GUICard();
      model = new Model();
      view = new View();
      timer = new Timer();
      controller = new Controller(model, view, timer, guiCard);
      timer.start();
   }

}


