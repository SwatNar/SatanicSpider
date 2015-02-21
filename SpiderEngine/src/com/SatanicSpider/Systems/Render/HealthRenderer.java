/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.SatanicSpider.Components.Game.Health;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.StaticSprite;
import com.jme3.scene.control.BillboardControl;
import org.jbox2d.common.Vec2;
//import playn.core.Surface;

/**
 *
 * @author bryant
 */
public class HealthRenderer extends EntitySystem
{

	@Mapper
	ComponentMapper<Health> hm;

	@Mapper
	ComponentMapper<PhysicsBody> pbm;

	@Mapper
	ComponentMapper<StaticSprite> ssm;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public HealthRenderer()
	{

		//need to also get for some sort of positionable material
		super(Aspect.getAspectForAll(Health.class));
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
			if (hm == null)
			{
				hm = world.getMapper(Health.class);
			}

			if (pbm == null)
			{
				pbm = world.getMapper(PhysicsBody.class);
			}
			if (ssm == null)
			{
				ssm = world.getMapper(StaticSprite.class);
			}

			if (!(hm == null || pbm == null || ssm == null))
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
	    //TODO: Redraw this ...

		if (!initialized)
		{
			initialize();
		}

		if (initialized)
		{

			Vec2 pos = new Vec2(0, 0);
			Vec2 size = new Vec2(0, 0);

			if (pbm.has(e))
			{
				PhysicsBody pb = pbm.getSafe(e);
				pos = pb.body.getPosition().mul(32);
			}
			if (ssm.has(e))
			{
				StaticSprite ss = ssm.getSafe(e);
				size = new Vec2(ss.width, ss.height);
			}

			Health hp = hm.getSafe(e);
			if(hp != null)
			{
				float percent = hp.health / (hp.max_health + 0.0f);
				BillboardControl c = new BillboardControl();
				
			}
			
		}
	}
}
