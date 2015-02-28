/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.SatanicSpider.Storage;

//import com.google.gson.Gson;
import com.SatanicSpider.Serialization.Serilization;
import com.SatanicSpider.core.Configuration;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author bryant
 */
public class FileStorage implements StorageConnector
{
	//Gson gson = new Gson();
	
	public enum FileMode
	{
		JSON,
		Serialize
	}
	
	public FileMode filemode = FileMode.JSON;
	
	String base_directory = Configuration.Storage.Directory + "/ObjectStore/";
	Path base_path;
	
	boolean enabled = false;
	
	public FileStorage()
	{
		initialize();
	}
	
	public void initialize()
	{
		base_path = Paths.get(base_directory);
		if(!Files.exists(base_path))
		{
			try
			{
				Files.createDirectories(base_path);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				disable();
			}
		}
		
		enabled = true;
	}
	
	
	public boolean Store(Object o)
	{
		if(o == null)
			return false;
		boolean ret = false;
		Class c = o.getClass();
		switch(filemode)
		{
			case JSON:
			{
				
				int fail = 0;
				try
				{
					
					String json = Serilization.getInstance().gson.toJson(o);
					UUID id;
					
					fail = 1;
					JSONObject j = new JSONObject(json);
					if(j != null)
					{
						String uid = j.getString("uuid");

						if(uid == null)
						{
							//Throw exception ... or create a random uuid
						}
						else
						{
							Path storePath = Paths.get(base_path.toString()+"/" + c.getName());
							Files.createDirectories(storePath);
							Path filePath = Paths.get(storePath.toString()+"/"  + uid);
							Files.deleteIfExists(filePath);
							File f = Files.createFile(filePath).toFile();
							Files.write(filePath, json.getBytes());
							ret = true;
						}
					}
					else
					{
						System.err.println("J was null?!");
					}
				}
				catch(JSONException jx)
				{
					System.err.println("Error Serializing: JSON [" + fail + "]");
					jx.printStackTrace();
				}
				catch(IOException iox)
				{
					
					System.err.println("Error Serializing: IO [" + fail + "]");
					iox.printStackTrace();
				}
				
			}; break;
			
			case Serialize:
			{
				System.err.println("Not Implemented!");
			};
		}
		return ret;
	}
	
	public Object UnStore(Class c, UUID id)
	{
		
		switch(filemode)
		{
			case JSON:
			{
				
				int fail = 0;
				try
				{
					
					//UUID id;
					
					fail = 1;
					//JSONObject j = new JSONObject(json);
					//String uid = j.getString("uuid");
					
					
						Path storePath = Paths.get(base_path.toString()+"/" + c.getName());
						Path filePath = Paths.get(storePath.toString()+"/"  + id);
						if(filePath != null && Files.exists(filePath))
						{
							String json = new String(Files.readAllBytes(filePath));
							System.err.println(Serilization.getInstance() + " " + Serilization.getInstance().gson);
							Object ret = Serilization.getInstance().gson.fromJson(json, c);
							return ret;
						}
					
				}
				catch(NoSuchFileException nsfx)
				{
					System.err.println("Error Serializing: File Not Found [" + fail + "]");
					nsfx.printStackTrace();
				}
				catch(IOException iox)
				{
					
					System.err.println("Error Serializing: IO [" + fail + "]");
					iox.printStackTrace();
				}
				catch(Exception ex)
				{
					System.err.println("Error Serializing: Unknown [" + fail + "]");
					ex.printStackTrace();
				}
				
			}; break;
			
			case Serialize:
			{
				System.err.println("Not Implemented!");
			};
		}
		//Got an error
		return null;
	}
	
	public void disable()
	{
		enabled = false;
	}
	
	public void enable()
	{
		initialize();
	}
	
}
