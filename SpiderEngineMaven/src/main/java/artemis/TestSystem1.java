/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artemis;

import com.SatanicSpider.artemis.Aspect;
import com.SatanicSpider.artemis.ComponentMapper;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.annotations.Mapper;
import com.SatanicSpider.artemis.systems.EntityProcessingSystem;

/**
 *
 * @author bryant
 */
public class TestSystem1 extends EntityProcessingSystem
{

	@Mapper
	ComponentMapper<TestComponent1> pm;

	public TestSystem1()
	{
		super(Aspect.getAspectForAll(TestComponent1.class));
	}

	public void process(Entity e)
	{
		//Process is just to print the component
		if (pm.has(e))
		{
			System.err.println("TestSystem1 Process " + e + " = " + pm.getSafe(e).count);
		} else
		{
			System.err.println("TestSystem1 Process " + e + " = " + null);
		}
	}
}
