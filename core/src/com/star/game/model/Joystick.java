package com.star.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.star.game.Assets;
import com.star.game.MyGameInputProcessor;
import com.star.game.Utils;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

/**
 * Created by viktor on 19.07.2017.
 */

public class Joystick {
    TextureAtlas.AtlasRegion joystickTexture;
    TextureRegion back;
    TextureRegion stick;

    Rectangle rect;

    Vector2 vs;
    float joyCenterX, joyCenterY;
    boolean touched;
    int lastId;

    public Joystick() {
        joystickTexture = Assets.getInstance().mainAtlas.findRegion("joystick");
        back = new TextureRegion(joystickTexture, 0, 0, 200, 200);
        stick = new TextureRegion(joystickTexture, 0, 200, 50, 50);
        rect = new Rectangle(50, 50, 200, 200);
        joyCenterX = rect.x + rect.width / 2;
        joyCenterY = rect.y + rect.height / 2;
        vs = new Vector2(0, 0);
        lastId = -1;
    }

    public void render(SpriteBatch batch) {
        batch.draw(back, rect.x, rect.y);
        batch.draw(stick, rect.x + 100 - 25 + vs.x, rect.y + 100 - 25 + vs.y);
    }

    public float getAngle() {
        return Utils.getAngle(0, 0, vs.x, vs.y);
    }

    public float power() {
        Vector2 v = new Vector2(vs.x, vs.y);
        return v.len();
    }

    public void update() {
        touched = false;
        MyGameInputProcessor mgip = (MyGameInputProcessor) Gdx.input.getInputProcessor();
        if (lastId == -1) {
            lastId = mgip.isTouchedInArea((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
        }
        if (lastId > -1) {
            touched = true;
            float touchX = mgip.getX(lastId);
            float touchY = mgip.getY(lastId);
            vs.x = touchX - joyCenterX;
            vs.y = touchY - joyCenterY;
            if (vs.len() > 75) {
                vs.nor().scl(75);
            }
        }
        if (lastId > -1 && !mgip.isTouched(lastId)) {
            lastId = -1;
            vs.x = 0;
            vs.y = 0;
        }
    }
}
