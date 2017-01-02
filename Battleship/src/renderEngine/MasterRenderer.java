package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrain.Terrain;

public class MasterRenderer
{	
	// Create important constants
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1500.0f;
	private Matrix4f projectionMatrix;
		
	// Create a Renderer and Shader which the MasterRenderer can use
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	// Create a HashMap to store every category of entities
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRenderer()
	{
		// Enable Culling
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
				
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
	}
	
	public void render(Light light, Camera camera)
	{
		// Prepare the renderer
		prepare();
		
		// Load the shader
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		
		// render
		renderer.render(entities);
		
		// Stop the shader
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		terrains.clear();
		
		// Clear the List of entities
		// Otherwise it would stack up each frame
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity)
	{
		// Extract the model and create a batch for the entities
		TexturedModel model = entity.getModel();
		List<Entity> batch = entities.get(model);
		
		// Either place the Entity in a suitable batch or create a new one
		if(batch != null)
		{
			batch.add(entity);
		} else
		{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}
	
	// Clean up the shader
	public void cleanUp()
	{
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	public void prepare()
	{		
		// Enable Depth-Test
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// Clear the Color and Depth-Test from the last frame
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1f);		
	}
	
	/* Create the projectionMatrix
	 *  / (1 / tan(FOV/2))/a	0				0				0					\
	 * /		0				1/tan(FOV/2)	0				0					 \
	 * \		0				0				-zP/zM			-(2*zFar*zNear)/*zM	 /
	 *  \		0				0				-1				0					/
	 *  
	 *  a = aspect ratio (mostly 16:9)
	 *  FOV = field of view (70)
	 *  zM = zFar - zNear = 1000.0f - 0.1f = 999.9f
	 *  zP = zFar + zNear = 1000.0f + 0.1f = 1000.1f
	 *  
	 */
	private void createProjectionMatrix()
	{
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float scaleY = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float scaleX = scaleY / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = scaleX;
		projectionMatrix.m11 = scaleY;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = - ((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
}
