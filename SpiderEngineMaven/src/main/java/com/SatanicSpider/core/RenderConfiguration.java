/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.core;

import com.jme3.math.Vector3f;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class RenderConfiguration
{
	//Switches
	public static boolean flyCamEnabled = false;
	public static boolean debugFpsEnabled = true;
	
	public static Vec2 CameraOffset = new Vec2(0,-8);
	
	
	public static Vector3f initialLocation = new Vector3f(20, 11, 28);
	public static float aspect;
}
