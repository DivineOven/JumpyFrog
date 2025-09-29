package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends Game {
    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Delegates render to the active screen
    }

    @Override
    public void dispose() {
        if (screen != null) {
            screen.dispose();
        }
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
