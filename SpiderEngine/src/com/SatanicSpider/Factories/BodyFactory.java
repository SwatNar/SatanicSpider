/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Factories;

import com.SatanicSpider.Callbacks.BodyFactoryCallback;
import com.SatanicSpider.core.Configuration;
import com.SatanicSpider.Management.Physics.PhysicsManager;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author bryant
 */
@Deprecated
public class BodyFactory
{
	
	
	private static boolean locked = false;
	
	//public static org.jbox2d.dynamics.World PhysWorld;
	
	public static Body getBody(BodyDef def)
	{
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a body with " + def);
		}
		
		Body ret = null;
		
		
		ret = PhysicsManager.PhysWorld.createBody(def);
		ret.setSleepingAllowed(true);
		
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
		
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a circle body");
		}
		
		
		FixtureDef fixtureDef = new FixtureDef();
		
		Body body = getBody(blankDynamicBodyDef());
		while(body == null)
			body = getBody(blankDynamicBodyDef());
		if(body != null)
		{

			CircleShape circleShape = new CircleShape();
			circleShape.m_radius = 0.5f;
			fixtureDef.shape = circleShape;
			fixtureDef.density = 0.4f;
			fixtureDef.friction = 0.1f;
			fixtureDef.restitution = 0.35f;
			circleShape.m_p.set(0, 0);
			body.createFixture(fixtureDef);
			body.setLinearDamping(0.2f);
			//body.setTransform(new Vec2(x, y), angle);
		}
		else
		{
			System.err.println("BODY IS NULL!");
		}
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
		if(dynamic)
			body = getBody(blankDynamicBodyDef());
		else
			body = getBody(blankStaticBodyDef());

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
		return body;

	}
	public static Body squareBody(Vec2 size, boolean dynamic)
	{
		
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Creating a circle body");
		}
		
		FixtureDef fixtureDef = new FixtureDef();
		
		Body body;
		if(dynamic)
			body = getBody(blankDynamicBodyDef());
		else
			body = getBody(blankStaticBodyDef());

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
		return body;

	}
	
}
