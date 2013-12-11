package com.maffi.book2;

import javax.microedition.khronos.opengles.GL10;

public class Particle {
	
	public float mX;
	public float mY;
	public float mSize;
	
	public boolean mIsActive;
	
	public float mMoveX;		//1�t���[���������X�������̈ړ���
	public float mMoveY;		//1�t���[���������Y�������̈ړ���
	
	public int mFrameNumber;	//��������̎��ԁi�t���[�����j
	public int mLifeSpan;		//�����i�t���[�����j
	
	public Particle(){
		
		this.mX = 0.0f;
		this.mY = 0.0f;
		this.mSize = 1.0f;
		
		this.mIsActive = false;
		
		this.mMoveX = 0.0f;
		this.mMoveY = 0.0f;
		
		this.mFrameNumber = 0;
		this.mLifeSpan = 30;	//�{���͂U�O
	}
	
	public void draw(GL10 gl, int texture){
		
		float lifePercentage = (float)mFrameNumber / (float)mLifeSpan;
		float alpha = 1.0f - lifePercentage;
		if(lifePercentage <= 0.5f){
			
			alpha = lifePercentage * 2.0f;
		}else{
			
			alpha = 1.0f - (lifePercentage - 0.5f) * 2.0f;
		}
		
		GraphicUtil.drawTexture(gl, mX, mY, mSize, mSize, texture, 1.0f, 1.0f, 1.0f, alpha);
	}
	public void update(){
		
		mFrameNumber++;
		if(mFrameNumber >= mLifeSpan){
			
			mIsActive = false;
		}
		mX += mMoveX;
		mY += mMoveY;
		
		mMoveY -= 0.0001f;
	}
	
	
	
}
















