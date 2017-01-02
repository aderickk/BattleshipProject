package renderEngine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.TerrainShader;
import terrain.Terrain;
import textures.ModelTexture;
import toolbox.Maths;

public class TerrainRenderer
{
	private TerrainShader shader;
	
	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix)
	{
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Terrain> terrains)
	{
		for(Terrain terrain:terrains)
		{
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			// final render
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTexturedModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain)
	{
		RawModel rawModel = terrain.getModel();
		
		// Bind the model's VAO
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		// Activate the 0th, 1st and 2nd AttributeList of the VAO
		// (which should contain the positions of the vertices and textureCoordinates)
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		
		// Handle the texture
		ModelTexture texture = terrain.getTexture();
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
	
	private void loadModelMatrix(Terrain terrain)
	{
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX()-terrain.getSIZE()/2, 0, terrain.getZ()-terrain.getSIZE()/2), 0, 0, 0, 1, 1, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
