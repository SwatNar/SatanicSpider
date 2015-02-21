/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Game;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;
 
public class Damage implements Component
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
	
	//TODO: Create a damage over time component
 
 public int damage;
 
 //Flag for processed?
  
 public Damage(int damage) {
  this.damage = damage;
  //System.err.println("Taking Damage!");
 }
  
}