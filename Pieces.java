/*
Author: Tyler Ferry
Date: 3/15/2018
Purpose: Set up how pieces in the tetris game can move
   called and used by TetrisGUI to make the class more readable.
   Also helps with abstraction of data to avoid data manipulation
   by the user.
*/


import java.awt.*;
public class Pieces{

   private int shapeNum;
   private int phase;
   private Color color;
   Color myOrange = new Color(253, 80, 37);
   boolean[][] vals = new boolean[4][4];
   
   //Constructor
   Pieces(int shape){
      shapeNum = shape;
      phase = 4000;
      OgRotate();
   }
   public void OgRotate(){
      switch(shapeNum){
         //****
         case 0:  for(int x = 0; x < 4; x++){
                     vals[1][x] = true;
                  }
                  color = Color.CYAN;
                  break;
         
         //***
         //  *
         case 1:  for(int x = 0; x < 3; x++){
                     vals[1][x] = true;
                  }
                  vals[2][2] = true;
                  color = Color.BLUE;
                  break;
         
         //***
         //*
         case 2:  for(int x = 0; x < 3; x++){
                     vals[1][x] = true;
                  }
                  vals[2][0] = true;
                  color = myOrange;
                  break;
         
         //**
         //**
         case 3:  for(int x = 1; x < 3; x++){
                     for(int y = 1; y < 3; y++){
                        vals[x][y] = true;
                     }
                  }
                  color = Color.YELLOW;
                  break;
         
         // **
         //**
         case 4:  for(int x = 1; x < 3; x++){
                     for(int y = 2-x; y < 4-x; y++){
                        vals[x][y] = true;
                     }
                  }
                  color = Color.GREEN;
                  break;
         
         //***
         // *
         case 5:  for(int x = 0; x < 3; x++){
                        vals[1][x] = true;
                  }
                  vals[2][1] = true;
                  color = Color.MAGENTA;
                  break;
         
         //**
         // **
         case 6:  for(int x = 1; x < 3; x++){
                     for(int y = x-1; y < x+1; y++){
                        vals[x][y] = true;
                     }
                  }
                  color = Color.RED;
                  break;
                  
         default:  System.out.println("ERROR");
                          break;
         
         }

   }
   //Rotates the piece
   public void rotate(int num){
      //Reset vals
      for(int row = 0; row < 4; row++){
         for(int col = 0; col < 4; col++){
            vals[row][col] = false;
         }
      }
      
      //Determines Direction
      if(num == 1)
         phase++;
      else
         phase--;

      //Phase Rotation
      //On fourth rotation, reset original shape   
      if(phase%4 == 0){
         OgRotate();
      }
      else if(phase%4 == 1){  
         switch(shapeNum){
            case 0:  for(int row = 0; row < 4; row++){
                        vals[row][1] = true;
                     }break;
            case 1:  for(int row = 0; row < 3; row++){
                        vals[row][1] = true;
                     }
                     vals[2][0] = true;
                     break;
            case 2:  for(int row = 0; row < 3; row++){
                        vals[row][1] = true;
                     }
                     vals[0][0] = true;
                     break;
            case 3:  OgRotate();
                     break;
            case 4:  vals[0][0] = true;
                     vals[1][0] = true;
                     vals[1][1] = true;
                     vals[2][1] = true;
                     break;
            case 5:  for(int row = 0; row < 3; row++){
                        vals[row][1] = true;
                     }
                     vals[1][0] = true;
                     break;
            case 6:  vals[0][1] = true;
                     vals[1][1] = true;
                     vals[1][0] = true;
                     vals[2][0] = true;
                     break;
         }
      }
      //Second time rotating
      else if(phase%4 == 2){  
         switch(shapeNum){
         
            case 0:  OgRotate();
                     break;
            case 1:  for(int col = 0; col < 3; col++){
                        vals[2][col] = true;
                     }
                     vals[1][0] = true;
                     break;
            case 2:  for(int col = 0; col < 3; col++){
                        vals[2][col] = true;
                     }
                     vals[1][2] = true;
                     break;
            case 3:  OgRotate();
                     break;
            case 4:  OgRotate();
                     break;
            case 5:  for(int col = 0; col < 3; col++){
                        vals[2][col] = true;
                     }
                     vals[1][1] = true;
                     break;
            case 6:  OgRotate();
                     break;
         }
      }
      //Third time rotating
      else if(phase%4 == 3){  
         switch(shapeNum){
               
            case 0:  for(int row = 0; row < 4; row++){
                        vals[row][1] = true;
                     }break;
            case 1:  for(int row = 0; row < 3; row++){
                        vals[row][1] = true;
                     }
                     vals[0][2] = true;
                     break;
            case 2:  for(int row = 0; row < 3; row++){
                        vals[row][1] = true;
                     }
                     vals[2][2] = true;
                     break;
            case 3:  OgRotate();
                     break;
            case 4:  vals[0][0] = true;
                     vals[1][0] = true;
                     vals[1][1] = true;
                     vals[2][1] = true;
                     break;
            case 5:  for(int row = 0; row < 3; row++){
                        vals[row][1] = true;
                     }
                     vals[1][2] = true;
                     break;
            case 6:  vals[0][1] = true;
                     vals[1][1] = true;
                     vals[1][0] = true;
                     vals[2][0] = true;
                     break;
         }
      }
   }
   //Abstraction
   public boolean[][] getvals(){
      return vals;
   }
   public Color getColor(){
      return color;
   }
   
   //If one or more row has been removed, move all 
   //rows down the number of removed rows
   public void specialDraw(int x){
      for(int row = 1; row < 4; row++){
         for(int col = 0; col < 4; col++){
            if(x == 1){
               if(vals[row][col]){
                  vals[row-1][col] = true;
               }
               else
                  vals[row-1][col] = false;
            }
            if(x == 2){
               if(row < 2)
                  vals[row][col] = false;
               else if(row == 3){
                  vals[2][col] = false;
               }
               else
                  if(vals[row][col])
                     vals[row-2][col] = true;
            }
         }
      }
         
   }
   
   public int lastrow(){
      int lastrow = 0;
      for (int x = 1; x < 4; x++)
         for(int y = 0; y < 4; y++)
            if(vals[x][y]){
               lastrow = x;
               break;
            }
      
      return lastrow;
   }
   //Find right most collumn number of piece
   //Called in TetrisGUI class
   public int colRight(){
      int colRight = 0;
      for (int col = 1; col < 4; col++)
         for(int row = 0; row < 4; row++)
            if(vals[row][col]){
               colRight = col;
               break;
            }
      
      return colRight;
   }
   //Find left most column number of piece
   //Called in TetrisGUI class
   public int colLeft(){
      int colLeft = 1;
      for (int col = 1; col >= 0; col--){
         for(int row = 0; row < 4; row++){
            if(vals[row][col]){
               colLeft = col;
               break;
            }
         }
      }
      return colLeft;
   }
}
