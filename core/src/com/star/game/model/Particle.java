package com.star.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by viktor on 03.07.2017.
 */
public class Particle implements Pool.Poolable {
    Vector2 position;
    Vector2 velocity;
    float r1, g1, b1, a1;
    float r2, g2, b2, a2;

    boolean active;
    float time;
    float timeMax;

    float size1, size2;

    public Particle() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        size1 = 1.0f;
        size2 = 1.0f;
    }

    public void init(float x, float y, float vx, float vy, float timeMax, float size1, float size2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.r1 = r1;
        this.r2 = r2;
        this.g1 = g1;
        this.g2 = g2;
        this.b1 = b1;
        this.b2 = b2;
        this.a1 = a1;
        this.a2 = a2;
        this.time = 0.0f;
        this.timeMax = timeMax;
        this.size1 = size1;
        this.size2 = size2;
        this.active = true;
    }

    @Override
    public void reset() {
        active = false;
    }

    public void update(float dt) {
        time += dt;
        position.add(velocity.x * dt, velocity.y * dt);
        if (time > timeMax) {
            active = false;
        }
    }
}
