/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Physics;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author bryant
 */
public class PhysicsBody implements Component
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
	public Body body;
	
	public PhysicsBody(Body bod)
	{
		//System.err.println("Adding a body of " + bod);
		this.body = bod;
	}
	
	public PhysicsBody()
	{
		//System.err.println("Adding a body of nothing");
		//TODO: Create an empty body
	}
	
}
