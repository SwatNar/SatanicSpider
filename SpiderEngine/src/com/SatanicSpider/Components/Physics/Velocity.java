/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Physics;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;
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
}
