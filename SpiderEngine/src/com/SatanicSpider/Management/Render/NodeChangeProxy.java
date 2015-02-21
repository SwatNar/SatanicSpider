/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Management.Render;

import com.jme3.scene.Node;

/**
 *
 * @author bryant
 */
public class NodeChangeProxy 
{
	
	public Node changer;
	public Node changed;
	
	public NodeChangeProxy(Node add, Node to)
	{
		changer = add;
		changed = to;
	}
	
	
}
