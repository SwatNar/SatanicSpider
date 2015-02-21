/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems.Game;

import com.SatanicSpider.Components.Game.Controller;
import com.SatanicSpider.Components.Game.FightControllerFlag;
import com.SatanicSpider.Components.Game.MoveSpeed;
import com.SatanicSpider.Components.Game.ScriptedControl;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

/**
 *
 * @author bryant
 */
public class FightController extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<Controller> pbm;

	@Mapper
	ComponentMapper<ScriptedControl> scm;

	public FightController()
	{
		super(Aspect.getAspectForAll(FightControllerFlag.class, Controller.class, ScriptedControl.class));
		System.err.println("EDC Init");
	}

	protected void process(Entity e)
	{

		Controller cm = pbm.getSafe(e);

		if (cm != null && (cm.one || cm.two || cm.three || cm.four))
		{
			ScriptedControl sc = scm.getSafe(e);
			if(sc != null)
			{
				if(cm.one)
					sc.execute("Attack 1", e);
				if(cm.two)
					sc.execute("Attack 2", e);
				if(cm.three)
					sc.execute("Attack 3", e);
				if(cm.four)
					sc.execute("Attack 4", e);
			}
		}
		else
		{
			
		}
	}

}
