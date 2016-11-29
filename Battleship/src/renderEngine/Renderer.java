package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

public class Renderer
{
	// Create important constants
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000.0f;
	
	// Create a projectionMatrix and Shader to use
	private Matrix4f projectionMatrix;
	private StaticShader shader;
	
	public Renderer(StaticShader shader)
	{
		
		this.shader = shader;
		
		// Enable Culling
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		// Create the projectionMatrix
		createProjectionMatrix();
		
		// Handle the shader
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void prepare()
	{		
		// Enable Depth-Test
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// Clear the Color and Depth-Test from the last frame
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1f);		
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities)
	{
		for(TexturedModel model:entities.keySet())
		{
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity:batch)
			{
				prepareInstances(entity);
				
				// final render
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model)
	{
		RawModel rawModel = model.getRawModel();
		
		// Bind the model's VAO
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		// Activate the 0th, 1st and 2nd AttributeList of the VAO
		// (which should contain the positions of the vertices and textureCoordinates)
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		
		// Handle the texture
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		// Activate the 0th TextureBank
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}
	
	private void unbindTexturedModel()
	{
		// Deactivate the 2nd, 1st and 0th AttributeList of the VAO
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		
		// Unbind the model's VAO
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstances(Entity entity)
	{
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScaleX(), entity.getScaleY(), entity.getScaleZ());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
	@Deprecated
	public void render(Entity entity, StaticShader shader, Camera camera)
	{
		// Extract the TexturedModel and RawModel
		TexturedModel texturedModel = entity.getModel();
		RawModel rawModel = texturedModel.getRawModel();
		
		// Bind the model's VAO
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		// Activate the 0th, 1st and 2nd AttributeList of the VAO
		// (which should contain the positions of the vertices and textureCoordinates)
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScaleX(), entity.getScaleY(), entity.getScaleZ());
		shader.loadTransformationMatrix(transformationMatrix);
		ModelTexture texture = texturedModel.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		
		// Activate the 0th TextureBank
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		
		// Render the model
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		// Deactivate the 2nd, 1st and 0th AttributeList of the VAO
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		
		// Unbind the model's VAO
		GL30.glBindVertexArray(0);
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
