package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

public class Loader
{
	// Create a VAOList and a VBOList to store them for deleting when closing the Game
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices)
	{
		// Create a VAO and add it to the VAOList
		int vaoID = createVAO();
		vaos.add(vaoID);
		
		// Bind the indicesBuffer
		bindIndicesBuffer(indices);
		
		// Store [positions] in the 0th Slot of the VAO and [textureCoods] in the 1st
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		
		// Unbind the VAO
		unbindVAO();
		
		return new RawModel(vaoID, indices.length);
	}
	
	public int loadTexture(String fileName)
	{
		// Try to load a Texture with [fileName]
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		} catch (FileNotFoundException e) {
			System.err.println("Texture '" + fileName + "' not found at location res/" + fileName + ".png");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error while loading texture: " + fileName + ".png");
			e.printStackTrace();
		}
		
		// Create a new TextureID, store it (for cleaning up later) and return it
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
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
		
		// Delete all Textures
		for(int texture:textures)
		{
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO()
	{
		// Create a VAO and bind it
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int size, float[] data)
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
		GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0,0);
		
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
