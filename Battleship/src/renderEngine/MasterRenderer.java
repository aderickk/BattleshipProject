package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;

public class MasterRenderer
{
	// Create a Renderer and Shader which the MasterRenderer can use
	private StaticShader shader = new StaticShader();
	private Renderer renderer = new Renderer(shader);
	
	// Create a HashMap to store every category of entities
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	
	public void render(Light light, Camera camera)
	{
		// Prepare the renderer
		renderer.prepare();
		
		// Load the shader
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		
		// render
		renderer.render(entities);
		
		// Stop the shader
		shader.stop();
		
		// Clear the List of entities
		// Otherwise it would stack up each frame
		entities.clear();
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
	}
}
