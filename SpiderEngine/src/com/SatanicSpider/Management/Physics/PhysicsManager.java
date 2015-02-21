/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Management.Physics;

import com.artemis.Entity;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Callbacks.BodyFactoryCallback;
import com.SatanicSpider.Storage.Storage;
import com.SatanicSpider.core.Configuration;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.iforce2d.Jb2dJson;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

/**
 *
 * @author bryant
 */
public class PhysicsManager extends Thread implements ContactListener
{

	public Vec2 buffer = new Vec2(2, 2);

	private static HashMap<Object, ArrayList<ContactListener>> Listeners = new HashMap<Object, ArrayList<ContactListener>>();

	public static Entity physics_target;

	public static boolean run = true;

	private static boolean locked = false;

	public static long target_step = 3;

	public static long target_entity_step = 20;
	public static long target_world_step = 20;

	private static PhysicsManager instance = null;

	public static org.jbox2d.dynamics.World PhysWorld;
	public static com.artemis.World PhysicsWorld;

	public static Jb2dJson JSONifier = new Jb2dJson();

	private static Class lockingClass;

	private static ArrayList<BodyFactoryCallback> step_callbacks = new ArrayList<BodyFactoryCallback>();

	HashMap<Vec2, PhysicsQuad> quads = new HashMap<Vec2, PhysicsQuad>();

	Vec2 lastQuad = new Vec2(0, 0);

	private PhysicsQuadIndex index;

	//TODO: Quad Manager
	//public static PhysicsQuad quad;
	public static PhysicsManager getInstance()
	{
		if (instance == null)
		{
			initialize();

			instance.start();
		}

		return instance;
	}

	private static void initialize()
	{
		instance = new PhysicsManager();
		System.err.println("Initializing Index ... ");
		instance.index = (PhysicsQuadIndex) Storage.unStore(PhysicsQuadIndex.class, Configuration.Physics.QuadIndexUUID);
		System.err.println(instance.index);
		if (instance.index == null)
		{
			instance.index = new PhysicsQuadIndex();
		}
		System.err.println(instance.index);
		PhysWorld.setContactListener(instance);

		target_entity_step = 1000 / Configuration.Physics.target_system_fps;
		target_world_step = 1000 / Configuration.Physics.target_world_fps;

		for (int x = (int) (instance.buffer.x / -2); x <= instance.buffer.x / 2; x++)
		{
			for (int y = (int) (instance.buffer.y / -2); y <= instance.buffer.y / 2; y++)
			{
				Vec2 off = new Vec2(x, y);
				//instance.quads.put(off, new PhysicsQuad(off));
				instance.restoreOrCreate(off);
			}
		}

	}

	public static synchronized void getCreateBodyCall(BodyFactoryCallback b)
	{
		step_callbacks.add(b);
		/*Thread creator = new Thread()
		 {
		 public void run()
		 {
		 //System.err.println("Starting callback");
		 while(!requestLock(this.getClass()));
		 b.onSuccess();
		 unlock(this.getClass());
		 }
		 };
		
		 creator.start();*/

	}

	public static boolean requestLock(Class requestingLock)
	{
		while (isLocked())
		{
			//System.err.println("Requesting lock ... delayed for  " + requestingLock + " " + lockingClass);
			try
			{
				Thread.sleep(1);
			} catch (Exception ex)
			{
				return false;
			}
		}
		lock(requestingLock);

		return true;
	}

	private static synchronized boolean lock(Class c)
	{
		if (!locked)
		{
			locked = true;
			lockingClass = c;
		} else
		{
			return false;
		}

		return true;
	}

	public static synchronized boolean unlock(Class c)
	{
		if (locked && c.equals(lockingClass))
		{
			locked = false;
			lockingClass = null;
		} else
		{
			return false;
		}

		return true;
	}

	public static synchronized boolean isLocked()
	{
		return locked;
	}

	//Mark the constructor private ... we be the only ones doing the constructing around herr
	private PhysicsManager()
	{

	}

