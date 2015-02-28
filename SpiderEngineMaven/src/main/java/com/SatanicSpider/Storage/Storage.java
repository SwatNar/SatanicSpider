/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Storage;

import com.SatanicSpider.Callbacks.StorageCallback;
import com.SatanicSpider.core.Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author bryant
 */
public class Storage extends Thread
{

	StorageConnector con;

	private static Storage ref;

	ArrayList<Object> store_queue = new ArrayList<Object>();
	ArrayList<StorageCallback> unstore_queue = new ArrayList<StorageCallback>();
	HashMap<Class, StorageConnector> special_store = new HashMap<Class,StorageConnector>();

	public static boolean run = true;

	public Storage()
	{
		if (Configuration.Storage.debug)
		{
			System.err.println("Storage Construct: " + Configuration.Storage.storageMode);
		}
		switch (Configuration.Storage.storageMode)
		{
			case File:
			{
				con = new FileStorage();
			}
			;
			break;
			case Database:
			{

			}
			;
			break;
			case Remote:
			{

			}
			;
			break;
			case Ram:
			{
				if (Configuration.Storage.debug)
				{
					System.err.println("Storage Construct: RamStorage ...");
				}
				con = new RamStorage();
			}
			break;
			case None:
			{
				con = new NullStorage();
			}
			;
			break;
		}
		
		//TODO:	Construct the special storage drivers here
	}

	public static void initialize()
	{
		if (Configuration.Storage.debug)
		{
			System.err.println("Storage Initialize");
		}
		ref = new Storage();
		ref.start();
	}

	public static synchronized void Store(Object o)
	{
		getInstance().store_queue.add(o);
	}

	public static synchronized void async_unStore(StorageCallback cb)
	{
		getInstance().unstore_queue.add(cb);
	}

	public void run()
	{
		while (run)
		{
			if (store_queue.size() > 0)
			{
				Object o = store_queue.remove(0);
				//Store here
				if (Configuration.Storage.debug)
				{
					System.err.println("Storage Store");
				}
				if (ref == null)
				{
					initialize();
				}
				if (Configuration.Storage.debug)
				{
					System.err.println("Storage Store: " + o);
				}
				if(o == null)
                                    continue;
				StorageConnector c = ref.special_store.get(
                                        o.getClass());
				if(c == null)
				{
					boolean ret = ref.con.Store(o);
					if (Configuration.Storage.debug)
					{
						System.err.println("Storage Store: Ret " + ret);
					}
				}
				else
				{
					boolean ret = c.Store(o);
					if (Configuration.Storage.debug)
					{
						System.err.println("Special Storage Store: Ret " + ret);
					}
				}
			}
			if (unstore_queue.size() > 0)
			{
				StorageCallback cb = unstore_queue.remove(0);
				//unstore here
				if (Configuration.Storage.debug)
				{
					System.err.println("Storage Unstore: " + cb.GetUUID());
				}
				if (ref == null)
				{
					initialize();
				}

				Object o = unStore(cb.GetClass(), cb.GetUUID());
				cb.returnCallback(o);
			}
			
			yield();
		}
	}
	
	public static Storage getInstance()
	{
		if(ref == null)
			initialize();
		
		return ref;
	}
	
	public static Object unStore(Class clazz, UUID id)
	{
		return getInstance().con.UnStore(clazz, id);
	}
}
