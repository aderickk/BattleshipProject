package models;

public class RawModel
{
	// Each model needs its own vao, which can be referenced by its vaoID
	// Also the number of vertices is stored, which the renderer needs
	private int vaoID;
	private int vertexCount;
	
	public RawModel(int vaoID, int vertexCount)
	{
		// Set Constructor-Variables
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	// Simple Getter for [vaoID]
	public int getVaoID() {
		return vaoID;
	}

	// Simple Getter for [vertexCount]
	public int getVertexCount() {
		return vertexCount;
	}
}
