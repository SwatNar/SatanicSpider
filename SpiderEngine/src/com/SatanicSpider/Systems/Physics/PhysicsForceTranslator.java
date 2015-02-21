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
import com.SatanicSpider.Components.Physics.Force;
import com.SatanicSpider.Components.Physics.Velocity;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import java.util.ArrayList;
import java.util.Collections;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class PhysicsForceTranslator extends EntityProcessingSystem
{
	@Mapper
	ComponentMapper<PhysicsBody> pbm;
	

	
	String source;
	ArrayList<String> destinations;
	
	World destinationWorld;
	ArrayList<World> SourceWorlds;
	
	public PhysicsForceTranslator(String dest,String ... sources)
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
		{
			//System.err.println("PBM was null!!!" + e + " " + pbm);
			return;
		}
		PhysicsBody pb = pbm.getSafe(e);
		if(pb != null)
		{
			Vec2 final_linear_force = new Vec2(0,0);
			float final_rotational_force = 0;
			boolean flag = false;
			for(World w : SourceWorlds)
			{

				Entity ent = w.getEntity(e.getUuid());
				if(ent != null)
				{
					Force force = ent.getComponent(Force.class);
					if(force != null)
					{
						Vec2 forfor = force.linear_force;
						float rotfor = force.rotational_force;
						if(forfor != null)
						{
							flag = true;
							final_linear_force = final_linear_force.add(forfor);
						}
						if(rotfor != 0)
						{
							flag = true;
							final_rotational_force += (rotfor);
						}
						
						
						
						ent.removeComponent(Force.class);
							
					}
						
				}

			}
			
			if(flag)
			{
				//System.err.println("Moving " + e + " with " + final_linear_force);
				pb.body.applyForceToCenter(final_linear_force);
				pb.body.applyTorque(final_rotational_force);
			}
		}
	}

}
