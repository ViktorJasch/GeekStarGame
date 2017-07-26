package com.star.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

/**
 * Created by viktor on 12.07.2017.
 */

public abstract class SpaceObject {
    TextureAtlas.AtlasRegion texture;
    public Vector2 position;
    public Vector2 velocity;
    public float angle;

    public int hp;
    public int hpMax;

    public float rotationSpeed;
    public float radius;

    public Circle hitArea;
    boolean screenSlider;

    public SpaceObject(TextureAtlas.AtlasRegion textureRegion, Vector2 position, Vector2 velocity, float rotationSpeed, int hpMax, int radius) {
        this.texture = textureRegion;
        this.position = position;
        this.velocity = velocity;
        this.rotationSpeed = rotationSpeed;
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.radius = radius;
        this.hitArea = new Circle(position.x, position.y, radius);
        this.screenSlider = true;
    }

    public abstract void render(SpriteBatch batch);

    public void update(float dt) {
        if (angle < -PI) angle += 2 * PI;
        if (angle > PI) angle -= 2 * PI;

        if (screenSlider) {
            if (position.y > 720 + radius) position.y = -radius;
            if (position.y < -radius) position.y = 720 + radius;
            if (position.x > 1280 + radius) position.x = -radius;
            if (position.x < -radius) position.x = 1280 + radius;
        }

        hitArea.x = position.x;
        hitArea.y = position.y;
    }

    public boolean takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            return true;
        }
        return false;
    }
}
