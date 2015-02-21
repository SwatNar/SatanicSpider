/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class SimpleMotion implements Component
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
	
	//TODO: Random Motion and Other Motion Profiles
	public Vec2 motion;
	
	public SimpleMotion(Vec2 motion)
	{
		this.motion = motion;
	}
}
