/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems.Game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.SatanicSpider.Components.Game.Damage;
import com.SatanicSpider.Components.Game.Health;
import com.SatanicSpider.Components.Invulnerability;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Factories.EntityFactory;
import org.jbox2d.common.Vec2;

public class DamageHandler extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<Damage> dm;
	
	@Mapper
	ComponentMapper<Health> hm;
	
	@Mapper
	ComponentMapper<Invulnerability> im;
	
	
	@Mapper
	ComponentMapper<Position> spm;
	
	

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public DamageHandler()
	{
		super(Aspect.getAspectForAll(Health.class));
		System.err.println("Creating damage system");
		
	}

	protected void process(Entity e)
	{
		if (dm.has(e))
		{
			if(hm.has(e))
			{
				Health hp = hm.getSafe(e);
				Damage dmg = dm.getSafe(e);
				hp.health -= dmg.damage;
				
				
				Vec2 pos = new Vec2(0,0);
				
				if(spm.has(e))
				{
					pos = spm.getSafe(e).pos;
					//System.err.println("Ent has a simple position");
				}
				
				Entity d_e = EntityFactory.createDamageParticle(dmg.damage,e);
								
				
				//d_e.addComponent(new Position(pos));
				//d_e.addComponent(new Parent(e));
				
				if(hp.health < 0)
				{
					//Death animation?
					hp.health = 0;
				}
					
				
				e.removeComponent(dmg);
				
			}
		}
	}

	@Override
	protected void end()
	{
		//batch.end();
	}
}
