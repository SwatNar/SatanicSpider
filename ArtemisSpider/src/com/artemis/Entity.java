package com.artemis;

import java.util.BitSet;
import java.util.UUID;

import com.artemis.utils.Bag;
import com.artemis.utils.Debug;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The entity class. Cannot be instantiated outside the framework, you must
 * create new entities using World.
 *
 * @author Arni Arent
 *
 */
public final class Entity
{

	private UUID uuid;

	private transient int id;
	private transient BitSet componentBits;
	private transient BitSet systemBits;

	private World world;
	private transient EntityManager entityManager;
	private transient ComponentManager componentManager;
	
	private boolean changed = false;
	
	ArrayList<Entity> Children = new ArrayList<Entity>();
	Entity Parent;
	ArrayList<Message> Messages = new ArrayList<Message>();
	
	//TODO: Optimizations via config class

	protected Entity(World world, int id)
	{
		this.world = world;
		this.id = id;
		this.entityManager = world.getEntityManager();
		this.componentManager = world.getComponentManager();
		systemBits = new BitSet();
		componentBits = new BitSet();

		reset();
	}
	
	protected Entity(World world, int id, UUID uuid)
	{
		this.world = world;
		
		this.id = id;
		this.entityManager = world.getEntityManager();
		this.componentManager = world.getComponentManager();
		systemBits = new BitSet();
		componentBits = new BitSet();

		reset();
		
		this.uuid = uuid;
	}

	/**
	 * The internal id for this entity within the framework. No other entity
	 * will have the same ID, but ID's are however reused so another entity may
	 * acquire this ID if the previous entity was deleted.
	 *
	 * @return id of the entity.
	 */
	
	//If Entity_AutoPropChanges is set to false the ent will not auto prop the changes
	public boolean wasChanged()
	{
		Debug.debugln("Entity: wasChanged " + Config.Entity_AutoPropChanges + "&&" +  changed);
		return Config.Entity_AutoPropChanges && changed;
	}
	
	public int getId()
	{
		Debug.debugln("Entity: getID " + id);
		return id;
	}

	/**
	 * Returns a BitSet instance containing bits of the components the entity
	 * possesses.
	 *
	 * @return
	 */
	protected BitSet getComponentBits()
	{
		Debug.debugln("Entity: getComponentBits " + componentBits);
		return componentBits;
	}

	/**
	 * Returns a BitSet instance containing bits of the components the entity
	 * possesses.
	 *
	 * @return
	 */
	protected BitSet getSystemBits()
	{
		Debug.debugln("Entity: getSystemBits " + systemBits);
		return systemBits;
	}

	/**
	 * Make entity ready for re-use. Will generate a new uuid for the entity.
	 */
	protected void reset()
	{
		Debug.debugln("Entity: reset");
		systemBits.clear();
		componentBits.clear();
		uuid = UUID.randomUUID();
	}

	@Override
	public String toString()
	{
		return "Entity[" + id + "]";
	}

	/**
	 * Add a component to this entity.
	 *
	 * @param component to add to this entity
	 *
	 * @return this entity for chaining.
	 */
	HashMap<ComponentType,Component> components = new HashMap<ComponentType,Component>();
	
	public Entity addComponent(Component component)
	{
		if(Config.Entity_AutoPropChanges) changedInWorld();
			Debug.debugln("Entity: addComponent " + component);
			
		addComponent(component, ComponentType.getTypeFor(component.getClass()));
		
		return this;
	}
	public Entity syncAddComponent(Component component)
	{
		if(Config.Entity_AutoPropChanges) changedInWorld();
		Debug.debugln("Entity: addComponent " + component);
		syncAddComponent(component, ComponentType.getTypeFor(component.getClass()));
		
		return this;
	}

