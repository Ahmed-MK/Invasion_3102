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


import com.oop.platformer.GameClass;
import com.oop.platformer.GameObjects.Player;
import com.oop.platformer.Scenes.Hud;


public class Level1 implements Screen {
    

    private OrthographicCamera gameCam; //game camera instance to move with the player character

    private Viewport gamePort; //Manages a Camera and determines how world coordinates are mapped to and from the screen.

    private TmxMapLoader mapLoader; //loads the level from assets

    private TiledMap map;//reference for the map itself

    private  Hud hud;
    private OrthogonalTiledMapRenderer renderer;

    private World world1;
    private Box2DDebugRenderer level1Floorb2dr;


    private Player player;
    
    public Level1(){
        //setup camera and window
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(GameClass.V_WIDTH,GameClass.V_HEIGHT,gameCam);

        //Load Map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map/level1.tmx");


        hud = new Hud(GameClass.batch);
        renderer = new OrthogonalTiledMapRenderer(map);


        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);


        //(0, 0) - No Gravity
        world1 = new World(new Vector2(0,0), true);

       
        renderFloor();

        
        addObjectsToTheWorld();
        
    }

    private void addObjectsToTheWorld(){
        //Adds player to the world
        player = new Player(world1);
    }

    private void renderFloor(){
        level1Floorb2dr = new Box2DDebugRenderer();
        
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
            floorBodyDef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            floor = world1.createBody(floorBodyDef);

            floorShape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            floorFixtureDef.shape = floorShape;
            floor.createFixture(floorFixtureDef);
        }
    }

    //User Input handling function
    public void handleInput(float deltaTime){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Math.ceil(gameCam.position.x) <= 1407)
            gameCam.position.x += 150 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && Math.floor(gameCam.position.x) > 225)
            gameCam.position.x -= 150 * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.F3))
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.graphics.setWindowedMode(GameClass.V_WIDTH*2, GameClass.V_HEIGHT*2);
    }


    //update the game state
    public void update(float deltaTime){
        //System.out.printf("%f\n", gameCam.position.x);
        handleInput(deltaTime);

        //set timeStamp and velocity 
        world1.step(1/60f, 6, 2);

        gameCam.update();
        renderer.setView(gameCam); //tells our renderer to draw only what camera can see in our game world
    }



    @Override
    public void show() {

    }


    /**
     * Logic Goes Here
     */
    @Override
    public void render(float delta) {

        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();

        //Debugger, may need to remove it
        level1Floorb2dr.render(world1, gameCam.combined);


        //Draw Hud
        GameClass.batch.setProjectionMatrix(hud.stage.getCamera().combined);
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
