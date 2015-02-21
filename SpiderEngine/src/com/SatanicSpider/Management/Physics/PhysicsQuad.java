/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Management.Physics;

import com.SatanicSpider.Storage.Storage;
import com.artemis.Entity;
//import com.google.gson.Gson;
import com.SatanicSpider.Components.Physics.OwningQuad;
import com.SatanicSpider.Callbacks.BodyFactoryCallback;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.Render.Polygon;
import com.SatanicSpider.Callbacks.RenderManagerCallback;
import java.util.ArrayList;
import java.util.UUID;
import com.SatanicSpider.Management.Render.RenderManager;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.json.JSONObject;

/**
 *
 * @author bryant
 */
public class PhysicsQuad implements ContactListener
{

	//TODO: Render this
	UUID uuid = UUID.randomUUID();

	
	
	public static Vec2 size;// = new Vec2(6.7f, 3.75f).mul(2);
	public Vec2 my_offset;

	transient Entity my_render_ent;

	transient Body boundSensor;

	transient ArrayList<Body> Bodies;// = new ArrayList<Body>();
	ArrayList<String> bodies;// = new ArrayList<JSONObject>();

	transient OwningQuad myQuadComponent = new OwningQuad();

	public PhysicsQuad(Vec2 offset)
	{
		//System.err.println("Creating quad at " + offset);
		//TopLeft = offset.mul(BottomRight.x);//,BottomRight.y);
		//BottomRight = BottomRight.add(TopLeft);
		//System.err.println(offset + " -> " + TopLeft + " -> " + BottomRight);
		Bodies = new ArrayList<Body>();
		bodies = new ArrayList<String>();
		size = new Vec2(6.7f, 3.75f).mul(2);
		my_offset = offset;
		createSensor(offset.mul(2));
		myQuadComponent.myQuad = this;
		PhysicsManager.RegisterQuad(this);
	}
	
	public PhysicsQuad()
	{
		Vec2 offset = new Vec2(0,0);
		Bodies = new ArrayList<Body>();
		bodies = new ArrayList<String>();
		size = new Vec2(6.7f, 3.75f).mul(2);
		if(my_offset == null)
			my_offset = offset;
		//createSensor(offset.mul(2));
		myQuadComponent.myQuad = this;
		//PhysicsManager.RegisterQuad(this);
	}

	public void store()
	{
		//System.err.println("Store called " + uuid + " " + my_offset + " " + Bodies.size());
		
		bodies = new ArrayList<String>();
		//Gson gson = new Gson();
		while (Bodies.size() > 0)
		{
			JSONObject obj = shiftOut(Bodies.get(0));
			//System.err.println("==>" + obj.toString());
			//bodies.add(obj);
			//System.err.println(bodies.size());
		}

		/*for (String j : bodies)
		{
			System.err.println(j.toString());
		}*/

		Bodies = new ArrayList<Body>();

		if (my_render_ent != null)
		{
			Polygon pg = my_render_ent.getComponent(Polygon.class);
			if (pg != null)
			{
				//System.err.println("PG cleanup");
				pg.cleanup(my_render_ent);
				my_render_ent.deleteFromWorld();
			} else
			{
				System.err.println("PG was null ...");
			}
		}
		
		
		PhysicsManager.PhysWorld.destroyBody(boundSensor);
		Storage.Store(this);
	}

