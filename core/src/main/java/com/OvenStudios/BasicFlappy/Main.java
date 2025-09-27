package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    Texture backgroundTexture;
    Texture groundTexture;
    Texture frogTexture;
    Texture alligatorTexture;

    SpriteBatch spriteBatch;
    Sprite frogSprite;

    FitViewport viewport;

    float frogVelocityY = 0f;
    float gravity = -3f;
    float spaceTimer = 0f;
    float maxJumpCharge = 4f;


    @Override
    public void create() {
        backgroundTexture = new Texture("BasicFlappyBackground.png");
        frogTexture = new Texture("frogSquareTest.png");

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        frogSprite = new Sprite(frogTexture);
        frogSprite.setSize(0.5f, 0.5f);
        frogSprite.setPosition(1, viewport.getWorldHeight() / 3f - frogSprite.getHeight() / 2f);

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
    }

    private void createAlligator() {
        //Create a new alligator obstacle at random height.
    }

    private void input() {
        if (frogSprite.getY() == 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
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
