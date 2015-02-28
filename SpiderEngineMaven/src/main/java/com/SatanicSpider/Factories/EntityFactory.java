/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Factories;

import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.World;
import com.SatanicSpider.Components.EntityTimeToLive;
import com.SatanicSpider.Callbacks.BodyFactoryCallback;
import com.SatanicSpider.Components.Game.Health;
import com.SatanicSpider.Components.Name;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Components.Render.DamageParticle;
import com.SatanicSpider.Components.Render.Particle;
import com.SatanicSpider.Components.SimpleMotion;
//import com.SatanicSpider.Components.StaticSprite;
//import com.satanicspider.topdownmmo.topdownmmo.PhysicsBody;
//import com.satanicspider.topdownmmo.topdownmmo.Components.StaticSpritom.satanicspider.topdownmmo.topdownmmo.Components.EntityTimeToLive;
//imponicspider.topdownmmo.topdownmmo.Components.Position;
//import mygaanager;
import com.SatanicSpider.Management.Physics.PhysicsManager;
import static com.SatanicSpider.SharedVars.assetManager;
import com.SatanicSpider.SpriteLibrary;
import com.SatanicSpider.Components.Render.Sprite;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.UUID;
import org.jbox2d.common.Vec2;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author bryant
 */
public class EntityFactory
{

	public static World RenderWorld;
	public static World GameWorld;
	public static World ScriptWorld;
	public static World PhysicsWorld;

	public static SpriteLibrary sl;
	

	public static Entity createPlayer()
	{
		return createPlayer(new Vec2(5, 5),0);//tmp);
	}

	public static Entity createPlayer(Vec2 pos)
	{
		return createPlayer(pos,0);//tmp);
	}

	public static Entity createPlayer(final Vec2 pos, float ang)//Body tmp)
	{

		//TODO: Lookup
		//And theres so much i want to say
		//I want to tell you how good it feels
		//when you look at me that way
		//David Grey please forgive me
		/*while(!PhysicsManager.requestLock(EntityFactory.class))
		 {
		 System.err.println("WAITING FOR PHYSICS LOCK");
		 }*/
		Entity player_ent = GameWorld.syncCreateEntity();
		System.err.println("Create ent " + player_ent + " " + player_ent.getUuid());
		
		Entity render_ent = RenderWorld.getEntity(player_ent.getUuid());
		final Entity phys_ent = PhysicsWorld.getEntity(player_ent.getUuid());
		
		render_ent.addComponent(new Position(new Vec2(50,50)));
		
		System.err.println("Create ent sprite ... " + render_ent);
		
		Sprite sprite = new Sprite("Textures/Sprite2.png", "Sprite " + render_ent.getUuid().toString(), assetManager, true, true, 9, 2, 0.05f, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP);

		sl.addSprite(sprite);
		
		Health h = new Health(100);

		render_ent.addComponent(sprite);

		BodyFactoryCallback bfc = new BodyFactoryCallback()
		{
			public void onSuccess()
			{

				Body tmp = PhysicsManager.circleBody(true,0.35f);
				if(tmp != null)
				{
					tmp.setTransform(pos, 0);
					tmp.setAngularDamping(1);
					PhysicsBody bod = new PhysicsBody(tmp);
					
					phys_ent.addComponent(bod);
					
					bod.body.setUserData(phys_ent.getUuid());
					bod.body.setFixedRotation(true);
					
					phys_ent.changedInWorld();
				}
				else
				{
					System.err.println("Body was null ... ");
					onSuccess();
				}
			}
		};

		PhysicsManager.getCreateBodyCall(bfc);
		
		player_ent.addComponent(h);
		render_ent.addComponent(h);
		//player_ent.addComponent(new BodyLinearVelocity(new Vec2(0,0)));
		//player_ent.addComponent(new Damage(1));

		player_ent.addToWorld();
		render_ent.addToWorld();
		phys_ent.addToWorld();
		
		//PhysicsManager.unlock(EntityFactory.class);
		return player_ent;
	}
	
