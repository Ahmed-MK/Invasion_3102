package com.oop.platformer.GameObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends GameObjects{

    public Player(World world){
        super(world);
        this.define();
    }

    @Override
    public void define() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32, 90);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fdef.shape = shape;
        b2body.createFixture(fdef);

    }
}