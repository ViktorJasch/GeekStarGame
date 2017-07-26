package com.star.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.game.Assets;
import com.star.game.screens.GameScreen;
import com.star.game.MyGameInputProcessor;
import com.star.game.Utils;

import static java.lang.Math.*;

/**
 * Created by viktor on 05.07.2017.
 */

public class Hero extends com.star.game.model.AbstractShip {
    Joystick joystick;
    public int score;
    public int money;
    public int lifes;
    TextureAtlas.AtlasRegion hpBarPack;
    TextureRegion redBar;
    TextureRegion greenBar;


    public Hero() {
        super(Assets.getInstance().mainAtlas.findRegion("ship"), new Vector2(640, 360), new Vector2(0, 0), 3.14f, 2000, 32, 200, 400, 0.125f);
        hpBarPack = Assets.getInstance().mainAtlas.findRegion("hpBar");
        redBar = new TextureRegion(hpBarPack, 0, 32, 224, 32);
        greenBar = new TextureRegion(hpBarPack, 0, 0, 224, 32);
        hitArea = new Circle(position.x, position.y, 25);
        lifes = 3;
        isItBot = false;
        joystick = new Joystick();
    }

    public void render(SpriteBatch batch) {
//        if (reddish > 0) {
//            batch.setColor(1, 1 - reddish, 1 - reddish, 1);
//        }
        batch.draw(texture, position.x - radius, position.y - radius, radius, radius, radius * 2, radius * 2, 1, 1, (float) toDegrees(angle));
//        batch.setColor(1, 1, 1, 1);
    }

    public void update(float dt) {
        super.update(dt);
        if (GameScreen.isAndroid) {
            joystick.update();
            MyGameInputProcessor mgip = (MyGameInputProcessor) Gdx.input.getInputProcessor();
            if (mgip.isTouchedInArea(1280 - 256 - 20, 20, 256, 256) > -1) {
                tryToFire(dt);
            }
            if (joystick.touched) {
                angle = Utils.rotateTo(angle, joystick.getAngle(), rotationSpeed, dt);
                if (currentEnginePower == 0) {
                    currentEnginePower = lowEnginePower;
                }
                currentEnginePower += joystick.power() * dt;
                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
                velocity.add((float) (currentEnginePower * cos(angle) * dt), (float) (currentEnginePower * sin(angle) * dt));
            } else {
                currentEnginePower = 0;
            }
        }
        if (!GameScreen.isAndroid) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                currentEnginePower = lowEnginePower;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                currentEnginePower += 100 * dt;
                if (currentEnginePower > maxEnginePower) currentEnginePower = maxEnginePower;
            } else {
                currentEnginePower = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                angle += rotationSpeed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                angle -= rotationSpeed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.L)) {
                tryToFire(dt);
            }
        }
        float size = velocity.len() / 100.0f;
        ParticleEmitter.getInstance().setupParticle(position.x - (float)cos(angle) * 24f, position.y - (float)sin(angle) * 24f, (float)(Math.random() - 0.5) * 30, (float)(Math.random() - 0.5) * 30, 0.5f, size, 0.2f, 1, 1, 1, 1, 0, 0, 1, 0f);
        if(reddish > 0.8f) {
            ParticleEmitter.getInstance().setupParticle(position.x, position.y , (float)(Math.random() - 0.5) * 250, (float)(Math.random() - 0.5) * 250, 0.8f, 1.8f, 1.4f, 1, 0, 0, 1, 1, 1, 0, 0f);
        }
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font, float x, float y) {
        font.draw(batch, "SCORE: " + score, x + 20, y);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(redBar, x, y - 60, 224, 32);
        batch.draw(greenBar, x, y - 60, 224 * ((float) hp / hpMax), 32);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        if (reddish > 0) {
            batch.setColor(1, 0, 0, reddish);
            batch.draw(redBar, x + (float) (Math.random() - 0.5f) * reddish * 20, y - 60 + (float) (Math.random() - 0.5f) * reddish * 20, 224, 32);
            batch.draw(greenBar, x + (float) (Math.random() - 0.5f) * reddish * 20, y - 60 + (float) (Math.random() - 0.5f) * reddish * 20, 224 * ((float) hp / hpMax), 32);
            batch.setColor(1, 1, 1, 1);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        font.draw(batch, "x" + lifes, x + 228, y - 32);
        joystick.render(batch);
    }

    @Override
    public boolean takeDamage(int dmg) {
        hp -= dmg;
        reddish += 0.1f;
        if (reddish > 1) {
            reddish = 1;
        }
        if (hp <= 0) {
            lifes--;
            hp = hpMax;
            return true;
        }
        return false;
    }


    public void fire() {
        BulletEmitter.getInstance().setupBullet(isItBot, position.x + (float) Math.cos(angle) * 24, position.y + (float) Math.sin(angle) * 24, angle);
        laserSound.play();
    }
}
