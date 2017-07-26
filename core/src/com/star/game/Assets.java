package com.star.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by viktor on 08.07.2017.
 */

public class Assets {
    private static final Assets ourInstance = new Assets();

    public static Assets getInstance() {
        return ourInstance;
    }

    public AssetManager assetManager;

    public TextureAtlas mainAtlas;

    private Assets() {
        assetManager = new AssetManager();
    }

    public void loadAssets(com.star.game.screens.ScreenManager.ScreenType type) {
        switch (type) {
            case MENU:
                assetManager.load("my.pack", TextureAtlas.class);
                assetManager.load("bg.png", Texture.class);
                assetManager.finishLoading();
                mainAtlas = assetManager.get("my.pack", TextureAtlas.class);
                mainAtlas.getTextures().first().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
            case GAME:
                assetManager.load("font.fnt", BitmapFont.class);
                assetManager.load("my.pack", TextureAtlas.class);
                assetManager.load("bg.png", Texture.class);
                assetManager.load("powerUps.png", Texture.class);
                assetManager.load("music.mp3", Music.class);
                assetManager.load("laser.wav", Sound.class);
                assetManager.finishLoading();
                mainAtlas = assetManager.get("my.pack", TextureAtlas.class);
                mainAtlas.getTextures().first().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                break;
        }
    }
}
