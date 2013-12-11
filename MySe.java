package com.maffi.book2;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class MySe {
	
	private SoundPool mSoundPool;		//SoundPool
	private int mHitSound;				//�ǂݍ��񂾌��ʉ��I�u�W�F�N�g
	
	public MySe(Context context){
		
		this.mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		this.mHitSound = mSoundPool.load(context, R.raw.explosion, 1);
	}
	
	public void playHitSound(){
		
		mSoundPool.play(mHitSound, 1.0f, 1.0f, 1, 0, 1.0f);
	}
}
