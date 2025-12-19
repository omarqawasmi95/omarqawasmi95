package com.example.demo1;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static final int MAX_LIVES = 5;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Paddle paddle;
    private final Ball ball;
    private final LevelManager levelManager = new LevelManager();

    private final List<Brick> bricks = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();

    private int lives = MAX_LIVES;
    private int level = 1;

    private GameState state = GameState.MENU;
    private long stateTimer;

    public GameManager(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        paddle = new Paddle(canvas);
        ball = new Ball(canvas, paddle);

        loadLevel();
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void onEnter() {
        if (state == GameState.MENU || state == GameState.GAME_OVER) {
            lives = MAX_LIVES;
            level = 1;
            loadLevel();
            state = GameState.LEVEL_START;
            stateTimer = System.currentTimeMillis();
        }
    }

    public void onEsc() {
        System.exit(0);
    }

    private void loadLevel() {
        bricks.clear();
        powerUps.clear();

        bricks.addAll(levelManager.createLevel(level, canvas));

        paddle.reset();
        ball.reset();
    }

    public void update() {

        if (state == GameState.MENU || state == GameState.GAME_OVER) return;

        if (state == GameState.LEVEL_START) {
            if (System.currentTimeMillis() - stateTimer > 1500) {
                state = GameState.PLAYING;
                ball.start();
            }
            return;
        }

        paddle.update();   // ⭐ إصلاح حركة المضرب
        ball.update();

        if (ball.collides(paddle)) {
            ball.bounceFromPaddle();
        }

        for (Brick b : bricks) {
            if (!b.isDestroyed() && ball.collides(b)) {
                ball.bounce(b);
                b.hit();

                if (b.isDestroyed() && b.hasPowerUp()) {
                    powerUps.add(
                            new PowerUp(b.getX(), b.getY(), b.getPowerType())
                    );
                }
                break;
            }
        }

        powerUps.forEach(p -> {
            p.update(canvas.getHeight());
            if (p.collides(paddle)) {
                p.activate(ball, paddle, this);
            }
        });

        powerUps.removeIf(p -> !p.isActive());

        if (ball.isOut()) {
            lives--;
            paddle.reset();
            ball.reset();

            state = (lives <= 0)
                    ? GameState.GAME_OVER
                    : GameState.LEVEL_START;

            stateTimer = System.currentTimeMillis();
        }

        if (bricks.stream().allMatch(Brick::isDestroyed)) {
            level++;
            loadLevel();
            state = GameState.LEVEL_START;
            stateTimer = System.currentTimeMillis();
        }
    }

    public void render() {

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (state == GameState.MENU) {
            drawCenter("BRICK BREAKER\n\nENTER - START\nESC - EXIT", 42);
            return;
        }

        if (state == GameState.GAME_OVER) {
            drawCenter("GAME OVER\n\nENTER - RESTART", 42);
            return;
        }

        bricks.forEach(b -> b.render(gc));
        powerUps.forEach(p -> p.render(gc));
        paddle.render(gc);
        ball.render(gc);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(18));
        gc.fillText("Stage: " + level, 20, 30);
        gc.fillText("Lives: " + lives, 20, 55);
    }

    private void drawCenter(String text, int fontSize) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(fontSize));

        String[] lines = text.split("\n");
        double y = canvas.getHeight() / 2 - lines.length * 20;

        for (String line : lines) {
            gc.fillText(line, canvas.getWidth() / 2 - 180, y);
            y += fontSize + 10;
        }
    }

    public void addLife() {
        if (lives < MAX_LIVES) lives++;
    }
}
