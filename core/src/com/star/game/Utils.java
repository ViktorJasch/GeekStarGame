package com.star.game;

import static java.lang.Math.PI;
import static java.lang.Math.atan2;

/**
 * Created by viktor on 08.07.2017.
 */

public class Utils {
    public static float getAngle(float x1, float y1, float x2, float y2) {
        return (float) atan2(y2 - y1, x2 - x1);
    }

    public static float rotateTo(float from, float to, float rotationSpeed, float dt) {
        if (from > to) {
            if (from - to < PI) {
                from -= rotationSpeed * dt;
            } else {
                from += rotationSpeed * dt;
            }
        }
        if (from < to) {
            if (to - from < PI) {
                from += rotationSpeed * dt;
            } else {
                from -= rotationSpeed * dt;
            }
        }
        return from;
    }
}
