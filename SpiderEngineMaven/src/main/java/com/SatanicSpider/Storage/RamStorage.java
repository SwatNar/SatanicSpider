/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Storage;

//import com.google.gson.Gson;
import com.SatanicSpider.Serialization.Serilization;
import com.SatanicSpider.core.Configuration;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONObject;

/**
 *
 * @author bryant
 */
public class RamStorage implements StorageConnector
{

	HashMap<Class, HashMap<UUID, String>> storage = new HashMap<Class, HashMap<UUID, String>>();

	boolean enabled = true;

	//Gson gson = new Gson();

	public RamStorage()
	{
		if (Configuration.Storage.debug)
		{
			System.err.println("RamStorage: Construct");
		}
	}

	@Override
	public boolean Store(Object o)
	{
		boolean ret = false;
		if (Configuration.Storage.debug)
		{
			System.err.println("RamStorage: Store " + o);
		}
		try
		{
			Class c = o.getClass();
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

	@Override
	public Object UnStore(Class c, UUID id)
	{
		if (Configuration.Storage.debug)
		{
			System.err.println("RamStorage: UnStore: " + c + " " + id);
		}

		if (!enabled)
		{
			return null;
		}

		HashMap<UUID, String> sto = storage.get(c);
		if (sto != null)
		{
			Object o = Serilization.getInstance().gson.fromJson(sto.get(id), c);
			if (Configuration.Storage.debug)
			{
				System.err.println("RamStorage: Returning " + o);
			}
			return o;
		}
		if (Configuration.Storage.debug)
		{
			System.err.println("RamStorage: No Storage Pool Found!");
		}
		return null;
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
