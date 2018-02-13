package ru.geekbrains.stargame.particle;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.pool.SpritesPool;

public class ParticlePool extends SpritesPool<Particle>{
    private TextureRegion particleTexture;
    private Rect worldBounds;

    public void setWorldBounds(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    public ParticlePool(TextureAtlas atlas) {

        this.particleTexture = atlas.findRegion("star");

    }

    @Override
    protected Particle newObject() {
        return new Particle();
    }

    public void setup(float x, float y, float vx, float vy, float duration, float size1, float size2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        Particle item = obtain();
        item.init(x, y, vx, vy, duration, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2, worldBounds);
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < activeObjects.size(); i++) {
            Particle o = activeObjects.get(i);
            float t = o.getTime() / o.getDuration();
            float scale = lerp(o.getSize1(), o.getSize2(), t);
            if(Math.random() < 0.04) {
                scale *= 2.0f;
            }
            batch.setColor(lerp(o.getR1(), o.getR2(), t), lerp(o.getG1(), o.getG2(), t), lerp(o.getB1(), o.getB2(), t), lerp(o.getA1(), o.getA2(), t));
            batch.draw(particleTexture, o.getPosition().x - 8, o.getPosition().y - 8, 8, 8, 16, 16, scale, scale, 0);
        }
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < activeObjects.size(); i++) {
            Particle o = activeObjects.get(i);
            float t = o.getTime() / o.getDuration();
            float scale = lerp(o.getSize1(), o.getSize2(), t);
            if(Math.random() < 0.04) {
                scale *= 2.0f;
            }
            batch.setColor(lerp(o.getR1(), o.getR2(), t), lerp(o.getG1(), o.getG2(), t), lerp(o.getB1(), o.getB2(), t), lerp(o.getA1(), o.getA2(), t));
            batch.draw(particleTexture, o.getPosition().x - 8, o.getPosition().y - 8, 8, 8, 16, 16, scale, scale, 0);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        for (int i = 0; i < activeObjects.size(); i++) {
            activeObjects.get(i).update(dt);
        }
    }

    public void resize(Rect worldBounds){
        for (int i = 0; i < activeObjects.size(); i++) {
            activeObjects.get(i).resize(worldBounds);
        }
    }

    public float lerp(float value1, float value2, float point) {
        return value1 + (value2 - value1) * point;
    }
}
