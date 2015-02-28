/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Physics;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class Velocity implements Component
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

	public Vec2 linear_velocity;
	public Vec2 rotational_velocity;
    public Velocity(){}
    public Velocity(Vec2 linearVel)
    { 
        this.linear_velocity = linearVel;
    }
    public Velocity(float x, float y)
    { 
        this.linear_velocity = new Vec2(x,y);
    }
}
