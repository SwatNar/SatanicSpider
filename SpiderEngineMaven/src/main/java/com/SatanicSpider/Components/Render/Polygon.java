/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Render;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.SatanicSpider.Callbacks.RenderManagerCallback;
import com.SatanicSpider.Management.Render.RenderManager;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class Polygon implements Component
{
    public void init(Entity e)
    {
	
    }

    public void cleanup(Entity e)
    {
		//System.err.println("Cleaning up Quad");
		//Geometry clone = my_geom.clone();
		if(my_mesh != null)
		{
			RenderManagerCallback rc = new RenderManagerCallback()
			{
				public void onSuccess()
				{
					//System.err.println("Removing from world");
					my_geom.removeFromParent();
				}
			};
			
			RenderManager.addCallback(rc);
		}
    }

    public void receiveMessage(Message m)
    {
	
    }
	
	public Vec2[] points;
	
	public Mesh my_mesh;
	
	public Geometry my_geom;
	
	public Vec2 offset;
	
	public Polygon()
	{
		
	}
	
	public Polygon(Vec2 ... points)
	{
		this.points = points;
	}
	
}
