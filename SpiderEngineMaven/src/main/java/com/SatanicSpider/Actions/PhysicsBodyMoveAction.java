/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Actions;

import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.Components.BodyLinearVelocity;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author bryant
 */
public class PhysicsBodyMoveAction implements Action
{
	public Vec2 speed;
	public Body body;
	public Entity ent;
	private float magnitude;
	
	public PhysicsBodyMoveAction(Vec2 speed, Body body, Entity ent)
	{
		this.speed = speed;
		magnitude = speed.length();
		this.body = body;
		this.ent = ent;
	}
	
	
	public void act(float delta)
	{
		
		//TODO: This should add a component
		//TODO: The system should add all the components to get a result vector so you can do any direction as well.
		
		
		//body.applyForceToCenter(speed.mul(delta/1000f));
		//body.setLinearVelocity(speed);//.mul(delta/1000f));
		Vec2 spd = speed.clone();
		BodyLinearVelocity l = ent.getComponent(BodyLinearVelocity.class);
		if(l != null)
			spd = spd.add(l.velocity);
		//Make it the unit vector
		spd.normalize();
		//Multiply by the magnitude
		spd = spd.mul(magnitude);
		BodyLinearVelocity blv = new BodyLinearVelocity(spd);
		System.err.println("Applying velocity of " + spd + " " + blv);
		ent.addComponent(blv);
	}
	
	public String toString()
	{
		return "Physics Move " + speed;
	}
}
