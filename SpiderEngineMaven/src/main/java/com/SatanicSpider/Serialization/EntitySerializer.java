/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Serialization;

import com.SatanicSpider.Management.Physics.PhysicsManager;
import com.SatanicSpider.artemis.Entity;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.jbox2d.dynamics.Body;
import org.json.JSONObject;

/**
 *
 * @author bryant
 */
public class EntitySerializer implements JsonSerializer<Entity>,JsonDeserializer<Entity>
{

	public JsonElement serialize(final Entity Src, final Type typeOfSrc, final JsonSerializationContext context)
	{
		final JsonObject jsonObject = new JsonObject();
		
		try
		{
			
			//jsonObject.addProperty("json", PhysicsManager.JSONifier.b2j(Src).toString());
			//jsonObject.addProperty("World", Src.getWorld());
			return jsonObject;
		}
		catch(Exception ex)
		{
			System.err.println("Unknown Exception");
			ex.printStackTrace();
		}
		return null;
	}
	
	public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		final JsonObject jsonObject = json.getAsJsonObject();
		try
		{
			JsonPrimitive jsonP = jsonObject.getAsJsonPrimitive("json");
			String jsonS = jsonP.getAsString();
			//Thsi is going to fuck up on other threads ...
			//Entity b = PhysicsManager.JSONifier.j2b2Body(PhysicsManager.PhysWorld, new JSONObject(jsonS));
		}
		catch(Exception ex)
		{
			System.err.println("Unknown Exception");
			ex.printStackTrace();
		}
		return null;
	}

	
}