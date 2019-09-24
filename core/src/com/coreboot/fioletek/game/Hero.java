package com.coreboot.fioletek.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Hero {

    int posx, posy;
    int sizex, sizey;
    int anim_frames;
    int anim_delay;
    int frame;

    Texture[] anim;

    {
        anim = new Texture[2];
    }

    Sound jump_sound;
    Sound run_sound;


  }