	public JSONObject shiftOut(Body b)
	{
		JSONObject s = null;
		if (Bodies.remove(b))
		{
			BodyFactoryCallback bfc = new BodyFactoryCallback()
			{
				public void onSuccess()
				{
					PhysicsManager.PhysWorld.destroyBody(b);

				}
			};
			try
			{
				s = PhysicsManager.JSONifier.b2j(b);
				//System.err.println("Store: " + b + " " + s + " " + s.toString());

				UUID u = (UUID) b.getUserData();

				Entity e = PhysicsManager.PhysicsWorld.getEntity(u);
				if (e != null)
				{
					e.removeComponent(PhysicsBody.class);
					e.changedInWorld();
				}

				if (s != null)
				{
					if (u != null)
					{
						s.put("userData", u.toString());
					}

					bodies.add(s.toString());
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			//TODO: Do we need to use a callback here? Shouldnt this already be in the phys thread?
			PhysicsManager.getCreateBodyCall(bfc);
		}
		return s;
	}

	//TODO: Create singleton contact listener master ... then add listeners to it
	public void createSensor(Vec2 offset)
	{
		//System.err.println("Create Sensor: " + offset + " " + size);
		offset = new Vec2(offset.x * size.x * 0.5f, offset.y * size.y * 0.5f).add(size.mul(0.5f));
		//System.err.println("Create Sensor2: " + offset);
		ContactListener lis = this;

		Vec2 off = offset.clone();

		BodyFactoryCallback bfc = new BodyFactoryCallback()
		{
			public void onSuccess()
			{
				//System.err.println("Creating Sensor " + off + " " + my_offset);
				FixtureDef fixtureDef = new FixtureDef();

				Body body;
				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyType.STATIC;
				//body = getBody(bodyDef);
				PolygonShape squareShape = new PolygonShape();

				squareShape.setAsBox(size.x * 0.5f, size.y * 0.5f);

				//squareShape.m_radius = 0.5f;
				fixtureDef.shape = squareShape;
				fixtureDef.isSensor = true;
				//squareShape.m_p.set(0, 0);

				boundSensor = PhysicsManager.PhysWorld.createBody(bodyDef);
				boundSensor.createFixture(fixtureDef);

				//Vec2 offset = TopLeft.add(new Vec2(BottomRight.x - TopLeft.x, BottomRight.y - TopLeft.y));
				boundSensor.setTransform(off, 0);
				boundSensor.setUserData("BOUND SENSOR");

				PhysicsManager.addContactListener(lis);

				Vec2[] points = ((PolygonShape) (boundSensor.getFixtureList().getShape())).getVertices();
				Vec2[] points2 = points;
				points = new Vec2[((PolygonShape) (boundSensor.getFixtureList().getShape())).getVertexCount()];

				System.arraycopy(points2, 0, points, 0, points.length);

				/*for (Vec2 point : points)
				{
					System.err.println(point + " -> " + ((PolygonShape) (boundSensor.getFixtureList().getShape())).getVertexCount());
				}*/

				//TODO: Create a renderer for this
				Polygon pg = new Polygon(points);
				pg.offset = off;//.mul(32);
				RenderManagerCallback rcb = new RenderManagerCallback()
				{
					public void onSuccess()
					{
						my_render_ent = RenderManager.RenderWorld.createEntity();
						my_render_ent.addComponent(pg);
						my_render_ent.addToWorld();
					}
				};

				RenderManager.addCallback(rcb);

			}
		};
		PhysicsManager.getCreateBodyCall(bfc);
	}

	public void restore()
	{
		//System.err.println("Restoring Quad " + uuid + " " + size + " " + my_offset);
		if (boundSensor == null)
		{
			createSensor(my_offset.mul(2));
		}

		//System.err.println("Shift In Start");

		//Todo: Change this to only go after the quad's bodies
		BodyFactoryCallback bf = new BodyFactoryCallback()
		{
			public void onSuccess()
			{
				//System.err.println("Body Callback Start");
				for (Body bod : Bodies)
				{
					//System.err.println("Destroy " + bod);
					//PhysicsManager.PhysWorld.destroyBody(bod);
				}
				Bodies = new ArrayList<Body>();
				//System.err.println("Body Callback End");
			}
		};
		PhysicsManager.getCreateBodyCall(bf);
		//System.err.println("Checking that bodies were destroyed");
		while (Bodies.size() > 0)
		{
			//System.err.println("Waiting for bodies to die");
			try
			{
				Thread.sleep(1);
			} catch (Exception e)
			{
			}
		}
		//System.err.println("Bodies were destroyed");
		//System.err.println("Restoring from json " + bodies);
		for (String j : bodies)
		{

			//{"fixture":[{"restitution":0.3499999940395355,"density":0.4000000059604645,"circle":{"center":0,"radius":0.5},"friction":0.10000000149011612}],"userData":"cfb96f8c-aac7-4cc2-a252-70909eeba2ec","linearDamping":0.20000000298023224,"angularDamping":1,"massData-I":0.039269909262657166,"angle":-0.35397034883499146,"massData-mass":0.3141592741012573,"position":{"x":0.5085497498512268,"y":2.3520007133483887},"type":2,"linearVelocity":0,"angularVelocity":0}
			shiftIn(j);
		}
		//System.err.println("Restore End");
	}

	public void shiftIn(String js)
	{
		try
		{
			JSONObject j = new JSONObject(js);
			//System.err.println("Shift in " + j);

			BodyFactoryCallback bfc = new BodyFactoryCallback()
			{
				public void onSuccess()
				{
					//System.err.println("Construct callback " + j);
					try
					{

						try
						{
							String uuid = j.getString("userData");
							UUID id = UUID.fromString(uuid);
							Entity e = PhysicsManager.PhysicsWorld.getEntity(id);
							//System.err.println("Found " + e);
							if (e != null)
							{
								//"position":{"map":{"x":5.562504291534424,"y":5.77381706237793}}
								//TODO: Why doesnt this deserialize correctly?
								Body b = PhysicsManager.JSONifier.j2b2Body(PhysicsManager.PhysWorld, j);
								//PhysicsManager.JSONifier.
								b.setUserData(id);
								//System.err.println("Added body " + b + " " +b.getPosition());
								Bodies.add(b);
								e.addComponent(new PhysicsBody(b));
								e.changedInWorld();
							} else
							{
								System.err.println("Didnt get ent!");
							}
						} catch (Exception e)
						{
							e.printStackTrace();
							System.err.println("No uuid on body ... deserialization will be borked");
						}

					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			};

			PhysicsManager.getCreateBodyCall(bfc);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public void addBody(String bodyString)
	{
		try
		{
			//JSONObject obj = new JSONObject(bodyString);
			bodies.add(bodyString);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void beginContact(Contact contact)
	{

		Body imposingBody;

		if (contact.m_fixtureA.m_body.equals(boundSensor))
		{
			imposingBody = contact.m_fixtureB.m_body;
		} else if (contact.m_fixtureB.m_body.equals(boundSensor))
		{
			imposingBody = contact.m_fixtureA.m_body;
		} else
		{
			return;
		}

		//System.err.print("Contact Begin on " + uuid + ": Shift in ... ");
		//System.err.println(imposingBody.getUserData());
		//Where did this come from ... shift it in ... shift it out of old set

		if (!Bodies.contains(imposingBody))
		{
			Bodies.add(imposingBody);
		}
		UUID u = UUID.class.cast(imposingBody.getUserData());

		if (u != null)
		{
			Entity e = PhysicsManager.PhysicsWorld.getEntity(u);
			//TODO: Get owning quad. If set .. tell him to give it up
			e.addComponent(myQuadComponent);
		}

	}

	//TODO: Remove owningQuadComponent
	@Override
	public void endContact(Contact contact)
	{

		Body imposingBody;

		if (contact.m_fixtureA.m_body.equals(boundSensor))
		{
			imposingBody = contact.m_fixtureB.m_body;
		} else if (contact.m_fixtureB.m_body.equals(boundSensor))
		{
			imposingBody = contact.m_fixtureA.m_body;
		} else
		{
			return;
		}
		//System.err.print("Contact End on " + uuid + ": Shift out ... ");
		//System.err.println(imposingBody.getUserData());

		Bodies.remove(imposingBody);
		//JSONObject obj = shiftOut(imposingBody);
		/*if (obj != null)
		 {
		 //Hand it off?

		 System.err.println("Body shifted out ... what do? " + obj);
		 }*/
		UUID id = (UUID) imposingBody.getUserData();

		Entity e = PhysicsManager.PhysicsWorld.getEntity(id);
		OwningQuad q = e.getComponent(OwningQuad.class);

		if (q != null && q.myQuad == this)
		{
			e.removeComponent(OwningQuad.class);
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{

	}

	public Vec2 getOffset()
	{
		return my_offset;
	}
	
}
