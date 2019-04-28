package com.oop.platformer.util;

import com.badlogic.gdx.physics.box2d.*;
import com.oop.platformer.GameObjects.Bullet;
import com.oop.platformer.GameObjects.DroneEnemy;
import com.oop.platformer.GameObjects.Enemy;
import com.oop.platformer.GameObjects.Player;

public class CollisionHandler implements ContactListener {

    // Every time a collision happens in any of box2d world objects all of ContactListener methods trigger

    private LevelManager levelManager;

    public CollisionHandler(LevelManager levelManager){
        this.levelManager = levelManager;
    }

    @Override
    public void beginContact(Contact contact) {
//        System.out.println("collision occurred");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getUserData() instanceof Player && fb.getUserData() instanceof Enemy){
            levelManager.playerIsHit();
//            levelManager.enemyIsHit((DroneEnemy) fb.getUserData());
        }

        if(fa.getUserData() instanceof Enemy && fb.getUserData() instanceof Bullet){
            System.out.println("bullet");
//            levelManager.playerIsHit();
//            levelManager.enemyIsHit((DroneEnemy) fb.getUserData());
        }
        else{
            levelManager.destroyBullet(fb);
//            levelManager.playerIsHit();
//            levelManager.enemyIsHit((DroneEnemy) fb.getUserData());
        }

    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }


}
