package ru.geekbrains.stargame.ship;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.pool.SpritesPool;
import ru.geekbrains.stargame.explosion.Explosion;

public class ShipPool extends SpritesPool<EnemyShip> {
    private  TextureAtlas atlas;
    private BulletPool bulletPool;
    private Rect worldBounds;

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;

    }

    public ShipPool(TextureAtlas atlas, BulletPool bulletPool) {
        this.atlas = atlas;
        this.bulletPool=bulletPool;
//        this.worldBounds=worldBounds;
    }


    protected EnemyShip newObject() {
        return new EnemyShip(atlas,bulletPool,worldBounds);
    }
}
