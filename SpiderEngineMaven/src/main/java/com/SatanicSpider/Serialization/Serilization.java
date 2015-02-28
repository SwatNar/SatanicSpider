/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Serialization;

import com.SatanicSpider.artemis.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author bryant
 */
public class Serilization
{

	private static Serilization ref;

	public Gson gson;

	public static Serilization getInstance()
	{
		if (ref == null)
		{
			ref = new Serilization();
		}
		return ref;
	}

	public Serilization()
	{
		//TODO: Make this dynamically find and instantiate 
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Body.class, new BodySerializer());
		//gsonBuilder.registerTypeAdapter(Entity.class, new EntitySerializer());
		gson = gsonBuilder.create();
	}
}
