/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Systems;

import com.SatanicSpider.artemis.Aspect;
import com.SatanicSpider.artemis.ComponentMapper;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.annotations.Mapper;
import com.SatanicSpider.artemis.systems.EntityProcessingSystem;
import com.SatanicSpider.Components.BodyLinearVelocity;
import com.SatanicSpider.Components.Game.Damage;
import com.SatanicSpider.Components.Game.Health;
import com.SatanicSpider.Components.Parent;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Factories.EntityFactory;

/**
 *
 * @author bryant
 */
public class BodyLinerarVelocityApplier extends EntityProcessingSystem
{

	
	@Mapper
	ComponentMapper<BodyLinearVelocity> blvm;
	@Mapper
	ComponentMapper<PhysicsBody> pbm;
	

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public BodyLinerarVelocityApplier()
	{
		//TODO: If I set this to the BodyLinearVelocity class as well Artemis doesnt pick up on the components ... why
		super(Aspect.getAspectForAll(PhysicsBody.class));
		System.err.println("---------------Creating LVA system----------------------");
		
	}

	protected void process(Entity e)
	{
		//System.err.println("LINBEAR VELOCITY");
		//TODO: Figure out why Artemis is stupid
		if(blvm.has(e))
		{
			BodyLinearVelocity blv = blvm.getSafe(e);

			if (pbm.has(e))
			{
				System.err.println("Physics Body Found");
				PhysicsBody pb = pbm.getSafe(e);

				if(pb != null)
					pb.body.setLinearVelocity(blv.velocity);
			}
			else
			{
				System.err.println("No Physics Body");
			}
		}
	}

	@Override
	protected void end()
	{
		//batch.end();
	}
}
