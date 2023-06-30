package com.project.som;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class SOMAPP extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	public static OrthographicCamera camera;
	//float[][] Ts = new float[][] {{0, 0, 0},{1, 0, 0},{0, 1, 0},{0, 0, 1},{1, 1, 1}};
	float[][] Ts = new float[][] {{0, 0, 0, 0},{1, 0, 0, 0.25f},{0, 1, 0, 0.5f},{0, 0, 1, 0.75f},{1, 1, 1, 1}};
	SOM som = new SOM(Ts[0].length, Ts);

	boolean f = false;
	private static final float cameraSpeed = 3;

	public static int screenX = 600;
	public static int screenY = 600;
	
	@Override
	public void create () {
		som.recolor();
		batch = new SpriteBatch();
		img = new Texture("pixel.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenX, screenY);
		camera.update();
	}

	private void inputHandler() {
		if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
			camera.zoom += 0.1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			camera.zoom -= 0.1;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			som.learn(100, 0.1f);
			if(f)
				som.search(som.Ts);
			else
				som.recolor();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.C)) {
			som.learn(100, 0.1f);
			if(f)
				som.search(som.Ts);
			else
				som.recolor();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			if(f)
				som.recolor();
			else
				som.search(som.Ts);
			f = !f;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			som = new SOM(Ts[0].length, Ts);
			if(f)
				som.search(som.Ts);
			else
				som.recolor();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(-cameraSpeed, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(cameraSpeed, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, -cameraSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, cameraSpeed, 0);
		}
	}

	@Override
	public void render () {
		inputHandler();
		camera.update();
		ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < som.neurons.length; i++) {
			batch.setColor(som.neurons[i].color);
			batch.draw(img, som.neurons[i].x, som.neurons[i].y + (som.HEIGHT + som.WIDTH) / 2 * som.neurons[i].w[3]);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
