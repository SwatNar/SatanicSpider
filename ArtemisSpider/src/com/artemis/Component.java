package com.artemis;

/**
 * A tag class. All components in the system must extend this class.
 * 
 * @author Arni Arent
 */
public interface Component 
{
	public void init(Entity e);
	public void cleanup(Entity e);
	public void receiveMessage(Message m);
}
