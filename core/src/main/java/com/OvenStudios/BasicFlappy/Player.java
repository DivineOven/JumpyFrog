package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Player extends Sprite {
    // Player constants
    public static final float WIDTH = 0.9f;
    public static final float HEIGHT = 0.6f;
    private static final float GRAVITY = -3f;
    private static final float MAX_CHARGE = 4f;
    private static final float CHARGE_SPEED = 4f;

    // Player state
    private float velocityY = 0f;
    private float currentCharge = 0f;
    private int chargeDirection = 1;
    private float savedCharge = 0f;

    public Player(Texture texture) {
        super(texture);
        setSize(WIDTH, HEIGHT);
    }

    public void update(float delta, float groundHeight, float worldHeight) {
        // Apply gravity
        velocityY += GRAVITY * delta;
        setY(getY() + velocityY * delta);

        // Ground check
        if (getY() < groundHeight) {
            setY(groundHeight);
            velocityY = 0;
            savedCharge = 0;
        }

        // Ceiling check
        if (getY() + getHeight() > worldHeight) {
            setY(worldHeight - getHeight());
            velocityY = 0;
        }
    }

    public void handleInput(float groundHeight) {
        // Charge jump
        if (getY() == groundHeight && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentCharge += CHARGE_SPEED * Gdx.graphics.getDeltaTime() * chargeDirection;
            if (currentCharge > MAX_CHARGE || currentCharge < 0) {
                chargeDirection *= -1;
            }
        }

        // Release jump
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && currentCharge > 0) {
            if (getY() == groundHeight) {
                velocityY = Math.abs(currentCharge);
            }
            savedCharge = currentCharge;
            chargeDirection = 1;
            currentCharge = 0;
        }
    }

    public void reset(float groundHeight) {
        velocityY = 0f;
        currentCharge = 0f;
        savedCharge = 0f;
        chargeDirection = 1;
        setPosition(0.5f, groundHeight);
    }

    public float getCurrentCharge() {
        return currentCharge;
    }

    public float getSavedCharge() {
        return savedCharge;
    }

    public float getMaxCharge() {
        return MAX_CHARGE;
    }
}
