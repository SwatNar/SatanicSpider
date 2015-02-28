/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Management.Game;

//import static com.SatanicSpider.Management.Physics.PhysicsManager.PhysWorld;
//import static com.SatanicSpider.Management.Physics.PhysicsManager.run;
import com.SatanicSpider.core.Configuration;
import com.SatanicSpider.artemis.World;

/**
 *
 * @author bryant
 */
public class GameManager extends Thread 
{
	public World GameWorld;
	public static boolean run = false;
	
	public static long target_entity_step = 20;
	
	public synchronized void run()
	{

		//Todo: Physics Watchdog ...
		//TODO: Make this run at a set framerate
		//TODO: Framerate error reporting
		int loop_counter = 0;
		int loop_counter_entity = 0;
		long[] fps_buffer = new long[1000];
		long[] fps_buffer_entity = new long[1000];

		long lastTime = System.currentTimeMillis();
		long lastSystemTime = lastTime;
		long lastWorldTime = lastTime;

		//todo: check objects out of bounds
		while (run)
		{

			long delta = (System.currentTimeMillis() - lastTime);
			//fps_buffer[loop_counter] = delta;
			lastTime = System.currentTimeMillis();

			/*while (!requestLock(this.getClass()))
			{
				//System.err.println("Physics tick waiting for lock!");
			}*/

			long entity_delta = (System.currentTimeMillis() - lastSystemTime);

			if (entity_delta > target_entity_step)
			{
				lastSystemTime = System.currentTimeMillis();
				GameWorld.setDelta(entity_delta / 1000f);
				GameWorld.process();
				loop_counter_entity++;
				if (loop_counter_entity >= fps_buffer_entity.length)
				{
					loop_counter_entity = 0;
					long tot = 0;
					for (int i = 0; i < fps_buffer.length; i++)
					{
						tot += fps_buffer_entity[i];
					}
					float avg = (float) (tot / 1000.0);
					float fps = (float)(1000.0 / avg);
					System.err.println("AVG Game Time: " + avg + " FPS: " + fps);// + " objects: " + PhysWorld.getBodyCount());
					if(fps < Configuration.Physics.target_system_fps - Configuration.Physics.adjustor_threshold)
					{
						System.err.println("Oh my look at the time for games");
						target_entity_step --;
					}
					
					if(fps > Configuration.Physics.target_system_fps + Configuration.Physics.adjustor_threshold)
					{
						System.err.println("Dear me we are early for games.");
						target_entity_step ++;
					}
						
				}
				fps_buffer_entity[loop_counter_entity] = entity_delta;
			}
		}
	}
	
	
	
	static GameManager instance;
	private static void initialize()
	{
		run = true;
		instance = new GameManager();
	}
	
	public static GameManager getInstance()
	{
		if (instance == null)
		{
			initialize();

			instance.start();
		}

		return instance;
	}
}
