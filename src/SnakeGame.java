import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.io.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{


   int tilesize = 25;
   int Widthoftheboard;
   int Heigthofboard;
   boolean gamestarted = false;
   boolean multiplayer = false;

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {
      if (gamestarted) {
         if (e.getKeyCode() == KeyEvent.VK_UP && speedy != 1) {
            speedx = 0;
            speedy = -1;
         } else if (e.getKeyCode() == KeyEvent.VK_DOWN && speedy != -1) {
            speedx = 0;
            speedy = 1;
         } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && speedx != -1) {
            speedx = 1;
            speedy = 0;
         } else if (e.getKeyCode() == KeyEvent.VK_LEFT && speedx != 1) {
            speedx = -1;
            speedy = 0;
         }
         if (multiplayer) {
            if (e.getKeyCode() == KeyEvent.VK_W && speedy2 != 1) {
               speedx2 = 0;
               speedy2 = -1;
            } else if (e.getKeyCode() == KeyEvent.VK_S && speedy2 != -1) {
               speedx2 = 0;
               speedy2 = 1;
            } else if (e.getKeyCode() == KeyEvent.VK_D && speedx2 != -1) {
               speedx2 = 1;
               speedy2 = 0;
            } else if (e.getKeyCode() == KeyEvent.VK_A && speedx2 != 1) {
               speedx2 = -1;
               speedy2 = 0;
            }
         }
      }
      if (!gamestarted) {
         if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            gamestarted = true;
         }
         if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            gamestarted = true;
            multiplayer = true;
         }
      }

   }

   @Override
   public void keyReleased(KeyEvent e) {

   }

   private class Tile {
      int x;
      int y;
      Tile(int x, int y) {
         this.x = x;
         this.y = y;
      }
   }
   Tile food;
   Tile snakeHead;
   ArrayList<Tile> snakeBody;

   int speedx;
   int speedy;
   Tile snakeHead2;
   ArrayList<Tile> snakeBody2;

   int speedx2;
   int speedy2;
   Random random;
   Timer gameLoop;
   boolean gameOver = false;

   SnakeGame(int Widthoftheboard, int Heigthofboard) throws IOException {
      this.Widthoftheboard = Widthoftheboard;
      this.Heigthofboard = Heigthofboard;
      addKeyListener(this);
      setFocusable(true);
      setBackground(Color.WHITE);
      setPreferredSize(new Dimension(this.Widthoftheboard, this.Heigthofboard));


      snakeHead = new Tile(5, 5);
      snakeBody = new ArrayList<Tile>();
      speedx = 0;
      speedy = 1;
      snakeHead2 = new Tile(20, 5);
      snakeBody2 = new ArrayList<Tile>();
      speedx2 = 0;
      speedy2 = 1;

      gameLoop = new Timer(100, this);
      gameLoop.start();
      food = new Tile(10, 10);
      random = new Random();
      foodplacing();
   }

   public void foodplacing() {
      food.x = random.nextInt(Widthoftheboard/tilesize);
      food.y = random.nextInt(Heigthofboard/tilesize);
      if (!multiplayer) {
         while (food.x == snakeHead.x && food.y == snakeHead.y) {
            food.x = random.nextInt(Widthoftheboard / tilesize);
            food.y = random.nextInt(Heigthofboard / tilesize);
         }
      }
      if (multiplayer) {
         while (food.x == snakeHead.x && food.y == snakeHead.y && food.x == snakeHead2.x && food.y == snakeHead2.y) {
            food.x = random.nextInt(Widthoftheboard / tilesize);
            food.y = random.nextInt(Heigthofboard / tilesize);
         }
      }
   }

   public boolean collisionbetweenobjects(Tile tile1, Tile tile2) {
      return tile1.x == tile2.x && tile1.y == tile2.y;
   }
   public void draw(Graphics g) {
      if (gamestarted) {
         g.setColor(Color.GREEN);
         g.fill3DRect(snakeHead.x * tilesize, snakeHead.y * tilesize, tilesize, tilesize, true);

         g.setColor(Color.RED);
         g.fill3DRect(food.x * tilesize, food.y * tilesize, tilesize, tilesize, true);
         if (multiplayer) {
            g.setColor(Color.MAGENTA);
            g.fill3DRect(snakeHead2.x * tilesize, snakeHead2.y * tilesize, tilesize, tilesize, true);
            for (int i = 0; i < snakeBody2.size(); i++) {
               Tile snakePart = snakeBody2.get(i);
               g.fill3DRect(snakePart.x * tilesize, snakePart.y * tilesize, tilesize, tilesize, true);
            }
         }
         g.setColor(Color.GREEN);
         try {
         for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tilesize, snakePart.y * tilesize, tilesize, tilesize, true);
         }  }catch (Exception e) {
            System.out.println("An error has occured");
         }
         g.setColor(Color.BLACK);
         g.setFont(new Font("Arial", Font.PLAIN, 16));
         if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size() + snakeBody2.size()), tilesize - 16, tilesize);
            try {
               BufferedReader reader = new BufferedReader(new FileReader("Highscore.txt"));
               int highscore = Integer.parseInt(reader.readLine());
               int currentscore = (snakeBody.size() + snakeBody2.size());
               if (currentscore > highscore){
                  BufferedWriter writer = new BufferedWriter(new FileWriter("Highscore.txt"));
                  writer.write(Integer.toString(currentscore));
                  writer.close();
                  g.drawString("New high score: " + String.valueOf(snakeBody.size() + snakeBody2.size()), tilesize - 16, tilesize + 40);
               }

            } catch (IOException e) {
               throw new RuntimeException(e);
            }
         } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size() + snakeBody2.size()), tilesize - 16, tilesize);
         }
      }
      if (!gamestarted) {
         g.setColor(Color.BLACK);
         g.setFont(new Font("Arial", Font.PLAIN, 25));
         g.drawString("Press space if you want to start", tilesize - 25, tilesize + 300);
         g.drawString("Press enter if you want to play 2 player mode", tilesize - 25, tilesize + 340);
         try {
            BufferedReader reader = new BufferedReader(new FileReader("Highscore.txt"));
            int highscore = Integer.parseInt(reader.readLine());
            g.drawString("High Score: " + String.valueOf(highscore), tilesize - 25, tilesize + 380);

         } catch (IOException e) {
            throw new RuntimeException(e);
         }

      }
   }
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      draw(g);
   }

   public void move() {
      if (collisionbetweenobjects(snakeHead, food)) {
         snakeBody.add(new Tile(food.x, food.y));
         foodplacing();
      }
      if (multiplayer) {
         if (collisionbetweenobjects(snakeHead2, food)) {
            snakeBody2.add(new Tile(food.x, food.y));
            foodplacing();
         }
         for (int i = snakeBody2.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody2.get(i);
            if (i == 0) {
               snakePart.x = snakeHead2.x;
               snakePart.y = snakeHead2.y;
            }
            else {
               Tile prevSnakePart = snakeBody2.get(i-1);
               snakePart.x = prevSnakePart.x;
               snakePart.y = prevSnakePart.y;
            }
         }
         snakeHead2.x += speedx2;
         snakeHead2.y += speedy2;
         if (snakeHead2.x*tilesize < 0 || snakeHead2.x*tilesize > Widthoftheboard || snakeHead2.y*tilesize < 0 || snakeHead2.y*tilesize > Heigthofboard) {
            gameOver = true;
         }
      }
      for (int i = snakeBody.size()-1; i >= 0; i--) {
         Tile snakePart = snakeBody.get(i);
         if (i == 0) {
            snakePart.x = snakeHead.x;
            snakePart.y = snakeHead.y;
         }
         else {
            Tile prevSnakePart = snakeBody.get(i-1);
            snakePart.x = prevSnakePart.x;
            snakePart.y = prevSnakePart.y;
         }
      }
      snakeHead.x += speedx;
      snakeHead.y += speedy;
      for (int i = 0; i < snakeBody.size(); i++) {
         Tile snakePart = snakeBody.get(i);
         if (collisionbetweenobjects(snakeHead, snakePart)) {
            gameOver = true;
         }
      }

      if (snakeHead.x*tilesize < 0 || snakeHead.x*tilesize > Widthoftheboard || snakeHead.y*tilesize < 0 || snakeHead.y*tilesize > Heigthofboard) {
         gameOver = true;
      }
   }
   @Override
   public void actionPerformed(ActionEvent e) {
      if (gamestarted) {
         move();
         repaint();
      }
      if (gameOver) {
         gameLoop.stop();
      }
   }

}