	public static Entity createTree(final Vec2 pos, float ang)//Body tmp)
	{

		//TODO: Sprites need to be initilized from the render thread
		Entity tree_ent = GameWorld.syncCreateEntity();
		//System.err.println("Create ent " + tree_ent + " " + tree_ent.getUuid());
		
		Entity render_ent = RenderWorld.getEntity(tree_ent.getUuid());
		final Entity phys_ent = PhysicsWorld.getEntity(tree_ent.getUuid());
		
		//render_ent.addComponent(new Position(new Vec2(50,50)));
		
		//System.err.println("Create ent sprite ... " + render_ent);
		
		Sprite sprite = new Sprite("tree1.png", "Sprite " + render_ent.getUuid().toString(), assetManager, 
				true, true, 
				4,4,//1, 1, 
				0.05f, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP, new Vector3f(FastMath.QUARTER_PI,0,0),
				new Vector3f(0,1,1));

		sl.addSprite(sprite);
		
		Name name = new Name("Tree");
		tree_ent.syncAddComponent(name);

		render_ent.addComponent(sprite);

		BodyFactoryCallback bfc = new BodyFactoryCallback()
		{
			public void onSuccess()
			{

				Body tmp = PhysicsManager.circleBody(false,0.35f);
				if(tmp != null)
				{
					tmp.setTransform(pos, 0);
					tmp.setAngularDamping(1);
					PhysicsBody bod = new PhysicsBody(tmp);
					
					phys_ent.addComponent(bod);
					
					bod.body.setUserData(phys_ent.getUuid());
					bod.body.setFixedRotation(true);
					
					phys_ent.changedInWorld();
				}
				else
				{
					System.err.println("Body was null ... ");
					onSuccess();
				}
			}
		};

		PhysicsManager.getCreateBodyCall(bfc);
		
		tree_ent.addComponent(new Health(100));
		//TODO: Drop resources?
		//TODO: Flamable?
		
		//player_ent.addComponent(new BodyLinearVelocity(new Vec2(0,0)));
		//player_ent.addComponent(new Damage(1));

		tree_ent.addToWorld();
		render_ent.addToWorld();
		phys_ent.addToWorld();
		
		//PhysicsManager.unlock(EntityFactory.class);
		return tree_ent;
	}
	
	public static Entity createHouse(final Vec2 pos, float ang)//Body tmp)
	{

		Entity tree_ent = GameWorld.syncCreateEntity();
		//System.err.println("Create ent " + tree_ent + " " + tree_ent.getUuid());
		
		Entity render_ent = RenderWorld.getEntity(tree_ent.getUuid());
		final Entity phys_ent = PhysicsWorld.getEntity(tree_ent.getUuid());
		
		//render_ent.addComponent(new Position(new Vec2(50,50)));
		
		//System.err.println("Create ent sprite ... " + render_ent);
		
		Sprite sprite = new Sprite("Textures/house1.png", "Sprite " + render_ent.getUuid().toString(), assetManager, true, true, 1, 1, 0.05f, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP,
		new Vector3f(FastMath.QUARTER_PI,0,0), new Vector3f(0,0.4f,1.5f));

		sl.addSprite(sprite);

		render_ent.addComponent(sprite);

		BodyFactoryCallback bfc = new BodyFactoryCallback()
		{
			public void onSuccess()
			{

				Body tmp = PhysicsManager.squareBody(false,new Vec2(2,1.2f));
				if(tmp != null)
				{
					tmp.setTransform(pos, 0);
					tmp.setAngularDamping(1);
					PhysicsBody bod = new PhysicsBody(tmp);
					
					phys_ent.addComponent(bod);
					
					bod.body.setUserData(phys_ent.getUuid());
					bod.body.setFixedRotation(true);
					
					phys_ent.changedInWorld();
				}
				else
				{
					System.err.println("Body was null ... ");
					onSuccess();
				}
			}
		};

		PhysicsManager.getCreateBodyCall(bfc);
		
		tree_ent.addComponent(new Health(100));
		//TODO: Drop resources?
		//TODO: Flamable?
		
		//player_ent.addComponent(new BodyLinearVelocity(new Vec2(0,0)));
		//player_ent.addComponent(new Damage(1));

		tree_ent.addToWorld();
		render_ent.addToWorld();
		phys_ent.addToWorld();
		
		//PhysicsManager.unlock(EntityFactory.class);
		return tree_ent;
	}
	
