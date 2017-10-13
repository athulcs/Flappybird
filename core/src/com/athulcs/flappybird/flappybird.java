package com.athulcs.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.FloatFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
//import com.sun.org.apache.xpath.internal.operations.String;

import java.util.Random;

//import static java.lang.Float.intBitsToFloat;

public class flappybird extends ApplicationAdapter {
	SpriteBatch batch;
    Texture bg;
	Texture bird,pipeUp,pipeDown,bird1,gameOver;
	int flapState=0,gameState=1,score=0;
	float velocity=0;
	float gravity=1;
	float birdY;
	float gaptube=300;
	Random rand;
	int randheight;
	float tubeX;
	Circle birdCircle;
	Rectangle pipeUpRect;
	Rectangle pipeDownRect;
	BitmapFont font;
	Sound sound;
	Sound flap;
	Sound point;
	ShapeRenderer shapeRenderer;



	@Override
	public void create() {
		batch = new SpriteBatch();
		bg = new Texture("bgf.png");
		bird=new Texture("bird.png");
		bird1=new Texture("bird2.png");
		pipeUp=new Texture("pipeup.png");
		pipeDown=new Texture("pipedown.png");
		gameOver=new Texture("gameover.png");
		birdY=Gdx.graphics.getHeight()/2-bird.getHeight()/2;
		rand=new Random();
		randheight=rand.nextInt(Gdx.graphics.getHeight()/2)+320;
		tubeX=Gdx.graphics.getWidth()+1;
		birdCircle= new Circle();
		shapeRenderer= new ShapeRenderer();
		pipeUpRect=new Rectangle();
		pipeDownRect=new Rectangle();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		sound=Gdx.audio.newSound(Gdx.files.internal("fart.mp3"));
		flap=Gdx.audio.newSound(Gdx.files.internal("flap.mp3"));
		point=Gdx.audio.newSound(Gdx.files.internal("sfx_point.wav"));
	}

	@Override
	public void render() {
		if(gameState==1){
			batch.begin();
			batch.draw(bg,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

			batch.draw(pipeUp, tubeX, randheight + gaptube / 2);
			pipeUpRect.set(tubeX, randheight + gaptube / 2, pipeUp.getWidth(), pipeUp.getHeight());
			batch.draw(pipeDown, tubeX, randheight - pipeDown.getHeight() - gaptube / 2);
			pipeDownRect.set(tubeX, randheight - pipeDown.getHeight() - gaptube / 2, pipeDown.getWidth(), pipeDown.getHeight());
			batch.draw(bird, Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY);
			birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + 35, bird.getWidth() / 2 - 10);


			if (Gdx.input.justTouched()) {
				flap.play();
				batch.draw(bird1, Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY);
				velocity -= 18;

			}
			if (birdY > 0 || velocity < 0) {

				birdY = birdY - velocity;
				velocity += gravity;
			}


			tubeX -=3;
			if (tubeX < -200) {
				tubeX = Gdx.graphics.getWidth();
				randheight = rand.nextInt(Gdx.graphics.getHeight() / 2) + 320;

			}
			if(Intersector.overlaps(birdCircle,pipeUpRect)||Intersector.overlaps(birdCircle,pipeDownRect)){
				batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-200);
				sound.play();
				gameState=0;
			}
			Gdx.app.log("Center",Float.toString(Gdx.graphics.getWidth()/2-pipeDown.getWidth()/2));
			Gdx.app.log("TubeX",Float.toString(tubeX));
			if(tubeX==Gdx.graphics.getWidth()/2-pipeDown.getWidth()/2){
				score++;
				point.play();
			}
			font.draw(batch,Integer.toString(score),100,200);

			batch.end();
		}
		if (Gdx.input.justTouched()&&gameState==0) {
			tubeX = Gdx.graphics.getWidth()+200;
			birdY=Gdx.graphics.getHeight()/2-bird.getHeight()/2;
			sound.pause();
			score=0;
			gameState=1;

		}
		/*shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.RED);
		shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		shapeRenderer.rect(pipeUpRect.x,pipeUpRect.y,pipeUpRect.getWidth(),pipeUpRect.getHeight());
		shapeRenderer.rect(pipeDownRect.x,pipeDownRect.y,pipeDownRect.getWidth(),pipeDownRect.getHeight());
		shapeRenderer.end();  */
	}

	@Override
	public void dispose() {
		batch.dispose();

	}
}