package com.SatanicSpider.artemis.systems;

import com.SatanicSpider.artemis.Aspect;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.EntitySystem;
import com.SatanicSpider.artemis.utils.ImmutableBag;

/**
 * A typical entity system. Use this when you need to process entities
 * possessing the provided component types.
 *
 * @author Arni Arent
 *
 */
public abstract class EntityProcessingSystem extends EntitySystem
{
	//TODO: Does this still work? 
	//If the system is created before any entity has a component
	//Then it may not work
	
	public EntityProcessingSystem(Aspect aspect)
	{
		super(aspect);
	}

	/**
	 * Process a entity this system is interested in.
	 *
	 * @param e the entity to process.
	 */
	protected abstract void process(Entity e);

	@Override
	protected final void processEntities(ImmutableBag<Entity> entities)
	{
		for (int i = 0, s = entities.size(); s > i; i++)
		{
			process(entities.get(i));
		}
	}

	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

}
