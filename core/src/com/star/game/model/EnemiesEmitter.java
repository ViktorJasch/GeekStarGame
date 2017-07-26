package com.star.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.star.game.Assets;

/**
 * Created by viktor on 05.07.2017.
 */

public class EnemiesEmitter {
    private static final EnemiesEmitter ourInstance = new EnemiesEmitter();

    public static EnemiesEmitter getInstance() {
        return ourInstance;
    }

    public final Array<Asteroid> activeAsteroids = new Array<Asteroid>();
    public final Array<Bot> activeBots = new Array<com.star.game.model.Bot>();
    public final Pool<Asteroid> asteroidsPool = new Pool<com.star.game.model.Asteroid>() {
        @Override
        protected com.star.game.model.Asteroid newObject() {
            return new Asteroid(asteroidTexture);
        }
    };

    final Pool<Bot> botsPool = new Pool<com.star.game.model.Bot>() {
        @Override
        protected com.star.game.model.Bot newObject() {
            return new Bot(botTexture);
        }
    };

    TextureAtlas.AtlasRegion asteroidTexture;
    TextureAtlas.AtlasRegion botTexture;

    private EnemiesEmitter() {
        asteroidTexture = Assets.getInstance().mainAtlas.findRegion("asteroid");
        botTexture = Assets.getInstance().mainAtlas.findRegion("bot");
    }

    public void reset() {
        activeAsteroids.clear();
        activeBots.clear();
        botsPool.clear();
        asteroidsPool.clear();
        asteroidTexture = Assets.getInstance().mainAtlas.findRegion("asteroid");
        botTexture = Assets.getInstance().mainAtlas.findRegion("bot");
    }

    public void render(SpriteBatch batch) {
        Asteroid item;
        int len = activeAsteroids.size;
        for (int i = len; --i >= 0; ) {
            item = activeAsteroids.get(i);
            item.render(batch);
        }

        Bot bot;
        len = activeBots.size;
        for (int i = len; --i >= 0; ) {
            bot = activeBots.get(i);
            bot.render(batch);
        }
    }

    public void update(float dt) {
        for (Asteroid o : activeAsteroids) {
            o.update(dt);
        }
        for (Bot o : activeBots) {
            o.update(dt);
        }
        Asteroid asteroid;
        int len = activeAsteroids.size;
        for (int i = len; --i >= 0; ) {
            asteroid = activeAsteroids.get(i);
            if (asteroid.hp <= 0) {
                if (asteroid.scl > 0.21f) {
                    for (int q = 0; q < 3; q++) {
                        setupAsteroid(asteroid.position.x, asteroid.position.y, asteroid.scl - 0.2f, (int) (asteroid.hpMax * 0.8f));
                    }
                }
                activeAsteroids.removeIndex(i);
                asteroidsPool.free(asteroid);
            }
        }

        Bot bot;
        len = activeBots.size;
        for (int i = len; --i >= 0; ) {
            bot = activeBots.get(i);
            if (bot.hp <= 0) {
                activeBots.removeIndex(i);
                botsPool.free(bot);
            }
        }
    }

    public void setupAsteroid(float x, float y, float scl, int hpMax) {
        Asteroid item = asteroidsPool.obtain();
        item.init(x, y, scl, hpMax);
        activeAsteroids.add(item);
    }

    public void setupBot(Hero target, float x, float y, int hpMax, float activeTime) {
        Bot item = botsPool.obtain();
        item.init(target, x, y, hpMax, activeTime);
        activeBots.add(item);
    }
}
