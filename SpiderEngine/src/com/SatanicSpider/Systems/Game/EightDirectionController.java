/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems.Game;

import com.SatanicSpider.Components.Game.Controller;
import com.SatanicSpider.Components.Game.EightDirectionControllerFlag;
import com.SatanicSpider.Components.Game.MoveSpeed;
import com.SatanicSpider.Components.Physics.Velocity;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.jme3.math.Vector3f;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class EightDirectionController extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<Controller> pbm;

	@Mapper
	ComponentMapper<MoveSpeed> msm;

	public EightDirectionController()
	{
		super(Aspect.getAspectForAll(EightDirectionControllerFlag.class));
		System.err.println("EDC Init");
	}

	protected void process(Entity e)
	{

		Controller cm = pbm.getSafe(e);

		if (cm != null && cm.changed)
		{
			MoveSpeed spd = null;

			spd = msm.getSafe(e);

			cm.changed = false;
			int ud = 0;
			int lr = 0;

			if (cm.up)
			{
				ud++;
			}
			if (cm.down)
			{
				ud--;
			}
			if (cm.left)
			{
				lr--;
			}
			if (cm.right)
			{
				lr++;
			}
			//System.err.println(ud + " " + lr +  " " + e);
			Vector3f v = new Vector3f(lr, ud, 0).normalize();
			Vec2 o = new Vec2(v.x, v.y);
			if (spd != null)
			{
				o = o.mul(spd.speed);
			}

			Velocity ov = new Velocity();
			ov.linear_velocity = o;

			e.addComponent(ov);
		}
	}

}
