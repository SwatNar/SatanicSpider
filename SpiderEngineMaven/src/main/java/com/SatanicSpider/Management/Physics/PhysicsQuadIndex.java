/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Management.Physics;

import com.SatanicSpider.Storage.Storage;
import com.SatanicSpider.core.Configuration;
import java.util.HashMap;
import java.util.UUID;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class PhysicsQuadIndex
{
	public UUID uuid = Configuration.Physics.QuadIndexUUID;
	private static final HashMap<Vec2, UUID> quad_uuid = new HashMap<Vec2, UUID>();
	
	public void put(Vec2 key, UUID id)
	{
		quad_uuid.put(key, id);
		Storage.Store(this);
	}
	
	public UUID get(Vec2 key)
	{
		return quad_uuid.get(key);
	}
}
