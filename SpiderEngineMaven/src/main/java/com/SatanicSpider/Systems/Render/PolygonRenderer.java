/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.SatanicSpider.Systems.Render;

import com.SatanicSpider.artemis.Aspect;
import com.SatanicSpider.artemis.ComponentMapper;
import com.SatanicSpider.artemis.Entity;
import com.SatanicSpider.artemis.annotations.Mapper;
import com.SatanicSpider.artemis.systems.EntityProcessingSystem;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.SatanicSpider.Components.Position;
import com.SatanicSpider.Components.Render.Polygon;
import static com.SatanicSpider.SharedVars.assetManager;
import static com.SatanicSpider.SharedVars.guiNode;
import org.jbox2d.common.Vec2;

/**
 *
 * @author bryant
 */
public class PolygonRenderer extends EntityProcessingSystem
{
//TODO: Refactor this to render any sprite

	@Mapper
	ComponentMapper<Position> position_mapper;
	@Mapper
	ComponentMapper<Polygon> polygon_mapper;

	boolean initialized = false;

	@SuppressWarnings("unchecked")
	public PolygonRenderer()
	{
		super(Aspect.getAspectForOne(Polygon.class));

	}

	@Override
	protected boolean checkProcessing()
	{
		return true;
	}

	@Override
	protected void begin()
	{

	}

	protected void process(Entity e)
	{
		Vec2 offset = new Vec2(0, 0);
		//System.err.println(e);
		Position p = position_mapper.getSafe(e);
		Polygon poly = polygon_mapper.getSafe(e);

		if (p != null)
		{
			offset = p.pos;
		}

		if (poly != null)
		{
			if (poly.my_mesh == null)
			{
				//Make the mesh
				Mesh m = new Mesh();

				// Vertex positions in space
				Vector3f[] vertices = new Vector3f[poly.points.length];
				for(int i = 0; i < poly.points.length; i++)
				{
					//System.err.println(poly + " Rendering point " + (poly.offset.x + poly.points[i].x*32) + "," + (poly.offset.y + poly.points[i].y*32));
					vertices[i] = new Vector3f((poly.offset.x + poly.points[i].x), (poly.offset.y + poly.points[i].y), 0);
				}
				//vertices[poly.points.length] = new Vector3f((poly.offset.x + poly.points[0].x*32), (poly.offset.y + poly.points[0].y*32), 0);
				
				// Texture coordinates
				/*Vector2f[] texCoord = new Vector2f[4];
				texCoord[0] = new Vector2f(0, 0);
				texCoord[1] = new Vector2f(1, 0);
				texCoord[2] = new Vector2f(0, 1);
				texCoord[3] = new Vector2f(1, 1);*/

				// Indexes. We define the order in which mesh should be constructed
				int[] indexes =
				{
					2, 0, 1, 0, 2, 3
				};

				// Setting buffers
				m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
				//m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
				m.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
				m.updateBound();

				// *************************************************************************
				// Third mesh will use a wireframe shader to show wireframe
				// *************************************************************************
				Mesh wfMesh = m.clone();
				Geometry wfGeom = new Geometry("wireframeGeometry", wfMesh);
				Material matWireframe = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
				matWireframe.setColor("Color", ColorRGBA.Green);
				matWireframe.getAdditionalRenderState().setWireframe(true);
				wfGeom.setMaterial(matWireframe);
				//wfGeom.setLocalTranslation(1f, 1f, 0);
				Node s_node = new Node();
				s_node.attachChild(wfGeom);
				
				//s_node.setLocalTranslation(poly.offset.x, poly.offset.y, 0);
				
				guiNode.attachChild(s_node);
				
				poly.my_mesh = wfMesh;
				poly.my_geom = wfGeom;
			}
			//render the mesh?
		} else
		{
			System.err.println("TRIED TO RENDER A NULL POLY");
		}

	}

	@Override
	protected void end()
	{

	}

}
