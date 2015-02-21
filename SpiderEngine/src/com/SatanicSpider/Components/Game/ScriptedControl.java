/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Game;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;
import java.util.HashMap;

/**
 *
 * @author bryant
 */

//TODO: Implement this ...
public class ScriptedControl implements Component
{
	public HashMap<String, Executable> comps = new HashMap<String, Executable>();
	 public void init(Entity e)
    {
	
    }

    public void cleanup(Entity e)
    {
	
    }

    public void receiveMessage(Message m)
    {
	
    }
	
	public void execute(String name, Entity context)
	{
		//System.err.println("Executing control: " + name);
		Executable ex = comps.get(name);
		if(ex != null)
		{
			ex.execute(context, null);
		}
		else
			System.err.println("No control found for: " + name);
	}
}
