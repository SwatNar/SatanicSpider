/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Callbacks;

import java.util.UUID;

/**
 *
 * @author bryant
 */
public interface StorageCallback extends Callback
{
	public Class GetClass();
	public UUID GetUUID();
	public void returnCallback(Object o);
}
