/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Mapping;

//import com.satanicspider.topdownmmo.topdownmmo.Entities.Entity;
import com.artemis.Entity;
import com.SatanicSpider.core.Configuration;
import java.util.ArrayList;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/*
import playn.core.CanvasImage;
import playn.core.DebugDrawBox2D;
import playn.core.Image;
import playn.core.Layer;
import static playn.core.PlayN.graphics;
*/

/**
 *
 * @author bryant
 */
public class Quad
{

	int world_x = 0;
	int world_y = 0;
	
	ArrayList<Entity> quadEnts;
	
	
	//Todo: Fix this
	//Layer background;
	//Image background_image;

	//static private DebugDrawBox2D debugDraw;

	World world;

	public void init(Vec2 gravity)
	{
	//	background_image = ResourceManager.getImage("images/maps/DefaultMap.png");
	//	background = graphics().createImageLayer(background_image);
		//background.setScale(graphics().width() / (float)background_image.width(), graphics().height() / (float)background_image.height());
	//	graphics().rootLayer().add(background);
		world = new World(gravity);

		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: Setting up world");
	//		CanvasImage image = graphics().createImage((int) (graphics().width() / Configuration.physUnitPerScreenUnit),
	//				(int) (graphics().height() / Configuration.physUnitPerScreenUnit));
	//		graphics().rootLayer().add(graphics().createImageLayer(image));
	//		debugDraw = new DebugDrawBox2D();
	//		debugDraw.setCanvas(image);
	//		debugDraw.setFlipY(false);
	//		debugDraw.setStrokeAlpha(150);
	//		debugDraw.setFillAlpha(75);
	//		debugDraw.setStrokeWidth(2.0f);
	//		debugDraw.setFlags(DebugDraw.e_shapeBit | DebugDraw.e_jointBit | DebugDraw.e_aabbBit);
	//		debugDraw.setCamera(0, 0, 1f / Configuration.physUnitPerScreenUnit);
	//		world.setDebugDraw(debugDraw);
			System.err.println("Physics: Finished Setting Up World");
		}

		//world.setContactListener(this);
		//BodyDef bd = blankBodyDef();

	}
	
	
	public void update(float period)
	{
		world.step(period, 8, 3);
	}

	public World getWorld()
	{
		return world;
	}

	//@Override
	public void beginContact(Contact cntct)
	{
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: begin contact " + cntct);
		}
	}

	//@Override
	public void endContact(Contact cntct)
	{
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: end contact " + cntct);
		}
	}

	//@Override
	public void preSolve(Contact cntct, Manifold mnfld)
	{
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: pre solve contact " + cntct);
		}
	}

	//@Override
	public void postSolve(Contact cntct, ContactImpulse ci)
	{
		
		if (Configuration.PhysicsDebug)
		{
			System.err.println("Physics: post solve " + cntct);
		}
	}
	

}