	public synchronized void run()
	{

		//Todo: Physics Watchdog ...
		//TODO: Make this run at a set framerate
		//TODO: Framerate error reporting
		int loop_counter = 0;
		int loop_counter_entity = 0;
		long[] fps_buffer = new long[1000];
		long[] fps_buffer_entity = new long[1000];

		long lastTime = System.currentTimeMillis();
		long lastSystemTime = lastTime;
		long lastWorldTime = lastTime;

		//todo: check objects out of bounds
		while (run)
		{

			long delta = (System.currentTimeMillis() - lastTime);
			//fps_buffer[loop_counter] = delta;
			lastTime = System.currentTimeMillis();

			while (!requestLock(this.getClass()))
			{
				//System.err.println("Physics tick waiting for lock!");
			}

			long entity_delta = (System.currentTimeMillis() - lastSystemTime);

			if (entity_delta > target_entity_step)
			{
				lastSystemTime = System.currentTimeMillis();
				PhysicsWorld.setDelta(entity_delta / 1000f);
				PhysicsWorld.process();
				loop_counter_entity++;
				if (loop_counter_entity >= fps_buffer_entity.length)
				{
					loop_counter_entity = 0;
					long tot = 0;
					for (int i = 0; i < fps_buffer.length; i++)
					{
						tot += fps_buffer_entity[i];
					}
					float avg = (float) (tot / 1000.0);
					float fps = (float)(1000.0 / avg);
					System.err.println("AVG Time: " + avg + " FPS: " + fps + " objects: " + PhysWorld.getBodyCount());
					if(fps < Configuration.Physics.target_system_fps - Configuration.Physics.adjustor_threshold)
					{
						System.err.println("Oh my look at the time");
						target_entity_step --;
					}
					
					if(fps > Configuration.Physics.target_system_fps + Configuration.Physics.adjustor_threshold)
					{
						System.err.println("Dear me we are early.");
						target_entity_step ++;
					}
						
				}
				fps_buffer_entity[loop_counter_entity] = entity_delta;
			}

			long world_delta = (System.currentTimeMillis() - lastWorldTime);
			if (world_delta > target_world_step)
			{
				lastWorldTime = System.currentTimeMillis();
				PhysWorld.step(world_delta / 1000f, 10, 5);
				loop_counter++;
				if (loop_counter >= fps_buffer.length)
				{
					loop_counter = 0;
					long tot = 0;
					for (int i = 0; i < fps_buffer.length; i++)
					{
						tot += fps_buffer[i];
					}
					float avg = (float) (tot / 1000.0);
					float fps = (float)(1000.0 / avg);
					System.err.println("AVG Time: " + avg + " FPS: " + fps + " objects: " + PhysWorld.getBodyCount());
					if(fps < Configuration.Physics.target_world_fps - Configuration.Physics.adjustor_threshold)
					{
						System.err.println("Oh my look at the entity time");
						target_world_step --;
					}
					
					if(fps > Configuration.Physics.target_world_fps + Configuration.Physics.adjustor_threshold)
					{
						System.err.println("Dear me we are early entity.");
						target_world_step ++;
					}
						
				}
				fps_buffer[loop_counter] = world_delta;
			}
			else
			{
				if (step_callbacks.size() > 0)
				{
					BodyFactoryCallback b = step_callbacks.remove(0);
					if (b != null)
					{
						b.onSuccess();
					}
				}
			}

			unlock(this.getClass());

			if (physics_target != null)
			{
				//Try to get the position
				PhysicsBody pb = physics_target.getComponent(PhysicsBody.class);
				if (pb != null)
				{
					Vec2 pos = pb.body.getPosition().clone();
					if (pos.x < 0)
					{
						pos.x -= PhysicsQuad.size.x;
					}
					if (pos.y < 0)
					{
						pos.y -= PhysicsQuad.size.y;
					}
					pos.x = (int) (pos.x / PhysicsQuad.size.x);
					pos.y = (int) (pos.y / PhysicsQuad.size.y);

					if (lastQuad.x != pos.x || lastQuad.y != pos.y)
					{
						//Shift it around
						//System.err.println("Shift the quads from " + lastQuad + " " + pos);
						if (lastQuad.x < pos.x)
						{
							shiftRight();
						} else if (lastQuad.x > pos.x)
						{
							shiftLeft();
						}

						if (lastQuad.y < pos.y)
						{
							shiftUp();
						} else if (lastQuad.y > pos.y)
						{
							shiftDown();
						}

						lastQuad = pos;
					}
					//System.err.println((int)(pos.x / PhysicsQuad.size.x) + " " + (int)(pos.y / PhysicsQuad.size.y) + " " + pos.x + " " + pos.y);
				}

			}
			//TODO: Calculate required delta here
			try
			{
				yield();
				//sleep(0, 500);//target_step);
			} catch (Exception e)
			{
			}
			//System.err.println("Physics tick: " + delta);
		}
	}

