package buildgame;

import java.io.File;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;

//===================
//  GUICard CLASS
//===================
/**
 * Represents the card images.
 */
class GUICard
{
   // static variables.
   /**
    * A 2D array of Card Images ([values][suits])
    */
   private static Icon[][] iconCards = new ImageIcon[14][4];
   /**
    * An array of Card Images for one deck (includes Jokers).
    */
   static Icon[] icon = new ImageIcon[57];
   /**
    * The image of the back of a Card.
    */
   private static Icon iconBack;
   /**
    * A check to see if the file images have already been loaded to the icon
    * array.
    */
   static boolean iconsLoaded = false;
   /**
    * Hashmap for arranging the iconCards array.
    */
   public static HashMap<Character,Integer> map = new HashMap<Character, Integer>();

   static
   {
       map.put('A', 8);map.put('2', 0);map.put('3',1);
       map.put('4',2);map.put('5', 3);map.put('6',4);
       map.put('7', 5);map.put('8',6);map.put('9',7);
       map.put('T',12);map.put('J',9);map.put('Q',11);
       map.put('K',10);map.put('X',13);
   }

   /**
    * Constructor to load cards files into the array icon.
    */
   public GUICard()
   {
      loadCardIcons();
   }

   /**
    * Loads the image file paths into the icon array, and saves the back of card
    * icon into the iconBack variable.
    */
   static void loadCardIcons()
   {
      // only load icons once
      if (!iconsLoaded)
      {
         iconsLoaded = true;
         File folderFile = new File("images");

         // loads an array with image files.
         File[] listFiles = folderFile.listFiles();

         // sorts the files into ascending order.
         sortFiles(listFiles);

         // an index for the back of card image.
         int saveIndex = 0;
         boolean skipped = false;
         for (int i = 0; i < listFiles.length; i++)
         {
            if (listFiles[i].isFile())
            {
               // if the file is the back of card image
               if (listFiles[i].getName().contains("BK"))
               {
                  skipped = true;
                  saveIndex = i;
                  i++;
               }
               // if the back of card image is not in the files.
               if (!skipped)
               {
                  icon[i] = new ImageIcon(listFiles[i].getAbsolutePath());
               }
               // if the back of cards is in the image files then move
               // cards down one spot.
               else
               {
                  icon[i - 1] = new ImageIcon(listFiles[i].getAbsolutePath());
               }
            }
         }
         // the back of card image is moved to the last index.
         icon[icon.length - 1] = new ImageIcon(
               listFiles[saveIndex].getAbsolutePath());

         // load the iconCards array according to value/suit pairs.
         for (int i = 0; i < icon.length - 1; i++)
         {
            iconCards[i / 4][i % 4] = icon[i];
         }
         // sets the back of card iconBack variable.
         iconBack = icon[icon.length - 1];
      }
   }

   /**
    * Gets the card image icon.
    * 
    * @param card - the card to get the image for.
    * @return the icon/image of the card.
    */
   public static Icon getIcon(Card card)
   {
      if(!iconsLoaded)
      {
      loadCardIcons();
      }
      return iconCards[valueAsInt(card)][suitAsInt(card)];
   }

   /**
    * Gets the value of a Card's index.
    *
    * @param card - The card to get the value index from.
    * @return
    */
   private static int valueAsInt(Card card)
   {
      if (map.containsKey(card.getValue()))
      {
         return map.get(card.getValue());
      }

      return -1;
   }

   /**
    * Gets the suit constant as an int (based on index)
    * 
    * @param card - The card to get the suit int from.
    * @return The suit enum constant index.
    */
   static int suitAsInt(Card card)
   {
      return card.getSuit().ordinal();
   }

   /**
    * Gets the back of card icon.
    * 
    * @return the back of card iconBack.
    */
   public static Icon getBackCardIcon()
   {
      return iconBack;
   }

   /**
    * Sorts the images stored in the array arr.
    * 
    * @param a File array.
    */
   private static void sortFiles(File[] arr)
   {
      for (int firstFile = 0; firstFile < arr.length - 1; firstFile++)
      {
         int minIndex = firstFile;
         for (int secondFile = firstFile
               + 1; secondFile < arr.length; secondFile++)
         {
            String fileName = arr[minIndex].getName();
            if (arr[secondFile].getName().compareTo(fileName) < 0)
            {
               minIndex = secondFile;
            }
         }
         File temp = arr[minIndex];
         arr[minIndex] = arr[firstFile];
         arr[firstFile] = temp;
      }
   }

   static String turnIntIntoCardValue(int k)
   {
      String[] arr =
      { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "X" };

      if (k >= 0 && k < arr.length)
      {
         return arr[k];
      }
      return "invalid value";
   }

   static String turnIntIntoCardSuit(int j)
   {
      String[] arr =
      { "C", "D", "H", "S" };
      if (j >= 0 && j < arr.length)
      {
         return arr[j];
      }
      return "invalid value";
   }
}


