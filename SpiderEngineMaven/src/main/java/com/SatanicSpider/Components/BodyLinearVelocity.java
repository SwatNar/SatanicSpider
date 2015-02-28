/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class BodyLinearVelocity implements Component
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
    
    
	public Vec2 velocity;
	
	public BodyLinearVelocity(Vec2 velocity)
	{
		this.velocity = velocity;
	}
	
	public String toString()
	{
		
		return "BodyLinearVelocity: " + velocity;
	}

    
}
