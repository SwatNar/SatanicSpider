package com.SatanicSpider.Components.Render;

import com.SatanicSpider.artemis.Component;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.Message;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.shape.Quad;

import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 * @author Nicholas Mamo - Nyphoon Games
 * @author Bryant Overgard - Fixded crap
 */
public class Sprite implements Component
{

	//TODO: rewrite this so its thread safe
	
	
	
	/*
	 * s_direction:
	 *	if it's set to 2, then (-1)^n would be positive i.e. sprite moves to next frame (to the right)
	 *	if it's set to 1, then (-1)^n would be negative i.e. sprite moves to previous frame (to the left)
	 */
	private boolean s_paused, s_animated;
	private int s_frames, s_rows, s_direction = 2, s_currentFrame = 0, s_frameWidth;
	private float s_timeSeparation, s_time;
	
	Vector3f offset = null;

	public void init(Entity e)
	{
	}

	public void cleanup(Entity e)
	{

	}

	public void receiveMessage(Message m)
	{

	}

	public enum spriteAction
	{

		START,
		SCROLL,
		LOOP,
		NOLOOP,
		REVERSE,
		NONE
	}
	private /*String*/ spriteAction s_onEnd, s_onResume, s_tempOnResume = spriteAction.NONE;
	private String s_name;

	private Geometry s_geometry;
	private float[] s_uvTextureArray;
	private VertexBuffer s_uvTexture;
	private Node s_node;
	
	float h_width;
	float h_height;

	public Sprite()
	{
	}
	
	public Sprite(String imageLocation,
			String name,
			AssetManager assetManager,
			boolean animated,
			boolean transparent,
			int frames,
			int rows,
			float timeSeparation,
			spriteAction onEnd,
			spriteAction onResume)
	{
		//todo construct based off of below ...
		init(imageLocation, name, assetManager, animated, transparent, frames, rows, timeSeparation, onEnd, onResume, new Vector3f(0,0,0), new Vector3f(0,0,0));
		
	}
	
	public void init (String imageLocation,
			String name,
			AssetManager assetManager,
			boolean animated,
			boolean transparent,
			int frames,
			int rows,
			float timeSeparation,
			spriteAction onEnd,
			spriteAction onResume, Vector3f rot, Vector3f offset)
	{
		//System.err.println("Sprite init: top");
		this.s_animated = animated;
		this.s_frames = frames;
		this.s_rows = rows;
		this.s_name = name;
		this.s_timeSeparation = timeSeparation;
		this.s_onEnd = onEnd;
		this.s_onResume = this.s_tempOnResume = onResume;

		Texture s_spritesheet = assetManager.loadTexture(imageLocation);
		
		h_width		= s_spritesheet.getImage().getWidth() / frames / 32.0f /2.0f;
		h_height	= s_spritesheet.getImage().getHeight() / rows / 32.0f / 2.0f;
		Quad s_quad = new Quad((h_width * 2.0f), (h_height * 2.0f));

		//System.err.println("Sprite init: Created Quad");
		
		s_geometry = new Geometry(name, s_quad);
		Material s_material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		s_material.setTexture("ColorMap", s_spritesheet);
		s_geometry.setMaterial(s_material);
		if (transparent)
		{
			s_material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
			s_material.getAdditionalRenderState().setAlphaTest(true);
		}

		
		s_node = new Node(name+Math.random());
		s_geometry.center();
		
		//s_geometry.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		s_node.attachChild(s_geometry);
		s_node.center();
		s_node.rotate(rot.x,rot.y,rot.z);
		//s_node.rotateUpTo(Vector3f.UNIT_Z);
		Vector3f trans = s_node.getLocalTranslation().add(offset);
		s_node.setLocalTranslation(trans.x, trans.y, trans.z);
		this.offset = offset;
		//s_node.setLocalTranslation(trans.x, trans.y, trans.z);
		//System.err.println("Sprite init: Attached Node");
		
		s_uvTexture = s_geometry.getMesh().getBuffer(VertexBuffer.Type.TexCoord);
		s_uvTextureArray = BufferUtils.getFloatArray((FloatBuffer) s_uvTexture.getData());
		for (int j = 0; j < s_uvTextureArray.length; j++)
		{
			if (j % 2 == 0)
			{
				s_uvTextureArray[j] /= frames;
			} else
			{
				s_uvTextureArray[j] /= rows;
			}
		}
		if (onEnd == spriteAction.SCROLL)
		{
			this.s_frameWidth = this.s_frames;
			this.s_frames = s_spritesheet.getImage().getWidth();
		}
		s_uvTexture.updateData(BufferUtils.createFloatBuffer(s_uvTextureArray));
		
		//System.err.println("Sprite init: bottom");
	}

	public Sprite(String imageLocation,
			String name,
			AssetManager assetManager,
			boolean animated,
			boolean transparent,
			int frames,
			int rows,
			float timeSeparation,
			spriteAction onEnd,
			spriteAction onResume, Vector3f rot)
	{
		init(imageLocation, name, assetManager, animated, transparent, frames, rows, timeSeparation, onEnd, onResume, rot,new Vector3f(0,0,0));
		
	}
	
	public Sprite(String imageLocation,
			String name,
			AssetManager assetManager,
			boolean animated,
			boolean transparent,
			int frames,
			int rows,
			float timeSeparation,
			spriteAction onEnd,
			spriteAction onResume, Vector3f rot, Vector3f offset)
	{
		init(imageLocation, name, assetManager, animated, transparent, frames, rows, timeSeparation, onEnd, onResume, rot, offset);
		
	}

