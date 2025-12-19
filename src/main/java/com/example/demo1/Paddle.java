package com.example.demo1;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle {

    private double x, y;
    private double width = 120;
    private final double height = 15;

    private double dx = 0;

    private static final double SPEED = 6;

    private final Canvas canvas;

    // ⭐ القوة المؤقتة
    private boolean boosted = false;
    private long boostEndTime = 0;

    public Paddle(Canvas canvas) {
        this.canvas = canvas;
        reset();
    }

    public void reset() {
        x = (canvas.getWidth() - width) / 2;
        y = canvas.getHeight() - 40;
        dx = 0;
        boosted = false;
    }

    public void moveLeft() {
        dx = -SPEED;
    }

    public void moveRight() {
        dx = SPEED;
    }

    public void stop() {
        dx = 0;
    }

    public void update() {

        // ⭐ إلغاء القوة بعد 20 ثانية
        if (boosted && System.currentTimeMillis() > boostEndTime) {
            width = 120;
            boosted = false;
        }

        x += dx;

        if (x < 0) x = 0;
        if (x + width > canvas.getWidth())
            x = canvas.getWidth() - width;
    }

    // ⭐ زيادة عرض المضرب (20 ثانية)
    public void increaseWidth() {
        width = 180;
        boosted = true;
        boostEndTime = System.currentTimeMillis() + 20_000;
    }

    public void render(GraphicsContext gc) {
        gc.setFill(boosted ? Color.ORANGE : Color.DARKBLUE);
        gc.fillRect(x, y, width, height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
