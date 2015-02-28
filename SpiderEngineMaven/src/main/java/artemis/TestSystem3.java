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
public class TestSystem3 extends EntityProcessingSystem
{
	@Mapper ComponentMapper<TestComponent1> pm1;
	@Mapper ComponentMapper<TestComponent2> pm2;
	
	public TestSystem3()
	{
		super(Aspect.getAspectForAll(TestComponent1.class, TestComponent2.class));
	}
	
	public void process(Entity e)
	{
		//Process is just to print the component
		
		System.err.println("TestSystem1 Process " + e + " = " + (pm1.getSafe(e).count + pm2.getSafe(e).count));
	}
}