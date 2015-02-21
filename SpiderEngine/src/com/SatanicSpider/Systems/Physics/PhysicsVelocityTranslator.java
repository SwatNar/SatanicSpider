/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Systems.Physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldManager;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.SatanicSpider.Components.Physics.Velocity;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import java.util.ArrayList;
import java.util.Collections;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class PhysicsVelocityTranslator extends EntityProcessingSystem
{
	@Mapper
	ComponentMapper<PhysicsBody> pbm;
	

	
	String source;
	ArrayList<String> destinations;
	
	World destinationWorld;
	ArrayList<World> SourceWorlds;
	
	public PhysicsVelocityTranslator(String dest,String ... sources)
	{
		
		super(Aspect.getAspectForAll(PhysicsBody.class));
		this.source = dest;
		this.destinations = new ArrayList<String>();
		Collections.addAll(this.destinations,sources);
		
		destinationWorld = WorldManager.getWorld(dest);
		
		SourceWorlds = new ArrayList<World>();
		
		for(String source:sources)
		{
			World w = WorldManager.getWorld(source);
			System.err.println("Adding Source World " + w + " from " + source);
			
			if(w != null)
				SourceWorlds.add(w);
		}
	}
	
	protected void process(Entity e)
	{
		if(pbm == null)
			return;
		PhysicsBody pb = pbm.getSafe(e);
		if(pb != null)
		{
			Vec2 finalVelocity = new Vec2(0,0);
			boolean flag = false;
			for(World w : SourceWorlds)
			{

				Entity ent = w.getEntity(e.getUuid());
				if(ent != null)
				{
					Velocity vel = ent.getComponent(Velocity.class);
					if(vel != null)
					{
						Vec2 velvel = vel.linear_velocity;
						if(velvel != null)
						{
							//System.err.println("Ent " + ent + " has a linear velocity of " + velvel + " from world " + w);
							flag = true;
							finalVelocity = finalVelocity.add(velvel);
						}
						
						ent.removeComponent(Velocity.class);
							
					}
						
				}

			}
			
			if(flag)
				pb.body.setLinearVelocity(finalVelocity);
		}
	}

}
