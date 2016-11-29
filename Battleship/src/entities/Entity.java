package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class Entity
{
	// Each Entity needs a model, position, rotation and scale
	private TexturedModel model;
	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scaleX, scaleY, scaleZ;
	
	// Constructor to fill the needed variables with the correct values
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scaleX,	float scaleY, float scaleZ)
	{
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}
	
	// Increase the position of the entity
	public void increasePosition(float x, float y, float z)
	{
		position.x += x;
		position.x += y;
		position.x += z;
	}
	
	// Decrease the position of the entity
	public void decreasePosition(float x, float y, float z)
	{
		position.x -= x;
		position.x -= y;
		position.x -= z;
	}
	
	// Increase the rotation of the entity
	public void increaseRotation(float x, float y, float z)
	{
		rotX += x;
		rotY += y;
		rotZ += z;
	}
	
	// Decrease the rotation of the entity
	public void decreaseRotation(float x, float y, float z)
	{
		rotX -= x;
		rotY -= y;
		rotZ -= z;
	}
	
	// Increase the scale of the entity
	public void increaseSclae(float x, float y, float z)
	{
		scaleX += x;
		scaleY += y;
		scaleZ += z;
	}
	
	// Decrease the scale of the entity
	public void decreaseScale(float x, float y, float z)
	{
		scaleX -= x;
		scaleY -= y;
		scaleZ -= z;
	}
		
	/*
	 * Simple Getters- and Setter-Section 
	 */
	public TexturedModel getModel()
	{
		return model;
	}
	
	public void setModel(TexturedModel model)
	{
		this.model = model;
	}
	
	public Vector3f getPosition()
	{
		return position;
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}
	
	public float getRotX()
	{
		return rotX;
	}
	
	public void setRotX(float rotX) 
	{
		this.rotX = rotX;
	}
	
	public float getRotY()
	{
		return rotY;
	}
	
	public void setRotY(float rotY)
	{
		this.rotY = rotY;
	}
	
	public float getRotZ()
	{
		return rotZ;
	}
	
	public void setRotZ(float rotZ)
	{
		this.rotZ = rotZ;
	}
	
	public float getScaleX()
	{
		return scaleX;
	}
	
	public void setScaleX(float scaleX)
	{
		this.scaleX = scaleX;
	}
	
	public float getScaleY()
	{
		return scaleY;
	}
	
	public void setScaleY(float scaleY)
	{
		this.scaleY = scaleY;
	}
	
	public float getScaleZ()
	{
		return scaleZ;
	}
	
	public void setScaleZ(float scaleZ)
	{
		this.scaleZ = scaleZ;
	}	
}
