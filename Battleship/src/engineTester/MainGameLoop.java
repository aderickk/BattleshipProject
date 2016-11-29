package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.ModelTexture;

public class MainGameLoop
{

	public static void main(String[] args)
	{
		// create a Display (width, height, fps_cap)
		DisplayManager.createDisplay(1920, 1080, 144);
		
		// Create important stuff
		Loader loader = new Loader();
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer();
		
		// Create a RawModel, load a Texture and create a TexturedModel
		RawModel model = OBJLoader.loadObjModel("dragon", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		Entity entity = new Entity(texturedModel, new Vector3f(20.0f, -5f, 0.0f), 0.0f, 270.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		Light light = new Light(new Vector3f(5,5,0), new Vector3f(1,1,1));
		
		// Set the shineVariable for the Texture
		texture.setReflectivity(2);
		texture.setShineDamper(15);		
		
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested())
		{
			/*--- Game Logic ---*/
			entity.decreaseRotation(0, 0.0f, 0);
			camera.move();
			
			// Tidy up all entities for rendering
			renderer.processEntity(entity);
			
			/*--- Rendering ---*/
			renderer.render(light, camera);
			
			// Update the Display
			DisplayManager.updateDisplay();
			
		}
		
		// Clean the renderer
		renderer.cleanUp();
		
		// CleanUp all the VAOs and VBOs
		loader.cleanUp();
		
		// Close Display
		DisplayManager.closeDisplay();
	}

}
