package com.star.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.star.game.Assets;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by viktor on 05.07.2017.
 */

public class BulletEmitter {
    private static final BulletEmitter ourInstance = new BulletEmitter();

    public static BulletEmitter getInstance() {
        return ourInstance;
    }

    public final Array<Bullet> activeBullets = new Array<com.star.game.model.Bullet>();

    final Pool<Bullet> bulletsPool = new Pool<com.star.game.model.Bullet>(256, 8192) {
        @Override
        protected Bullet newObject() {
            return new Bullet();
        }
    };

    TextureRegion texture;

    public void reset() {
        bulletsPool.clear();
        activeBullets.clear();
        texture = Assets.getInstance().assetManager.get("my.pack", TextureAtlas.class).findRegion("bullet");
    }

    private BulletEmitter() {
        texture = Assets.getInstance().assetManager.get("my.pack", TextureAtlas.class).findRegion("bullet");
    }

    public void update(float dt) {
        Bullet bullet;
        int len = activeBullets.size;
        for (int i = len; --i >= 0; ) {
            bullet = activeBullets.get(i);
            bullet.update(dt);
            if(!bullet.active) {
                activeBullets.removeIndex(i);
                bulletsPool.free(bullet);
            }
        }
    }

    public void render(SpriteBatch batch) {
        Bullet bullet;
        int len = activeBullets.size;
        for (int i = len; --i >= 0; ) {
            bullet = activeBullets.get(i);
            batch.draw(texture, bullet.position.x - 16, bullet.position.y - 16);
        }
    }

    public void setupBullet(boolean isItBot, Vector2 position, float angle) {
        setupBullet(isItBot, position.x, position.y, angle);
    }

    public void setupBullet(boolean isItBot, float x, float y, float angle) {
        Bullet item = bulletsPool.obtain();
        item.setup(isItBot, x, y, 400 * (float) cos(angle), 400 * (float) sin(angle));
        activeBullets.add(item);
    }
}
