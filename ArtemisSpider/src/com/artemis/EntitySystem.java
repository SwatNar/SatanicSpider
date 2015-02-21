package com.artemis;

import java.util.BitSet;
import java.util.HashMap;

import com.artemis.utils.Bag;
import com.artemis.utils.Debug;
import com.artemis.utils.ImmutableBag;

/**
 * The most raw entity system. It should not typically be used, but you can
 * create your own entity system handling by extending this. It is recommended
 * that you use the other provided entity system implementations.
 *
 * @author Arni Arent
 *
 */
public abstract class EntitySystem implements EntityObserver
{

	private final int systemIndex;

	protected World world;

	private Bag<Entity> actives;

	private Aspect aspect;

	private BitSet allSet;
	private BitSet exclusionSet;
	private BitSet oneSet;

	private boolean passive;

	private boolean dummy;

	/**
	 * Creates an entity system that uses the specified aspect as a matcher
	 * against entities.
	 *
	 * @param aspect to match against entities
	 */
	public EntitySystem(Aspect aspect)
	{
		Debug.debugln("EntitySystem: init");
		actives = new Bag<Entity>();
		Debug.debugln("EntitySystem: init " + actives);
		this.aspect = aspect;
		allSet = aspect.getAllSet();
		Debug.debugln("EntitySystem: init " + allSet);
		exclusionSet = aspect.getExclusionSet();
		Debug.debugln("EntitySystem: init " + exclusionSet);
		oneSet = aspect.getOneSet();
		Debug.debugln("EntitySystem: init " + oneSet);
		systemIndex = SystemIndexManager.getIndexFor(this.getClass());
		
		Debug.debugln("EntitySystem: init " + systemIndex);
		
		dummy = allSet.isEmpty() && oneSet.isEmpty(); // This system can't possibly be interested in any entity, so it must be "dummy"
		Debug.debugln("EntitySystem: init " + dummy);
	}

	/**
	 * Called before processing of entities begins.
	 */
	protected void begin()
	{
		Debug.debugln("EntitySystem: begin");
	}

	public final void process()
	{
		Debug.debugln("EntitySystem: process");
		if (checkProcessing())
		{
			begin();
			processEntities(actives);
			end();
		}
	}

	/**
	 * Called after the processing of entities ends.
	 */
	protected void end()
	{
		Debug.debugln("EntitySystem: end");
	}

	/**
	 * Any implementing entity system must implement this method and the logic
	 * to process the given entities of the system.
	 *
	 * @param entities the entities this system contains.
	 */
	protected abstract void processEntities(ImmutableBag<Entity> entities);

	/**
	 *
	 * @return true if the system should be processed, false if not.
	 */
	protected abstract boolean checkProcessing();

	/**
	 * Override to implement code that gets executed when systems are
	 * initialized.
	 */
	protected void initialize()
	{
		Debug.debugln("EntitySystem: initialize");
	}

	;

	/**
	 * Called if the system has received a entity it is interested in, e.g. created or a component was added to it.
	 * @param e the entity that was added to this system.
	 */
	protected void inserted(Entity e)
	{
		Debug.debugln("EntitySystem: inserted " + e);
	}

	;

	/**
	 * Called if a entity was removed from this system, e.g. deleted or had one of it's components removed.
	 * @param e the entity that was removed from this system.
	 */
	protected void removed(Entity e)
	{
		Debug.debugln("EntitySystem: removed " + e);
	}

	;

