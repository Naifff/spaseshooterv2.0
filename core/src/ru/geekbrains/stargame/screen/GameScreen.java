package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.Background;
import ru.geekbrains.stargame.bullet.Bullet;
import ru.geekbrains.stargame.bullet.BulletPool;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;
import ru.geekbrains.stargame.explosion.Explosion;
import ru.geekbrains.stargame.explosion.ExplosionPool;
import ru.geekbrains.stargame.particle.ParticlePool;
import ru.geekbrains.stargame.ship.EnemyShip;
import ru.geekbrains.stargame.ship.MainShip;
import ru.geekbrains.stargame.ship.ShipPool;
import ru.geekbrains.stargame.star.TrackingStar;

public class GameScreen extends Base2DScreen {

    private static final int STAR_COUNT = 56;
    private static final float STAR_HEIGHT = 0.01f;

    private Texture backgroundTexture;
    private Background background;

    private TextureAtlas atlas;

    private MainShip mainShip;

    private TrackingStar star[];

    private final BulletPool bulletPool = new BulletPool();
    private ExplosionPool explosionPool;
    private ShipPool shipPool;
    private float randomShipSpawn=0;

    private Sound soundExplosion;
    private Music music;
    public Rect worldBounds;

    private ParticlePool particlePool;
    private Bullet tempBullet;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        music  = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        soundExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        backgroundTexture = new Texture("textures/bg.png");
        background = new Background(new TextureRegion(backgroundTexture));

        atlas = new TextureAtlas("textures/mainAtlas.tpack");

        mainShip = new MainShip(atlas, bulletPool);

        star = new TrackingStar[STAR_COUNT];
        for (int i = 0; i < star.length; i++) {
            star[i] = new TrackingStar(atlas, Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.5f, -0.1f), STAR_HEIGHT, mainShip.getV());
        }
        this.explosionPool = new ExplosionPool(atlas, soundExplosion);
        shipPool=new ShipPool(atlas,bulletPool);
        particlePool = new ParticlePool(atlas);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        deleteAllDestroyed();
        update(delta);
        draw();
    }

    public void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        shipPool.freeAllDestroyedActiveObjects();
    }

    public void update(float delta) {
        for (int i = 0; i < star.length; i++) {
            star[i].update(delta);
        }
        bulletPool.updateActiveObjects(delta);
       tempBullet=bulletPool.collision(mainShip);
       if(tempBullet!=null){
           System.out.println("bam!");
//           Explosion explosion = explosionPool.obtain();
//        explosion.set(0.1f, tempBullet.pos);
           particlePool.setup(tempBullet.getLeft(), tempBullet.getBottom(), 0, -0.00000001f, 22.1f*delta, 0.005f, 0.00004f, 1, 0.2f, 0, 1, 1, 1f, 0, 0.0f);
           tempBullet.isDestroyed();
       }
        explosionPool.updateActiveObjects(delta);
        mainShip.update(delta);
        randomShipSpawn+=delta;

        if(randomShipSpawn>8){
           EnemyShip enemyShip= shipPool.obtain();
           enemyShip.set(atlas,bulletPool,worldBounds);
            randomShipSpawn=0f;
        }
        shipPool.updateActiveObjects(delta);
//        particlePool.setup(mainShip.getLeft ()+mainShip.getHalfWidth()/2, mainShip.getBottom ()+mainShip.getHalfHeight(), 0, -0.2f, 22.1f, 0.005f, 0.00004f, 1, 0.2f, 0, 1, 1, 1f, 0, 0.0f);
//        particlePool.setup(mainShip.getRight ()-mainShip.getHalfWidth()/2, mainShip.getBottom ()+mainShip.getHalfHeight(), 0, -0.2f, 22.1f, 0.005f, 0.00004f, 1, 0.2f, 0, 1, 1, 1f, 0, 0.0f);
        particlePool.update(delta);
        particlePool.freeAllDestroyedActiveObjects();

    }

    public void draw() {
        Gdx.gl.glClearColor(0.7f, 0.3f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < star.length; i++) {
            star[i].draw(batch);
        }particlePool.render(batch);
        mainShip.draw(batch);

        bulletPool.drawActiveObjects(batch);
        explosionPool.drawActiveObjects(batch);
        shipPool.drawActiveObjects(batch);

        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds=worldBounds;
        background.resize(worldBounds);
        for (int i = 0; i < star.length; i++) {
            star[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
        shipPool.setWorldBounds(worldBounds);
        particlePool.resize(worldBounds);


    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
        atlas.dispose();
        bulletPool.dispose();
        explosionPool.dispose();
        soundExplosion.dispose();
        shipPool.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        mainShip.touchDown(touch, pointer);

//        Explosion explosion = explosionPool.obtain();
//        explosion.set(0.1f, touch);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        mainShip.touchUp(touch, pointer);
    }

    private boolean colision(MainShip ship, Bullet bullet){
        return ship.isMe(bullet.pos);
    }
}
