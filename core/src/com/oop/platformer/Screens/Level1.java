package com.oop.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.oop.platformer.Constants;
import com.oop.platformer.GameClass;
import com.oop.platformer.GameObjects.DroneEnemy;
import com.oop.platformer.GameObjects.Player;
import com.oop.platformer.Scenes.Hud;
import com.oop.platformer.util.CollisionHandler;


public class Level1 implements Screen {

    private GameClass gameClass;

    private OrthographicCamera gameCam; //game camera instance to move with the player character

    private Viewport gamePort; //Manages a Camera and determines how world coordinates are mapped to and from the screen.

    private TiledMap map;//reference for the map itself

    private  Hud hud;
    private OrthogonalTiledMapRenderer renderer;

    private World world;

    // for rendering debugging
    private Box2DDebugRenderer floorDebugger;

    private Player player;
    private DroneEnemy droneEnemy;

    public Level1(GameClass gameClass){

        this.gameClass = gameClass;
        //setup camera and window
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(GameClass.V_WIDTH / GameClass.PPM, GameClass.V_HEIGHT / GameClass.PPM, gameCam);

        //Load Map
        //loads the level from assets
        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load(Constants.MAP);

        hud = new Hud(gameClass.batch);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / GameClass.PPM);

        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

        //(0, -8) - Gravity on y equals -8
        world = new World(new Vector2(0,-8), true);
        //Adding contact listener to listen for collisions between bodies
        world.setContactListener(new CollisionHandler());

        renderFloor();

        addObjectsToTheWorld();
    }

    private void addObjectsToTheWorld(){
        //Adds player to the world in position (30,90)
        player = new Player(world, new Vector2(30 / GameClass.PPM, 200 / GameClass.PPM),this); //!!!!!!!!!Reset this to 90
        droneEnemy = new DroneEnemy(world,new Vector2(220 / GameClass.PPM, 150 / GameClass.PPM),this);
    }

    private void renderFloor(){
        floorDebugger = new Box2DDebugRenderer();
        
        //defines what the body consists of
        BodyDef floorBodyDef = new BodyDef();

        PolygonShape floorShape = new PolygonShape();

        //to add bodies to the world
        FixtureDef floorFixtureDef = new FixtureDef();
        
        Body floor;

        //Create Floor Objects which's in the 4th layer
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            //Shaped as rectangles in the map objects
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            floorBodyDef.type = BodyDef.BodyType.StaticBody;

            //getX return the start of rect then add half of the width to get the center
            //The same for Y
            floorBodyDef.position.set((rect.getX() + rect.getWidth() / 2) / GameClass.PPM, (rect.getY() + rect.getHeight() / 2) / GameClass.PPM);

            floor = world.createBody(floorBodyDef);

            floorShape.setAsBox(rect.getWidth() / 2 / GameClass.PPM, rect.getHeight() / 2 / GameClass.PPM);
            floorFixtureDef.shape = floorShape;
            floor.createFixture(floorFixtureDef);
        }
    }

    //User Input handling function
    private void handleInput(){

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if(player.getState() != Player.State.Jumping && player.getState() != Player.State.Falling)
                player.body.applyLinearImpulse(new Vector2(0, 4f), player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2)
            player.body.applyLinearImpulse(new Vector2(0.1f,0), player.body.getWorldCenter(),true);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2)
            player.body.applyLinearImpulse(new Vector2(-0.1f,0), player.body.getWorldCenter(),true);

        //screen controls
        if (Gdx.input.isKeyPressed(Input.Keys.F3))
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.graphics.setWindowedMode((GameClass.V_WIDTH * 2), (GameClass.V_HEIGHT * 2));


    }

    //update the game state
    private void update(float deltaTime){
        //System.out.printf("%f\n", gameCam.position.x);
        handleInput();
        /*
        set timeStamp and velocity 
        to avoid CPU & GPU speed differences
        */
        world.step(1/60f, 60, 2);
        player.update(deltaTime);
        droneEnemy.update();
        gameCam.position.x = player.body.getWorldCenter().x;
        gameCam.update();
        renderer.setView(gameCam); //tells our renderer to draw only what camera can see in our game world
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        floorDebugger.render(world, gameCam.combined); //remove this line to remove green debugging lines on objects

        gameClass.batch.setProjectionMatrix(gameCam.combined);
        gameClass.batch.begin();
        player.draw(gameClass.batch);
        droneEnemy.draw(gameClass.batch);
        gameClass.batch.end();

        //Draw Hud
        gameClass.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hud.dispose();
    }

}