	public static void RegisterQuad(PhysicsQuad q)
	{
		//System.err.println("Registering Quad: " + q.uuid + " " + q.getOffset());
		getInstance().index.put(q.getOffset(), q.uuid);
		getInstance().quads.put(q.getOffset(), q);
	}

	public void restoreOrCreate(Vec2 off)
	{

		UUID id = index.get(off);
		PhysicsQuad quack = null;
		if (id != null)
		{
			//System.err.println("Unstoring " + off + " " + id);
			quack = (PhysicsQuad) (Storage.unStore(PhysicsQuad.class, id));
		}
		if (quack == null)
		{
			//System.err.println("Could not find a reference to the quad ... " + off);
			quack = new PhysicsQuad(off);
		} else
		{
			quack.restore();
		}

		quads.put(off, quack);

	}

	public void shiftUp()
	{
		//System.out.println("Up: " + lastQuad);
		//TODO: Get Quads from file
		//TODO: Ignore the above. Get the quads from storage: done

		//We need to make a row of quads (or load them from file)
		//Then we need to append it to the top of the quads
		//Then we need to shift out the bottom 
		for (int x = (int) (buffer.x / -2 + lastQuad.x); x <= lastQuad.x + buffer.x / 2; x++)
		{
			Vec2 off = new Vec2(x, lastQuad.y + buffer.y / 2 + 1);
			//PhysicsQuad q = quads.get(off);
			//if (q == null)
			restoreOrCreate(off);

			off = new Vec2(x, lastQuad.y - buffer.y / 2);
			store(off);
		}
	}

	public void shiftDown()
	{
		//System.out.println("Down: " + lastQuad);
		for (int x = (int) (buffer.x / -2 + lastQuad.x); x <= lastQuad.x + buffer.x / 2; x++)
		{
			Vec2 off = new Vec2(x, lastQuad.y - buffer.y / 2 - 1);
			//PhysicsQuad q = quads.get(off);
			//if (q == null)
			restoreOrCreate(off);

			off = new Vec2(x, lastQuad.y + buffer.y / 2);
			store(off);
		}
	}

	public void shiftLeft()
	{
		//System.out.println("Left: " + lastQuad);
		for (int y = (int) (buffer.y / -2 + lastQuad.y); y <= lastQuad.y + buffer.y / 2; y++)
		{
			Vec2 off = new Vec2(lastQuad.x - buffer.x / 2 - 1, y);
			//PhysicsQuad q = quads.get(off);
			//if (q == null)
			restoreOrCreate(off);

			off = new Vec2(lastQuad.x + buffer.x / 2, y);
			store(off);
		}
	}

	public void shiftRight()
	{
		//System.out.println("Right: " + lastQuad);

		for (int y = (int) (buffer.y / -2 + lastQuad.y); y <= lastQuad.y + buffer.y / 2; y++)
		{
			Vec2 off = new Vec2(lastQuad.x + buffer.x / 2 + 1, y);
			//PhysicsQuad q = quads.get(off);
			//if (q == null)
			restoreOrCreate(off);

			off = new Vec2(lastQuad.x - buffer.x / 2, y);
			store(off);
		}

	}

	public void store(Vec2 off)
	{
		PhysicsQuad q = quads.get(off);
		if (q != null)
		{
			//System.err.println("Quad was not null " + off);
			q.store();
			quads.remove(off);
		} else
		{
			System.err.println("Quad was null " + off);
		}
	}

	//todo merge body factory here :(
	public static Body getBody(BodyDef def)
	{
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a body with " + def);
		}

		Body ret = null;

		ret = PhysicsManager.PhysWorld.createBody(def);

