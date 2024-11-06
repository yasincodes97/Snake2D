package com.example.snake;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    Controller controller;
    Scene scene;

    public void start(Stage primaryStage) {        //initializes the scene

        controller = new Controller();
        scene = new Scene(controller.root, 514, 543);
        myHandler();
                                                 //styling the FX elements with inline CSS
        controller.root.setStyle("-fx-background-color: #333333");
        controller.score.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FFFFFF;");
        controller.quitButton.setStyle("-fx-background-color: #444444; -fx-text-fill: #FFFFFF; -fx-font-size: 18px;");
                                                //styling the score label
        controller.score.setTranslateY(200);
        controller.score.setTranslateX(130);
        controller.score.setFont(Font.font("Arial", 24));
        controller.score.setTextFill(Color.WHITE);
        controller.score.setPadding(new Insets(10));
        controller.score.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        controller.score.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                                             //adding the elements to the scene
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(543);
        primaryStage.setMinWidth(514);
        primaryStage.setMaxHeight(543);
        primaryStage.setMaxWidth(514);
        primaryStage.setOnCloseRequest(event -> controller.gameOver = true);
        primaryStage.show();
    }

    public void myHandler() {                 //registers an event-handler for the keyboard inputs
        scene.setOnKeyPressed(event -> {
            KeyCode k = event.getCode();     //overwrites the handle() method
            switch (k) {                    //determines the numbers for the variable direction
                case RIGHT:
                    if (controller.direction != 1 && ((controller.direction == 2 || controller.direction == 0) && controller.headLoc[1] != controller.snakeY[0])) controller.direction = 3;
                    break;
                case LEFT:
                    if (controller.direction != 3 && ((controller.direction == 2 || controller.direction == 0) && controller.headLoc[1] != controller.snakeY[0])) controller.direction = 1;
                    break;
                case DOWN:
                    if (controller.direction != 2 && ((controller.direction == 3 || controller.direction == 1) && controller.headLoc[0] != controller.snakeX[0])) controller.direction = 0;
                    break;
                case UP:
                    if (controller.direction != 0 && ((controller.direction == 3 || controller.direction == 1) && controller.headLoc[0] != controller.snakeX[0])) controller.direction = 2;
                    break;
            }
            controller.headLoc = new int[]{controller.snakeX[0], controller.snakeY[0]};
                                            //updates the headLoc variable
        });
    }

    public static void main(String[] args) {
        launch(args);                     //starts the program
    }
}