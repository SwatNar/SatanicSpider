package com.SatanicSpider.artemis;

import com.SatanicSpider.artemis.utils.Debug;
import java.util.BitSet;

/**
 * An Aspects is used by systems as a matcher against entities, to check if a
 * system is interested in an entity. Aspects define what sort of component
 * types an entity must possess, or not possess.
 *
 * This creates an aspect where an entity must possess A and B and C:
 * Aspect.getAspectForAll(A.class, B.class, C.class)
 *
 * This creates an aspect where an entity must possess A and B and C, but must
 * not possess U or V. Aspect.getAspectForAll(A.class, B.class,
 * C.class).exclude(U.class, V.class)
 *
 * This creates an aspect where an entity must possess A and B and C, but must
 * not possess U or V, but must possess one of X or Y or Z.
 * Aspect.getAspectForAll(A.class, B.class, C.class).exclude(U.class,
 * V.class).one(X.class, Y.class, Z.class)
 *
 * You can create and compose aspects in many ways:
 * Aspect.getEmpty().one(X.class, Y.class, Z.class).all(A.class, B.class,
 * C.class).exclude(U.class, V.class) is the same as:
 * Aspect.getAspectForAll(A.class, B.class, C.class).exclude(U.class,
 * V.class).one(X.class, Y.class, Z.class)
 *
 * @author Arni Arent
 *
 */
public class Aspect
{

	private BitSet allSet;
	private BitSet exclusionSet;
	private BitSet oneSet;

	private Aspect()
	{
		Debug.debugln("Aspect: Private Aspect Constructor");
		this.allSet = new BitSet();
		this.exclusionSet = new BitSet();
		this.oneSet = new BitSet();
	}

	protected BitSet getAllSet()
	{
		Debug.debugln("Aspect: getAllSet");
		return allSet;
	}

	protected BitSet getExclusionSet()
	{
		Debug.debugln("Aspect: getExclusiveSet");
		return exclusionSet;
	}

	protected BitSet getOneSet()
	{
		Debug.debugln("Aspect: getOneSet");
		return oneSet;
	}

	/**
	 * Returns an aspect where an entity must possess all of the specified
	 * component types.
	 *
	 * @param type a required component type
	 * @param types a required component type
	 * @return an aspect that can be matched against entities
	 */
	public Aspect all(Class<? extends Component> type, Class<? extends Component>... types)
	{
		Debug.debugln("Aspect: all " + type);
		allSet.set(ComponentType.getIndexFor(type));

		for (Class<? extends Component> t : types)
		{
			Debug.debugln("Aspect: " + type + " ... " + t);
			allSet.set(ComponentType.getIndexFor(t));
		}

		return this;
	}

	/**
	 * Excludes all of the specified component types from the aspect. A system
	 * will not be interested in an entity that possesses one of the specified
	 * exclusion component types.
	 *
	 * @param type component type to exclude
	 * @param types component type to exclude
	 * @return an aspect that can be matched against entities
	 */
	public Aspect exclude(Class<? extends Component> type, Class<? extends Component>... types)
	{
		Debug.debugln("Aspect: exclude " + type);
		exclusionSet.set(ComponentType.getIndexFor(type));

		for (Class<? extends Component> t : types)
		{
			Debug.debugln("Aspect: exclude " + type + " " + t);
			exclusionSet.set(ComponentType.getIndexFor(t));
		}
		return this;
	}

	/**
	 * Returns an aspect where an entity must possess one of the specified
	 * component types.
	 *
	 * @param type one of the types the entity must possess
	 * @param types one of the types the entity must possess
	 * @return an aspect that can be matched against entities
	 */
	public Aspect one(Class<? extends Component> type, Class<? extends Component>... types)
	{
		Debug.debugln("Aspect: one " + type);
		oneSet.set(ComponentType.getIndexFor(type));

		for (Class<? extends Component> t : types)
		{
			Debug.debugln("Aspect: one " + type + " " + t);
			oneSet.set(ComponentType.getIndexFor(t));
		}
		return this;
	}

	/**
	 * Creates an aspect where an entity must possess all of the specified
	 * component types.
	 *
	 * @param type the type the entity must possess
	 * @param types the type the entity must possess
	 * @return an aspect that can be matched against entities
	 *
	 * @deprecated
	 * @see getAspectForAll
	 */
	public static Aspect getAspectFor(Class<? extends Component> type, Class<? extends Component>... types)
	{
		Debug.debugln("Aspect: getAspectFor " + type);
		return getAspectForAll(type, types);
	}

	/**
	 * Creates an aspect where an entity must possess all of the specified
	 * component types.
	 *
	 * @param type a required component type
	 * @param types a required component type
	 * @return an aspect that can be matched against entities
	 */
	public static Aspect getAspectForAll(Class<? extends Component> type, Class<? extends Component>... types)
	{
		Debug.debugln("Aspect: getAspectForAll " + type);
		Aspect aspect = new Aspect();
		aspect.all(type, types);
		return aspect;
	}

	/**
	 * Creates an aspect where an entity must possess one of the specified
	 * component types.
	 *
	 * @param type one of the types the entity must possess
	 * @param types one of the types the entity must possess
	 * @return an aspect that can be matched against entities
	 */
	public static Aspect getAspectForOne(Class<? extends Component> type, Class<? extends Component>... types)
	{
		Debug.debugln("Aspect: getAspectForOne " + type);
		Aspect aspect = new Aspect();
		aspect.one(type, types);
		return aspect;
	}

	/**
	 * Creates and returns an empty aspect. This can be used if you want a
	 * system that processes no entities, but still gets invoked. Typical usages
	 * is when you need to create special purpose systems for debug rendering,
	 * like rendering FPS, how many entities are active in the world, etc.
	 *
	 * You can also use the all, one and exclude methods on this aspect, so if
	 * you wanted to create a system that processes only entities possessing
	 * just one of the components A or B or C, then you can do:
	 * Aspect.getEmpty().one(A,B,C);
	 *
	 * @return an empty Aspect that will reject all entities.
	 */
	public static Aspect getEmpty()
	{
		Debug.debugln("Aspect: getEmpty");
		return new Aspect();
	}

}
