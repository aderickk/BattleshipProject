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
import terrain.Terrain;
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
		//RawModel model = OBJLoader.loadObjModel("dragon", loader);
		//ModelTexture texture = new ModelTexture(loader.loadTexture("white"));
		//TexturedModel texturedModel = new TexturedModel(model, texture);
		
		RawModel battleship = OBJLoader.loadObjModel("battleship", loader);
		ModelTexture battleshipTexture = new ModelTexture(loader.loadTexture("white"));
		TexturedModel battleshipTexturedModel = new TexturedModel(battleship, battleshipTexture);
		
		RawModel canon = OBJLoader.loadObjModel("simpleCanon", loader);
		ModelTexture canonTexture = new ModelTexture(loader.loadTexture("simpleCanon"));
		TexturedModel canontexturedModel = new TexturedModel(canon, canonTexture);
		
		battleshipTexture.setReflectivity(2);
		battleshipTexture.setShineDamper(15);
		
		canonTexture.setReflectivity(2);
		canonTexture.setShineDamper(15);
		
		//Entity entity = new Entity(texturedModel, new Vector3f(-20.0f, -20f, -180.0f), 0.0f, 180.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		Entity canonEntity = new Entity(canontexturedModel, new Vector3f(0.0f, -13f, -234.5f), 0.0f, 180.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		Entity battleshipEntity = new Entity(battleshipTexturedModel, new Vector3f(50.0f, -35f, 0.0f), 0.0f, 30.0f, 0.0f, 3.0f, 3.0f, 3.0f);
		
		Light light = new Light(new Vector3f(-100,500,-300), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")), "heightmap");
		Terrain water = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("water")), "waterHeightMap");
		
		// Set the shineVariable for the Texture
		//texture.setReflectivity(2);
		//texture.setShineDamper(15);		
		
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested())
		{
			/*--- Game Logic ---*/
			//entity.decreaseRotation(0, 0.0f, 0);
			camera.move();
			if(Keyboard.isKeyDown(Keyboard.KEY_N))
			{
				camera.setCameraPosition(0, -10, -240, 0, -90);
			}
			
			// Tidy up all entities for rendering
			renderer.processTerrain(terrain);
			renderer.processTerrain(water);
			renderer.processEntity(canonEntity);
			renderer.processEntity(battleshipEntity);
			//renderer.processEntity(entity);
			
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
