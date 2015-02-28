/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Storage.Artimis;

import com.SatanicSpider.Serialization.Serilization;
import com.SatanicSpider.Storage.StorageConnector;
import com.SatanicSpider.core.Configuration;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.World;
import com.SatanicSpider.artemis.WorldManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONObject;

/**
 *
 * @author bryant
 */
public class EntityRamStorage implements StorageConnector
{
	//<World<Entid,Data>>
	
		//TODO: This needs to be by the world name OR the world id if we are constructing worlds with set ids.
		//Because if we dont when we deser the world will be null;
	HashMap<String, HashMap<UUID, String>> storage = new HashMap<String, HashMap<UUID, String>>();

	boolean enabled = true;

	//Gson gson = new Gson();

	public EntityRamStorage()
	{
		if (Configuration.Storage.debug)
		{
			System.err.println("RamStorage: Construct");
		}
	}

	@Override
	public boolean Store(Object o)
	{
		
		//TODO: This needs to be by the world name OR the world id if we are constructing worlds with set ids.
		//Because if we dont when we deser the world will be null;
		if(o instanceof Entity)
		{
			boolean ret = false;
			if (Configuration.Storage.debug)
			{
				System.err.println("RamStorage: Store " + o);
			}
			try
			{
				Entity e = (Entity)o;
				
				String c = e.getWorld().name;
				
				JSONObject obj = new JSONObject(Serilization.getInstance().gson.toJson(o));

				String uuid = obj.getString("uuid");
				if (Configuration.Storage.debug)
				{
					System.err.println("RamStorage: ID " + uuid);
				}
				if (uuid == null)
				{
					if (Configuration.Storage.debug)
					{
						System.err.println("Object [" + o + "] did not have a uuid");
					}
				} else
				{
					HashMap<UUID, String> storage_obj = storage.get(c);
					if (storage_obj == null)
					{
						if (Configuration.Storage.debug)
						{
							System.err.println("RamStorage: Generating new storage pool for " + c);
						}
						storage_obj = new HashMap<UUID, String>();
						storage.put(c, storage_obj);
					}
					if (Configuration.Storage.debug)
					{
						System.err.println("RamStorage: Placing Object Into Storage Pool\n" + obj.toString());
					}
					storage_obj.put(UUID.fromString(uuid), obj.toString());
					if (Configuration.Storage.debug)
					{
						System.err.println("RamStorage: Storage Pool Now Contains " + storage_obj.keySet().size() + " objects");
					}
					ret = true;
				}
			} catch (Exception ex)
			{
				System.err.println("Unknown error");
				ex.printStackTrace();
			}

			return ret;
		}
		return false;
	}

	@Override
	public Object UnStore(Class c, UUID id)
	{
		//TODO: How do I add an entity to a world when the ent has already be constructed
		
		if (Configuration.Storage.debug)
		{
			System.err.println("EntityRamStorage: UnStore: " + c + " " + id);
		}

		if (!enabled)
		{
			return null;
		}

		ArrayList<Entity> ents = new ArrayList<Entity>();
		
		for(String key: storage.keySet())
		{
			HashMap<UUID, String> sto = storage.get(key);
			if (sto != null)
			{
				Object o = Serilization.getInstance().gson.fromJson(sto.get(id), c);


				if (Configuration.Storage.debug)
				{
					System.err.println("EntityRamStorage: Returning " + o);
				}
				
				if(o != null)
				{
					World w = WorldManager.getWorld(key);
					if(w != null)
						w.addEntity((Entity)o);
					else
						System.err.println("WORLD " + key + " MISSING");
				}

			}
		}
		
		return ents;
	}

	@Override
	public void disable()
	{
		if (Configuration.Storage.debug)
		{
			System.err.println("RamStorage: Disable");
		}
		enabled = false;
	}

	@Override
	public void enable()
	{
		if (Configuration.Storage.debug)
		{
			System.err.println("RamStorage: Enable");
		}
		enabled = true;
	}

}
