/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Render;

import com.SatanicSpider.Management.Render.NodeChangeProxy;
import com.SatanicSpider.Management.Render.PositiveNode;
import com.SatanicSpider.Management.Render.RenderManager;
import com.SatanicSpider.SharedVars;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;
import com.jme3.scene.Node;
//import playn.core.Layer;
//import static playn.core.PlayN.graphics;

/**
 *
 * @author bryant
 */
public class Particle implements Component
{
	public boolean addedFlag = false;
	ParticleInitilizer i;
	
    public void init(Entity e)
    {
			System.err.println("Particle: init");
		
    }
	
	public void inititilize()
	{
		System.err.println("Particle: Initilize");
		i.inititilize(this);
		System.err.println("Particle: " + Particle);
		//SharedVars.rootNode.attachChild(Particle);
		RenderManager.addNode(new NodeChangeProxy(Particle, SharedVars.rootNode));
	}

    public void cleanup(Entity e)
    {
		if(Particle != null)
		{
			//SharedVars.rootNode.detachChild(Particle);
			
			RenderManager.removeNode(new NodeChangeProxy(Particle, SharedVars.rootNode));
		}
    }

    public void receiveMessage(Message m)
    {
	
    }
	
	public Particle(ParticleInitilizer i)
	{
		this.i = i;
	}
	
	public Node Particle;
}
