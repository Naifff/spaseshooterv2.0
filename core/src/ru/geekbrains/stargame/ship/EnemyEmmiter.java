package ru.geekbrains.stargame.ship;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;
import ru.geekbrains.stargame.engine.utils.Regions;

public class EnemyEmmiter {

    private static final float ENEMY_SMALL_HEIGHT = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIGHT = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY = -0.3f;
    private static final int ENEMY_SMALL_BULLET_DAMAGE = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL = 3f;
    private static final int ENEMY_SMALL_HP = 1;

    private static final float ENEMY_MIDDLE_HEIGHT = 0.1f;
    private static final float ENEMY_MIDDLE_BULLET_HEIGHT = 0.02f;
    private static final float ENEMY_MIDDLE_BULLET_VY = -0.25f;
    private static final int ENEMY_MIDDLE_BULLET_DAMAGE = 5;
    private static final float ENEMY_MIDDLE_RELOAD_INTERVAL = 4f;
    private static final int ENEMY_MIDDLE_HP = 5;

    private static final float ENEMY_BIG_HEIGHT = 0.2f;
    private static final float ENEMY_BIG_BULLET_HEIGHT = 0.04f;
    private static final float ENEMY_BIG_BULLET_VY = -0.3f;
    private static final int ENEMY_BIG_BULLET_DAMAGE = 10;
    private static final float ENEMY_BIG_RELOAD_INTERVAL = 4f;
    private static final int ENEMY_BIG_HP = 20;

    private static final float ENEMY_BOSS_HEIGHT = 0.4f;
    private static final float ENEMY_BOSS_BULLET_HEIGHT = 0.09f;
    private static final float ENEMY_BOSS_BULLET_VY = -0.9f;
    private static final int ENEMY_BOSS_BULLET_DAMAGE = 20;
    private static final float ENEMY_BOSS_RELOAD_INTERVAL = 4f;
    private static final int ENEMY_BOSS_HP = 50;

    private final Vector2 enemySmallV = new Vector2(0f, -0.2f);
    private final Vector2 enemyMiddleV = new Vector2(0f, -0.03f);
    private final Vector2 enemyBigV = new Vector2(0f, -0.005f);
    private final Vector2 enemyBossV = new Vector2(0f, 0);

    private int countSmall=10;
    private int countMedium=5;
    private int countBig =2;

    private float generateTimer;
    private float generateInterval = 4f;

    private final EnemyPool enemyPool;
    private Rect worldBounds;

    private final TextureRegion[] enemySmallRegion;
    private final TextureRegion[] enemyMiddleRegion;
    private final TextureRegion[] enemyBigRegion;
    private final TextureRegion[] enemyBossRegion;

    private TextureRegion bulletRegion;

    private int stage;
    private int create=0;

    public EnemyEmmiter(EnemyPool enemyPool, Rect worldBounds, TextureAtlas atlas) {
        this.enemyPool = enemyPool;
        this.worldBounds = worldBounds;
        enemySmallRegion = Regions.split(atlas.findRegion("enemy1"), 1, 2, 2);
        enemyMiddleRegion = Regions.split(atlas.findRegion("enemy2"), 1, 2, 2);
        enemyBigRegion = Regions.split(atlas.findRegion("enemy3"), 1, 2, 2);
        enemyBossRegion = Regions.split(atlas.findRegion("enemy4"), 1, 2, 2);
        bulletRegion = atlas.findRegion("bulletred");
    }

    public void setToNewGame() {
        stage = 1;
        countSmall=10;
        countMedium=5;
        countBig =2;
        EnemyShip.isBossAlive=false;
    }

    public void generateEnemy(float delta, int frags) {
//           stage = frags / 17 + 1;
        generateTimer += delta;
        if (generateInterval <= generateTimer) {
            generateTimer = 0f;
            if(!EnemyShip.isBossAlive) {

                EnemyShip enemy = enemyPool.obtain();
                create = 1;
                for (; ; ) {



                    float type = (float) Math.random();
                    if (type < 0.7f & countSmall > 0) {
                        countSmall -= 1;
                        enemy.set(
                                enemySmallRegion,
                                enemySmallV,
                                bulletRegion,
                                ENEMY_SMALL_BULLET_HEIGHT,
                                ENEMY_SMALL_BULLET_VY,
                                ENEMY_SMALL_BULLET_DAMAGE * stage,
                                ENEMY_SMALL_RELOAD_INTERVAL,
                                ENEMY_SMALL_HEIGHT,
                                ENEMY_SMALL_HP * stage
                        );
                        enemy.setBoss(false);
                        create = 0;
                    } else if (type < 0.9f & countMedium > 0) {
                        countMedium -= 1;
                        enemy.set(
                                enemyMiddleRegion,
                                enemyMiddleV,
                                bulletRegion,
                                ENEMY_MIDDLE_BULLET_HEIGHT,
                                ENEMY_MIDDLE_BULLET_VY,
                                ENEMY_MIDDLE_BULLET_DAMAGE * stage,
                                ENEMY_MIDDLE_RELOAD_INTERVAL,
                                ENEMY_MIDDLE_HEIGHT,
                                ENEMY_MIDDLE_HP * stage
                        );
                        enemy.setBoss(false);
                        create = 0;
                    } else if (countBig > 0) {
                        countBig -= 1;
                        enemy.set(
                                enemyBigRegion,
                                enemyBigV,
                                bulletRegion,
                                ENEMY_BIG_BULLET_HEIGHT,
                                ENEMY_BIG_BULLET_VY,
                                ENEMY_BIG_BULLET_DAMAGE * stage,
                                ENEMY_BIG_RELOAD_INTERVAL,
                                ENEMY_BIG_HEIGHT,
                                ENEMY_BIG_HP * stage
                        );
                        enemy.setBoss(false);
                        create = 0;
                    }
                    if (countBig == 0 & countMedium == 0 & countSmall == 0) {
                        countSmall -= 1;
                        countMedium -= 1;
                        countBig -= 1;
                        enemy.set(
                                enemyBossRegion,
                                enemyBossV,
                                bulletRegion,
                                ENEMY_BOSS_BULLET_HEIGHT,
                                ENEMY_BOSS_BULLET_VY,
                                ENEMY_BOSS_BULLET_DAMAGE * stage,
                                ENEMY_BOSS_RELOAD_INTERVAL,
                                ENEMY_BOSS_HEIGHT,
                                ENEMY_BOSS_HP * stage
                        );
                        create = 0;
                        EnemyShip.isBossAlive = true;
                        enemy.setBoss(true);
                    }
                    if (EnemyShip.isBossAlive) {
                        break;
                    }
                    if (create == 0) {
                        break;
                    }


                }
                enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth());
                enemy.setBottom(worldBounds.getTop());
            }
        }
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public void nextLvl(){
        this.stage++;
        this.countSmall=10;
        this.countMedium=5;
        this.countBig =2;
    }
}
