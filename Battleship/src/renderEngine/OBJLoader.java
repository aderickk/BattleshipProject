package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

public class OBJLoader
{
	public static RawModel loadObjModel(String fileName, Loader loader)
	{
		// Try to access the .obj-file
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Could not load " + fileName + ".obj");
			e.printStackTrace();
		}
		
		// Buffer the whole .obj-file
		BufferedReader reader = new BufferedReader(fr);		
		String line;
		
		// Create Lists to store vertices, textureCoordinates, normal vectors and indices
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textureCoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		// Loader needs Array, not Lists, so at some point we have to convert the Lists into the Arrays
		// But we don't know the size yet
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		
		try {
			// read whole file
			while(true)
			{
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				
				if(line.startsWith("v "))
				{
					// Read and add the vertex
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if(line.startsWith("vt "))
				{
					// Read and add the textureCoordinate
					Vector2f textureCoord = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textureCoords.add(textureCoord);
				}else if(line.startsWith("vn "))
				{
					// Read and add the normal
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if(line.startsWith("f "))
				{
					// Face-section reached -> move on to parse the indices
					// Now we know the sizes
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					
					// Break out of the loop to move on
					break;
				}
			}
			
			// Get the indices in the correct order
			while(line != null)
			{
				if(!line.startsWith("f "))
				{
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				// Process the textureCoordinates and normalVectors of the three vertices of the current Triangle
				processVertex(vertex1, indices, textureCoords, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textureCoords, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textureCoords, normals, textureArray, normalsArray);
				
				// Read the next Line
				line = reader.readLine();
			}
			
			// Close the Reader
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Store all vertices in the Array
		verticesArray = new float[vertices.size() * 3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for(Vector3f vertex:vertices)
		{
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		// Store all indices in the Array
		for(int i=0; i<indices.size(); i++)
		{
			indicesArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray)
	{
		// Store the vertexIndex
		int currentVertexPointer = Integer.parseInt(vertexData[0]) -1;
		indices.add(currentVertexPointer);
		
		// Store the textureInidices for the currentVertex
		Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) -1);
		textureArray[currentVertexPointer * 2] = currentTexture.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;
		
		// Store the normalsIndices for the currentVertex
		Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNormal.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNormal.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNormal.z;
	}
}
