package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import models.TexturedModel;

public class Renderer
{
	public void prepare()
	{
		// Clear the Color from the last frame
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1f);		
	}
	
	public void render(TexturedModel model)
	{
		// Extract the RawModel
		RawModel rawModel = model.getRawModel();
		
		// Bind the model's VAO
		GL30.glBindVertexArray(rawModel.getVaoID());
		
		// Activate the 0th and 1st AttributeList of the VAO
		// (which should contain the positions of the vertices and textureCoordinates)
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		// Activate the 0th TextureBank
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
		
		// Render the model
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		// Deactivate the 1st and 0th AttributeList of the VAO
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		
		// Unbind the model's VAO
		GL30.glBindVertexArray(0);
	}
}
