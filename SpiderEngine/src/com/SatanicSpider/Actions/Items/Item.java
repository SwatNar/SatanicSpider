/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Actions.Items;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;

/**
 *
 * @author bryant
 */
public abstract class Item  implements Component
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
	protected long lastTime = System.currentTimeMillis();
	public long timeout = 1000; //In ms
	
	public abstract void init();
	
	public final void step()
	{
		if(getUse())
		{
			if(System.currentTimeMillis() - lastTime > timeout)
				use();
		}
	}
	
	public abstract boolean getUse(); //Defines if the item is being used
	public abstract void use(); //Defines what the Item actually does
	
}
