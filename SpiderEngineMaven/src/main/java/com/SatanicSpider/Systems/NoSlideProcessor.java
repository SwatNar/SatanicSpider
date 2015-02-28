/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Systems;

import com.SatanicSpider.artemis.Aspect;
import com.SatanicSpider.artemis.ComponentMapper;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.EntitySystem;
import com.SatanicSpider.artemis.annotations.Mapper;
import com.SatanicSpider.artemis.utils.ImmutableBag;
import com.SatanicSpider.Components.NoSlide;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.StaticSprite;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author bryant
 */
public class NoSlideProcessor extends EntitySystem
{

	@Mapper
	ComponentMapper<NoSlide> nsm;

	@Mapper
	ComponentMapper<PhysicsBody> pbm;

	
	//This will be used to add dust i guess
	@Mapper
	ComponentMapper<StaticSprite> ssm;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public NoSlideProcessor()
	{

		//need to also get for some sort of positionable material
		super(Aspect.getAspectForAll(NoSlide.class,PhysicsBody.class));
		//pm = new ComponentMapper<PhysicsBody>(PhysicsBody.class, world);

		//this.camera = camera;
	}

	@Override
	protected void initialize()
	{
		// batch = new SpriteBatch();
		System.err.println("Initializing");
		System.err.println("World: " + world);
		if (world != null)
		{
			if (nsm == null)
			{
				nsm = world.getMapper(NoSlide.class);
			}

			if (pbm == null)
			{
				pbm = world.getMapper(PhysicsBody.class);
			}
			if (ssm == null)
			{
				ssm = world.getMapper(StaticSprite.class);
			}

			if (!(nsm == null || pbm == null || ssm == null))
			{
				initialized = true;
			}
		}

	}

	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities)
	{

		for (int i = 0; i < entities.size(); i++)
		{
			process(entities.get(i));
		}
	}

	@Override
	protected void begin()
	{
		//batch.setProjectionMatrix(camera.combined);
		//batch.begin();
	}

	protected void process(Entity e)
	{

		if (!initialized)
		{
			initialize();
		}

		if (initialized)
		{
			Body myBod = pbm.getSafe(e).body;
			Vec2 impulse = myBod.getWorldVector(new Vec2(1, 0));
			impulse = impulse.mul(Vec2.dot(impulse, myBod.getLinearVelocity()));
			impulse = impulse.mul(-1 * myBod.getMass());
			myBod.applyLinearImpulse(impulse, myBod.getWorldCenter());
		}
	}

	
	
	/*Vec2 getLateralVelocity()
	{
		Vec2 currentRightNormal = myBod.getWorldVector(new Vec2(1, 0));
		
		//b2Math.b2Dot();
		
		return currentRightNormal.mul(Vec2.dot(currentRightNormal, myBod.getLinearVelocity()));
	}
	
	
	Vec2 impulse = getLateralVelocity().mul(-1 * myBod.getMass());
		myBod.applyLinearImpulse(impulse, myBod.getWorldCenter());
		Vec2 pos = myBod.getPosition().mul(Configuration.physUnitPerScreenUnit);*/
}
