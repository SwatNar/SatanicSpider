/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Components.Render;

import com.SatanicSpider.Components.Game.Health;
import com.SatanicSpider.SharedVars;
import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;

/**
 *
 * @author bryant
 */
public class HealthRender implements Component
{

	public void init(Entity e)
	{
		Health h = e.getComponent(Health.class);
		//Todo: Make this accept any geo not just sprites
		Sprite s = e.getComponent(Sprite.class);
		if (h != null)
		{
			init(h.max_health, h.health, s);
		}
	}

	public void cleanup(Entity e)
	{

	}

	public void receiveMessage(Message m)
	{

	}
	Node textNode;

	public int current, max;
	BitmapText bmText;
	BitmapFont font = SharedVars.assetManager.loadFont("Interface/Fonts/Default.fnt");

	//Todo Add a bar
	public void init(int max, int cur, Sprite target)
	{
		if(bmText == null)
		{
			System.err.println(target);
			current = cur;
			this.max = max;

			bmText = new BitmapText(font);
			bmText.setSize(0.4f);
			bmText.setText(cur + "/" + max);
			bmText.setQueueBucket(RenderQueue.Bucket.Transparent);
			bmText.setColor(new ColorRGBA(241f / 255f, 30f / 255f, 34f / 255f, 1f));
			//bmText.setBox(new Rectangle(20,20,20,20));
			bmText.setAlignment(BitmapFont.Align.Left);
			bmText.setLineWrapMode(LineWrapMode.NoWrap);
			bmText.center();

			// Create node and add a BillboardControl so it rotates with the cam
			BillboardControl bbControl = new BillboardControl();
			bbControl.setAlignment(BillboardControl.Alignment.Screen);

			textNode = new Node();
			textNode.setQueueBucket(RenderQueue.Bucket.Transparent);

			textNode.attachChild(bmText);
			bmText.addControl(bbControl);
			textNode.center();
			textNode.setLocalTranslation(new Vector3f(-1, 1, 10));
			target.getNode().attachChild(textNode);
		}
	}

	public void update(int max, int cur)
	{
		if (cur != current || max != this.max)
		{
			
			//textNode.detachChild(bmText);

			current = cur;
			this.max = max;
			//bmText.setText("xXxXxXxXxXxXxXxXxXxX");
			//bmText = new BitmapText(font);
			//bmText.setSize(0.2f);
			bmText.setText(cur + "/" + max);
			//bmText.setQueueBucket(RenderQueue.Bucket.Transparent);
			//bmText.setColor(new ColorRGBA(241f / 255f, 30f / 255f, 34f / 255f, 1f));
			//bmText.setBox(new Rectangle(20,20,20,20));
			//bmText.setAlignment(BitmapFont.Align.Left);
			//bmText.setLineWrapMode(LineWrapMode.NoWrap);
			//bmText.center();
			//textNode.attachChild(bmText);
		}
	}
}
