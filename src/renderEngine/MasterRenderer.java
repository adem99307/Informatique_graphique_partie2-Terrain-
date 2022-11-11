package renderEngine;

import entities.Camera;
import entities.Node;
import entities.Lumiere;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import HeightFields.HeightField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer{

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;

	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private NodeRenderer renderer;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();


	private Map<TexturedModel,List<Node>> entities = new HashMap<TexturedModel,List<Node>>();
	private List<HeightField> heightFields = new ArrayList<HeightField>();

	public MasterRenderer(){
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjectionMatrix();
		renderer = new NodeRenderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
	}



	public void render(Lumiere sun, Camera camera){
		prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(heightFields);
		terrainShader.stop();
		heightFields.clear();
		entities.clear();
	}

	public void processTerrain(HeightField heightField){
		heightFields.add(heightField);
	}

	public void processEntity(Node node){
		TexturedModel entityModel = node.getModel();
		List<Node> batch = entities.get(entityModel);
		if(batch!=null){
			batch.add(node);
		}else{
			List<Node> newBatch = new ArrayList<Node>();
			newBatch.add(node);
			entities.put(entityModel, newBatch);
		}
	}

	public void cleanUp(){
		shader.cleanUp();
		terrainShader.cleanUp();
	}

	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.49f, 89f, 0.98f, 1);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}


}