	/**
	 * Faster adding of components into the entity. Not neccessery to use this,
	 * but in some cases you might need the extra performance.
	 *
	 * @param component the component to add
	 * @param type of the component
	 *
	 * @return this entity for chaining.
	 */
	public Entity addComponent(Component component, ComponentType type)
	{
		if(Config.Entity_AutoPropChanges) changedInWorld();
		Debug.debugln("Entity: addComponent " + component);
		component.init(this);
		componentManager.addComponent(this, type, component);
		components.put(type, component);
		return this;
	}
	public Entity syncAddComponent(Component component, ComponentType type)
	{
		if(Config.Entity_AutoPropChanges) changedInWorld();
		Debug.debugln("Entity: addComponent " + component);
		component.init(this);
		componentManager.addComponent(this, type, component);
		components.put(type, component);
		
		for(World w : WorldManager.worlds.values())
		{
			if(!w.equals(world))
			{
				w.getComponentManager().addComponent(w.getEntity(uuid), type, component);
			}
		}
		
		return this;
	}

	/**
	 * Removes the component from this entity.
	 *
	 * @param component to remove from this entity.
	 *
	 * @return this entity for chaining.
	 */
	public Entity removeComponent(Component component)
	{
		if(Config.Entity_AutoPropChanges) changedInWorld();
		Debug.debugln("Entity: removeComponent " + component);
		component.cleanup(this);
		removeComponent(component.getClass());
		return this;
	}

	/**
	 * Faster removal of components from a entity.
	 *
	 * @param component to remove from this entity.
	 *
	 * @return this entity for chaining.
	 */
	public Entity removeComponent(ComponentType type)
	{
		if(Config.Entity_AutoPropChanges) changedInWorld();
		//TODO: Do we need to cal cleanup here?
		Debug.debugln("Entity: removeComponent " + type);
		componentManager.removeComponent(this, type);
		return this;
	}

	/**
	 * Remove component by its type.
	 *
	 * @param type
	 *
	 * @return this entity for chaining.
	 */
	public Entity removeComponent(Class<? extends Component> type)
	{
		if(Config.Entity_AutoPropChanges) changedInWorld();
		//TODO: Do we need to cal cleanup here?
		Debug.debugln("Entity: removeComponent" +  type);
		removeComponent(ComponentType.getTypeFor(type));
		return this;
	}

	/**
	 * Checks if the entity has been added to the world and has not been deleted
	 * from it. If the entity has been disabled this will still return true.
	 *
	 * @return if it's active.
	 */
	public boolean isActive()
	{
		Debug.debugln("Entity: isActive " + entityManager.isActive(id));
		return entityManager.isActive(id);
	}

	/**
	 * Will check if the entity is enabled in the world. By default all entities
	 * that are added to world are enabled, this will only return false if an
	 * entity has been explicitly disabled.
	 *
	 * @return if it's enabled
	 */
	public boolean isEnabled()
	{
		Debug.debugln("Entity: isEnabled " + entityManager.isEnabled(id));
		return entityManager.isEnabled(id);
	}

	/**
	 * This is the preferred method to use when retrieving a component from a
	 * entity. It will provide good performance. But the recommended way to
	 * retrieve components from an entity is using the ComponentMapper.
	 *
	 * @param type in order to retrieve the component fast you must provide a
	 * ComponentType instance for the expected component.
	 * @return
	 */
	public Component getComponent(ComponentType type)
	{
		Component ret = componentManager.getComponent(this, type);
		Debug.debugln("Entity: getComponent " + type + " = " + ret);
		return ret;
	}

	/**
	 * Slower retrieval of components from this entity. Minimize usage of this,
	 * but is fine to use e.g. when creating new entities and setting data in
	 * components.
	 *
	 * @param <T> the expected return component type.
	 * @param type the expected return component type.
	 * @return component that matches, or null if none is found.
	 */
	public <T extends Component> T getComponent(Class<T> type)
	{
		T ret = type.cast(getComponent(ComponentType.getTypeFor(type)));
		Debug.debugln("Entity: getComponent " + type + " = " + ret);
		return ret;
	}

	/**
	 * Returns a bag of all components this entity has. You need to reset the
	 * bag yourself if you intend to fill it more than once.
	 *
	 * @param fillBag the bag to put the components into.
	 * @return the fillBag with the components in.
	 */
	public Bag<Component> getComponents(Bag<Component> fillBag)
	{
		Bag<Component> ret = componentManager.getComponentsFor(this, fillBag);
		Debug.debugln("Entity: getComponents " + fillBag + " = " + ret);
		return ret;
	}

