package com.example.demo1;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class GameApp extends Application {

    private final Set<KeyCode> keys = new HashSet<>();
    private GameManager game;

    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(1200, 800);
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());
        stage.setFullScreen(true);

        game = new GameManager(canvas);

        scene.setOnKeyPressed(e -> keys.add(e.getCode()));
        scene.setOnKeyReleased(e -> keys.remove(e.getCode()));

        stage.setScene(scene);
        stage.setTitle("Brick Breaker");
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                processInput();
                game.update();
                game.render();
            }
        }.start();
    }

    private void processInput() {

        boolean left  = keys.contains(KeyCode.LEFT);
        boolean right = keys.contains(KeyCode.RIGHT);

        if (left && !right) {
            game.getPaddle().moveLeft();
        }
        else if (right && !left) {
            game.getPaddle().moveRight();
        }
        else {
            // ⭐ هذا هو السطر المهم
            game.getPaddle().stop();
        }

        if (keys.contains(KeyCode.ENTER)) game.onEnter();
        if (keys.contains(KeyCode.ESCAPE)) game.onEsc();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
