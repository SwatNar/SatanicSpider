/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.artemis.utils;

/**
 *
 * @author bryant
 */
public class Debug
{
	public static boolean debug = false;
	
	public static void debugln(String st)
	{
		if(debug)
		{
			System.err.println("DEBUG: " + st);
		}
	}
}
