package com.star.game.model;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.star.game.Assets;

/**
 * Created by viktor on 03.07.2017.
 */
public class ParticleEmitter {
    private static ParticleEmitter ourInstance = new ParticleEmitter();

    public static ParticleEmitter getInstance() {
        return ourInstance;
    }

    final Array<Particle> activeParticles = new Array<Particle>();

    final Pool<Particle> particlesPool = new Pool<Particle>() {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };

    TextureRegion oneParticle;

    private ParticleEmitter() {
        this.oneParticle = new TextureRegion(Assets.getInstance().mainAtlas.findRegion("particle"), 0, 0, 16, 16);
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (Particle o : activeParticles) {
            float t = o.time / o.timeMax;
            float scale = lerp(o.size1, o.size2, t);
            batch.setColor(lerp(o.r1, o.r2, t), lerp(o.g1, o.g2, t), lerp(o.b1, o.b2, t), lerp(o.a1, o.a2, t));
            batch.draw(oneParticle, o.position.x - 8, o.position.y - 8, 8, 8, 16, 16, scale, scale, 0);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (Particle o : activeParticles) {
            float t = o.time / o.timeMax;
            float scale = lerp(o.size1, o.size2, t);
            batch.setColor(lerp(o.r1, o.r2, t), lerp(o.g1, o.g2, t), lerp(o.b1, o.b2, t), lerp(o.a1, o.a2, t));
            batch.draw(oneParticle, o.position.x - 8, o.position.y - 8, 8, 8, 16, 16, scale, scale, 0);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setupParticle(float x, float y, float vx, float vy, float timeMax, float size1, float size2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        Particle item = particlesPool.obtain();
        item.init(x, y, vx, vy, timeMax, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
        activeParticles.add(item);
    }

    public void update(float dt) {
        Particle item;
        int len = activeParticles.size;
        for (int i = len; --i >= 0; ) {
            item = activeParticles.get(i);
            item.update(dt);
            if (!item.active) {
                activeParticles.removeIndex(i);
                particlesPool.free(item);
            }
        }
    }

    public float lerp(float value1, float value2, float point) {
        return value1 + (value2 - value1) * point;
    }
}
