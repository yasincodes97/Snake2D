package com.example.snake;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class Controller {

    Pane root = new Pane();                                      //root of the scene
    boolean gameOver = false;                                   //says if the game is over
    Random rand = new Random();                                //random number generator
    Rectangle[] rect = new Rectangle[100];                    //snake body made of an array of rectangle objects
    Rectangle food;                                          //food made of a rectangle
    int[] snakeX = new int[100];                            //x and y coordinates of snake
    int[] snakeY = new int[100];
    private int count = 3;                               //length of snake
    int direction = 3;                                  //direction of snake
    private int grow = 0;                              //says if the snake is growing
    int[] foodLoc, headLoc;                           //location of food and head of snake

    Label score = new Label("Score: " + count);
    Button quitButton = new Button("Quit");
    HBox hbox = new HBox();
    Thread game;

    public Controller() {
        initRect();
        initialize();
    }

    void move() {                                 //moves the snake
        int[][] tmp = {{snakeX[1], snakeY[1]}, {}};
        snakeX[1] = snakeX[0];
        snakeY[1] = snakeY[0];
        for (int i = 2; i < count; i++) {       //updates the rectangles behind the head
            tmp[1] = new int[]{snakeX[i], snakeY[i]};
            snakeX[i] = tmp[0][0];
            snakeY[i] = tmp[0][1];
            tmp[0] = tmp[1];
            if (grow > 0 && snakeX[count - 1] == foodLoc[0] && snakeY[count - 1] == foodLoc[1]) {
                rect[count - 1].setVisible(true);
                --grow;
            }
        }
        switch (direction) {                   //determine the position of the snake
            case 0:
                if (snakeY[0] + 50 <= 450) snakeY[0] += 50;
                else snakeY[0] = 0;
                break;
            case 1:
                if (snakeX[0] - 50 >= 0) snakeX[0] -= 50;
                else snakeX[0] = 450;
                break;
            case 2:
                if (snakeY[0] - 50 >= 0) snakeY[0] -= 50;
                else snakeY[0] = 450;
                break;
            case 3:
                if (snakeX[0] + 50 <= 450) snakeX[0] += 50;
                else snakeX[0] = 0;
                break;
        }
        if (intersects(snakeX[0], snakeY[0])) {  //checks collision of head with another rectangle
            System.out.print("GAME OVER");
            gameOver = true;
            Platform.runLater( () -> {      //run method is overwritten
                gameOver();
            });
        } else {                           //else the for loop is executed
            for (int i = 0; i < count; i++) { //updates the visibility and position of snake
                rect[i].setTranslateX(snakeX[i]);
                rect[i].setTranslateY(snakeY[i]);
                rect[i].setVisible(true);
            }
            if (rect[0].getBoundsInParent().intersects(food.getBoundsInParent())) { //checks collision of head with food
                boolean isChanged = false;
                foodLoc = new int[]{(int) food.getTranslateX(), (int) food.getTranslateY()};
                while (!isChanged) {
                    food.setTranslateX(rand.nextInt(10) * 50);
                    food.setTranslateY(rand.nextInt(10) * 50);
                    if (intersects(foodLoc[0], foodLoc[1]) && (int) food.getTranslateX() != foodLoc[0] || (int) food.getTranslateY() != foodLoc[1])
                        isChanged = true;
                }
                snakeX[count] = (int) food.getTranslateX();
                snakeY[count] = (int) food.getTranslateY();
                rect[count].setTranslateX(rect[count - 1].getX());
                rect[count].setTranslateY(rect[count - 1].getY());
                ++count;
                ++grow;
                System.out.println(count + "");
            }
        }
    }

    public boolean intersects(int x, int y) {   //returns true if snake collides with food
        int i = 0;
        for (Rectangle part : rect) {
            if (part != rect[0] && i > 0 && part.isVisible() && x == snakeX[i] && y == snakeY[i]) {
                return true;
            }
            i++;
        }
        return false;
    }

    void gameOver() {                           //is invoked when game is over to update the user interface
        score.setText("Score: " + count);      //adding the quit button, score label to the hbox and then to the layout
                                              //event handler for quit button
        if (gameOver) {
            hbox.getChildren().add(quitButton);
            hbox.getChildren().add(score);
            root.getChildren().add(hbox);

            quitButton.setOnAction(event -> {
                System.exit(0);
            });
        }
    }

    Rectangle initRect() {                //initializes rectangle
        Rectangle res = new Rectangle(45, 45);
        res.setFill(Color.PURPLE);
        res.setStroke(Color.BLACK);
        res.setVisible(false);
        return res;
    }

    void initialize() {                 //creates head of the snake with snakeX, snakeY
        headLoc = new int[]{snakeX[0], snakeY[0]};

        food = initRect();             //creates food out of one rectangle and styles it
        food.setStroke(Color.RED);
        food.setFill(Color.WHITESMOKE);
        food.setVisible(true);
        food.setTranslateX(rand.nextInt(10) * 50);
        food.setTranslateY(rand.nextInt(10) * 50);
        root.getChildren().add(food); //adds the rectangle food to the main layout

        for (int i = 0; i < 3; i++) { //creates the first 3 rectangles and places on the playground
            rect[i] = initRect();
            rect[i].setTranslateX(50 + 50 * i);        //*i that the shift depends on the current position
            rect[i].setTranslateY(50 + 50 * i);
            rect[i].setVisible(true);
            root.getChildren().add(rect[i]);          //adding to the main layout
        }
        for (int i = 3; i < 100; i++) {              //creates the rest of the rectangles and appends to the tail
            rect[i] = initRect();
            rect[i].setTranslateX(50 + 50 * i);      //1st param. is starting position of the rectangle
            rect[i].setTranslateY(50 + 50 * i);     //2nd param. is shifting
            root.getChildren().add(rect[i]);       //adding to the main layout
        }

        rect[0].setFill(Color.PURPLE);            //head of the snake
        game = new Thread(() -> {                //starts a new thread parallel to the JavaFX-Thread
            while (!gameOver) {
                move();                         //move() is called in each iteration
                try {
                    Thread.sleep(200);   //controls the acceleration of the snake
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        game.start();                       //thread start
    }
}