	public void setPaused(boolean paused)
	{
		/*
		 * When paused is set to false, i.e. animation is set to resume/play, check whether or not onResume has been changed (onResume != tempOnResume). 
		 *      If it hasn't, then just play;
		 *      Else set to play and then replace onResume by tempOnResume
		 */
		this.s_paused = paused;
		if (!s_paused && !(s_onResume == s_tempOnResume))
		{
			s_onResume = s_tempOnResume;
		} else if (!s_paused && (s_onResume == spriteAction.START))
		{
			if (s_onEnd == spriteAction.SCROLL)
			{
				s_currentFrame = 0;
				s_time = 0;
				s_uvTextureArray = BufferUtils.getFloatArray((FloatBuffer) s_uvTexture.getData());
				for (int j = 0; j < s_uvTextureArray.length; j++)
				{
					if (j == 0 || j == 6)
					{
						s_uvTextureArray[j] = 0;
					} else if (j % 2 == 0)
					{
						s_uvTextureArray[j] = 1f / s_frameWidth;
					}
				}
				s_uvTexture.updateData(BufferUtils.createFloatBuffer(s_uvTextureArray));
			} else
			{
				s_currentFrame = 0;
				s_time = 0;
				s_uvTextureArray = BufferUtils.getFloatArray((FloatBuffer) s_uvTexture.getData());
				for (int j = 0; j < s_uvTextureArray.length; j++)
				{
					if (j == 0 || j == 6)
					{
						s_uvTextureArray[j] = 0;
					} else if (j % 2 == 0)
					{
						s_uvTextureArray[j] = 1f / s_frames;
					}
				}
				s_uvTexture.updateData(BufferUtils.createFloatBuffer(s_uvTextureArray));
			}
		}
	}

	public void setFrames(int frames)
	{
		this.s_frames = frames;
	}

	public void changeDirection()
	{
		if (s_direction == 2)
		{
			s_direction = 1;
		} else
		{
			s_direction = 2;
		}
	}

	public void setCurrentFrame(int currentFrame)
	{
		s_currentFrame = currentFrame;
	}

	public void setTimeSeparation(int timeSeparation)
	{
		this.s_timeSeparation = timeSeparation;
	}

	public void setTime(float time)
	{
		this.s_time = time;
	}

	public void setOnEnd(spriteAction onEnd)
	{
		/*
		 * Possible values: 
		 *      "Loop"      - Start from the beginning
		 *      "NoLoop"    - Stop animation (setPaused(true)), store onResume into tempOnResume and update onResume to start all over
		 *      "Reverse"   - Go back, restart upon reaching beginning
		 *	"Scroll"    - Scroll, like in a side-scrolling texture. In this case, frames = texture.width
		 */
		this.s_onEnd = onEnd;
	}

	public void setOnResume(spriteAction onResume)
	{
		/*
		 * Possible values: 
		 *      "Continue"  - Continue from current frame
		 *      "Start"     - Start from the beginning
		 */
		this.s_onResume = onResume;
	}

	public void setName(String name)
	{
		this.s_name = name;
	}

	public boolean getPaused()
	{
		return s_paused;
	}

	public boolean getAnimated()
	{
		return s_animated;
	}

	public int getFrames()
	{
		return s_frames;
	}

	public int getDirection()
	{
		return s_direction;
	}

	public int getCurrentFrame()
	{
		return s_currentFrame;
	}

	public int getFrameWidth()
	{
		return s_frameWidth;
	}

	public float getTimeSeparation()
	{
		return s_timeSeparation;
	}

	public float getTime()
	{
		return s_time;
	}

	public spriteAction getOnEnd()
	{
		return s_onEnd;
	}

	public spriteAction getOnResume()
	{
		return s_onResume;
	}

	public String getName()
	{
		return s_name;
	}

	public Geometry getGeometry()
	{
		return s_geometry;
	}

	public float[] getVertexArray()
	{
		return s_uvTextureArray;
	}

	public VertexBuffer getVertexBuffer()
	{
		return s_uvTexture;
	}

	public Node getNode()
	{
		return s_node;
	}

	public void updateVertexArray(int index, float value)
	{
		s_uvTextureArray[index] = value;
	}

	public void move(float x, float y)
	{

		s_node.setLocalTranslation(s_node.getLocalTranslation().x + x, s_node.getLocalTranslation().y + y, s_node.getLocalTranslation().z);
	}

	public void moveAbsolute(float x, float y)
	{
		//System.err.println("Sprite moveAbsolute: " + x + " " + y);
		
		//s_node.setLocalTranslation(x-h_width, y-h_height, s_node.getLocalTranslation().z);
		Vector3f trans = offset.add(x, y, 0);
		s_node.setLocalTranslation(trans);
	}

	public void setOrder(int z)
	{
		s_node.setLocalTranslation(s_node.getLocalTranslation().x, s_node.getLocalTranslation().y, s_node.getLocalTranslation().z + z);
	}

	public void setOrderAbsolute(int z)
	{
		s_node.setLocalTranslation(s_node.getLocalTranslation().x, s_node.getLocalTranslation().y, z);
	}

	public void rotate(float x, float y, float z)
	{
		
		//System.err.println("Rotate " + z);
		//Quaternion yaw = new Quaternion();
		//yaw.fromAngleAxis(z, Vector3f.UNIT_Z);
		//s_node.setLocalRotation(yaw);
		//s_node.rotate(FastMath.DEG_TO_RAD * x, FastMath.DEG_TO_RAD * y, FastMath.DEG_TO_RAD * z);
		
	}
}
