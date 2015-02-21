/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems.Render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Components.Render.DamageParticle;
import com.SatanicSpider.Components.Render.Particle;
import com.SatanicSpider.SharedVars;
import com.artemis.systems.EntityProcessingSystem;
//import playn.core.Surface;

/**
 *
 * @author bryant
 */
public class ParticleRenderer extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<Particle> pam;
	@Mapper
	ComponentMapper<Position> pom;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public ParticleRenderer()
	{

		//need to also get for some sort of positionable material
		super(Aspect.getAspectForOne(Particle.class));
		//pm = new ComponentMapper<PhysicsBody>(PhysicsBody.class, world);

		//this.camera = camera;
	}


	protected void process(Entity e)
	{
	    //TODO: Redraw this ...
		//System.err.println("ParticleRenderer: process");
		Particle p = pam.getSafe(e);
		if(p != null)
		{
			if(!p.addedFlag)
			{
				System.err.println("ParticleRenderer: Initilizing particle");
				p.inititilize();
				//SharedVars.rootNode.attachChild(p.Particle);
				p.addedFlag = true;
			}
			//System.err.println("ParticleRenderer: Rendering Particle");
			Position po = pom.getSafe(e);
			if(po != null && po.changed)
			{
				po.changed = false;
				System.err.println("ParticleRenderer: Has Position " + po.pos);
				p.Particle.setLocalTranslation(po.pos.x, po.pos.y, p.Particle.getLocalTranslation().z);
			}
			//See if the particle has moved ...
		}
		else
		{
			System.err.println("ParticleRenderer: Particle was null!");
		}

	}
}
