package renderEngine;


import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager
{
	
	private static int FPS_CAP = 60;
	private static String title = "Battleship";
	
	public static void createDisplay(int width, int height)
	{
		createDisplay(width, height, 60);
	}
	
	public static void createDisplay(int width, int height, int fps_cap)
	{
		// Setting up ContextAttribs
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		
		// Set maximum FPS
		FPS_CAP = fps_cap;
		
		// Undecorated Window
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		
		// Try to create a new Display
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(title);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//Define displayable Area
		GL11.glViewport(0, 0, width, height);
	}
	
	public static void updateDisplay()
	{
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void closeDisplay()
	{
		Display.destroy();
	}
	
	public void setTitle(String title)
	{
		DisplayManager.title = title;
		Display.setTitle(title);
	}
}
