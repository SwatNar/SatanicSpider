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
import com.SatanicSpider.Components.Render.Particle;
import com.SatanicSpider.Components.SimpleMotion;
import com.SatanicSpider.Components.Position;

/**
 *
 * @author bryant
 */
public class SimpleMotionProcessor extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<SimpleMotion> smm;

	@Mapper
	ComponentMapper<Position> spm;

	@SuppressWarnings("unchecked")
	public SimpleMotionProcessor()
	{
		super(Aspect.getAspectForAll(SimpleMotion.class,Position.class));
	}
	
	@Override
	protected boolean checkProcessing()
	{
		return true;
	}
	
	protected void begin()
	{
		//batch.setProjectionMatrix(camera.combined);
		//batch.begin();
	}

	protected void process(Entity e)
	{
		Position pos = spm.getSafe(e);
		SimpleMotion mot = smm.getSafe(e);
		pos.pos = pos.pos.add(mot.motion.mul(world.getDelta()/1000f));
		pos.changed = true;
	}
	
}
