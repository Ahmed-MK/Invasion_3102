package com.oop.platformer.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.oop.platformer.GameClass;
import com.oop.platformer.util.Assets;

public class DroneEnemy extends Enemy{

    public DroneEnemy(World world, Vector2 spritePosition, Array<Vector2> path) {
        super(world, spritePosition, path);
    }
    @Override
    public void initSprite() {
        setBounds(0, 0, 35 / GameClass.PPM, 50 / GameClass.PPM);
        setRegion(Assets.instance.droneEnemyAssets.idleAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    public void updateSprite() {
        setRegion(Assets.instance.droneEnemyAssets.idleAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    public void setHealthPoints() {
        healthPoints = 3;
    }

    @Override
    public void define() {
//        super.define();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(spritePosition);
        body = world.createBody(bodyDef);
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(35 / (2f) / GameClass.PPM, 50 / (2f) / GameClass.PPM);
        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.density = 1f;
        fixtureDef.restitution = 1f;
//        fixtureDef.shape = bodyShape;
        CircleShape shape = new CircleShape();
        shape.setRadius(25 / GameClass.PPM);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }


}
