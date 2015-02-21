/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.SatanicSpider.Components.ComponentTimeToLive;
import com.SatanicSpider.Components.EntityTimeToLive;
import com.SatanicSpider.Components.Render.Particle;

/**
 *
 * @author bryant
 */
public class ComponentTimeToLiveKiller extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<ComponentTimeToLive> ttlm;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public ComponentTimeToLiveKiller()
	{
		super(Aspect.getAspectForAll(ComponentTimeToLive.class));
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

		ComponentTimeToLive ttl = ttlm.getSafe(e);
		ttl.ttl -= world.getDelta();
		if (ttl.ttl <= 0)
		{
			e.removeComponent(ttl.component);
			e.removeComponent(ttl);
		}

	}
}
