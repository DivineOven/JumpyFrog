package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture groundTexture;
    Texture frogTexture;
    Texture alligatorTexture;

    Array<Sprite> alligatorSprites;

    SpriteBatch spriteBatch;
    Sprite frogSprite;
    //Sprite alligatorSprite;

    FitViewport viewport;
    Random random = new Random();

    float frogVelocityY = 0f;
    float gravity = -3f;
    float spaceTimer = 0f;
    float maxJumpCharge = 4f;
    float alligatorSpeed = 1.5f;

    private static final int ALLIGATOR_COUNT = 4;
    private static final float ALLIGATOR_SPACING = 4f;
    private static final float ALLIGATOR_WIDTH = 1f;
    private static final float ALLIGATOR_HEIGHT = 5f;


    @Override
    public void create() {
        backgroundTexture = new Texture("BasicFlappyBackground.png");
        frogTexture = new Texture("frog.png");
        alligatorTexture = new Texture("alligatorTest.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        frogSprite = new Sprite(frogTexture);
        frogSprite.setSize(0.9f, 0.6f);
        frogSprite.setPosition(0.5f, 0);

        alligatorSprites = new Array<>();
        float firstAlligatorX = frogSprite.getX() + 2f; // Start the first alligator in the middle of the screen.
        for (int i = 0; i < ALLIGATOR_COUNT; i++) {
            Sprite alligator = new Sprite(alligatorTexture);
            alligator.setSize(ALLIGATOR_WIDTH, ALLIGATOR_HEIGHT);
            alligatorSprites.add(alligator);
            // Position the alligators starting from the new X position.
            repositionAlligator(alligator, firstAlligatorX + i * ALLIGATOR_SPACING);
        }


    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }



    private void logic() {
        float delta = Gdx.graphics.getDeltaTime();

        // Apply gravity to frog's vertical velocity
        frogVelocityY += gravity * delta;

        // Update frog's position based on its velocity
        frogSprite.setY(frogSprite.getY() + frogVelocityY * delta);

        // Prevent the frog from falling below the ground
        if (frogSprite.getY() < 0) {
            frogSprite.setY(0);
            frogVelocityY = 0;
        }

        // Prevent the frog from going above the screen
        if (frogSprite.getY() + frogSprite.getHeight() > viewport.getWorldHeight()) {
            frogSprite.setY(viewport.getWorldHeight() - frogSprite.getHeight());
            frogVelocityY = 0;
        }

        // Move and recycle alligators
        if (frogSprite.getY() > 0) {
            // Move and recycle alligators
            for (Sprite alligator : alligatorSprites) {
                // Move the alligator to the left
                alligator.translateX(-alligatorSpeed * delta);

                // If the alligator has moved off-screen, recycle it
                if (alligator.getX() + alligator.getWidth() < 0) {
                    // Reposition it to the right of the entire chain of alligators
                    float newX = alligator.getX() + ALLIGATOR_COUNT * ALLIGATOR_SPACING;
                    repositionAlligator(alligator, newX);
                }
            }
        }
    }


    private void repositionAlligator(Sprite alligator, float x) {
        // Decide on a random *visible* height for this instance.
        float minVisibleHeight = 0.5f;
        float maxVisibleHeight = 2.5f;
        float randomVisibleHeight = random.nextFloat() * (maxVisibleHeight - minVisibleHeight) + minVisibleHeight;

        // Calculate the Y position to hide the bottom part of the sprite.
        // y_position = visible_part - total_height
        float alligatorY = randomVisibleHeight - ALLIGATOR_HEIGHT;

        // Position the alligator off-screen to the right, with its bottom part hidden.
        alligator.setPosition(x, alligatorY);
    }

    //TODO: ADD A CHARGE METER.
    private void input() {
        if (frogSprite.getY() == 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) { // TODO: CHANGE GETY() == 0 TO == GROUND.GETY() WHEN GROUND IS ADDED.
            spaceTimer += Gdx.graphics.getDeltaTime(); // Charge jump while on the ground and space is pressed.
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && spaceTimer > 0) {
            if (frogSprite.getY() == 0) {
                frogVelocityY = Math.min(spaceTimer * 5f, maxJumpCharge); // Add jump velocity based on charge time.
            }
            spaceTimer = 0f; // Reset jump charge timer.
        }
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        frogSprite.draw(spriteBatch);
        for (Sprite alligator : alligatorSprites) {
            alligator.draw(spriteBatch);
        }


        spriteBatch.end();
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
