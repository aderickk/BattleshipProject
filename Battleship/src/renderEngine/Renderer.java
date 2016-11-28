package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer
{
	public void prepare()
	{
		// Clear the Color from the last frame
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0.2f, 0.2f, 0.2f, 1f);		
	}
	
	public void render(RawModel model)
	{
		// Bind the model's VAO
		GL30.glBindVertexArray(model.getVaoID());
		
		// Activate the 0th AttributeList of the VAO
		// (which should contain the positions of the vertices)
		GL20.glEnableVertexAttribArray(0);
		
		// Render the model
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		// Deactivate the 0th AttributeList of the VAO
		GL20.glDisableVertexAttribArray(0);
		
		// Unbind the model's VAO
		GL30.glBindVertexArray(0);
	}
}
