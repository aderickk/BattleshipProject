package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light
{
	// Create Variable for the position and color of the light
	private Vector3f position;
	private Vector3f color;
	
	// Constructor
	public Light(Vector3f position, Vector3f color) {
		super();
		this.position = position;
		this.color = color;
	}
	
	/*
	 * Simple Getters- and Setter-Section 
	 */
	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
}
