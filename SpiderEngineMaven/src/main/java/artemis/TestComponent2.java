/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package artemis;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import com.SatanicSpider.artemis.utils.Debug;

/**
 *
 * @author bryant
 */
public class TestComponent2 implements Component
{
	public int count;

	public TestComponent2(int i)
	{
		count = i;
	}
	
	public void init(Entity e)
	{
		Debug.debugln("TestComponent2: init " + e);
	}

	public void cleanup(Entity e)
	{
		Debug.debugln("TestComponent2: cleanup " + e);
	}

	
	public void receiveMessage(Message m)
	{
		
	}
}