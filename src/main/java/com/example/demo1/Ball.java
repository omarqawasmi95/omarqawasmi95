package com.example.demo1;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {

    private double x, y;
    private final double r = 8;
    private double dx = 0, dy = 0;

    private final Canvas canvas;
    private final Paddle paddle;

    private static final double BASE_SPEED = 3.2;
    private static final double MAX_SPEED = 6.5;

    private boolean stuck = true;

    public Ball(Canvas canvas, Paddle paddle) {
        this.canvas = canvas;
        this.paddle = paddle;
        reset();
    }

    public void reset() {
        stuck = true;
        dx = 0;
        dy = 0;
        followPaddle();
    }

    public void start() {
        if (!stuck) return;
        stuck = false;
        dx = 0;
        dy = -BASE_SPEED;
    }
    public void increaseSpeedSlow() {
        dx *= 1.15;
        dy *= 1.15;
        limitSpeed();
    }

    public void update() {
        if (stuck) {
            followPaddle();
            return;
        }

        x += dx;
        y += dy;

        if (x - r <= 0 || x + r >= canvas.getWidth()) dx = -dx;
        if (y - r <= 0) dy = -dy;
    }

    public void bounceFromPaddle() {

        double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
        double distance = x - paddleCenter;
        double percent = distance / (paddle.getWidth() / 2);
        percent = Math.max(-1, Math.min(1, percent));

        double angle = percent * Math.toRadians(60);
        double speed = BASE_SPEED * 1.05;

        dx = speed * Math.sin(angle);
        dy = -Math.abs(speed * Math.cos(angle));

        limitSpeed();
    }

    public void bounce(Brick b) {
        dy = -dy;
        limitSpeed();
    }

    private void limitSpeed() {
        double speed = Math.sqrt(dx * dx + dy * dy);
        if (speed > MAX_SPEED) {
            dx = dx / speed * MAX_SPEED;
            dy = dy / speed * MAX_SPEED;
        }
    }

    private void followPaddle() {
        x = paddle.getX() + paddle.getWidth() / 2;
        y = paddle.getY() - r - 1;
    }

    public boolean collides(Paddle p) {
        return x + r >= p.getX()
                && x - r <= p.getX() + p.getWidth()
                && y + r >= p.getY()
                && y - r <= p.getY() + p.getHeight();
    }

    public boolean collides(Brick b) {
        return !b.isDestroyed()
                && x + r >= b.getX()
                && x - r <= b.getX() + b.getWidth()
                && y + r >= b.getY()
                && y - r <= b.getY() + b.getHeight();
    }

    public boolean isOut() {
        return y - r > canvas.getHeight();
    }

    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x - r, y - r, r * 2, r * 2);
    }
}
