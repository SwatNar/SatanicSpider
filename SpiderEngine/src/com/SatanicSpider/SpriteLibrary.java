package com.SatanicSpider;

import com.SatanicSpider.Components.Render.Sprite;
import com.SatanicSpider.Management.Render.NodeChangeProxy;
import com.SatanicSpider.Management.Render.RenderManager;
import com.jme3.scene.Node;
import java.util.ArrayList;

public class SpriteLibrary
{

	static Node l_guiNode;
	private boolean l_staticLibrary;
	private Node l_node = new Node("");
	private ArrayList<Sprite> l_library = new ArrayList<Sprite>();

	public SpriteLibrary(String name, boolean staticLibrary)
	{
		l_node.setName(name);
		this.l_staticLibrary = staticLibrary;
	}

	public void setStatic(boolean staticLibrary)
	{
		this.l_staticLibrary = staticLibrary;
	}

	public void addSprite(Sprite sprite)
	{
		l_library.add(sprite);
		Node n = sprite.getNode();
		//n.rotate(FAST_MATH.PI, 0, 0);
		RenderManager.addNode(new NodeChangeProxy(n,l_node));
		//l_node.attachChild(n);
	}

	public void removeSprite(String name)
	{
		//TODO: Make this a hashmap to cut return times to nil
		for (int i = 0; i < l_library.size(); i++)
		{
			if (l_library.get(i).getName().equals(name))
			{
				l_library.remove(i);
			}
		}
	}

	public boolean getStatic()
	{
		return l_staticLibrary;
	}

	public Node getNode()
	{
		return l_node;
	}

	public ArrayList<Sprite> getLibrary()
	{
		return l_library;
	}

	public Sprite getSprite(int index)
	{
		return l_library.get(index);
	}

	public Sprite getSprite(String name)
	{
		//TODO: Make this a hashmap to cut return times to nil
		for (int i = 0; i < l_library.size(); i++)
		{
			if (this.l_library.get(i).getName().equals(name))
			{
				return this.l_library.get(i);
			}
		}
		return null;
	}

	public String getName()
	{
		return l_node.getName();
	}
}
