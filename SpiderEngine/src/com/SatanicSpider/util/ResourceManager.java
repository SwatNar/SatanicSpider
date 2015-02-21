/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.util;

import com.SatanicSpider.core.Configuration;
import java.util.HashMap;
//import playn.core.Image;
//import static playn.core.PlayN.assets;

/**
 *
 * @author bryant
 */
public class ResourceManager
{
	
/*	static HashMap<String,Image> imageCache = new HashMap<String,Image>();
	
	public static Image getImage(String path)
	{
		Image i = imageCache.get(path);
		
		if(i == null)
		{
			if(Configuration.ResourcesDebug)
			{
				System.err.println("ResourceManager: Cache miss on " + path);
			}
			
			i = assets().getImage(path);
			
			if(Configuration.ResourcesDebug && i == null)
			{
				System.err.println("ResourceManager: Error: Asset not found! - " + path);
			}
			imageCache.put(path, i);
		}
		else
		{
			if(Configuration.ResourcesDebug)
			{
				System.err.println("ResourceManager: Cache hit on " + path);
			}
		}
		
		return i;
	}
*/	
}
