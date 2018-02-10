package ru.geekbrains.stargame.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

public class EnemyShip extends Ship{
//    public EnemyShip(TextureRegion region, int rows, int cols, int frames) {
//        super(region, rows, cols, frames);
//    }

    private static final float SHIP_HEIGHT = 0.15f;
    private static final float TOP_MARGIN = 0.2f;
    private static final int INVALID_POINTER = -1;

    private final Vector2 v0 = new Vector2(0.0f, -0.1f);
//    private final Vector2 left = new Vector2(-1, 0);


    private boolean pressedLeft;
    private boolean pressedRight;

    private Sound sound;


    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;
private Rect worldBounds;

    public EnemyShip(TextureAtlas atlas, BulletPool bulletPool, Rect worldBounds) {
        super(atlas.findRegion(randomTexture()), 1, 1, 1);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("laser1");
        this.bulletHeight = 0.02f;
        this.bulletV.set(0, -0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.5f;
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        this.worldBounds=worldBounds;
        super.resize(worldBounds);
        setBottom(worldBounds.getTop());
        setLeft(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()-getWidth()));



    }

    private static String randomTexture(){
        return "enemy"+((int)(Math.random()*6)+1);
    }


    @Override
    public void update(float delta) {

        this.pos.mulAdd(v0, delta);
        if (isOutside(worldBounds)){this.setDestroyed(true);}
        reloadTimer += delta;
        animationTimer += delta;
//        System.out.println("animationtimer: "+animationTimer);

        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
            sound.play(0.9f);
        }
//        if (getRight() > worldBounds.getRight()) {
//            setRight(worldBounds.getRight());
//            stop();
//        }
//        if (getLeft() < worldBounds.getLeft()) {
//            setLeft(worldBounds.getLeft());
//            stop();
//        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setTop(worldBounds.getTop() + TOP_MARGIN);
    }

//    public void keyDown(int keycode) {
//        switch (keycode) {
//            case Input.Keys.A:
//            case Input.Keys.LEFT:
//                pressedLeft = true;
//                moveLeft();
//                break;
//            case Input.Keys.D:
//            case Input.Keys.RIGHT:
//                pressedRight = true;
//                moveRight();
//                break;
//        }
//    }

//    public void keyUp(int keycode) {
//        switch (keycode) {
//            case Input.Keys.A:
//            case Input.Keys.LEFT:
//                pressedLeft = false;
//                if (pressedRight) {
//                    moveRight();
//                } else {
//                    stop();
//                }
//                break;
//            case Input.Keys.D:
//            case Input.Keys.RIGHT:
//                pressedRight = false;
//                if (pressedLeft) {
//                    moveLeft();
//                } else {
//                    stop();
//                }
//                break;
//            case Input.Keys.UP:
//                shoot();
//                break;
//        }
//    }

//    @Override
//    public void touchDown(Vector2 touch, int pointer) {
//        if (worldBounds.pos.x > touch.x) {
//            if (leftPointer != INVALID_POINTER) return;
//            leftPointer = pointer;
//            moveLeft();
//        } else {
//            if (rightPointer != INVALID_POINTER) return;
//            rightPointer = pointer;
//            moveRight();
//        }
//    }

//    @Override
//    public void touchUp(Vector2 touch, int pointer) {
//        if (pointer == leftPointer) {
//            leftPointer = INVALID_POINTER;
//            if (rightPointer != INVALID_POINTER) moveRight();
//            else stop();
//        } else if (pointer == rightPointer) {
//            rightPointer = INVALID_POINTER;
//            if (leftPointer != INVALID_POINTER) moveLeft();
//            else stop();
//        }
//    }

    private void moveRight() {
        v.set(v0);
//        if (frame < 10) {
//            frame += 4;
//        }
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
//        if (frame > 10) {
//            frame -= 4;
//        }
    }

    private void stop() {
        v.setZero();
    }

    public Vector2 getV() {
        return v;
    }

    public void set(
            TextureAtlas atlas, BulletPool bulletPool, Rect worldBounds
    ) {
//        super(atlas.findRegion(randomTexture()), 1, 1, 1);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletPool = bulletPool;
        this.bulletRegion = atlas.findRegion("laser1");
        this.bulletHeight = 0.02f;
        this.bulletV.set(0, -0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.5f;
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        this.worldBounds=worldBounds;
        super.resize(worldBounds);
        setBottom(worldBounds.getTop());
        setLeft(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()-getWidth()));
    }
}
