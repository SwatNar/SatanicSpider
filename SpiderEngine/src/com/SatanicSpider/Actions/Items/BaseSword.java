/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Actions.Items;

import com.artemis.Entity;
import com.artemis.World;

/**
 *
 * @author bryant
 */
public class BaseSword extends Item
{
	Entity parent;
	World world;
	
	int Damage;
	
	public BaseSword(Entity parent,World world)
	{
		this.parent = parent;
		this.world = world;
		Damage = 10;
	}
	
	@Override
	public void init()
	{
		
	}

	@Override
	public boolean getUse()
	{
		//this will continiously swing the sword
		return true;
	}

	@Override
	public void use()
	{
		//Create sword entity
		//Attach StaticSprite
		//Attach PhysicsBody
		//Attach Damageing
		//Attach Parent
		//Attach TileToLive
		//Set it in motion
	}
	
}
