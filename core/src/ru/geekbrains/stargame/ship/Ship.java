package ru.geekbrains.stargame.ship;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.bullet.Bullet;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.explosion.Explosion;
import ru.geekbrains.stargame.explosion.ExplosionPool;

/**
 * Базовый класс для кораблей
 */

public abstract class Ship extends Sprite{
    private Vector2 tmpVector;

    private boolean boss=false;

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public boolean isBoss() {

        return boss;
    }

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;
    private float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    protected final Vector2 v = new Vector2(); // скорость корабля
    protected Rect worldBounds; // границы мира

    protected int hp; // жизни корабля

    protected ExplosionPool explosionPool;
    protected BulletPool bulletPool;
    protected TextureRegion bulletRegion;

    protected Sound shootSound;


    protected final Vector2 bulletV = new Vector2(); // скорость пули
    protected float bulletHeight; // высота пули
    protected  int bulletDamage; // урон

    protected float reloadInterval; // время перезарядки
    protected float reloadTimer; // таймер для стрельбы

    public Ship(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.shootSound = shootSound;
    }

    public Ship(TextureRegion region, int rows, int cols, int frames, BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound shootSound) {
        super(region, rows, cols, frames);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        this.shootSound = shootSound;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
//            frame = 0;
            if(frame%2!=0){frame -=1;
//            if (frame<0)frame=0;
//                System.out.println("frame= "+frame+"norm");
            }
        }
    }

    public void damage(int damage) {
//        frame = 1;
        if(frame%2==0){frame +=1;
//        if(frame>21)frame=21;
//            System.out.println("frame= "+frame+"damaged");
        }
        damageAnimateTimer = 0;
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
        if (hp == 0) {
            boom();
            setDestroyed(true);
        }
    }

//    public void damage(int damage, int frames) {
//        if(frames%2!=0){frame +=1;}
//        damageAnimateTimer = 0;
//        hp -= damage;
//        if (hp < 0) {
//            hp = 0;
//        }
//        if (hp == 0) {
//            boom();
//            setDestroyed(true);
//        }
//    }

    protected void shoot() {
        if (!boss){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, pos, bulletV, bulletHeight, worldBounds, bulletDamage);
        shootSound.play();}
        else{
            Bullet bullet = bulletPool.obtain();
            tmpVector=bulletV;
            tmpVector.x+= MathUtils.random(-0.1f,0);
            bullet.set(this, bulletRegion, pos, tmpVector, bulletHeight, worldBounds, bulletDamage);
            shootSound.play();
            Bullet bullet1 = bulletPool.obtain();

            tmpVector.x+= 0.05f;
            bullet1.set(this, bulletRegion, pos, tmpVector, bulletHeight, worldBounds, bulletDamage);
            shootSound.play();
            Bullet bullet2 = bulletPool.obtain();
            tmpVector.x+= 0.05f;
            bullet2.set(this, bulletRegion, pos, tmpVector, bulletHeight, worldBounds, bulletDamage);
            shootSound.play();
        }
    }

    public void boom() {
        hp = 0;
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public int getHp() {
        return hp;
    }
}
