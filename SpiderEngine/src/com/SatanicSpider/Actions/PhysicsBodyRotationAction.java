/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Actions;

import org.jbox2d.dynamics.Body;

/**
 *
 * @author bryant
 */
public class PhysicsBodyRotationAction implements Action
{
	float ang;
	Body body;
	
	public PhysicsBodyRotationAction(float ang, Body body)
	{
		this.ang = ang;
		this.body = body;
	}
	
	
	public void act(float delta)
	{
		//body.applyForceToCenter(speed.mul(delta/1000f));
		body.setTransform(body.getPosition(), ang);
		
	}
	
	public String toString()
	{
		return "Physics Angle " + ang;
	}
}
