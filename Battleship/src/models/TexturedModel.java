package models;

import textures.ModelTexture;

public class TexturedModel
{
	// Variables for the model and texture
	private RawModel rawModel;
	private ModelTexture texture;
	
	// Constructor
	public TexturedModel(RawModel model, ModelTexture texture)
	{
		this.rawModel = model;
		this.texture = texture;
	}
	
	// Simple Getter
	public RawModel getRawModel()
	{
		return rawModel;
	}
	
	// Simple Getter
	public ModelTexture getTexture()
	{
		return texture;
	}
}
