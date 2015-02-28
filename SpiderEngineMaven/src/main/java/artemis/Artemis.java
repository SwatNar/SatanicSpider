/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artemis;

import com.SatanicSpider.artemis.Config;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.EntitySystem;
import com.SatanicSpider.artemis.World;
import com.SatanicSpider.artemis.utils.Debug;

/**
 *
 * @author bryant
 */
public class Artemis
{

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		//Test1 Create ent, attach component, register system
		//Test2 Create ent, register system, attach component
		//Test3 Create ent, attach component1, register system, attach component2
		Debug.debug = true;
		Config.Entity_AutoPropChanges = true;
		World w = new World();

		
		Entity r = w.createEntity();
		r.addToWorld();
		//r.addComponent(new TestComponent1(1));
		
		Entity e = w.createEntity();
		e.addToWorld();

		EntitySystem s = new TestSystem1();
		w.setSystem(s);

		w.initialize();
		int i = 0;
		while (true)
		{
			i++;
			Debug.debugln(i+"");
			if (i == 20)
			{
				Debug.debugln("Adding components");
				e.addComponent(new TestComponent1(7));
				e.addComponent(new TestComponent2(2));
				//e.changedInWorld();
			}
			w.setDelta(300);

			w.process();
			try
			{
				Thread.sleep(1000);
			} catch (Exception exs)
			{
			};
		}

	}

}
