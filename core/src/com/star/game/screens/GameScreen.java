package com.star.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.star.game.Assets;
import com.star.game.Background;
import com.star.game.MyGameInputProcessor;
import com.star.game.model.*;
import com.star.game.model.ParticleEmitter;
import com.star.game.model.PowerUp;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by viktor on 15.07.2017.
 */

public class GameScreen implements Screen {
    public static boolean isAndroid = true;
    BitmapFont fnt;
    SpriteBatch batch;
    Background background;
    com.star.game.model.Hero hero;
    ArrayList<com.star.game.model.PowerUp> powerUps;
    TextureAtlas.AtlasRegion fireBtnRegion;
    final Array<SpaceObject> collisionList = new Array<SpaceObject>();
    Music music;

    private FrameBuffer fbo = null;
    private TextureRegion fboRegion = null;

    private ShaderProgram shaderProgram;

    float time;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        fnt = Assets.getInstance().assetManager.get("font.fnt", BitmapFont.class);
        fireBtnRegion = Assets.getInstance().mainAtlas.findRegion("btExit");
        background = new Background();
        powerUps = new ArrayList<PowerUp>();
        hero = new Hero();
        music = Assets.getInstance().assetManager.get("music.mp3", Music.class);
        EnemiesEmitter.getInstance().reset();
        BulletEmitter.getInstance().reset();
        for (int i = 0; i < 3; i++) {
            EnemiesEmitter.getInstance().setupAsteroid((float) Math.random() * 1280, (float) Math.random() * 720, 1.0f, 100);
        }
        for (int i = 0; i < 1; i++) {
            EnemiesEmitter.getInstance().setupBot(hero, (float) (1280 * Math.random()), (float) (720 * Math.random()), 50, 20);
        }
        MyGameInputProcessor mgip = (MyGameInputProcessor) Gdx.input.getInputProcessor();
        mgip.clear();
        music.play();
        music.setLooping(true);
        fbo = new FrameBuffer(Pixmap.Format.RGB565, 1280, 720, false);
        fboRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboRegion.flip(false, true);
        shaderProgram = new ShaderProgram(Gdx.files.internal("vertex.glsl").readString(), Gdx.files.internal("fragment.glsl").readString());
        //shaderProgram = new ShaderProgram("vertex.glsl", "fragment.glsl");
        if (!shaderProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());
    }

    @Override
    public void render(float delta) {
        update(delta);
        fbo.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        background.render(batch);
        hero.render(batch);
        EnemiesEmitter.getInstance().render(batch);
        BulletEmitter.getInstance().render(batch);
        for (PowerUp o : powerUps) {
            o.render(batch);
        }
        ParticleEmitter.getInstance().render(batch);
        hero.renderHUD(batch, fnt, 20, 700);
        batch.draw(fireBtnRegion, 1280 - 256 - 20, 20);
        batch.end();
        if (hero.lifes < 0) {
            ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.MENU);
        }
        fbo.end();
        batch.begin();
        batch.setShader(shaderProgram);
        int n = shaderProgram.getUniformLocation("reddish");
        shaderProgram.setUniformf(n, hero.reddish);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.draw(fboRegion, 0, 0, 1280, 720);
        batch.end();
        batch.setShader(null);
    }

    public void update(float dt) {
        time += dt;
        background.update(hero, dt);
        hero.update(dt);
        EnemiesEmitter.getInstance().update(dt);
        BulletEmitter.getInstance().update(dt);
        Iterator<PowerUp> iter = powerUps.iterator();
        while (iter.hasNext()) {
            PowerUp p = iter.next();
            p.update(dt);
            if (hero.hitArea.contains(p.position)) {
                switch (p.type) {
                    case 0:
                        hero.money += 10;
                        break;
                    case 1:
                        hero.money += 25;
                        break;
                    case 2:
                        hero.money += 50;
                        break;
                    case 3:
                        hero.hp = hero.hpMax;
                        break;
                }
                p.time = 10;
            }
            if (p.time > 5.0f) {
                iter.remove();
            }
        }
        ParticleEmitter.getInstance().update(dt);
        checkCollision();
    }

    @Override
    public void dispose() {
    }

    public void checkCollision() {
        collisionList.clear();
        collisionList.add(hero);
        for (Asteroid o : EnemiesEmitter.getInstance().activeAsteroids) {
            collisionList.add(o);
        }
        for (Bot o : EnemiesEmitter.getInstance().activeBots) {
            collisionList.add(o);
        }
        Vector2 vt = new Vector2(0, 0);
        for (int i = 0; i < collisionList.size; i++) {
            for (int j = i + 1; j < collisionList.size; j++) {
                SpaceObject a = collisionList.get(i);
                SpaceObject b = collisionList.get(j);
                if (a == b || (a instanceof Asteroid && b instanceof Asteroid)) continue;
                if (b.hitArea.overlaps(a.hitArea)) {
                    Vector2 acc = b.position.cpy().sub(a.position).nor();
                    Vector2 l = b.position.cpy().sub(a.position);
                    float sr = a.hitArea.radius + b.hitArea.radius;
                    float raznesti = (sr - l.len()) / 2;

                    b.position.mulAdd(acc, raznesti);
                    a.position.mulAdd(acc, -raznesti);

                    a.takeDamage(5);
                    b.takeDamage(5);

                    b.velocity.mulAdd(acc, 1);
                    a.velocity.mulAdd(acc, -1);
                }
            }
        }

        for (Bullet b : BulletEmitter.getInstance().activeBullets) {
            for (Asteroid a : EnemiesEmitter.getInstance().activeAsteroids) {
                if (a.hitArea.contains(b.position)) {
                    if (a.takeDamage(20)) {
                        hero.score += 10;
                        if (Math.random() < 0.1) {
                            powerUps.add(new PowerUp(a.position.x, a.position.y));
                        }
                    }
                    b.destroy();
                }
            }
            if (!b.isItBot) {
                for (Bot o : EnemiesEmitter.getInstance().activeBots) {
                    if (o.hitArea.contains(b.position)) {
                        if (o.takeDamage(20)) {
                            hero.score += 100;
                            if (Math.random() < 0.1) {
                                powerUps.add(new PowerUp(o.position.x, o.position.y));
                            }
                        }
                        b.destroy();
                    }
                }
            }
            if (b.isItBot) {
                if (hero.hitArea.contains(b.position)) {
                    hero.takeDamage(20);
                    b.destroy();
                }
            }
        }
    }


    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().onResize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
