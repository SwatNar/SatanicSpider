/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import com.SatanicSpider.Actions.KeyboardActionMapper;

/**
 *
 * @author bryant
 */
public class KeyboardInput implements Component
{
    public void init(Entity e)
    {
	
    }

    public void cleanup(Entity e)
    {
	
    }

    public void receiveMessage(Message m)
    {
	
    }
	public KeyboardActionMapper[] binds;
	
	
	public KeyboardInput(KeyboardActionMapper ... binds)
	{
		this.binds = binds;
	}
}
