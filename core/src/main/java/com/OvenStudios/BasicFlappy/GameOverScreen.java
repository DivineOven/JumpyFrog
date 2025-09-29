package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameOverScreen implements Screen {
    private final Main game;
    private final Screen screenToDispose;
    private final FitViewport viewport;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;

    public GameOverScreen(final Main game, Screen screenToDispose) {
        this.game = game;
        this.screenToDispose = screenToDispose;
        viewport = new FitViewport(8, 5);
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game, this));
            return;
        }

        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        game.spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        game.spriteBatch.begin();
        String text = "Game Over!\nPress Space to Restart";
        glyphLayout.setText(font, text, Color.WHITE, 0, Align.center, false);
        float x = (viewport.getWorldWidth() - glyphLayout.width) / 2;
        float y = (viewport.getWorldHeight() + glyphLayout.height) / 2;
        font.draw(game.spriteBatch, glyphLayout, x, y);
        game.spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
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
