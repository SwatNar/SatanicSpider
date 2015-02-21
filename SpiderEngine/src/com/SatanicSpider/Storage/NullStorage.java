/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Storage;

import com.SatanicSpider.core.Configuration;
import java.util.UUID;

/**
 *
 * @author bryant
 */
public class NullStorage implements StorageConnector
{

	@Override
	public boolean Store(Object o)
	{
		if(Configuration.Storage.debug)
			System.err.println("Null Storage: Store: " + o);
		return true;
	}

	@Override
	public Object UnStore(Class c, UUID id)
	{
		return null;
	}

	@Override
	public void disable()
	{

	}

	@Override
	public void enable()
	{
		
	}
	
}
