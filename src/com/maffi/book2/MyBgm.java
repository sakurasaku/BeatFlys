package com.maffi.book2;

import android.content.Context;
import android.media.MediaPlayer;

public class MyBgm {
	
	private MediaPlayer mBgm;
	
	public MyBgm(Context context){
		
		//BGM�t�@�C����ǂݍ���
		this.mBgm = MediaPlayer.create(context, R.raw.master1);
		this.mBgm.setLooping(true);				//���[�v����悤�ɂ���
		this.mBgm.setVolume(1.0f, 1.0f);		//���E�̃{�����[�����ő�ɂ���
	}
	//BGM���Đ�����
	public void start(){
		
		if(!mBgm.isPlaying()){
			mBgm.seekTo(0);
			mBgm.start();
		}
	}
	//BGM���~����
public void stop(){
		
		if(mBgm.isPlaying()){
			mBgm.stop();
			mBgm.prepareAsync();
		}
	}
}
