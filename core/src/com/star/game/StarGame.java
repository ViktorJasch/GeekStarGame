package com.star.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StarGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        MyGameInputProcessor mgip = new MyGameInputProcessor();
        Gdx.input.setInputProcessor(mgip);
        com.star.game.screens.ScreenManager.getInstance().init(this);
        com.star.game.screens.ScreenManager.getInstance().switchScreen(com.star.game.screens.ScreenManager.ScreenType.MENU);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        com.star.game.screens.ScreenManager.getInstance().dispose();
    }
}
