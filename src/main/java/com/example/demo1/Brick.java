package com.example.demo1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick {

    private final double x, y, w, h;
    private int hits;
    private boolean destroyed = false;
    private boolean hasPowerUp;
    private PowerUp.Type powerType; // ✅ جديد

    // ✅ constructor القديم (نتركه لتفادي كسر الكود)
    public Brick(double x, double y, double w, double h, int hits, boolean power) {
        this(x, y, w, h, hits, power, null);
    }

    // ✅ constructor الجديد (يدعم PowerUp.Type)
    public Brick(double x, double y, double w, double h,
                 int hits, boolean power, PowerUp.Type powerType) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.hits = hits;
        this.hasPowerUp = power;
        this.powerType = powerType;
    }

    public void hit() {
        hits--;
        if (hits <= 0) destroyed = true;
    }

    public void render(GraphicsContext gc) {
        if (destroyed) return;

        if (hasPowerUp) gc.setFill(Color.GOLD);
        else if (hits == 2) gc.setFill(Color.SADDLEBROWN);
        else gc.setFill(Color.DARKGRAY);

        gc.fillRect(x, y, w, h);
    }

    public boolean isDestroyed() { return destroyed; }
    public boolean hasPowerUp() { return hasPowerUp; }

    // ✅ getter جديد
    public PowerUp.Type getPowerType() {
        return powerType;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return w; }
    public double getHeight() { return h; }
}
