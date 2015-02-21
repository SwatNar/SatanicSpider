/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Components;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.Message;
import com.SatanicSpider.util.ResourceManager;
/*
 import playn.core.Image;
 import playn.core.ImageLayer;
 import static playn.core.PlayN.graphics;
 import playn.core.util.Callback;
 */

/**
 *
 * @author bryant
 */
@Deprecated
public class StaticSprite implements Component
{

    public void init(Entity e)
    {
    }

    public void cleanup(Entity e)
    {
    }

    public void receiveMessage(Message m)
    {
    }
    //public Image image;
    //public ImageLayer imageLayer;
    public float width = 0.0f;
    public float height = 0.0f;

    public StaticSprite()
    {
	//	image = ResourceManager.getImage("images/Null.png");
	init();
    }

    public void init()
    {
	/*	if(image.width() == 0)
	 {
	 System.err.println("CALLBACK NEEDED");
	 image.addCallback(new Callback()
	 {

	 public void onSuccess(Object result)
	 {
					
	 width = image.width();
	 height = image.height();
	 System.err.println("CALLBACK SUCESS! " + width);
	 }

	 @Override
	 public void onFailure(Throwable cause)
	 {
	 System.err.println("CALLBACK Fail");
	 }
			
	 });
	 }
	 else
	 {
			
	 width = image.width();
	 height = image.height();
	 System.err.println("CALLBACK NOT NEEDED " + width);
	 }
	 imageLayer = graphics().createImageLayer(image);
	 graphics().rootLayer().add(imageLayer);
	 */
    }

    public StaticSprite(String spritePath)
    {
	//	image = ResourceManager.getImage(spritePath);
	init();
    }
    /*public StaticSprite(Image image)
     {
     this.image = image;
     init();
     }*/
}