	public static Entity createPlayer(final Vec2 pos, float ang, UUID id)//Body tmp)
	{

		//TODO: Lookup
		//And theres so much i want to say
		//I want to tell you how good it feels
		//when you look at me that way
		//David Grey please forgive me
		/*while(!PhysicsManager.requestLock(EntityFactory.class))
		 {
		 System.err.println("WAITING FOR PHYSICS LOCK");
		 }*/
		Entity player_ent = GameWorld.syncCreateEntity(id);
		System.err.println("Create ent " + player_ent + " " + player_ent.getUuid());
		
		Entity render_ent = RenderWorld.getEntity(player_ent.getUuid());
		final Entity phys_ent = PhysicsWorld.getEntity(player_ent.getUuid());
		
		render_ent.addComponent(new Position(new Vec2(50,50)));
		
		System.err.println("Create ent sprite ... " + render_ent);
		
		Sprite sprite = new Sprite("Textures/Sprite2.png", "Sprite " + render_ent.getUuid().toString(), assetManager, true, true, 9, 2, 0.05f, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP);

		sl.addSprite(sprite);

		render_ent.addComponent(sprite);

		BodyFactoryCallback bfc = new BodyFactoryCallback()
		{
			public void onSuccess()
			{

				Body tmp = PhysicsManager.circleBody(true,0.35f);
				if(tmp != null)
				{
					tmp.setTransform(pos, 0);
					tmp.setAngularDamping(1);
					PhysicsBody bod = new PhysicsBody(tmp);
					
					phys_ent.addComponent(bod);
					
					bod.body.setUserData(phys_ent.getUuid());
					bod.body.setFixedRotation(true);
					
					phys_ent.changedInWorld();
				}
				else
				{
					System.err.println("Body was null ... ");
					onSuccess();
				}
			}
		};

		PhysicsManager.getCreateBodyCall(bfc);
		Health h = new Health(100);
		player_ent.addComponent(h);
		render_ent.addComponent(h);
		//player_ent.addComponent(new BodyLinearVelocity(new Vec2(0,0)));
		//player_ent.addComponent(new Damage(1));

		player_ent.addToWorld();
		render_ent.addToWorld();
		phys_ent.addToWorld();
		
		//PhysicsManager.unlock(EntityFactory.class);
		return player_ent;
	}

	public static Entity createStaticPhysicalRound()
	{

		return createStaticPhysicalRound(new Vec2(2, 2), 0);

	}

	public static Entity createStaticPhysicalRound(Vec2 pos)
	{
		return createStaticPhysicalRound(pos, 0);
	}

	public static Entity createStaticPhysicalRound(Vec2 pos, float angle)
	{

		//Body tmp = PhysicsManager.circleBody(false);
		//tmp.setTransform(new Vec2(2, 2), angle);
		//tmp.setAngularDamping(1);

		return createPlayer(pos,angle);//tmp);
	}

	public static Entity createStaticPhysicalRound(Body tmp)
	{

		Entity player_ent = GameWorld.createEntity();
		Entity render_ent = GameWorld.createEntity();
		
		Sprite sprite = new Sprite("Textures/Sprite2.png", "Sprite " + render_ent.getUuid().toString(), assetManager, true, true, 9, 2, 0.05f, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP);

		sl.addSprite(sprite);

		render_ent.addComponent(sprite);

		//render_ent.addComponent(new StaticSprite("images/buildings/Default.png"));

		PhysicsBody bod = new PhysicsBody(tmp);
		player_ent.addComponent(bod);

		//TODO: Translate body position to simple position to reduce overhead
		render_ent.addComponent(bod);

		player_ent.addToWorld();
		render_ent.addToWorld();

		return player_ent;
	}

	public static Entity createStaticPhysicalSquare(Vec2 pos, float angle)
	{

		Body tmp = PhysicsManager.squareBody(false);
		tmp.setTransform(new Vec2(2, 2), angle);
		tmp.setAngularDamping(1);

		return createStaticPhysicalSquare(tmp);
	}
	
	

