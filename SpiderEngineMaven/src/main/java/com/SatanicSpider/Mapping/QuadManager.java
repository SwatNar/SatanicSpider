/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Mapping;

import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author bryant
 */
public class QuadManager
{
	static HashMap<UUID,Quad> quads = new HashMap<UUID,Quad>();
	
	static UUID index;
	
	public static Quad getCurrentQuad()
	{
		return quads.get(index);
	}
	
	public static UUID registerQuad(Quad q)
	{
		UUID ret = UUID.randomUUID();
		quads.put(ret, q);
		if(index == null)
			index = ret;
		return ret;
	}
	
	public static void setCurrentQuad(UUID quad)
	{
		Quad cur = quads.get(quad);
		if(cur == null)
		{
			cur = new Quad();
			quads.put(quad, cur);
		}
	}
}
