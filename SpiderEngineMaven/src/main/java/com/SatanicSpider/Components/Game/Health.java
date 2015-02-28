/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Components.Game;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
//import static playn.core.PlayN.graphics;
//import playn.core.SurfaceLayer;

/**
 *
 * @author bryant
 */
public class Health implements Component
{
    public void init(Entity e)
    {
	
    }

    public void cleanup(Entity e)
    {
	
    }

    public void receiveMessage(Message m)
    {
	
    }

	public int health;
	public int max_health;
	
//	public SurfaceLayer surf;

	public Health(int hp)
	{
		this.health = hp;
		this.max_health = hp;
//		surf = graphics().createSurfaceLayer(40, 5);
//		graphics().rootLayer().add(surf);
	}
}
