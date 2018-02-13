package ru.geekbrains.stargame.bullet;

import ru.geekbrains.stargame.engine.pool.SpritesPool;
import ru.geekbrains.stargame.ship.MainShip;
import ru.geekbrains.stargame.ship.Ship;


public class BulletPool extends SpritesPool<Bullet> {
    private Bullet tmp=new Bullet();


    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    @Override
    protected void debugLog() {
//        System.out.println("BulletPool change active/free:" + activeObjects.size() + "/" + freeObjects.size());
    }

    public Bullet collision(MainShip ship){

        for (int i = 0; i <activeObjects.size() ; i++) {
            if (ship.isMe(activeObjects.get(i).pos)){
                tmp=activeObjects.get(i);
                activeObjects.get(i).setDestroyed(true);
                return tmp;
            }
        }
        return null;
    }
}
