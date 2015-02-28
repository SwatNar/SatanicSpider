package com.SatanicSpider.artemis;

import java.util.BitSet;

import com.SatanicSpider.artemis.utils.Bag;
import java.util.HashMap;
import java.util.UUID;

public class EntityManager extends Manager
{

	private Bag<Entity> entities;
	private HashMap<UUID,Entity> UUIDMap = new HashMap<UUID,Entity>();
	
	private BitSet disabled;

	private int active;
	private long added;
	private long created;
	private long deleted;

	private IdentifierPool identifierPool;

	public int getEntityCount()
	{
		return entities.size();
	}
	
	public EntityManager()
	{
		entities = new Bag<Entity>();
		disabled = new BitSet();
		identifierPool = new IdentifierPool();
	}

	@Override
	protected void initialize()
	{
	}

	protected Entity createEntityInstance()
	{
		//System.err.println("EntityManager: createInstance");
		Entity e = new Entity(world, identifierPool.checkOut());
		UUIDMap.put(e.getUuid(), e);
		created++;
		return e;
	}
	
	protected Entity createEntityInstance(UUID uuid)
	{
		//System.err.println("EntityManager: createInstance");
		Entity e = new Entity(world, identifierPool.checkOut(),uuid);
		UUIDMap.put(e.getUuid(), e);
		
		created++;
		return e;
	}

	@Override
	public void added(Entity e)
	{
		//System.err.print("Entity " + e + " ");
		//System.err.print(e.getUuid().toString() + " added");
		active++;
		added++;
		entities.set(e.getId(), e);
		UUIDMap.put(e.getUuid(), e);
	}

	@Override
	public void enabled(Entity e)
	{
		disabled.clear(e.getId());
	}

	@Override
	public void disabled(Entity e)
	{
		disabled.set(e.getId());
	}

	@Override
	public void deleted(Entity e)
	{
		//TODO: This is what we need
		entities.set(e.getId(), null);
		UUIDMap.put(e.getUuid(), null);
		disabled.clear(e.getId());

		identifierPool.checkIn(e.getId());

		active--;
		deleted++;
	}

	/**
	 * Check if this entity is active. Active means the entity is being actively
	 * processed.
	 *
	 * @param entityId
	 * @return true if active, false if not.
	 */
	public boolean isActive(int entityId)
	{
		return entities.get(entityId) != null;
	}

	/**
	 * Check if the specified entityId is enabled.
	 *
	 * @param entityId
	 * @return true if the entity is enabled, false if it is disabled.
	 */
	public boolean isEnabled(int entityId)
	{
		return !disabled.get(entityId);
	}

	/**
	 * Get a entity with this id.
	 *
	 * @param entityId
	 * @return the entity
	 */
	protected Entity getEntity(int entityId)
	{
		return entities.get(entityId);
	}
	protected Entity getEntity(UUID entityId)
	{
		//System.err.println("Getting entity by uuid ... " + UUIDMap.keySet().size());
		return UUIDMap.get(entityId);//entities.get(entityId);
	}

	/**
	 * Get how many entities are active in this world.
	 *
	 * @return how many entities are currently active.
	 */
	public int getActiveEntityCount()
	{
		return active;
	}

	/**
	 * Get how many entities have been created in the world since start. Note: A
	 * created entity may not have been added to the world, thus created count
	 * is always equal or larger than added count.
	 *
	 * @return how many entities have been created since start.
	 */
	public long getTotalCreated()
	{
		return created;
	}

	/**
	 * Get how many entities have been added to the world since start.
	 *
	 * @return how many entities have been added.
	 */
	public long getTotalAdded()
	{
		return added;
	}

	/**
	 * Get how many entities have been deleted from the world since start.
	 *
	 * @return how many entities have been deleted since start.
	 */
	public long getTotalDeleted()
	{
		return deleted;
	}

	/*
	 * Used only internally to generate distinct ids for entities and reuse them.
	 */
	private class IdentifierPool
	{

		private Bag<Integer> ids;
		private int nextAvailableId;

		public IdentifierPool()
		{
			ids = new Bag<Integer>();
		}

		public int checkOut()
		{
			if (ids.size() > 0)
			{
				return ids.removeLast();
			}
			return nextAvailableId++;
		}

		public void checkIn(int id)
		{
			ids.add(id);
		}
	}

}
