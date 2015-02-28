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
import com.SatanicSpider.Components.Game.Health;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Components.StaticSprite;
//import com.satanicspider.topdownmmo.topdownmmo.PhysicsBody;
//import com.satanicspider.topdownmmo.topdownmmo.Components.mport com.satanicspider.topdownmmo.topdownmmo.Componentste;
import com.SatanicSpider.Components.Render.Sprite;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class StaticBodyRenderer extends EntityProcessingSystem
{
//TODO: Refactor this to render any sprite
	@Mapper
	ComponentMapper<PhysicsBody> pbm;
	@Mapper
	ComponentMapper<Position> spm;
	@Mapper
	ComponentMapper<Sprite> sm;

	//@Mapper
	//ComponentMapper<Parent> pm;

	@Mapper
	ComponentMapper<Health> hm;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public StaticBodyRenderer()
	{
		super(Aspect.getAspectForOne(PhysicsBody.class, StaticSprite.class));

	}

	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

	@Override
	protected void begin()
	{

	}

	protected void process(Entity e)
	{
		Vec2 pos = new Vec2(0, 0);
		float angle = 0;

		//TODO: Get the top level item - done needs modifications to moving
		//TODO: Component to prevent top leveling
		//TODO: Detect an entity loop - Done ... needs testing/optimizing
		//Find all the parent components and apply them to a stack
		//Get the translations (positions of each) and add them together
		//TODO: Use the SimpleAngle component as well
	//	Entity cur = e;
	//	HashMap<Entity, Boolean> used = new HashMap<Entity, Boolean>();
	//	used.put(e, true);
		float p_ang = 0;
	/*	while (pm.has(cur))
		{
			
			cur = pm.getSafe(cur).parent;
			if (used.get(cur) == null)
			{
				used.put(cur, true);

				//System.err.println("Ent has a parent");
				Vec2 tmp = new Vec2(0, 0);
				float ang = 0;
				PhysicsBody peanut = cur.getComponent(PhysicsBody.class);
				if (peanut != null)
				{
					tmp = peanut.body.getPosition().mul(32);
					ang = peanut.body.getAngle();
					p_ang = ang;
				} else
				{
					//need to also have an angle component and  get it
					SimplePosition jelly = spm.getSafe(cur);
					if (jelly != null)
					{
						tmp = jelly.pos;
					}
				}
				//System.err.println("\tadding" + tmp);
				
				
				pos = pos.add(tmp);
				angle += ang;
			} else
			{
				System.err.println("ENTITY LOOP DETECTED");
			}
		}*/

		Body b = pbm.getSafe(e).body;
		if (b != null)
		{
			//TODO: get the body once ...

			Vec2 tmp = b.getPosition().mul(32);
			float ang = b.getAngle();
			pos = pos.add(tmp);
			angle += ang;

		} else if (spm.has(e))
		{
			
			/*if(pm.has(e))
			{
				//This wont work with more then 1 parent ...
				Vec2 tmp = spm.getSafe(e).pos;
				float xprime = (float)(tmp.x*Math.cos(p_ang) - tmp.y*Math.sin(p_ang));
				float yprime = (float)(tmp.x*Math.sin(p_ang) + tmp.y*Math.cos(p_ang));
				//System.out.println(xprime + " " + yprime);
				pos = pos.add(new Vec2(xprime,yprime));
			}
			else*/
				pos = pos.add(spm.getSafe(e).pos);
		}

		//TODO: offsets with rotations are borked
		//if (sm.has(e)) //Assume we have a sprite
		{
		//System.err.println("Rendering sprite at " + pos);
			Sprite sprite = sm.get(e);
			Vec2 pos2 = pos;//.mul(32.0f);
			//System.err.println("Rendering " + e + " at " + pos2);
			sprite.moveAbsolute((int)pos2.x, (int)pos2.y);
			//Vec2 pos = body.body.getPosition().mul(32);
			//sprite.imageLayer.setOrigin(sprite.width / 2.0f, sprite.height / 2.0f);
			//sprite.imageLayer.transform().setRotation(angle);
			//sprite.imageLayer.transform().setTx(pos.x);
			//sprite.imageLayer.transform().setTy(pos.y);

		}
		//else
		//{
		//	System.err.println(e + " Does not have a sprite ???? ");
		//}

	}

	@Override
	protected void end()
	{

	}
}