		return ret;
	}

	public static BodyDef blankBodyDef()
	{

		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a blank body definition");
		}
		return new BodyDef();
	}

	public static BodyDef blankDynamicBodyDef()
	{

		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a blank dynamic body definition");
		}

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position = new Vec2(0, 0);
		return bodyDef;
	}

	public static BodyDef blankStaticBodyDef()
	{

		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a blank static body definition");
		}

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.STATIC;
		bodyDef.position = new Vec2(0, 0);
		return bodyDef;
	}

	public synchronized static Body circleBody(boolean dynamic)
	{

		return circleBody(dynamic, 0.5f);

	}

	public synchronized static Body circleBody(boolean dynamic, float radius)
	{

		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a circle body");
		}

		FixtureDef fixtureDef = new FixtureDef();

		Body body;
		if (dynamic)
		{
			body = getBody(blankDynamicBodyDef());
		} else
		{
			body = getBody(blankStaticBodyDef());
		}
		while (body == null)
		{
			body = getBody(blankDynamicBodyDef());
		}
		if (body != null)
		{

			CircleShape circleShape = new CircleShape();
			circleShape.m_radius = radius;
			fixtureDef.shape = circleShape;
			fixtureDef.density = 0.4f;
			fixtureDef.friction = 0.1f;
			fixtureDef.restitution = 0.35f;
			circleShape.m_p.set(0, 0);
			body.createFixture(fixtureDef);
			body.setLinearDamping(0.2f);
			//body.setTransform(new Vec2(x, y), angle);
		} else
		{
			System.err.println("BODY IS NULL!");
		}

//		quad.Bodies.add(body);
		//body.setUserData(body);
		return body;

	}

	public static Body squareBody(boolean dynamic)
	{

		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a circle body");
		}

		FixtureDef fixtureDef = new FixtureDef();

		Body body;
		if (dynamic)
		{
			body = getBody(blankDynamicBodyDef());
		} else
		{
			body = getBody(blankStaticBodyDef());
		}

		PolygonShape squareShape = new PolygonShape();
		squareShape.setAsBox(0.5f, 0.5f);
		//squareShape.m_radius = 0.5f;
		fixtureDef.shape = squareShape;
		fixtureDef.density = 0.4f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.35f;
		//squareShape.m_p.set(0, 0);

		body.createFixture(fixtureDef);
		body.setLinearDamping(0.2f);
		//body.setTransform(new Vec2(x, y), angle);
//		quad.Bodies.add(body);
		return body;

	}

	public static Body squareBody(boolean dynamic, Vec2 size)
	{

		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a circle body");
		}

		FixtureDef fixtureDef = new FixtureDef();

		Body body;
		if (dynamic)
		{
			body = getBody(blankDynamicBodyDef());
		} else
		{
			body = getBody(blankStaticBodyDef());
		}

		PolygonShape squareShape = new PolygonShape();
		squareShape.setAsBox(size.x, size.y);
		//squareShape.m_radius = 0.5f;
		fixtureDef.shape = squareShape;
		fixtureDef.density = 0.4f;
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0.35f;
		//squareShape.m_p.set(0, 0);

		body.createFixture(fixtureDef);
		body.setLinearDamping(0.2f);
		//body.setTransform(new Vec2(x, y), angle);

		//quad.Bodies.add(body);
		return body;

	}

	public static void addContactListener(ContactListener l, Body... filter)
	{
		for (Body b : filter)
		{
			ArrayList<ContactListener> lists = Listeners.get(b);
			if (lists == null)
			{
				lists = new ArrayList<ContactListener>();
				Listeners.put(b, lists);
			}

			lists.add(l);
		}
	}

	public static void addContactListener(ContactListener l)
	{

		ArrayList<ContactListener> lists = Listeners.get("all");
		if (lists == null)
		{
			lists = new ArrayList<ContactListener>();
			Listeners.put("all", lists);
		}

		lists.add(l);
	}

	@Override
	public void beginContact(Contact contact)
	{
		ArrayList<ContactListener> lists = Listeners.get("all");
		if (lists == null)
		{
			lists = new ArrayList<ContactListener>();
			Listeners.put("all", lists);
		}

		for (ContactListener l : lists)
		{
			l.beginContact(contact);
		}
	}

	//TODO: Remove owningQuadComponent
	@Override
	public void endContact(Contact contact)
	{
		ArrayList<ContactListener> lists = Listeners.get("all");
		if (lists == null)
		{
			lists = new ArrayList<ContactListener>();
			Listeners.put("all", lists);
		}

		for (ContactListener l : lists)
		{
			l.endContact(contact);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		ArrayList<ContactListener> lists = Listeners.get("all");
		if (lists == null)
		{
			lists = new ArrayList<ContactListener>();
			Listeners.put("all", lists);
		}

		for (ContactListener l : lists)
		{
			l.preSolve(contact, oldManifold);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		ArrayList<ContactListener> lists = Listeners.get("all");
		if (lists == null)
		{
			lists = new ArrayList<ContactListener>();
			Listeners.put("all", lists);
		}

		for (ContactListener l : lists)
		{
			l.postSolve(contact, impulse);
		}
	}

}
