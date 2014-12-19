package com.vp.game.gamecore;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.vp.game.units.Obstacle;
import com.vp.game.units.Unit;
import com.vp.game.units.Wolf;

public class Renderer {
	final private BitmapFont font;
	final private Stage stage;
	final private Label label;
	final private StringBuilder stringBuilder;

	final private ModelBuilder modelBuilder;
	final private ModelBatch modelBatch;

	final private AssetManager assets;

	private Model planeModel;
	final private ModelInstance[] floorTiles = new ModelInstance[3];
	final private Model ninjaModel;
	final private ModelInstance ninjaModelInstance;
	final private Model beastModel;
	final private ModelInstance beastModelInstance;	

	final private Environment environment;
	final private AnimationController animController;
	final private AnimationController animController2;
	private Simulation sim;

	public Renderer() {

		stage = new Stage();
		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		stringBuilder = new StringBuilder();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		assets = new AssetManager();
		assets.load("icetexture.jpg", Texture.class);
		assets.load("HeroDemonHunterfullbaked.g3dj", Model.class);
		assets.load("Wolf7.g3dj", Model.class);
		assets.finishLoading();

		modelBatch = new ModelBatch();

		modelBuilder = new ModelBuilder();

		ninjaModel = assets.get("HeroDemonHunterfullbaked.g3dj", Model.class);
		ninjaModel.materials.get(0).set(
				IntAttribute.createCullFace(GL20.GL_NONE));
		ninjaModel.nodes.get(0).scale.set(0.15f, 0.15f, 0.15f);
		ninjaModelInstance = new ModelInstance(ninjaModel);
		animController = new AnimationController(ninjaModelInstance);
		// Pick the current animation by name
		animController.setAnimation("Take 001", -1);
		
		
		beastModel = assets.get("HeroDemonHunterfullbaked.g3dj", Model.class);
		for(int i= 0; i<4; i++){
			beastModel.materials.get(0).set(
					IntAttribute.createCullFace(GL20.GL_NONE));
			beastModel.nodes.get(0).scale.set(0.15f, 0.15f, 0.15f);
//			beastModel.nodes.get(i).scale.set(35f, 35f, 35f);
//			beastModel.nodes.get(i).translation.set(0,12.5f,0);
		}

		beastModelInstance = new ModelInstance(beastModel);
		animController2 = new AnimationController(beastModelInstance);
		Wolf.setModel(beastModel);
		//initialize pool
		for (int i = 0; i < 40; i++) {
			Wolf.pool.free(new Wolf());
		}
		// Pick the current animation by name
		//animController2.setAnimation("A|Wolf_wartepose", -1);
	}
	
	public void setSim(Simulation sim){
		this.sim = sim;
		planeModel = createPlaneModel(sim.floor.tileWidth, sim.floor.height, new Material(), 0, 0, 1, 1);
		floorTiles[0] = new ModelInstance(planeModel);
		floorTiles[1] = new ModelInstance(planeModel);
		floorTiles[2] = new ModelInstance(planeModel);
		Texture iceTex = assets.get("icetexture.jpg", Texture.class);
		floorTiles[0].materials.get(0).set(TextureAttribute.createDiffuse(iceTex));
		floorTiles[1].materials.get(0).set(TextureAttribute.createDiffuse(iceTex));
		floorTiles[2].materials.get(0).set(TextureAttribute.createDiffuse(iceTex));
	}

	public void render(float delta) {

		// Clear screen
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Update animations
		animController.update(delta);
		animController2.update(delta);

		sim.cam.update();
		// Update Models
		double angle = Math.atan2(sim.ninja.direction.x, sim.ninja.direction.y);
		ninjaModelInstance.transform.idt().rotateRad(0, 1, 0, (float) angle)
				.setTranslation(sim.ninja.position.x, sim.ninja.positionY, sim.ninja.position.y);
		//long startTime = System.nanoTime();
		for(int i = 0; i < Unit.units.size; i++){
			Unit unit = Unit.units.get(i);
			unit.animC.update(delta);
			angle = Math.atan2(unit.direction.x, unit.direction.y);
			unit.modelInstance.transform.idt().rotateRad(0, 1, 0, (float) angle)
				.setTranslation(unit.position.x, unit.positionY, unit.position.y);
		}
		//System.out.println("render move obs modelInst: " + (System.nanoTime() - startTime)/1000);		
	
		if(sim.floor.hasChanged){
			floorTiles[0].transform.setTranslation(sim.floor.xPositions[0], 0, 0);
			floorTiles[1].transform.setTranslation(sim.floor.xPositions[1], 0, 0);
			floorTiles[2].transform.setTranslation(sim.floor.xPositions[2], 0, 0);
			sim.floor.hasChanged = false;
		}

		// Render Models
		modelBatch.begin(sim.cam);
		modelBatch.render(floorTiles[0], environment);
		modelBatch.render(floorTiles[1], environment);
		modelBatch.render(floorTiles[2], environment);
		modelBatch.render(ninjaModelInstance, environment);
		//startTime = System.nanoTime();
		for(int i = 0; i < Unit.units.size; i++){
			Unit unit = Unit.units.get(i);
			modelBatch.render(unit.modelInstance, environment);
		}
		//System.out.println("render render obs: " + (System.nanoTime() - startTime)/1000);
		modelBatch.end();
		//System.out.println("render render obs2: " + (System.nanoTime() - startTime)/1000);
		// Draw FPS
		stringBuilder.setLength(0);
		stringBuilder.append(" FPS: ")
				.append(Gdx.graphics.getFramesPerSecond()).append(", XPos: " + (int) sim.ninja.position.x).append(", YPos: " + (int) sim.ninja.position.y);
		label.setText(stringBuilder);
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose() {
		assets.dispose();
		modelBatch.dispose();
		planeModel.dispose();
		ninjaModel.dispose();
	}

	private Model createPlaneModel(final float width, final float height,
			final Material material, final float u1, final float v1,
			final float u2, final float v2) {

		modelBuilder.begin();
		MeshPartBuilder bPartBuilder = modelBuilder.part("rect",
				GL20.GL_TRIANGLES, Usage.Position | Usage.Normal
						| Usage.TextureCoordinates, material);
		bPartBuilder.setUVRange(u1, v1, u2, v2);
		bPartBuilder.rect(-(width * 0.5f), 0, (height * 0.5f), (width * 0.5f),
				0, (height * 0.5f), (width * 0.5f), 0, -(height * 0.5f),
				-(width * 0.5f), 0, -(height * 0.5f), 0, 1, 0);

		return (modelBuilder.end());
	}

}
