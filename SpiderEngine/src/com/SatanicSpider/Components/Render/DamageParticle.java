/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Render;

import com.SatanicSpider.SharedVars;
import com.artemis.Entity;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;
import com.jme3.font.Rectangle;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
//import playn.core.Canvas;
//import playn.core.CanvasImage;
//import playn.core.ImageLayer;
//import static playn.core.PlayN.graphics;

/**
 *
 * @author bryant
 */
public class DamageParticle implements ParticleInitilizer
{
   
//	public ImageLayer surf;
	public Node textNode;
	
	public int current, max;
	BitmapText bmText;
	
	int val;
	/*public void initilize(Particle p)
	{
		
	}*/
	//Todo Add a bar
	public void init(int val)
	{
		System.err.println("DamageParticle: init " + val);
		BitmapFont font = SharedVars.assetManager.loadFont("Interface/Fonts/Default.fnt");
		System.err.println("DamageParticle: init " + font);
		bmText = new BitmapText(font);
		System.err.println("DamageParticle: init " + bmText);
		bmText.setSize(0.4f);
		bmText.setText(val + "");
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
		System.err.println("DamageParticle: init " + textNode);
		textNode.setQueueBucket(RenderQueue.Bucket.Transparent);

		textNode.attachChild(bmText);
		System.err.println("DamageParticle: init " + textNode);
		bmText.addControl(bbControl);
		textNode.center();
		//textNode.setLocalTranslation(new Vector3f(-1, 1, 10));
		//target.getNode().attachChild(textNode);
		
		//SharedVars.rootNode.attachChild(textNode);
	}
	
	public void update(int max, int cur)
	{
		//bmText.setText("XxXxXxXx");
		bmText.setText(cur + "\t/\t" + max);
	}
	
	
	//TODO: Simple Quad Renderer ...
	
	public DamageParticle(int number)
	{
		this.val = number;
		System.err.println("DamageParticle: Create: " + number);
/*		CanvasImage canvasImage = graphics().createImage(10, 10);
		
		Canvas can = canvasImage.canvas();
		
		can.setFillColor(0x80FF0000);
		can.drawText(number+"", 0, 0);
	
		
		surf = graphics().createImageLayer(canvasImage);
		
		graphics().rootLayer().add(surf);
*/
	}

	@Override
	public void inititilize(Particle p)
	{
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		System.err.println("DamageParticle: Initilize");
		init(val);
		p.Particle = textNode;
	}
	
}
