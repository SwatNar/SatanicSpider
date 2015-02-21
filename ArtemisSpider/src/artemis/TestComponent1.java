/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artemis;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;
import com.artemis.utils.Debug;

/**
 *
 * @author bryant
 */
public class TestComponent1 implements Component
{

	public int count;

	public TestComponent1(int i)
	{
		count = i;
	}
	
	public void init(Entity e)
	{
		Debug.debugln("TestComponent1: init " + e);
	}

	public void cleanup(Entity e)
	{
		Debug.debugln("TestComponent1: cleanup " + e);
	}

	@Override
	public void receiveMessage(Message m)
	{
		
	}
}
