package com.star.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.star.game.Assets;
import com.star.game.StarGame;

/**
 * Created by viktor on 15.07.2017.
 */

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME
    }

    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private Game game;

    private Viewport viewport;
    private Camera camera;

    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    public void init(Game game) {
        this.game = game;

        camera = new OrthographicCamera(1280, 720);
        viewport = new FitViewport(1280, 720, camera);
        viewport.update(1280, 720, true);
        viewport.apply();

        this.menuScreen = new MenuScreen(((StarGame)game).batch);
        this.gameScreen = new GameScreen(((StarGame)game).batch);
    }

    public Camera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void onResize(int width, int height) {
        viewport.update(width, height, true);
        viewport.apply();
    }

    public void switchScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Assets.getInstance().assetManager.clear();
        Assets.getInstance().assetManager.dispose();
        Assets.getInstance().assetManager = new AssetManager();
        if (screen != null) {
            screen.dispose();
        }
        switch (type) {
            case MENU:
                Assets.getInstance().loadAssets(ScreenType.MENU);
                game.setScreen(menuScreen);
                break;
            case GAME:
                Assets.getInstance().loadAssets(ScreenType.GAME);
                game.setScreen(gameScreen);
                break;
        }
    }

    private ScreenManager() {
    }

    public void dispose() {
    }
}
