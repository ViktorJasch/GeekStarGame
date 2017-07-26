package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by viktor on 05.07.2017.
 */

public class Background {
    class Star {
        Vector2 position;
        Vector2 velocity;
        float scl;

        public Star() {
            position = new Vector2((float) Math.random() * 1280, (float) Math.random() * 720);
            velocity = new Vector2((float) (Math.random() - 0.5) * 5f, (float) (Math.random() - 0.5) * 5f);
            scl = 0.5f + (float) Math.random() / 4.0f;
        }

        public void update(float dt) {
            position.mulAdd(velocity, dt);
            position.y -= 80 * dt;
            float half = textureStar.getRegionWidth() * scl;
            if (position.x < -half) position.x = 1280 + half;
            if (position.x > 1280 + half) position.x = -half;
            if (position.y < -half) position.y = 720 + half;
            if (position.y > 720 + half) position.y = -half;
        }

        public void update(com.star.game.model.Hero hero, float dt) {
            position.mulAdd(velocity, dt);
            position.mulAdd(hero.velocity, -0.005f);
            float half = textureStar.getRegionWidth() * scl;
            if (position.x < -half) position.x = 1280 + half;
            if (position.x > 1280 + half) position.x = -half;
            if (position.y < -half) position.y = 720 + half;
            if (position.y > 720 + half) position.y = -half;
        }
    }

    Texture texture;
    TextureAtlas.AtlasRegion textureStar;
    Star[] stars;

    public Background() {
        texture = Assets.getInstance().assetManager.get("bg.png", Texture.class);
        textureStar = Assets.getInstance().assetManager.get("my.pack", TextureAtlas.class).findRegion("star16");

        stars = new Star[250];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, 0, 0);
        for (Star s : stars) {
            batch.draw(textureStar, s.position.x - 8, s.position.y - 8, 8, 8, 16, 16, s.scl, s.scl, 0);
        }
    }

    public void update(float dt) {
        for (Star s : stars) {
            s.update(dt);
        }
    }

    public void update(com.star.game.model.Hero hero, float dt) {
        for (Star s : stars) {
            s.update(hero, dt);
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
