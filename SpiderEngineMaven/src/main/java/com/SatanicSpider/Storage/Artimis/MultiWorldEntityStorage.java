/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Storage.Artimis;

import com.SatanicSpider.Storage.StorageConnector;
import com.SatanicSpider.artemis.Entity;
import java.util.UUID;

/**
 *
 * @author bryant
 */
public class MultiWorldEntityStorage implements StorageConnector
{

	EntityStorageConnector myCon;

	public MultiWorldEntityStorage(EntityStorageConnector c)
	{
		myCon = c;
	}

	public boolean Store(Object o)
	{
		if (o instanceof Entity)
		{
			return myCon.Store(o);
		}
		return false;
	}

	public Object UnStore(Class c, UUID id)
	{
		if (c.isAssignableFrom(Entity.class))
		{
			return myCon.UnStore(c, id);
		}
		return null;
	}

	public void disable()
	{
		myCon.disable();
	}

	public void enable()
	{
		myCon.enable();
	}
}
