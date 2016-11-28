package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop
{

	public static void main(String[] args)
	{
		// create a Display (width, height, fps_cap)
		DisplayManager.createDisplay(1920, 1080, 144);
		
		// Create important stuff
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		StaticShader shader = new StaticShader();
		
		// Hardcoded data BEGIN
		// |
		float[] vertices = {
				0.5f, 0.5f, 0f,
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f
		};
		// |
		// |
		int[] indices = {
				0,1,2,
				0,2,3
		};
		// |
		// |
		float[] textureCoords = {
				1, 0,
				0, 0,
				0, 1,
				1, 1
		};
		// |
		// Hardcoded data END
		
		// Create a RawModel, load a Texture and create a TexturedModel
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("logo"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested())
		{
			/*--- Game Logic ---*/
			
			/*--- Rendering ---*/
			
			// Prepare the Renderer
			// (clear the color of the last frame)
			renderer.prepare();
			
			// Start the Shader
			shader.start();
			
			// Render the model
			renderer.render(texturedModel);
			
			// Stop the Shader
			shader.stop();
			
			// Update the Display
			DisplayManager.updateDisplay();
			
		}
		
		// CleanUp the Shader
		shader.cleanUp();
		
		// CleanUp all the VAOs and VBOs
		loader.cleanUp();
		
		// Close Display
		DisplayManager.closeDisplay();
	}

}
