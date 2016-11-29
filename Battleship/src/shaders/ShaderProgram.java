package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class ShaderProgram
{
	// Create Variable to store the ID oh the ShaderProgram and the two Shaders
	private static int programID;
	private static int vertexShaderID;
	private static int fragmentShaderID;
	
	// matrixBuffer for loading a matrix as a uniform variable
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile)
	{
		// Load both Shaders + Program and store their IDs
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);		
		programID = GL20.glCreateProgram();
		
		// Attach both Shaders to the program
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		
		// Link Inputs to the Shadersprograms
		bindAttributes();
		
		// Link the program
		GL20.glLinkProgram(programID);
		
		// Validate the program
		GL20.glValidateProgram(programID);
		
		// Load all uniform variables
		getAllUniformLocations();
	}
	
	// abstract Method: needs to be implemented in every instance
	protected abstract void getAllUniformLocations();
	
	// Returns the location of the uniform variable
	protected int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	// Start the Program
	public void start()
	{
		GL20.glUseProgram(programID);
	}
	
	// Stop the program
	public void stop()
	{
		GL20.glUseProgram(0);
	}
	
	public void cleanUp()
	{
		// Any maybe running Program
		stop();
		
		// Detach both Shaders
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		
		// Delete both Shaders
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		
		// Delete the Program
		GL20.glDeleteProgram(programID);
	}
	
	// Has to be implemented in every Instance
	protected abstract void bindAttributes();
	
	// Bind an Attribute to the Program
	protected void bindAttribute(int attribute, String variableName)
	{
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	
	// Load a float as a uniform variable
	protected void loadFloat(int location, float value)
	{
		GL20.glUniform1f(location, value);
	}
	
	// Load a vector as a uniform variable
	protected void loadVector(int location, Vector3f vector)
	{
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	// Load a boolean as a uniform variable
	// GLSL has no boolean -> float to represent true (1) and false (0)
	protected void loadBoolean(int location, boolean value)
	{
		if(value)
		{
			GL20.glUniform1f(location, 1f);
		} else
		{
			GL20.glUniform1f(location, 0f);
		}
	}
	
	protected void loadMatrix(int location, Matrix4f matrix)
	{
		// Store the matrix in the matrixBuffer
		matrix.store(matrixBuffer);
		
		// Change it from write to read
		matrixBuffer.flip();
		
		// Load the matrixBuffer as a uniform variable
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	private static int loadShader(String file, int type)
	{
		// Create a new StringBuilder
		StringBuilder shaderSource = new StringBuilder();
		
		// Try read the whole file and buffer it
		try {
			// Create a new BufferedReader to store the whole file
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
			// Read the whole file
			while((line = reader.readLine()) != null)
			{
				shaderSource.append(line).append("\n");
			}
			
			// Close the reader
			reader.close();
		} catch (IOException e) {
			// File could not be read -> ErrorMessage + exit System
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Create a new ShaderID with type [type]
		int shaderID = GL20.glCreateShader(type);
		
		// Pass the SourceCode of the Shader
		GL20.glShaderSource(shaderID, shaderSource);
		
		// Compile the Shader
		GL20.glCompileShader(shaderID);
		
		// When Shader could not be compiled -> ErrorMessage
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
		{
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			if(type == GL20.GL_VERTEX_SHADER)
			{
				System.err.println("Could not compile Vertex-Shader");
			} else
			{
				System.err.println("Could not compile Fragment-Shader");
			}			
			System.exit(-1);
		}
		
		return shaderID;
	}
}
