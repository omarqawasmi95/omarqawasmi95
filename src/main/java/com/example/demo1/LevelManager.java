package com.example.demo1;

import javafx.scene.canvas.Canvas;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelManager {

    private static final int COLS = 10;
    private static final double GAP = 12;
    private static final int MAX_ROWS = 6;

    private final Random random = new Random();

    public List<Brick> createLevel(int level, Canvas canvas) {

        List<Brick> bricks = new ArrayList<>();

        // ✅ صفوف تزداد تدريجيًا مع حد أقصى
        int rows = Math.min(1 + level, MAX_ROWS);

        double brickWidth =
                (canvas.getWidth() - (COLS + 1) * GAP) / COLS;
        double brickHeight = 30;

        double startY = 80;

        // ✅ عدد الطوب الصلب مع حد أقصى
        int maxBricks = rows * COLS;
        int solidCount = (int) Math.pow(2, level - 1);
        solidCount = Math.min(solidCount, maxBricks);

        List<Integer> solidPositions = new ArrayList<>();
        while (solidPositions.size() < solidCount) {
            int pos = random.nextInt(maxBricks);
            if (!solidPositions.contains(pos)) {
                solidPositions.add(pos);
            }
        }

        int index = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < COLS; c++) {

                double x = GAP + c * (brickWidth + GAP);
                double y = startY + r * (brickHeight + GAP);

                boolean solid = solidPositions.contains(index);
                int hits = solid ? 2 : 1;

                boolean hasPowerUp =
                        !solid && random.nextDouble() < 0.2;

                PowerUp.Type powerType =
                        hasPowerUp ? PowerUp.randomType() : null;

                bricks.add(new Brick(
                        x,
                        y,
                        brickWidth,
                        brickHeight,
                        hits,
                        hasPowerUp,
                        powerType
                ));

                index++;
            }
        }

        return bricks;
    }
}
