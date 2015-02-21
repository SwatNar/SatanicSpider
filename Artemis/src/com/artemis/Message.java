/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.artemis;

import java.util.UUID;

/**
 *
 * @author bryant
 */
public interface Message
{
	enum MessageType
	{
		BROADCAST,
		TARGETED,
		NONE;
	}
	UUID getUUID();
	MessageType getMessageType();
	 <T extends Component> Class<T> getTarget();
	//Should do something along the line of the mapper shit here
}
