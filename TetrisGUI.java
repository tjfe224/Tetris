/*
Author: Tyler Ferry
Date: 3/15/2018
Purpose: Set up the main GUI for the program and controls
   the game clock and movement of pieces. Ensures game runs
   well and looks like the original game by using functions in 
   Pieces class as well as classes implemented in this class.
   Also writes highScore text file so the score can be saved after
   the game is over
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.io.*;
import java.io.PrintWriter;

public class TetrisGUI implements ActionListener, KeyListener{
   
   //Create GUI elements
   private int stepNum, scoreNum, timerTime = 400;
   private int colLeft = 3;
   final int HEIGHT = 20;
   Color myColor = new Color(0, 120, 200);
   boolean check, timeCheck = true;
   boolean[][] board = new boolean[20][10];
   boolean status = true;
   JButton[][] color = new JButton[20][10];
   JButton[][] nextDisp = new JButton[4][4];
   Timer time = new Timer(timerTime, this);
   Timer fastTime = new Timer(50, this);
   JFrame frame = new JFrame("Tetris");
   JPanel panel = new JPanel();
   JPanel smallPanel = new JPanel();
   JPanel score = new JPanel();
   JPanel highScore = new JPanel();
   JLabel scoreTot = new JLabel("0");
   JLabel highScoreLabel = new JLabel("0");;
   Pieces piece, newPiece;
   JButton button;
   
   //Constructor
   TetrisGUI(){
      stepNum = 0;
      scoreNum = 0;
      readWrite();
      frame.setLayout(new GridBagLayout());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      GridBagConstraints c = new GridBagConstraints();
      UIManager.put("Button.disabledText", new ColorUIResource(Color.RED));
      c.weightx = 1.0;
      //Add restart button
      JButton restart = new JButton("Restart");
      restart.addActionListener(
         new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            status = true;
            stepNum = 0;
            colLeft = 3;
            for(int row = 0; row < 20; row++){
               for(int col = 0; col < 10; col++){
                  color[row][col].setBackground(Color.BLACK);
                  board[row][col] = false;
               }
            }
            scoreNum = 0;
            makeListener();
            
            scoreTot.setText(Integer.toString(scoreNum));
            color[10][0].setText("");
            color[10][1].setText("");
            color[10][2].setText("");
            color[10][3].setText("");
            color[10][6].setText("");
            color[10][7].setText("");
            color[10][8].setText("");
            color[10][9].setText("");
            piece = new Pieces((int)(Math.random()*7));
            newPiece = new Pieces((int)(Math.random()*7));
            draw();
            smallDraw();
            time.start();
            
         }
      });
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 0;
      frame.add(restart, c);
      restart.setFocusable(false);
      //setup Pause button
      JButton pause = new JButton("Pause");
      pause.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(timeCheck){
                  time.stop();
                  timeCheck = false;
               }
               else{
                  time.start();
                  timeCheck = true;
               }
            }
         });
      c.gridx = 1;
      c.gridy = 0;
      frame.add(pause, c);
      pause.setFocusable(false);
      
      panel.setLayout(new GridLayout(20,10));
      panel.setSize(300,600); 
      panel.addKeyListener(this);
      
      
      //Creates Board
      for(int row = 0; row < 20; row++){
         for(int col = 0; col < 10; col++){
            color[row][col] = new JButton();
            color[row][col].setEnabled(false);
            color[row][col].setBackground(Color.BLACK);
            panel.add(color[row][col]);
         }
      }
      
      panel.setVisible(true);
      c.fill = GridBagConstraints.VERTICAL;
      c.weightx = 0.5;
      c.weighty = 1.0;
      c.gridx = 0;
      c.gridy = 1;
      frame.add(panel, c);
   
      smallPanel.setLayout(new GridLayout(5,4));
      smallPanel.setSize(120,120); 
      smallPanel.setBackground(myColor);
      score.setLayout(new GridLayout(2,1));
      score.setBackground(myColor);
      JLabel tempScore = new JLabel("SCORE");
      tempScore.setForeground(Color.ORANGE);
      scoreTot.setForeground(Color.ORANGE);
      tempScore.setFont(new Font("Rockwell", Font.BOLD, 10));
      score.add(tempScore);
      score.add(scoreTot);
      
      highScore.setLayout(new GridLayout(2,1));
      highScore.setBackground(myColor);
      JLabel tempHigh = new JLabel("HIGH");
      tempHigh.setForeground(Color.ORANGE);
      highScoreLabel.setForeground(Color.ORANGE);
      tempHigh.setFont(new Font("Rockwell", Font.BOLD, 10));
      highScore.add(tempHigh);
      highScore.add(highScoreLabel);
   
      for(int row = 0; row < 4; row++){
         for(int col = 0; col < 4; col++){
            nextDisp[row][col] = new JButton();
            nextDisp[row][col].setEnabled(false);
            nextDisp[row][col].setBackground(Color.BLACK);
            smallPanel.add(nextDisp[row][col]);
         }
      }
      smallPanel.add(score);
      smallPanel.add(new JLabel(""));
      smallPanel.add(highScore);
      c.fill = GridBagConstraints.NONE;
      c.ipadx = 20;
      c.ipady = 0; 
      c.weighty = 0;
      c.weightx = 1.0;
      c.gridx = 1;
      c.anchor = GridBagConstraints.WEST;
      frame.add(smallPanel, c);
      smallPanel.setVisible(true);
      panel.setBackground(Color.DARK_GRAY);
      panel.setFocusable(true);
      panel.requestFocusInWindow(); 
      frame.setSize(new Dimension(480, 600));
      frame.getContentPane().setBackground(Color.DARK_GRAY);
      frame.setVisible(true);
      
      piece = new Pieces((int)(Math.random()*7));
      newPiece = new Pieces((int)(Math.random()*7));
      draw();
      smallDraw();
      //start clock
      time.start();
   }//End of constructor
   
   public void makeListener(){
      panel.addKeyListener(this);
   }
   
   public boolean isFocusable(){
      return true;
   }
   
   //Create a random piece out of 7 options
   public void newPiece(){
      stepNum = 0;
      colLeft = 3;
      time.restart();
      piece = newPiece;
      newPiece = new Pieces((int)(Math.random()*7));
      smallDraw();
      //Checks if game is over
      //Piece is already stuck when it starts
      if(overlapH())
         endGame();
      else
         draw();
   }
   
   public void nextStep(){
      stepNum++;
      //Check if the piece can move down a row
      if(status){
         //Check if at bottom of board
         if(stepNum + piece.lastrow() == HEIGHT)
            setPiece();
         //Check if hits another piece  
         else if(overlapH())
            setPiece();
         //Otherwise draw the new piece      
         else   
            draw(); 
      }
   }
   
   public void draw(){
      //Reset non-set pieces
      for(int row = 0; row < 20; row++){
         for(int col = 0; col < 10; col++){
            if(!(board[row][col]))
               color[row][col].setBackground(Color.BLACK);
         }
      }
      
      //Draw piece in new position
      for(int row = 0; row < 4; row++){
         for(int col = 0; col < 4; col++){
            if(piece.getvals()[row][col])
               color[stepNum+row][col+colLeft].setBackground(piece.getColor());
         }
      }
   }
   
   public void smallDraw(){
      //Reset Board
      for(int row = 0; row < 4; row++){
         for(int col = 0; col < 4; col++){
            nextDisp[row][col].setBackground(Color.BLACK);
         }
      }
      //Color new Disp
      for(int row = 0; row < 4; row++){
         for(int col = 0; col < 4; col++){
            if(newPiece.getvals()[row][col])
               nextDisp[row][col].setBackground(newPiece.getColor());
         }
      }
   }
   
   //Sets Final position of Piece
   public void setPiece(){
         stepNum--;
   
         for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col ++){
               if(piece.getvals()[row][col]){
                  board[stepNum+row][col+colLeft] = true;
                  color[stepNum+row][col+colLeft].setBackground(piece.getColor());
               }
            }
         }
         checkComplete();
         newPiece();
   }
   //Checks if a row is full of game pieces
   public void checkComplete(){
      int iterations = 0;
      for(int row = 0; row < HEIGHT; row++){
         check = true;
         for(int col = 0; col < 10; col++){
            if(!(board[row][col]))
               check = false;
         }
         if(check){
            removeRow(row);
            iterations++;
         }
      }
      //Check how many rows are cleared using loop and speed up time
      if(iterations == 1){
         scoreNum += 40;
         timerTime -= 5;
      }
      else if(iterations == 2){
         scoreNum += 100;
         timerTime -= 10;   
      }
      else if(iterations == 3){
         scoreNum += 300;
         timerTime -= 15;   
      }
      else if(iterations == 4){
         scoreNum += 1200;
         timerTime -= 20;   
      }
      else{}
      
      //Make game faster
      if(iterations > 0 && timerTime > 0){
         Timer time = new Timer(timerTime, this);
      }
      //Reset scoreboard score   
      scoreTot.setText(Integer.toString(scoreNum));
   }
   
   //Called if a piece is in set position and removes all values in the row
   //then moves all pieces above it down
   public void removeRow(int row){
      if(row > 0){
         for(int col = 0; col < 10; col++){
            color[row][col].setBackground(Color.BLACK);
            color[row][col].setBackground(color[row-1][col].getBackground());
            if(color[row-1][col].getBackground().equals(Color.BLACK))
               board[row][col] = false;
            else
               board[row][col] = true;
         }
         removeRow(row-1);
      }
   }
   
   //Sends piece to lowest possible postion
   public void hardFall(){
      stepNum++;
      if(stepNum + piece.lastrow() < (HEIGHT - 1) && (!overlapH())){
         hardFall();
      }
      else{
         stepNum--;
         draw();
         nextStep();
      }
      
   }
   
   //Checks overlap in horizontal direction
   public boolean overlapH(){
      for(int row = 0; row < 4; row++){
         for(int col = 0; col < 4; col++){
            if(piece.getvals()[row][col] &&
               (board[stepNum+row][col+colLeft])){
                  return true; 
            }
         }
      }
      return false; 
   }
   
   //Checks if there is overlap in vertical direction
   public boolean overlapV(){
         for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
               if(piece.colRight()+colLeft > 9){
                  return true;
               }
               else if(piece.colLeft()+colLeft < 0){
                  return true;
               }
               else if(piece.getvals()[row][col] &&
                  (board[stepNum+row][col+colLeft])){
                     return true; 
               }
            }
         }
      
      return false; 
   }
   
   //Reads and writes to the 'highScore' if it exists
   //If it doesn't, create a new one and catch any errors
   public void readWrite(){
      
      int tempHighScore;
      
      //Reads file, writes it if it doesn't exist
      try{
         FileReader fr = new FileReader("highScore.txt");
         BufferedReader br = new BufferedReader(fr);
         
         String str = br.readLine();
         highScoreLabel.setText(str);
         tempHighScore = Integer.parseInt(str);
         if(scoreNum > tempHighScore){
            highScoreLabel.setText(Integer.toString(scoreNum));
            try{
               FileWriter fw = new FileWriter("highScore.txt");
               PrintWriter pw = new PrintWriter(fw);
               pw.println(scoreNum);
               pw.close();
            }
            catch(IOException e){}
         }  
         br.close();
      }
      //catches potential IOexecption which happens, continue doing what it was trying
      //while skipping the likely problem section
      catch(IOException e){
         try{
            FileWriter fw = new FileWriter("highScore.txt");
            PrintWriter pw = new PrintWriter(fw);
            pw.println(scoreNum);
            pw.close();
         }
         catch(IOException c){}
      }
      
   }
   
   //Check if keyboard action has occured
   public void actionPerformed(ActionEvent e) {
      nextStep();
   }
   
   //Checks if a key has been pressed and does it's associated action
   //if one exists
   public void keyPressed(KeyEvent e){
      int key = e.getKeyCode();
      
      //Specify Which keys do what
      switch(key){
         //Left arrow
         case (KeyEvent.VK_LEFT):   if(colLeft+piece.colLeft() > 0){
                                       colLeft--;
                                       if(overlapV())
                                          colLeft++;
                                       else
                                          draw();
                                    }
                                    break;
         //Right arrow
         case (KeyEvent.VK_RIGHT):  if(colLeft + piece.colRight() < 9){
                                       colLeft++;
                                       if(overlapV())
                                          colLeft--;
                                       else
                                          draw();
                                    }
                                    break;
         //Up arrow
         case (KeyEvent.VK_UP):     piece.rotate(1);
                                    if(overlapV()){
                                       piece.rotate(0);
                                    }
                                    for(int row = 0; row < 4; row++){
                                       for(int col = 0; col < 4; col++){
                                          if(piece.getvals()[row][col] && board[row+stepNum][col+colLeft])
                                             piece.rotate(0);
                                       }
                                    }
                                    draw();
                                          
                                    break;
         //z key
         case (KeyEvent.VK_Z):      piece.rotate(0);
                                    for(int row = 0; row < 4; row++){
                                       for(int col = 0; col < 4; col++){
                                          if(piece.getvals()[row][col] && board[row+stepNum][col+colLeft])
                                             piece.rotate(0);
                                       }
                                    }
                                    if(piece.colRight()+colLeft < 10 &&
                                       (colLeft >= 0 && piece.colLeft()+colLeft >=0)){
                                          draw();
                                    }
                                    else
                                       piece.rotate(1);
                                          
                                    break;
         //Down key - stops normal time and starts fast time
         case (KeyEvent.VK_DOWN):   time.stop();
                                    fastTime.start(); 
                                    break;
         //Space bar - calls hardFall()
         case (KeyEvent.VK_SPACE):  hardFall();
                                    break;
      }
   }
   
   //Checks if down arrow is released
   public void keyReleased(KeyEvent e){
      if(e.getKeyCode() == KeyEvent.VK_DOWN)
         fastTime.stop();
         time.start();
   }
   
   //Since interface is used, function has to be included, even if it does nothing   
   public void keyTyped(KeyEvent e){}
   
   //Stop the game from moving
   public void endGame(){     
      time.stop();
      readWrite();
      boolean temp = true;
      status = false;
      System.out.println("Game Over");
      //Stop reading input from keyboard
      panel.removeKeyListener(this);
      int finalRow = -1;
      for(int row = 0; row < 2; row++){
         for(int col = 0; col < 11; col++){
            if(col < 10){
               if(board[row][col]){
                  temp = false;
               }
               
            }
            else{
               if(temp)
                  finalRow = row;
            }
         }
      }
      System.out.println(finalRow);
      switch(finalRow){
         
         case -1: break;
         case 1: piece.specialDraw(1);
                  draw();
                  break;
         case 0: piece.specialDraw(2);
                  draw();
                  break;
      }
      //End Game Screen
      for(int row = 9; row < 12; row++){
         for(int col = 0; col < 10; col++){
            color[row][col].setBackground(Color.WHITE);
            color[row][col].setMargin(new Insets(0,0,0,0));
            color[row][col].setFont(new Font("Rockwell", Font.BOLD, 21));
            
         }
      }
      //Write "GAME OVER" with game board
      color[10][0].setText("G");
      color[10][1].setText("A");
      color[10][2].setText("M");
      color[10][3].setText("E");
      color[10][6].setText("O");
      color[10][7].setText("V");
      color[10][8].setText("E");
      color[10][9].setText("R");
   }

}