	public static Entity createDamageParticle(int damage, Entity source)
	{

		System.err.println("Damage Particle Factory calleded");

		//Entity part = GameWorld.createEntity();
		//TODO: Isnt this ONLY rendering?
		Entity render_part = RenderWorld.createEntity();
		Entity physics_ent = PhysicsWorld.createEntity(render_part.getUuid());
		Position p = source.getComponent(Position.class);
		
		//TODO: Create a damage partacle for rendering

		//part.addComponent(new DamageParticle(damage));
		render_part.addComponent(new EntityTimeToLive(100));
		physics_ent.addComponent(new EntityTimeToLive(100));
		Vec2 pos = new Vec2(p.pos.x,p.pos.y);
		p = new Position(pos);
		render_part.addComponent(p);
		physics_ent.addComponent(p);
		physics_ent.addComponent(new SimpleMotion(new Vec2((float) (Math.random() * 100 - 50)*50, (float) (Math.random() * 100 - 50)*50)));
		DamageParticle dp = new DamageParticle(damage);
		Particle part = new Particle(dp);
		render_part.addComponent(part);
	//CanvasImage canvasImage = graphics().createImage(40, 40);
		//Canvas can = canvasImage.canvas();
		//can.setFillColor(0x80FF0000);
		//can.drawText(damage+"", 0, 10);
		//can.fillRect(0, 0, 20, 20);
		//Layer lay = graphics().createImageLayer(canvasImage);
		//graphics().rootLayer().add(lay);
		//part.addComponent(new Particle(lay));
		render_part.addToWorld();
		physics_ent.addToWorld();

		return render_part;
	}

	public static Entity createStaticPhysicalSquare(Body tmp)
	{

		Entity player_ent = GameWorld.createEntity();
		Entity render_ent = RenderWorld.createEntity();

		Sprite sprite = new Sprite("Textures/house1.png", "Sprite " + render_ent.getUuid().toString(), assetManager, true, true, 9, 2, 0.05f, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP);
		sl.addSprite(sprite);
		render_ent.addComponent(sprite);
		
		PhysicsBody bod = new PhysicsBody(tmp);
		player_ent.addComponent(bod);

		//TODO: Translate body position to a simple position to reduce overhead
		render_ent.addComponent(bod);

		player_ent.addToWorld();

		return player_ent;
	}
	
	public static Entity createHouse(Body tmp)
	{

		Entity player_ent = GameWorld.createEntity();
		Entity render_ent = RenderWorld.createEntity();

		Sprite sprite = new Sprite("Textures/house1.png", "Sprite " + render_ent.getUuid().toString(), assetManager, true, true, 9, 2, 0.05f, Sprite.spriteAction.LOOP /*"Scroll"*/, Sprite.spriteAction.LOOP);
		sl.addSprite(sprite);
		render_ent.addComponent(sprite);
		
		PhysicsBody bod = new PhysicsBody(tmp);
		player_ent.addComponent(bod);

		//TODO: Translate body position to a simple position to reduce overhead
		render_ent.addComponent(bod);

		player_ent.addToWorld();

		return player_ent;
	}

	public static Entity createPhysicalSquareFromImage(String path, final Vec2 pos, final float angle, boolean dynamic)
	{

		//TODO: Actually do somethingg here
		final Entity player_ent = GameWorld.createEntity();

		//final Image im = ResourceManager.getImage(path);

		/*im.addCallback(new Callback()
		 {

		 @Override
		 public void onSuccess(Object result)
		 {
		 Body tmp = PhysicsManager.squareBody(new Vec2(im.width(), im.height()).mul(1/64f), false);
		 tmp.setTransform(pos, angle);
		 tmp.setAngularDamping(1);

		 player_ent.addComponent(new StaticSprite(im));
		 player_ent.addComponent(new PhysicsBody(tmp));

		 player_ent.addToWorld();
		 }

		 @Override
		 public void onFailure(Throwable cause)
		 {

		 }

		 });
		 */
		return player_ent;
	}
    //TODO: Create a factoy for a building
	
	
	//TODO: Factory for a basic sword weapon
	
}
