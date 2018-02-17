package ru.geekbrains.stargame.ship;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.explosion.ExplosionPool;

public class MainShip extends Ship {

    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;

    private final Vector2 v0 = new Vector2(0.5f, 0.0f);
    private final Vector2 left = new Vector2(-1, 0);


    private boolean pressedLeft;
    private boolean pressedRight;
    private float animationTimer=0f;



    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;


    public MainShip(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound laserSound) {
        super(atlas.findRegion("myship"), 3, 8, 22, bulletPool, explosionPool, worldBounds, laserSound);
        setHeightProportion(SHIP_HEIGHT);
        this.bulletRegion = atlas.findRegion("bulletgreen");
    }

    public void setToNewGame() {
        pos.x = worldBounds.pos.x;
        this.bulletHeight = 0.01f;
        this.bulletV.set(0, 0.5f);
        this.bulletDamage = 1;
        this.reloadInterval = 0.2f;
frame=11;
        hp = 100;
        setDestroyed(false);
    }

//    @Override
//    public void update(float delta) {
//        super.update(delta);
//        pos.mulAdd(v, delta);
//        reloadTimer += delta;
//        if (reloadTimer >= reloadInterval) {
//            reloadTimer = 0f;
//            shoot();
//        }
//        if (getRight() > worldBounds.getRight()) {
//            setRight(worldBounds.getRight());
//            stop();
//        }
//        if (getLeft() < worldBounds.getLeft()) {
//            setLeft(worldBounds.getLeft());
//            stop();
//        }
//    }

        @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        reloadTimer += delta;
        animationTimer += delta;
//        System.out.println("animationtimer: "+animationTimer);
        if (animationTimer > 0.1f) {
            if ((left.x * v.x + left.y * v.y) > 0) {
                if (frame > 1) {
                    frame -= 2;
                    if(frame<0){frame+=2;}
                }
            } else if ((left.x * v.x + left.y * v.y) < 0) {
                if (frame < 20) {
                    frame += 2;
                    if(frame>21){frame+=2;}
                }
            } else {
                if (frame < 10) {
                    frame += 2;
                    if(frame>21){frame-=2;}
                }
                if (frame > 10) {
                    frame -= 2;
                    if(frame<0){frame-=2;}
                }
            }
            animationTimer = 0f;
        }

        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
//            sound.play(0.01f);
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    public void keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
    }

    public void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        if (worldBounds.pos.x > touch.x) {
            if (leftPointer != INVALID_POINTER) return;
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) return;
            rightPointer = pointer;
            moveRight();
        }
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) moveRight(); else stop();
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) moveLeft(); else stop();
        }
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
        );
    }


    private void moveRight() {
        v.set(v0);
        if (frame < 10) {
            frame += 4;
        }
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
        if (frame > 10) {
            frame -= 4;}
    }

    private void stop() {
        v.setZero();
    }

    public Vector2 getV() {
        return v;
    }
}
