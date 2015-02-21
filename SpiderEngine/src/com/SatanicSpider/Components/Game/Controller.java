/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Game;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;

/**
 *
 * @author bryant
 */
public class Controller implements Component
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

	public boolean up		= false;
	public boolean down		= false;
	public boolean left		= false;
	public boolean right	= false;
	
	public boolean one		= false;
	public boolean two		= false;
	public boolean three	= false;
	public boolean four		= false;
	
	
	
	
	
	
	
	public boolean changed	= false;
	
	
	
}
