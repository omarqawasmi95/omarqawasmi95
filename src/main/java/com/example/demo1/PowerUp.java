package com.example.demo1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class PowerUp {

    public enum Type { LIFE, SPEED, STRENGTH }

    private double x, y;
    private final double size = 20;
    private boolean active = true;
    private final Type type;

    public PowerUp(double x, double y, Type t) {
        this.x = x;
        this.y = y;
        this.type = t;
    }

    public static Type randomType() {
        return Type.values()[new Random().nextInt(Type.values().length)];
    }

    public void update(double h) {
        y += 2;
        if (y > h) active = false;
    }

    public void render(GraphicsContext gc) {
        if (!active) return;

        gc.setFill(
                switch (type) {
                    case LIFE -> Color.RED;
                    case SPEED -> Color.BLUE;
                    case STRENGTH -> Color.GREEN;
                }
        );
        gc.fillOval(x, y, size, size);
    }

    public boolean collides(Paddle p) {
        return x + size >= p.getX() &&
                x <= p.getX() + p.getWidth() &&
                y + size >= p.getY();
    }

    public void activate(Ball ball, Paddle paddle, GameManager game) {
        switch (type) {
            case LIFE -> game.addLife();
            case SPEED -> ball.increaseSpeedSlow();
            case STRENGTH -> paddle.increaseWidth();
        }
        active = false;
    }

    public boolean isActive() { return active; }
}
