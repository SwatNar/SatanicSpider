/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.artemis;

import java.util.HashMap;

/**
 *
 * @author bryant
 */
public class WorldManager
{

	static HashMap<String, World> worlds = new HashMap<String, World>();

	public static void setWorld(String name, World world)
	{
		worlds.put(name, world);
	}

	public static World getWorld(String name)
	{
		return worlds.get(name);
	}
}
