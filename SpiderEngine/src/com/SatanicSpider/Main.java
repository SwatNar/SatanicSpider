package com.SatanicSpider;

import com.SatanicSpider.Components.Game.Controller;
import com.SatanicSpider.Components.Game.Damage;
import com.SatanicSpider.Components.Game.EightDirectionControllerFlag;
import com.SatanicSpider.Components.Game.Executable;
import com.SatanicSpider.Components.Game.FightControllerFlag;
import com.SatanicSpider.Components.Game.MoveSpeed;
import com.SatanicSpider.Components.Game.ScriptedControl;
import com.SatanicSpider.Components.Physics.Force;
import com.SatanicSpider.Components.Render.Sprite;
import com.SatanicSpider.Factories.EntityFactory;
import com.SatanicSpider.Management.Game.GameManager;
import com.SatanicSpider.Management.Physics.PhysicsManager;
import com.SatanicSpider.Management.Render.RenderManager;
import static com.SatanicSpider.Management.Render.RenderManager.engine;
import com.SatanicSpider.Scripting.Exposure;
import com.SatanicSpider.Storage.Storage;
import com.SatanicSpider.Systems.EntityTimeToLiveKiller;
import com.SatanicSpider.Systems.Game.DamageHandler;
import com.SatanicSpider.Systems.Game.EightDirectionController;
import com.SatanicSpider.Systems.Game.FightController;
import com.SatanicSpider.Systems.Physics.PhysicsForceTranslator;
import com.SatanicSpider.Systems.Physics.PhysicsPositionalTranslator;
import com.SatanicSpider.Systems.Physics.PhysicsVelocityTranslator;
import com.SatanicSpider.Systems.Render.ParticleRenderer;
import com.SatanicSpider.Systems.Render.PolygonRenderer;
import com.SatanicSpider.Systems.Render.SpriteRenderer;
import com.SatanicSpider.Systems.SimpleMotionProcessor;
import com.SatanicSpider.core.Configuration;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldManager;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.script.ScriptEngineManager;
import org.jbox2d.common.Vec2;
import scriptoholic2.GUIConsole;

//TODO: RPG Player Speed Component
//TODO
public class Main extends SimpleApplication {

