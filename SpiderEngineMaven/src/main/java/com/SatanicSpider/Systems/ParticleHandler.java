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
import com.SatanicSpider.artemis.systems.EntityProcessingSystem;
import com.SatanicSpider.artemis.utils.ImmutableBag;
import com.SatanicSpider.Components.Render.Particle;
import com.SatanicSpider.Components.Position;
import org.jbox2d.common.Vec2;


//import playn.core.Layer;

/**
 *
 * @author bryant
 */
public class ParticleHandler extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<Particle> pm;

	@Mapper
	ComponentMapper<Position> spm;
	
	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public ParticleHandler()
	{
		
		super(Aspect.getAspectForAll(Particle.class));
		System.err.println("creating particle system");
		//pm = new ComponentMapper<PhysicsBody>(PhysicsBody.class, world);

		//this.camera = camera;
	}

	/*@Override
	rotected void initialize()
	{
		// batch = new SpriteBatch();
		System.err.println("Initializing particle system");
		if (world != null)
		{
			if (pm == null)
			{
				pm = world.getMapper(Particle.class);
			}
			if (spm == null)
			{
				spm = world.getMapper(SimplePosition.class);
			}
		}
		
		initialized = pm != null;
	}*/

	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

	/*@Override
	protected void processEntities(ImmutableBag<Entity> entities)
	{

		for (int i = 0; i < entities.size(); i++)
		{
			process(entities.get(i));
		}
	}*/

	@Override
	protected void begin()
	{
		//batch.setProjectionMatrix(camera.combined);
		//batch.begin();
	}

	protected void process(Entity e)
	{
		/*if (!initialized)
		{
			initialize();
		}

		if (initialized)*/
		{
			//System.err.println("Rendering particle!");
			if(spm.has(e))
			{
				//TODO: SimpleMotion handler
				//How does I render without a position?
				
				Vec2 pos = spm.getSafe(e).pos;
				//System.err.println("Rendering particle! " + pos.x + "," + pos.y);
				//Layer lay = pm.getSafe(e).layer;
				//lay.transform().setTx(pos.x);
				//lay.transform().setTy(pos.y);
			}
			else
			{
				//System.err.println("Particle has no position!");
			}
						
		}
	}
}