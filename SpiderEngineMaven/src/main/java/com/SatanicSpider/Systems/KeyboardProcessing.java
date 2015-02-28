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
import com.SatanicSpider.Actions.KeyboardActionMapper;
import com.SatanicSpider.Components.KeyboardInput;
//import com.satanicspider.topdownmmo.topdownmmo.Control.KeyboardState;

/**
 *
 * @author bryant
 */
public class KeyboardProcessing extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<KeyboardInput> kim;
	
	//KeyboardState state;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public KeyboardProcessing()//KeyboardState state)
	{
		super(Aspect.getAspectForAll(KeyboardInput.class));
		//this.state = state;
	}

	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

	@Override
	protected void begin()
	{
		//batch.setProjectionMatrix(camera.combined);
		//batch.begin();
	}

	protected void process(Entity e)
	{

		KeyboardInput ttl = kim.getSafe(e);
		
		/*for(KeyboardActionMapper map : ttl.binds)
		{
			if(state.getKeyPressed(map.key))
			{
				System.err.println("Pressed: " + map.key + " doing " + map.action);
				map.action.act(world.getDelta());
			}
		}*/
	}
}
