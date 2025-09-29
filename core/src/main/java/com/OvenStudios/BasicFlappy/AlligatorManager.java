package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class AlligatorManager {
    // Alligator configuration constants
    private static final int ALLIGATOR_COUNT = 4;
    private static final float ALLIGATOR_SPACING = 4f;
    private static final float ALLIGATOR_SPEED = 1.5f;

    private final Texture alligatorTexture;
    private final Array<Alligator> alligators;
    private final Random random = new Random();

    public AlligatorManager() {
        this.alligatorTexture = new Texture("alligatorTest.png");
        this.alligators = new Array<>();
        for (int i = 0; i < ALLIGATOR_COUNT; i++) {
            alligators.add(new Alligator(alligatorTexture));
        }
    }

    public void update(float delta) {
        for (Alligator alligator : alligators) {
            alligator.translateX(-ALLIGATOR_SPEED * delta);

            if (alligator.getX() + alligator.getWidth() < 0) {
                float newX = alligator.getX() + ALLIGATOR_COUNT * ALLIGATOR_SPACING;
                reposition(alligator, newX);
            }
        }
    }

    public boolean hasCollided(Sprite frogSprite) {
        for (Alligator alligator : alligators) {
            if (frogSprite.getBoundingRectangle().overlaps(alligator.getBoundingRectangle())) {
                return true;
            }
        }
        return false;
    }

    public void draw(SpriteBatch batch) {
        for (Alligator alligator : alligators) {
            alligator.draw(batch);
        }
    }

    public void reset(float startX) {
        for (int i = 0; i < alligators.size; i++) {
            Alligator alligator = alligators.get(i);
            reposition(alligator, startX + i * ALLIGATOR_SPACING);
        }
    }

    private void reposition(Alligator alligator, float x) {
        float minVisibleHeight = 0.5f;
        float maxVisibleHeight = 2.5f;
        float randomVisibleHeight = random.nextFloat() * (maxVisibleHeight - minVisibleHeight) + minVisibleHeight;
        float alligatorY = randomVisibleHeight - Alligator.HEIGHT;
        alligator.setPosition(x, alligatorY);
    }

    public void dispose() {
        alligatorTexture.dispose();
    }
}
