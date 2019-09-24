package com.coreboot.fioletek.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class Fioletek extends ApplicationAdapter {
	SpriteBatch batch;
	Texture fioletek1;
	Texture fioletek2;
	Texture bee;

	Texture bg;
	Texture bg1;

	Music run_sound;
	Sound jump_sound;
	Sound bee_sound;
	Sound ouch_sound;

	int xPos;
	int yPos;

	int bg_xPos;

	int bee_xPos;
	int bee_yPos;

	static int FRAME_DELAY = 6;
	int fioletek_run_frame;

	Random random;

	BitmapFont font;
	BitmapFont hiscore_font;


	int score;
	int hiscore;

	int gamestate;

	Rectangle fioletekRectangle;
	Rectangle beeRectangle;

	private Preferences prefs ;

	Hero bohater;



	@Override
	public void create () {

		prefs = Gdx.app.getPreferences("My Preferences");


		bohater = new Hero();
		bohater.anim_frames = 2;
		bohater.jump_sound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
		bohater.anim[0] = new Texture("fioletek_run_1.png");
		bohater.anim[1] = new Texture("fioletek_run_2.png");


		batch = new SpriteBatch();
		bg = new Texture("bg.png");
		bg1 = new Texture("bg.png");
		fioletek1 = new Texture("fioletek_run_1.png");
		fioletek2 = new Texture("fioletek_run_2.png");
		bee = new Texture("bee.png");

		fioletekRectangle = new Rectangle();
		beeRectangle = new Rectangle();


		xPos = 0; //(Gdx.graphics.getWidth()/2) - (400/2);
		yPos = 0;

		bee_xPos = Gdx.graphics.getWidth()+960/4/2;
		bee_yPos = 200;

		run_sound = Gdx.audio.newMusic(Gdx.files.internal("run.wav"));
		run_sound.setLooping(true);
		run_sound.setVolume(0.3f);
		//run_sound.play();

		//jump_sound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
		bee_sound = Gdx.audio.newSound(Gdx.files.internal("bee1.wav"));
		ouch_sound = Gdx.audio.newSound(Gdx.files.internal("whaaa.wav"));

		random = new Random();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);
		hiscore_font = new BitmapFont();
		hiscore_font.setColor(Color.GREEN);
		hiscore_font.getData().setScale(4);

		float height = random.nextFloat() * Gdx.graphics.getHeight() / 2 + 500 / 2/2;
		bee_yPos = (int) height;

		hiscore = prefs.getInteger("highscore");

	}


	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		batch.begin();
		if(gamestate == 0)
		{
			if(Gdx.input.justTouched())
			{
				gamestate = 1;
				score = 0;
				run_sound.play();
				bee_xPos = Gdx.graphics.getWidth() + 960 / 4/2;

			}
		}


			batch.draw(bg, bg_xPos, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(bg1, bg_xPos + Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			if(gamestate == 1) {
				bee_xPos -= 6;
				bg_xPos -= 4;
				fioletek_run_frame++;

				if (Gdx.input.isTouched()) {
					yPos += 16;
					if(yPos > (Gdx.graphics.getHeight() - 400)) yPos = (Gdx.graphics.getHeight() - 400);
				} else {
					if (yPos > 0) yPos -= 16;
				}
			}

			if (bee_xPos < -(1000 / 4)) {
				bee_xPos = Gdx.graphics.getWidth() + 960 / 4;
				bee_sound.play(0.5f);
				float height = random.nextFloat() * Gdx.graphics.getHeight() - 684 / 4/2; /// 2 + 500 / 2;
				bee_yPos = (int) height;
				score++;

			}



			if (bg_xPos <= -Gdx.graphics.getWidth()) {
				bg_xPos = 0; //Gdx.graphics.getWidth();
			}


			if (fioletek_run_frame > FRAME_DELAY) fioletek_run_frame = 0;

			if (fioletek_run_frame > (FRAME_DELAY / 2)) {
				//batch.draw(fioletek1, xPos, yPos, 200, 400);
				batch.draw(bohater.anim[0], xPos, yPos, 200, 400);
			} else {
				//batch.draw(fioletek2, xPos, yPos, 200, 400);
				batch.draw(bohater.anim[1], xPos, yPos, 200, 400);
			}
			fioletekRectangle.set(xPos+50, yPos,100, 400);
			//fioletekRectangle.setSize(400, 800);

			if (Gdx.input.justTouched()) {
				//jump_sound.play(0.5f);
				bohater.jump_sound.play(0.5f);
			}

			batch.draw(bee, bee_xPos, bee_yPos, 960 / 4/2, 684 / 4/2);
			beeRectangle.set(bee_xPos, bee_yPos, 960 / 4/2, 684/4/2);

			if(hiscore < score) {
				hiscore = score;
				prefs.putInteger("highscore", hiscore);
				prefs.flush();
			}

			font.draw(batch, String.valueOf(score), 50, Gdx.graphics.getHeight() - 50);
			hiscore_font.draw(batch, String.valueOf(hiscore), Gdx.graphics.getWidth()-80, Gdx.graphics.getHeight() - 50);

			if(Intersector.overlaps(fioletekRectangle, beeRectangle))
			{
				if(gamestate != 0 )
				{
					ouch_sound.play(0.9f);


				}
				gamestate = 0;

				run_sound.stop();
			}



		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		//img.dispose();
	}
}
