/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;

/**
 *
 * @author bryant
 */
public class Angle implements Component
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
	public float angle;
	
	public Angle(float angle)
	{
		this.angle = angle;
	}
}