    boolean run = true;
    static Main app;

    
    GUIConsole console;
    public static void main(String[] args) {
        
        app = new Main();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GUIConsole console = new GUIConsole();
                console.setVisible(true);
                app.initConsole(console);
            }
        });
        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        app.setPauseOnLostFocus(false);
        app.start();
    }

    public void initConsole(GUIConsole console)
    {
        this.console = console;
        
        console.bind_static("Util", "com.SatanicSpider.Scripting.Exposure");

//        Thread t = new Thread() {
//            public void run() {
//                Scanner sc = new Scanner(System.in);
//                while (true) {
//                    //Not the best way to do this
//                    try {
//                        String line = sc.nextLine();
//                        System.err.println("Executing " + line);
//                        egn.eval(line);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//
//        t.start();
        
    }
    //Sprite sprite;
    ArrayList<Sprite> sprites = new ArrayList<Sprite>();
    ArrayList<Entity> ents = new ArrayList<Entity>();

    Entity player;
    Entity gamePlayer;

    World GameWorld;
    World RenderWorld;
    World PhysicsWorld;
    World ScriptWorld;

    Controller control = new Controller();

    org.jbox2d.dynamics.World PhysWorld;

    BitmapText helloText;

    //ArrayList<PhysicsQuad> quads = new ArrayList<PhysicsQuad>();//, quad2, quad3, quad4,quad5,quad6;
    @Override
    public void simpleInitApp() {
        //TODO: Progress bars ...
        initConfig();
        initSystem();
        initRender();
        initPhysics();
        initInput();
        initGame();
        initFinal();
    }

    private void initConfig() {
        System.err.println("Loading Config");
        Configuration config = (Configuration) Storage.unStore(Configuration.class, Configuration.s_uuid);
        if (config == null) {
            System.err.println("Initializing Config For The First Time");
            config = new Configuration();
            Storage.Store(config);
        }
        Integer version = config.version;

        if (version == null) {
            System.err.println("Initializing Config With Version " + Configuration.current_version);
            config.initialize();
            Storage.Store(config);
        } else if (version < Configuration.current_version) {
            System.err.println("Initializing Config Upgrade From " + version + " to " + Configuration.current_version);
            config.upgrade();
            Storage.Store(config);
        }

        Configuration.curent = config;

        System.err.println("Player id is: " + config.playerUuid + " " + Configuration.curent.playerUuid);
    }

    private void initRender() {
        assetManager.registerLocator("/tmp/", FileLocator.class);
        flyCam.setEnabled(Configuration.Render.flyCamEnabled);
        cam.setLocation(Configuration.Render.initialLocation);
        //cam.
        Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f dir = new Vector3f(0.0f, 1.0f, -1.0f);

        Configuration.Render.aspect = (float) settings.getWidth() / settings.getHeight();

        //Configuration.Render.invZoom = 20f;
        float frustumSize = 10f;
        cam.setParallelProjection(true);
        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);

		//cam.apply();
        // Move our camera to a correct place and orientation.
        //cam.setFrame(Configuration.Render.initialLocation, left, up, dir);
        //cam.setFrustumPerspective(100, Configuration.Render.aspect, 0, 9999);
        cam.update();

        RenderManager.cam = cam;
        setDisplayFps(Configuration.Render.debugFpsEnabled);

        SpriteLibrary.l_guiNode = rootNode;// guiNode;
        SpriteLibrary library = new SpriteLibrary("Library 1", false);
        EntityFactory.sl = library;

        PolygonRenderer rend = new PolygonRenderer();
        RenderWorld.setSystem(rend);
        engine.addLibrary(library);

    }

    private World getOrCreateWorld(HashMap<String, World> worlds, String name) {
        World w = worlds.get(name);

        if (w == null) {
            w = new World();
            w.name = name;
            Configuration.curent.worlds.add(w);
            Storage.Store(Configuration.curent);
            //System.err.println("Created new world with " + name + w.toString());
        }
        //System.err.println("Returning " + w);
        return w;
    }

    private void initSystem() {
        SharedVars.assetManager = assetManager;
        SharedVars.rootNode = rootNode;
        SharedVars.guiNode = rootNode;

        HashMap<String, World> worlds = new HashMap<String, World>();
        for (World w : Configuration.curent.worlds) {
            worlds.put(w.name, w);
        }

        RenderWorld = getOrCreateWorld(worlds, "Render");

        //RenderWorld.name = "Render";
        GameWorld = getOrCreateWorld(worlds, "Game");
        PhysicsWorld = getOrCreateWorld(worlds, "Physics");
        ScriptWorld = getOrCreateWorld(worlds, "Script");

		//Configuration.curent.worlds.add(GameWorld);
        //Configuration.curent.worlds.add(RenderWorld);
        //Configuration.curent.worlds.add(PhysicsWorld);
        //Configuration.curent.worlds.add(ScriptWorld);
        EntityFactory.RenderWorld = RenderWorld;
        EntityFactory.GameWorld = GameWorld;
        EntityFactory.PhysicsWorld = PhysicsWorld;
        EntityFactory.ScriptWorld = ScriptWorld;

        WorldManager.setWorld("Game", GameWorld);
        WorldManager.setWorld("Render", RenderWorld);
        WorldManager.setWorld("Physics", PhysicsWorld);
        WorldManager.setWorld("Script", ScriptWorld);

        RenderWorld.setSystem(new SpriteRenderer());
        RenderWorld.setSystem(new ParticleRenderer());
        PhysicsWorld.setSystem(new PhysicsPositionalTranslator("Physics", "Render", "Game", "Script"));
        PhysicsWorld.setSystem(new PhysicsForceTranslator("Physics", "Render", "Game", "Script"));
        PhysicsWorld.setSystem(new PhysicsVelocityTranslator("Physics", "Render", "Game", "Script"));
        PhysicsWorld.setSystem(new SimpleMotionProcessor());
        PhysicsWorld.setSystem(new EntityTimeToLiveKiller());
        RenderWorld.setSystem(new EntityTimeToLiveKiller());

        GameWorld.setSystem(new EightDirectionController());
        GameWorld.setSystem(new FightController());
        GameWorld.setSystem(new DamageHandler());

        RenderManager.RenderWorld = RenderWorld;

    }

    private void initPhysics() {
        PhysWorld = new org.jbox2d.dynamics.World(new Vec2(0.0f, 0.0f));

        PhysicsManager.PhysWorld = PhysWorld;
        PhysicsManager.PhysicsWorld = PhysicsWorld;
        //PhysicsManager.quad = quads.get(0);
        PhysicsManager.getInstance();

        /*Obsolete: 2014/06/04 - Added it to the initilize code
		
         Thread PhysicsThread = PhysicsManager.getInstance();
         PhysicsThread.start();*/
    }

    private void initInput() {
        inputManager.addMapping("Increase", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Out", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("In", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("cam_up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("cam_down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("cam_left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("cam_right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("r_right", new KeyTrigger(KeyInput.KEY_RBRACKET));
        inputManager.addMapping("r_left", new KeyTrigger(KeyInput.KEY_LBRACKET));
        inputManager.addMapping("damage", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addMapping("attack 1", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("attack 2", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("attack 3", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("attack 4", new KeyTrigger(KeyInput.KEY_4));

        inputManager.addListener(analogListener, "cam_left", "cam_right", "cam_up", "cam_down", "Out", "In", "r_right", "r_left");
        inputManager.addListener(digiList, "Out", "In", "cam_left", "cam_right", "Left", "Right", "Up", "Down", "cam_up", "cam_down", "Increase", "damage", "attack 1", "atrtack 2", "attack 3", "attack 4");

        inputManager.setCursorVisible(true);
    }

    private void initGame() {

		//Entity e = EntityFactory.createTree(new Vec2(0f, 0f), 0);
		/*for(int i = 0; i < 100; i++)
         {
         if(Math.random() > 0.5)
         e = EntityFactory.createTree(new Vec2((float)Math.random()*100, (float)Math.random()*100), 0);
         else
         e = EntityFactory.createHouse(new Vec2((float)Math.random()*100, (float)Math.random()*100), 0);
         }*/
        player = EntityFactory.createPlayer(new Vec2(5, 5), 0, Configuration.curent.playerUuid);
        Exposure.player = player;
        gamePlayer = GameWorld.getEntity(player.getUuid());
        MoveSpeed spd = new MoveSpeed();
        spd.speed = 15.2f;

        gamePlayer.addComponent(control);
        gamePlayer.addComponent(spd);
        gamePlayer.addComponent(new EightDirectionControllerFlag());
        gamePlayer.addComponent(new FightControllerFlag());
        ScriptedControl sc = new ScriptedControl();
        Executable ae = new Executable() {
            long lastTime = 0;
            long fightTime = 1000;

            public void execute(Entity context, HashMap<String, Object> props) {
                long curTime = System.currentTimeMillis();
                if (curTime - lastTime > fightTime) {
                    System.err.println("ATTAAAAAAAAAAAAAAAAAAAAAAAAAAACK");
                    Exposure.spawn_tree_offset(new Vec2(1, 1));
                    lastTime = curTime;
                }
            }
        };
        sc.comps.put("Attack 1", ae);
        gamePlayer.addComponent(sc);
        gamePlayer.changedInWorld();

        //Storage.Store(e);
        RenderManager.camera_target = RenderWorld.getEntity(player.getUuid());
        PhysicsManager.physics_target = PhysicsWorld.getEntity(player.getUuid());

        GameManager.getInstance().GameWorld = GameWorld;
    }

    ScriptEngineManager engineManager = new ScriptEngineManager();
    javax.script.ScriptEngine egn = engineManager.getEngineByName("nashorn");

    private void initFinal() {
        RenderWorld.initialize();
        GameWorld.initialize();
        PhysicsWorld.initialize();
        ScriptWorld.initialize();

        console.bind("WORLD", ScriptWorld);
    }

    public void oldsimpleInitApp() {

        /*for(int x = -5; x <= 5; x++)
         for(int y = -5; y <= 5; y++)
         quads.add(new PhysicsQuad(new Vec2(x,y)));*/
		//Configuration.PhysicsDebug = false;
        // guiNode;
        //Fluff for testing
        Entity e = EntityFactory.createPlayer();
        e = EntityFactory.createPlayer(new Vec2(5.1f, 5));

        player = EntityFactory.createPlayer(new Vec2(0, 0));

        RenderManager.camera_target = RenderWorld.getEntity(player.getUuid());
        PhysicsManager.physics_target = PhysicsWorld.getEntity(player.getUuid());

        Sprite sprite;

        for (int i = 0; i < 3; i++) {
            e = EntityFactory.createPlayer(new Vec2(5.1f + i / 10f, (float) (5 + Math.random())));
			//sprite = new Sprite("Textures/Sprite2.png", "Sprite " + i, assetManager, true, true, 9, 2, 0.005f * i, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP);
            //sprites.add(sprite);
            //sprite.moveAbsolute(160 + (i+1) * 20, 160);
            //System.err.println("Creating sprite at " + (160 + (i+1) * 20) + " " + (160));
            //library.addSprite(sprite);
        }

        //e.changedInWorld();
        Thread gameUpdater = new Thread() {
            public void run() {
                while (run) {
                    GameWorld.setDelta(10);
                    GameWorld.process();
                    helloText.setText("Entities: " + GameWorld.getEntityCount());

                    //TODO: Move this to its own class
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                    }
                }
            }
        };

        gameUpdater.start();
        //adder.start();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("Hello World");
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);

        Storage.Store(player);
    }

    int target_ents = 0;

    private ActionListener digiList = new ActionListener() {
        public void onAction(String name, boolean bln, float f) {
            if (bln) {
                if (name.equals("damage")) {

                    System.err.println("Damage warning!");
                    //gamePlayer.addComponent(new Damage((int)(Math.random()*20)));
                    Exposure.spawn_tree();
                }

                if (name.equals("Increase")) {
                    target_ents += 10;
                }
                if (name.equals("damage")) {

                    System.err.println("Damage warning!");
                    gamePlayer.addComponent(new Damage((int) (Math.random() * 20)));
                }
            }

            if (name.equals("Up")) {
                control.up = bln;
                control.changed = true;
            }
            if (name.equals("Down")) {
                control.down = bln;
                control.changed = true;
            }

            if (name.equals("Left")) {
                control.left = bln;
                control.changed = true;
            }
            if (name.equals("Right")) {
                control.right = bln;
                control.changed = true;
            }

            if (name.equals("attack 1")) {
                control.one = bln;
                control.changed = true;
            }
            if (name.equals("attack 2")) {
                control.two = bln;
                control.changed = true;
            }
            if (name.equals("attack 3")) {
                control.three = bln;
                control.changed = true;
            }
            if (name.equals("attack 4")) {
                control.four = bln;
                control.changed = true;
            }
        }

    };

    //TODO: Make a controller class
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (run) {
                //System.err.println(name);

                if (name.equals("Out")) {
                    RenderManager.zoom_out();
                }
                if (name.equals("In")) {
                    RenderManager.zoom_in();
                }
                if (name.equals("cam_left")) {
                    Configuration.Render.CameraOffset = Configuration.Render.CameraOffset.add(new Vec2(-0.1f, 0));
                    System.err.println(Configuration.Render.CameraOffset);
                }
                if (name.equals("cam_right")) {
                    Configuration.Render.CameraOffset = Configuration.Render.CameraOffset.add(new Vec2(0.1f, 0));
                    System.err.println(Configuration.Render.CameraOffset);
                }
                if (name.equals("cam_down")) {
                    Configuration.Render.CameraOffset = Configuration.Render.CameraOffset.add(new Vec2(0, -0.1f));
                    System.err.println(Configuration.Render.CameraOffset);
                }
                if (name.equals("cam_up")) {
                    Configuration.Render.CameraOffset = Configuration.Render.CameraOffset.add(new Vec2(0, 0.1f));
                    System.err.println(Configuration.Render.CameraOffset);
                }

                if (name.equals("r_right")) {
                    Entity e = RenderWorld.getEntity(player.getUuid());

                    Force l = e.getComponent(Force.class);
                    if (l == null) {
                        l = new Force();
                        l.linear_force = new Vec2(0, 0);
                        e.addComponent(l);
                    }
                    l.rotational_force = l.rotational_force -= 0.5f;

                }
                if (name.equals("r_left")) {
                    Entity e = RenderWorld.getEntity(player.getUuid());

                    Force l = e.getComponent(Force.class);
                    if (l == null) {
                        l = new Force();
                        l.linear_force = new Vec2(0, 0);
                        e.addComponent(l);
                    }
                    l.rotational_force = l.rotational_force += 0.5f;

                }
                if (name.equals("Up")) {
                    control.up = true;
					//Entity e = gamePlayer;//RenderWorld.getEntity(player.getUuid());

					//Velocity l = e.getComponent(Velocity.class);
                    //if (l == null)
                    //{
                    //	l = new Velocity();
                    //	l.linear_velocity = new Vec2(0, 0);
                    //	e.addComponent(l);
                    //}
                    //l.linear_velocity = l.linear_velocity.add(new Vec2(0, 5));//.mul(tpf));
					/*Force l = e.getComponent(Force.class);
                     if (l == null)
                     {
                     l = new Force();
                     l.linear_force = new Vec2(0, 0);
                     e.addComponent(l);
                     }
                     l.linear_force = l.linear_force.add(new Vec2(0,900).mul(tpf));*/
                }
                if (name.equals("Down")) {
                    Entity e = gamePlayer;//RenderWorld.getEntity(player.getUuid());
					/*
                     Velocity l = e.getComponent(Velocity.class);
                     if (l == null)
                     {
                     l = new Velocity();
                     l.linear_velocity = new Vec2(0, 0);
                     }
                     l.linear_velocity = l.linear_velocity.add(new Vec2(0, -5));//.mul(tpf));
                     //l.velocity.normalize();
                     //l.velocity = l.velocity.mul(5);
                     */
                    Force l = e.getComponent(Force.class);
                    if (l == null) {
                        l = new Force();
                        l.linear_force = new Vec2(0, 0);
                        e.addComponent(l);
                    }
                    l.linear_force = l.linear_force.add(new Vec2(0, -900).mul(tpf));

                }
                if (name.equals("Left")) {

                    Entity e = gamePlayer;//RenderWorld.getEntity(player.getUuid());

                    /*Velocity l = e.getComponent(Velocity.class);
                     if (l == null)
                     {
                     l = new Velocity();
                     l.linear_velocity = new Vec2(0, 0);
                     e.addComponent(l);
                     }
                     l.linear_velocity = l.linear_velocity.add(new Vec2(-5, 0));//.mul(tpf));
                     //l.velocity.normalize();
                     //l.velocity = l.velocity.mul(5);
                     */
                    Force l = e.getComponent(Force.class);
                    if (l == null) {
                        l = new Force();
                        l.linear_force = new Vec2(0, 0);
                        e.addComponent(l);
                    }
                    l.linear_force = l.linear_force.add(new Vec2(-900, 0).mul(tpf));

                }
                if (name.equals("Right")) {
                    Entity e = gamePlayer;//RenderWorld.getEntity(player.getUuid());
					/*
                     Velocity l = e.getComponent(Velocity.class);
                     if (l == null)
                     {
                     l = new Velocity();
                     l.linear_velocity = new Vec2(0, 0);
                     }
                     l.linear_velocity = l.linear_velocity.add(new Vec2(5, 0));//.mul(tpf));
                     //l.velocity.normalize();
                     //l.velocity = l.velocity.mul(5);
                     e.addComponent(l);
                     */

                    Force l = e.getComponent(Force.class);
                    if (l == null) {
                        l = new Force();
                        l.linear_force = new Vec2(0, 0);
                        e.addComponent(l);
                    }
                    l.linear_force = l.linear_force.add(new Vec2(900, 0).mul(tpf));

                }

            } else {
                System.out.println("Press P to unpause.");
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        RenderManager.update(tpf);

        //sprites.get(0).move(tpf * 10, 0);
    }

    public void destroy() {
        super.destroy();
        System.err.println("Finalizing player");
        Storage.Store(player);
        run = false;
        PhysicsManager.run = false;
        Storage.run = false;
        GameManager.run = false;
    }
}
