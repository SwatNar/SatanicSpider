/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider;

import com.SatanicSpider.Storage.Storage;
import com.artemis.Entity;
import java.util.ArrayList;

/**
 *
 * @author bryant
 */
public class IndexedEntities
{
	ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public void index(Entity e)
	{
		entities.add(e);
		Storage.Store(this);
	}
}