	/**
	 * Will check if the entity is of interest to this system.
	 * @param e entity to check
	 */
	protected final void check(Entity e)
	{
		Debug.debugln("EntitySystem: check");
		if (dummy)
		{
			Debug.debugln("EntitySystem: check dummy");
			return;
		}

		boolean contains = e.getSystemBits().get(systemIndex);
		Debug.debugln("EntitySystem: check " + contains);
		boolean interested = true; // possibly interested, let's try to prove it wrong.

		BitSet componentBits = e.getComponentBits();
		Debug.debugln("EntitySystem: check " + componentBits);

		// Check if the entity possesses ALL of the components defined in the aspect.
		if (!allSet.isEmpty())
		{
			Debug.debugln("EntitySystem: check allSet ! empty");
			for (int i = allSet.nextSetBit(0); i >= 0 && interested; i = allSet.nextSetBit(i + 1))
			{
				if (!componentBits.get(i))
				{
					Debug.debugln("EntitySystem: check ent doesnt have componentBit [" + i + "] = " + componentBits.get(i)); 
					interested = false;
					break;
				}
			}
		}

		// Check if the entity possesses ANY of the exclusion components, if it does then the system is not interested.
		if (!exclusionSet.isEmpty() && interested)
		{
			interested = !exclusionSet.intersects(componentBits);
			Debug.debugln("EntitySystem: check exclusionSet intersect " + interested);
		}

		// Check if the entity possesses ANY of the components in the oneSet. If so, the system is interested.
		if (!oneSet.isEmpty())
		{
			interested = oneSet.intersects(componentBits);
			Debug.debugln("EntitySystem: check oneSet intersect " + interested);
		}

		if (interested && !contains)
		{
			Debug.debugln("EntitySystem: check ! contains");
			insertToSystem(e);
		} else if (!interested && contains)
		{
			Debug.debugln("EntitySystem: check ! interested");
			removeFromSystem(e);
		}
	}

	private void removeFromSystem(Entity e)
	{
		Debug.debugln("EntitySystem: removeFromSystem " + e);
		actives.remove(e);
		e.getSystemBits().clear(systemIndex);
		removed(e);
	}

	private void insertToSystem(Entity e)
	{
		Debug.debugln("EntitySystem: insertToSystem " + e);
		actives.add(e);
		e.getSystemBits().set(systemIndex);
		inserted(e);
	}

	@Override
	public final void added(Entity e)
	{
		Debug.debugln("EntitySystem: added " + e);
		check(e);
	}

	@Override
	public final void changed(Entity e)
	{
		Debug.debugln("EntitySystem: changed " + e);
		check(e);
	}

	@Override
	public final void deleted(Entity e)
	{
		Debug.debugln("EntitySystem: deleted " + e);
		if (e.getSystemBits().get(systemIndex))
		{
			removeFromSystem(e);
		}
	}

	@Override
	public final void disabled(Entity e)
	{
		Debug.debugln("EntitySystem: disabled " + e);
		if (e.getSystemBits().get(systemIndex))
		{
			removeFromSystem(e);
		}
	}

	@Override
	public final void enabled(Entity e)
	{
		Debug.debugln("EntitySystem: enabled " + e);
		check(e);
	}

	protected final void setWorld(World world)
	{
		Debug.debugln("EntitySystem: setWorld " + world);
		this.world = world;
	}

	protected boolean isPassive()
	{
		Debug.debugln("EntitySystem: isPassive " + passive);
		return passive;
	}

	protected void setPassive(boolean passive)
	{
		Debug.debugln("EntitySystem: setPassive " + passive);
		this.passive = passive;
	}

	public ImmutableBag<Entity> getActives()
	{
		Debug.debugln("EntitySystem: getActives " + actives);
		return actives;
	}

	/**
	 * Used to generate a unique bit for each system. Only used internally in
	 * EntitySystem.
	 */
	private static class SystemIndexManager
	{

		private static int INDEX = 0;
		private static HashMap<Class<? extends EntitySystem>, Integer> indices = new HashMap<Class<? extends EntitySystem>, Integer>();

		private static int getIndexFor(Class<? extends EntitySystem> es)
		{
			Debug.debugln("EntitySystem: SystemIndexManager: getIndexFor " + es);
			Integer index = indices.get(es);
			if (index == null)
			{
				index = INDEX++;
				indices.put(es, index);
			}
			return index;
		}
	}

}