	/**
	 * Refresh all changes to components for this entity. After adding or
	 * removing components, you must call this method. It will update all
	 * relevant systems. It is typical to call this after adding components to a
	 * newly created entity.
	 */
	public void addToWorld()
	{
		Debug.debugln("Entity: addToWorld");
		world.addEntity(this);
		changed = false;
	}

	/**
	 * This entity has changed, a component added or deleted.
	 * TODO: why is this not called automatically? Does it update systems?
	 * TODO: Call this on every update loop where it hasn't been called before
	 */
	public void changedInWorld()
	{
		Debug.debugln("Entity: changedInWorld");
		world.changedEntity(this);
		changed = false;
	}

	/**
	 * Delete this entity from the world.
	 */
	public void deleteFromWorld()
	{
		Debug.debugln("Entity: deleteFromWorld");
		world.deleteEntity(this);
	}

	/**
	 * (Re)enable the entity in the world, after it having being disabled. Won't
	 * do anything unless it was already disabled.
	 */
	public void enable()
	{
		Debug.debugln("Entity: enable");
		world.enable(this);
	}

	/**
	 * Disable the entity from being processed. Won't delete it, it will
	 * continue to exist but won't get processed.
	 */
	public void disable()
	{
		Debug.debugln("Entity: disable");
		world.disable(this);
	}

	/**
	 * Get the UUID for this entity. This UUID is unique per entity (re-used
	 * entities get a new UUID).
	 *
	 * @return uuid instance for this entity.
	 */
	public UUID getUuid()
	{
		Debug.debugln("Entity: getUUID " + uuid);
		if(Config.Entity_AutoPropChanges)
			if(changed)
				changedInWorld();
		return uuid;
	}

	/**
	 * Returns the world this entity belongs to.
	 *
	 * @return world of entity.
	 */
	public World getWorld()
	{
		Debug.debugln("Entity: getWorld " + world);
		return world;
	}
	
	
	
	
	public boolean hasParent()
	{
		return Parent != null;
	}
	
	public boolean hasChildren()
	{
		return Children.size() > 0;
	}
	
	public void addChild(Entity child)
	{
		if(!Children.contains(child))
			Children.add(child);
	}
	
	public void addChild(int id)
	{
		Entity e = world.getEntity(id);
		if(e != null)
			if(!Children.contains(e))
				Children.add(e);
			else
				Debug.debugln("Entity: addChild " + id + ": ERR: REC: tried to add a child entity twice!");
		else
			Debug.debugln("Entity: addChild " + id + ": ERR: REC: tried to add a non-esistant entity!");
	}
	
	public void setOwner(Entity e)
	{
		Parent = e;
	}
	public void setOwner(int id)
	{
		Entity e = world.getEntity(id);
		if(e != null)
		{
			Parent = e;
			//Make sure the parent knows we are its child
			Parent.addChild(this);
		}
		else
			Debug.debugln("Entity: addChild " + id + ": ERR: REC: tried to add a non-esistant entity!");
	}
	
	
	public void report(Message m)
	{
		if(Parent != null)
			Parent.receiveMessage(m);
	}
	
	public void order(Message m)
	{
		for(Entity e : Children)
			e.receiveMessage(m);
	}
	
	public void broadcast(Message m)
	{
		if(! messageInQueue(m))
		{
			if(Parent != null)
				Parent.broadcast(m);
			for(Entity e : Children)
				e.broadcast(m);
		}
	}
	
	public void receiveMessage(Message m)
	{
		Messages.add(m);
	}
	
	public boolean messageInQueue(Message m)
	{
		return (Messages.contains(m));
	}
	
	protected void processMessage(Message m)
	{
		//Do something here
		
		switch(m.getMessageType())
		{
			case BROADCAST:
			{
				//Send to all componenents
			}break;
			case TARGETED:
			{
				//Send to target ... if it exists
				Class target = m.getTarget();
				Component t = this.getComponent(target);
				if(t != null)
					t.receiveMessage(m);
			}
			case NONE: break; // DOnt do anything
		}
	}
	
	protected void pumpMessages()
	{
		if(Messages.size() > 0)
		{
			Message m = Messages.remove(0);
			processMessage(m);
		}
	}

}
