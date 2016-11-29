package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera
{
	// Create Variables for the Camera's position and orientation
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;	// x-Axis
	private float yaw;		// y-Axis
	private float roll;		// z-Axis
	
	// Creating Variables for the Camera's move- and rotation-speed
	private float moveSpeed = 0.05f;
	private float rotateSpeed = 0.5f;
	
	// Create a Vector for the Camera's orientation
	private Vector3f cameraVector;
	
	// Empty Constructor. May be used in the future
	public Camera()
	{
		// Default orientation
		pitch = 0;
		yaw = 0;
		roll = 0;
		
		// initialize the CameraVector
		updateCameraVector();
	}
	
	// Move the Camera around
	public void move()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.x += cameraVector.x * moveSpeed;
			position.z += cameraVector.z * moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			position.x += cameraVector.z * moveSpeed;
			position.z -= cameraVector.x * moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			position.x -= cameraVector.x * moveSpeed;
			position.z -= cameraVector.z * moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x -= cameraVector.z * moveSpeed;
			position.z += cameraVector.x * moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			position.y += moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			position.y -= moveSpeed;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			yaw = (yaw + rotateSpeed) % 360;
			updateCameraVector();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			yaw = (yaw - rotateSpeed) % 360;
			updateCameraVector();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			pitch = (pitch - rotateSpeed) % 360;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			pitch = (pitch + rotateSpeed) % 360;
		}
	}
	
	// Update the cameraVector
	private void updateCameraVector()
	{
		cameraVector = new Vector3f((float) Math.cos(Math.toRadians(yaw)), 0, (float) -Math.sin(Math.toRadians(yaw)));
		//printOrientation();
	}
	
	// Print the camera's orientation and -Vector
	/*private void printOrientation()
	{
		System.out.println("Orientation: \npitch: " + pitch + "\nyaw: " + yaw + "\nroll: " + roll + "\n\n");
		System.out.println(cameraVector.x + " | " + cameraVector.z);
	}*/
	
	/*
	 * Simple Getters- and Setter-Section 
	 */
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
}
