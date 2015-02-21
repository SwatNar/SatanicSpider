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
import com.SatanicSpider.Components.Angle;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.Position;
import java.util.ArrayList;
import java.util.Collections;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

/**
 *
 * @author bryant
 */
public class PhysicsPositionalTranslator extends EntityProcessingSystem
{
	@Mapper
	ComponentMapper<PhysicsBody> pbm;
	

	
	String source;
	ArrayList<String> destinations;
	
	World sourceWorld;
	ArrayList<World> destinationWorlds;
	
	public PhysicsPositionalTranslator(String source,String ... destinations)
	{
		
		super(Aspect.getAspectForAll(PhysicsBody.class));
		this.source = source;
		this.destinations = new ArrayList<String>();
		Collections.addAll(this.destinations,destinations);
		
		sourceWorld = WorldManager.getWorld(source);
		
		destinationWorlds = new ArrayList<World>();
		
		for(String destination:destinations)
		{
			World w = WorldManager.getWorld(destination);
			//System.err.println("Adding World " + w + " from " + destination);
			
			if(w != null)
				destinationWorlds.add(w);
		}
	}
	
	protected void process(Entity e)
	{
		if(pbm == null)
		{
			//System.err.println("PBM was null!!!! " + e + " " + pbm);
			return;
		}
		PhysicsBody pb = pbm.getSafe(e);
		if(pb != null)
		{
			boolean awake = pb.body.isAwake();
			if(awake)
			{
				
				//System.err.println("Setting pos of " + e);
				boolean stat = pb.body.m_type.equals(BodyType.STATIC);
				
				if(stat)
					pb.body.setAwake(false);
				
				Vec2 position = pb.body.getPosition();//.mul(1.0f);
				float angle = pb.body.getAngle();
				boolean dynamic = pb.body.m_type == BodyType.DYNAMIC;

				Position pos = new Position(position);
				pos.dynamic = dynamic;
				Angle ang = new Angle(angle);

				
				
				
				for(World w : destinationWorlds)
				{

					Entity ent = w.getEntity(e.getUuid());
					if(ent != null)
					{
						//System.err.println("Translatoring positional info for " + ent + " " + ent.getUuid() + " from " + e + " in world " + w + " " + position);
						Boolean ciw = false;
						if(ent.getComponent(Position.class) == null || ent.getComponent(Angle.class) == null)
							ciw = true;

						ent.addComponent(pos);
						ent.addComponent(ang);

						if(ciw)
						{
							//System.err.println("Ent needs initializing ...");
							ent.changedInWorld();
						}
					}

				}
			}
		}
	}

}
