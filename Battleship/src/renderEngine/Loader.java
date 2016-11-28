package renderEngine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Loader
{
	// Create a VAOList and a VBOList to store them for deleting when closing the Game
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, int[] indices)
	{
		// Create a VAO and add it to the VAOList
		int vaoID = createVAO();
		vaos.add(vaoID);
		
		// Bind the indicesBuffer
		bindIndicesBuffer(indices);
		
		// Store [positions] in the 0th Slot of the VAO
		storeDataInAttributeList(0, positions);
		
		// Unbind the VAO
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	public void cleanUp()
	{
		// Delete all VAOs
		for(int vao:vaos)
		{
			GL30.glDeleteVertexArrays(vao);
		}
		
		// Delete all VBOs
		for(int vbo:vbos)
		{
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	private int createVAO()
	{
		// Create a VAO and bind it
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, float[] data)
	{
		// Generate a VBO and add it to the VBOList
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		
		// Bind the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		// Create a FloatBuffer from [data] and store it in the VBO
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
		// Put the VBO in an AttributeList of the VAO
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0,0);
		
		// Unbind the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO()
	{
		// Unbinds the current VAO
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices)
	{
		// Generate a VBO and add it to the VBOList
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		
		// Bind the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		
		// Create IntBuffer from [indices] and store it in the VBO
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data)
	{
		// Create an IntBuffer
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		
		// Put [data] in the FloatBuffer and flip it (write -> read)
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data)
	{
		// Create a FloatBuffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		
		// Put [data] in the FloatBuffer and flip it (write -> read)
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
}
