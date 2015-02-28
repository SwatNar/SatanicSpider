/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Management.Render;

import com.SatanicSpider.artemis.Entity;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Callbacks.RenderManagerCallback;
import com.SatanicSpider.core.Configuration;
import java.util.ArrayList;
import com.SatanicSpider.SpriteEngine;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class RenderManager
{
	
	float dir = 10;
	
	float max = 75;
	
	float min = 0;

	public static Entity camera_target;
	public float zoom = 14;
	public float zoom2 = 1;
	public static Camera cam;
	public static SpriteEngine engine = new SpriteEngine();

	public static com.SatanicSpider.artemis.World RenderWorld;

	private static RenderManager instance;

	public static Node rootNode;
	public static Node guiNode;
	
	private ArrayList<RenderManagerCallback> queuedCallbacks = new ArrayList<RenderManagerCallback>();
	
	private ArrayList<NodeChangeProxy> nodes_to_add = new ArrayList<NodeChangeProxy>();
	private ArrayList<NodeChangeProxy> nodes_to_remove = new ArrayList<NodeChangeProxy>();

	public static RenderManager getInstance()
	{
		if (instance == null)
		{
			instance = new RenderManager();
		}
		return instance;
	}

	public static void zoom_in()
	{
		getInstance().zoom -=0.1;
		System.err.println(getInstance().zoom);
	}
	
	public static void zoom_out()
	{
		getInstance().zoom +=0.1;
		System.err.println(getInstance().zoom);
	}
	
	public static void update(float tpf)
	{
		getInstance().iupdate(tpf);
	}
	
	public static void addNode(NodeChangeProxy n)
	{
		getInstance().nodes_to_add.add(n);
	}
	
	public static void removeNode(NodeChangeProxy n)
	{
		getInstance().nodes_to_remove.add(n);
	}
	
	
	
	public static synchronized void addCallback(RenderManagerCallback cb)
	{
		//TODO: This needs to lock ...
		getInstance().queuedCallbacks.add(cb);
	}
	
	public static int max_add_per_frame = 5;
	public static int max_remove_per_frame = 5;

	public void iupdate(float tpf)
	{
		for(int i = 0; nodes_to_add.size() > 0 && i < max_add_per_frame; i++)
		{
			NodeChangeProxy n = nodes_to_add.remove(0);
			n.changed.attachChild(n.changer);
		}
		for(int i = 0; nodes_to_remove.size() > 0 && i < max_remove_per_frame; i++)
		{
			NodeChangeProxy n = nodes_to_remove.remove(0);
			n.changed.detachChild(n.changer);
		}
		//zoom += dir*tpf;
		if(zoom > max)
		{
			zoom = max;
			dir *= -1;
		}
		
		if(zoom < min)
		{
			zoom = min;
			dir *= -1;
		}
		engine.update(tpf);
		RenderWorld.setDelta(tpf);
		RenderWorld.process();
		if (camera_target != null)
		{
			if (cam != null)
			{
				Position pos = camera_target.getComponent(Position.class);
				if (pos != null)
				{
					//Vec2 posp = pos.pos.add(Configuration.Render.CameraOffset);
					cam.setLocation(new Vector3f(pos.pos.x, pos.pos.y, zoom));
					//cam.setFrustum( 0, 15000, -100, 100, -100, 100 );
					//cam.update();
					
				} else
				{
					System.err.println("Pos was null " + camera_target + " " + camera_target.getUuid());
				}
			} else
			{
				System.err.println("Cam was null");
			}
		}
		
		while(queuedCallbacks.size() > 0)
		{
			RenderManagerCallback callback = queuedCallbacks.remove(0);
			
			if(callback != null)
				callback.onSuccess();
		}
	}
}
