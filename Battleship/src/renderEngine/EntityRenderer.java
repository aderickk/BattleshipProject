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

public class EntityRenderer
{
	
	
	// Create a Shader to use
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix)
	{
		
		this.shader = shader;		
		
		// Handle the shader
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
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
}
