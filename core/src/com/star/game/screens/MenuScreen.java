package com.star.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.star.game.Assets;
import com.star.game.Background;
import com.star.game.MyGameInputProcessor;

/**
 * Created by viktor on 15.07.2017.
 */

public class MenuScreen implements Screen {
    SpriteBatch batch;
    Background background;
    TextureAtlas.AtlasRegion startGameBtnTexture;
    TextureAtlas.AtlasRegion endGameBtnTexture;

    public MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        background = new Background();
        startGameBtnTexture = Assets.getInstance().mainAtlas.findRegion("btPlay");
        endGameBtnTexture = Assets.getInstance().mainAtlas.findRegion("btExit");
        MyGameInputProcessor mgip = (MyGameInputProcessor)Gdx.input.getInputProcessor();
        mgip.clear();
    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        background.render(batch);
        batch.draw(startGameBtnTexture, 50, 50, 256, 256);
        batch.draw(endGameBtnTexture, 1280 - 256 - 50, 50, 256, 256);
        batch.end();
    }

    public void update(float dt) {
        background.update(dt);
        MyGameInputProcessor mgip = (MyGameInputProcessor) Gdx.input.getInputProcessor();
        if (mgip.isTouchedInArea(50, 50, 256, 256) > -1) {
            ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.GAME);
        }
        if (mgip.isTouchedInArea(1280 - 256 - 50, 50, 256, 256) > -1) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().onResize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
