/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems.Render;

import com.SatanicSpider.artemis.Aspect;
import com.SatanicSpider.artemis.ComponentMapper;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.annotations.Mapper;
import com.SatanicSpider.artemis.systems.EntityProcessingSystem;
import com.SatanicSpider.Components.Angle;
import com.SatanicSpider.Components.Game.Health;
import com.SatanicSpider.Components.Physics.PhysicsBody;
import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Components.Render.HealthRender;
import com.SatanicSpider.Components.Render.Sprite;
import org.jbox2d.common.Vec2;

public class SpriteRenderer extends EntityProcessingSystem
{
//TODO: Refactor this to render any sprite

	@Mapper
	ComponentMapper<Position> pm;
	@Mapper
	ComponentMapper<Angle> am;
	@Mapper
	ComponentMapper<Sprite> sm;

	//@Mapper
	//ComponentMapper<Parent> pm;
	@Mapper
	ComponentMapper<Health> hm;
	
	@Mapper
	ComponentMapper<HealthRender> hrm;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public SpriteRenderer()
	{
		super(Aspect.getAspectForOne(PhysicsBody.class, Sprite.class, Angle.class));

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
		Position p = pm.getSafe(e);
		Angle a = am.getSafe(e);
		boolean changed = false;
		boolean isDynamic = false;

		if (p == null)
		{
			//System.err.println("Position was null!");
			//pos = new Vec2(10,10);
		} else
		{
			pos = p.pos;
			changed = p.changed;
			isDynamic = p.dynamic;
		}

		//TODO: Unretard this
		//if(a != null)
		//	angle = am.getSafe(e).angle;
		Sprite sprite = sm.getSafe(e);

		if (sprite != null && changed)
		{
			//System.err.println("Rendering " + e + " at " + pos);
			sprite.moveAbsolute(pos.x, pos.y);
			sprite.rotate(0, 0, angle);
			if (p != null && !isDynamic)
			{
				p.changed = false;
			}

			Health h = hm.getSafe(e);
			HealthRender hr = hrm.getSafe(e);
			
			if (h != null)
			{
				if(hr == null)
				{
					hr = new HealthRender();
					e.addComponent(hr);
				}
				
				else
					
				{
					hr.update(h.max_health,h.health);
				}
			}

		} else
		{
			//System.err.println("TRIED TO RENDER A NULL SPRITE");
		}

	}

	@Override
	protected void end()
	{

	}
}
