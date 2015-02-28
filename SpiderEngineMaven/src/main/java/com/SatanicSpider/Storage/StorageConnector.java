/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Storage;

import java.nio.file.Path;
import java.util.UUID;

/**
 *
 * @author bryant
 */
public interface StorageConnector
{
	boolean Store(Object o);
	Object UnStore(Class c, UUID id);
	void disable();
	void enable();
}
