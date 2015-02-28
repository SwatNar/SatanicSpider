/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.core;

import com.SatanicSpider.artemis.World;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author bryant
 */
public class Configuration
{

	public static UUID s_uuid = UUID.fromString("fe0da949-779a-420f-8528-366ef9f2fcbb");
	public UUID uuid = s_uuid;

	public static final float physUnitPerScreenUnit = 32;

	public static final boolean PhysicsDebug = false;
	public static final boolean ResourcesDebug = false;

	public static final StorageConfiguration Storage = new StorageConfiguration();
	public static final PhysicsConfiguration Physics = new PhysicsConfiguration();
	public static final RenderConfiguration Render = new RenderConfiguration();

	double rand = Math.random();
	
	transient public static Configuration curent;

	//public HashMap<String, Object> DynaConfigTable = new HashMap<String, Object>();

	public Integer version;

	public UUID playerUuid;
	
	public static final Integer current_version = 5;

	public ArrayList<World> worlds = new ArrayList<World>();
	
	/*
	 Gson gson=new Gson(); 
	 String json="{\"k1\":\"v1\",\"k2\":\"v2\"}";
	 Map<String,String> map=new HashMap<String,String>();
	 map=(Map<String,String>) gson.fromJson(json, map.getClass());
	 */
	public void initialize()
	{
		
		if (playerUuid == null)
		{
			System.err.println("Initializing Player For The First Time");
			playerUuid = UUID.randomUUID();
		}
		
		if(worlds == null)
		{
			worlds = new ArrayList<World>();
		}

		//DynaConfigTable.put("Config Version", current_version);
		version = current_version;

		//Storage.Storage.Store(this);
	}

	public void upgrade()
	{
		initialize();
		version = current_version;
	}

}
