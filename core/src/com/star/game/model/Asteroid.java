package com.star.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by viktor on 05.07.2017.
 */

public class Asteroid extends SpaceObject implements Pool.Poolable {
    float scl;

    public Asteroid(TextureAtlas.AtlasRegion texture) {
        super(texture, new Vector2(0, 0), new Vector2(0, 0), 0, 0, 0);
    }

    public void init(float x, float y, float scl, int hpMax) {
        this.position.set(x, y);
        this.velocity.set((float) (Math.random() - 0.5) * 200, (float) (Math.random() - 0.5) * 200);
        this.scl = scl;
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.radius = 128;
        this.hitArea.radius = 120 * scl;
        this.hitArea.x = x;
        this.hitArea.y = y;
        this.rotationSpeed = (float)(Math.random() - 0.5f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - radius, position.y - radius, radius, radius, radius * 2, radius * 2, scl, scl, (float)Math.toDegrees(angle));
    }

    @Override
    public void reset() {
        hp = 0;
        position.set(0, 0);
    }

    public void update(float dt) {
        super.update(dt);
        this.position.mulAdd(velocity, dt);
        this.angle += this.rotationSpeed * dt;
    }
}
