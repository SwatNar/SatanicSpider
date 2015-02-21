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
public class PositiveNode
{
	public Node to_be_added;
	public Node to_be_added_to;
	
	public PositiveNode(Node add, Node to)
	{
		to_be_added = add;
		to_be_added_to = to;
	}
}
