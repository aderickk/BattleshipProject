package shaders;

public class StaticShader extends ShaderProgram
{
	// Stringpaths for the default Shaders
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	// Constructor with no Shaders specified -> use default Shaders
	public StaticShader()
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	// Constructor with specific Shaders
	public StaticShader(String vertexFile, String fragmentFile)
	{
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes()
	{
		// Bind the Attributes to the VertexShader
		// Note that those names has to be the same as in the VertexShader!
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

}
