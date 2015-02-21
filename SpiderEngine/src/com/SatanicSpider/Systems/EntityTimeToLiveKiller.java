/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.SatanicSpider.Components.Render.Particle;
import com.SatanicSpider.Components.EntityTimeToLive;

/**
 *
 * @author bryant
 */
public class EntityTimeToLiveKiller extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<EntityTimeToLive> ttlm;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public EntityTimeToLiveKiller()
	{
		super(Aspect.getAspectForAll(EntityTimeToLive.class));
	}

	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

	@Override
	protected void begin()
	{
		//batch.setProjectionMatrix(camera.combined);
		//batch.begin();
	}

	protected void process(Entity e)
	{

		EntityTimeToLive ttl = ttlm.getSafe(e);
		ttl.ttl -= world.getDelta();
		if (ttl.ttl <= 0)
		{
				//System.err.println(e + " Has outlived his stay");

			if (e.getComponent(Particle.class) != null)
			{
			//s	((Particle) e.getComponent(Particle.class)).cleanup();
			}

			/*if(e.getComponent(Particle.class) != null)
			 {
			 ((Particle)e.getComponent(Particle.class)).cleanup();
			 }*/
			world.deleteEntity(e);

			e.deleteFromWorld();
		}

	}
}
