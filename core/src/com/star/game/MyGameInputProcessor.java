package com.star.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by viktor on 15.07.2017.
 */

public class MyGameInputProcessor implements InputProcessor {
    class TouchInfo {
        int x;
        int y;
        boolean touched;
    }

    HashMap<Integer, TouchInfo> map = new HashMap<Integer, TouchInfo>();

    public MyGameInputProcessor() {
        for (int i = 0; i < 5; i++) {
            map.put(i, new TouchInfo());
        }
    }

    public void clear() {
        for (int i = 0; i < 5; i++) {
            map.put(i, new TouchInfo());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        map.get(pointer).x = (int) com.star.game.screens.ScreenManager.getInstance().getViewport().unproject(new Vector2(screenX, 0)).x;
        map.get(pointer).y = (int) com.star.game.screens.ScreenManager.getInstance().getViewport().unproject(new Vector2(0, screenY)).y;
        map.get(pointer).touched = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        map.get(pointer).x = 0;
        map.get(pointer).y = 0;
        map.get(pointer).touched = false;
        return true;
    }

    public int isTouchedInArea(int x, int y, int w, int h) {
        for (Map.Entry<Integer, TouchInfo> o : map.entrySet()) {
            if (o.getValue().touched) {
                int id = o.getKey();
                TouchInfo t = o.getValue();
                if (t.x > x && t.x < x + w && t.y > y && t.y < y + h) {
                    return id;
                }
            }
        }
        return -1;
    }

    public boolean isTouched(int pointer) {
        return map.get(pointer).touched;
    }

    public int getY(int pointer) {
        return map.get(pointer).y;
    }

    public int getX(int pointer) {
        return map.get(pointer).x;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        map.get(pointer).touched = true;
        map.get(pointer).x = (int) com.star.game.screens.ScreenManager.getInstance().getViewport().unproject(new Vector2(screenX, 0)).x;
        map.get(pointer).y = (int) com.star.game.screens.ScreenManager.getInstance().getViewport().unproject(new Vector2(0, screenY)).y;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
