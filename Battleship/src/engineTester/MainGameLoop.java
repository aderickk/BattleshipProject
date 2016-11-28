package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;

public class MainGameLoop
{

	public static void main(String[] args)
	{
		// create a Display (width, height, fps_cap)
		DisplayManager.createDisplay(1920, 1080, 144);
		
		// Create important stuff
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		// Hardcoded data BEGIN
		// |
		float[] vertices = {
				0.2f, 0.1f, 0f,
				-0.2f, 0.1f, 0f,
				0.2f, -0.1f, 0f,
				0.2f, 0.2f, 0f,
				-0.2f, 0.2f, 0f,
				1f, 1f, 0f
		};
		// |
		// |
		int[] indices = {
				0,1,2,
				3,4,5
		};
		// |
		// Hardcoded data END
		
		RawModel model = loader.loadToVAO(vertices, indices);
		RawModel model2 = loader.loadToVAO(vertices, indices);
		
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested())
		{
			/*--- Game Logic ---*/
			
			/*--- Rendering ---*/
			
			// Prepare the Renderer
			// (clear the color of the last frame)
			renderer.prepare();
			
			// Render the model
			renderer.render(model);
			renderer.render(model2);
			
			// Update the Display
			DisplayManager.updateDisplay();
			
		}
		
		// CleanUp all the VAOs and VBOs
		loader.cleanUp();
		
		// Close Display
		DisplayManager.closeDisplay();
	}

}
