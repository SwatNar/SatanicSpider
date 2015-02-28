/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.core;

import com.SatanicSpider.Storage.Storage;

/**
 *
 * @author bryant
 */
public class StorageConfiguration
{
	public enum StorageMode
	{
		File,
		Database,
		Remote,
		Ram,
		None
	}
	
	public static final StorageMode storageMode = StorageMode.File;
	
	public static final String Directory = "/tmp/";//System.getProperty("user.home");
	
	public static final boolean debug = true;
	
	public static final Storage Storage = new Storage();
}
