package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
    private final Main game;
    private final Screen screenToDispose;

    // Textures
    private final Texture backgroundTexture;
    private final Texture groundTexture;
    private final Texture frogTexture;

    // Rendering
    private final FitViewport viewport;

    // Game Objects
    private final Player player;
    private final Sprite groundSprite;
    private final AlligatorManager alligatorManager;

    // Game State
    private boolean acceptingInput = false;

    public GameScreen(final Main game) {
        this(game, null);
    }

    public GameScreen(final Main game, Screen screenToDispose) {
        this.game = game;
        this.screenToDispose = screenToDispose;

        backgroundTexture = new Texture("background.png");
        groundTexture = new Texture("waterGround.png");
        frogTexture = new Texture("frog.png");

        viewport = new FitViewport(8, 5);

        groundSprite = new Sprite(groundTexture);
        groundSprite.setSize(viewport.getWorldWidth(), groundTexture.getHeight() * (viewport.getWorldWidth() / groundTexture.getWidth()));
        groundSprite.setPosition(0, 0);

        player = new Player(frogTexture);
        alligatorManager = new AlligatorManager();

        resetGame();
    }

    private void resetGame() {
        player.reset(groundSprite.getHeight());
        float firstAlligatorX = player.getX() + 2f;
        alligatorManager.reset(firstAlligatorX);
    }

    @Override
    public void render(float delta) {
        handleInput();
        updateLogic(delta);

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        game.spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        game.spriteBatch.begin();
        game.spriteBatch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        groundSprite.draw(game.spriteBatch);
        player.draw(game.spriteBatch);
        alligatorManager.draw(game.spriteBatch);
        game.spriteBatch.end();

        drawChargeMeter();
    }

    private void handleInput() {
        if (!acceptingInput) {
            if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                acceptingInput = true;
            }
            return;
        }
        player.handleInput(groundSprite.getHeight());
    }

    private void updateLogic(float delta) {
        player.update(delta, groundSprite.getHeight(), viewport.getWorldHeight());

        if (player.getY() > groundSprite.getHeight()) {
            alligatorManager.update(delta);

            if (alligatorManager.hasCollided(player)) {
                game.setScreen(new GameOverScreen(game, this));
            }
        }
    }

    private void drawChargeMeter() {
        float meterHeight = 0.2f;
        float meterWidth = player.getMaxCharge() / 2;
        float chargeToDisplay = player.getCurrentCharge() > 0 ? player.getCurrentCharge() : player.getSavedCharge();
        float chargeRatio = chargeToDisplay / player.getMaxCharge();

        game.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glLineWidth(2f);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(viewport.getWorldWidth() / 2f - meterWidth / 2f, viewport.getWorldHeight() - 0.5f, meterWidth, meterHeight);
        game.shapeRenderer.end();
        Gdx.gl.glLineWidth(1f);

        if (chargeRatio > 0) {
            game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            game.shapeRenderer.setColor(Color.GREEN);
            game.shapeRenderer.rect(viewport.getWorldWidth() / 2f - meterWidth / 2f, viewport.getWorldHeight() - 0.5f, meterWidth * chargeRatio, meterHeight);
            game.shapeRenderer.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        groundTexture.dispose();
        frogTexture.dispose();
        alligatorManager.dispose();
    }

    @Override
    public void show() {
        if (screenToDispose != null) {
            screenToDispose.dispose();
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
