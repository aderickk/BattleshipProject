package textures;

public class ModelTexture
{
	// Create Variable for the used TextureID
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	// Constructor
	public ModelTexture(int id)
	{
		this.textureID = id;
	}
	
	/*
	 * Simple Getters- and Setter-Section 
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public int getTextureID() {
		return textureID;
	}
	
}
