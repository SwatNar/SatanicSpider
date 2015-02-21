/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Components.Game;

import com.artemis.Entity;
import java.util.HashMap;

/**
 *
 * @author bryant
 */
public interface Executable
{
	public void execute(Entity context, HashMap<String,Object> properties);
}
