package com.OvenStudios.BasicFlappy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Alligator extends Sprite {
    public static final float WIDTH = 1f;
    public static final float HEIGHT = 5f;

    public Alligator(Texture texture) {
        super(texture);
        setSize(WIDTH, HEIGHT);
    }
}

