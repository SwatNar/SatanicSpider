/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Scripting;

import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Factories.EntityFactory;
import com.artemis.Entity;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class Exposure
{
	public static Entity player;
	public static void spawn_tree()
	{
		Position p = player.getComponent(Position.class);
		System.err.println("Spawing tree at " + p.pos);
		EntityFactory.createTree(p.pos, 0);
	}
	public static void spawn_tree_offset(Vec2 offset)
	{
		//TODO: Create tree doesnt obay the threadsafe ops
		Position p = player.getComponent(Position.class);
		offset = offset.add(p.pos);
		System.err.println("Spawing tree offset at " + offset);
		EntityFactory.createTree(offset, 0);
	}
	
	
	/*
	TODO:
	1) Spawn mobs
		What is a mob?
		Components:
			Sprite
			Position
			Physics
				Body
				Sensors
			Health
			Speed
			Weapon
				Damage
				Range
				Speed
				Wait
				Animation/Sprite
			Inventory
			AI/Controller
	2) Spawn movable boxes
	3) Select closest object
	4) Move selected object
	
	*/
	
	
	
	
}
