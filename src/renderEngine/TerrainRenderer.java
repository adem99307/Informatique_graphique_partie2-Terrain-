package renderEngine;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import HeightFields.HeightField;
import textures.TerrainTexturePack;
import toolbox.Maths;

import java.util.List;

public class TerrainRenderer {

	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(List<HeightField> heightFields) {
		for (HeightField heightField : heightFields) {
			prepareTerrain(heightField);
			loadModelMatrix(heightField);
			GL11.glDrawElements(GL11.GL_TRIANGLES, heightField.getModel().getVertexCount(),
					GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}

	private void prepareTerrain(HeightField heightField) {
		RawModel rawModel = heightField.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bind(heightField);
		shader.loadShineVariables(1,0);

	}

	private void  bind(HeightField heightField){
		TerrainTexturePack texturePack = heightField.getTexturePack();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, heightField.getBlendMap().getTextureID());
	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(HeightField heightField) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(heightField.getX(), 0, heightField.getZ()), 0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
