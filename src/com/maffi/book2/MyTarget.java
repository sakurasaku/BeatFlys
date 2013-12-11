package com.maffi.book2;

import javax.microedition.khronos.opengles.GL10;

public class MyTarget {
	
	public float mAngle;
	public float mX, mY;
	public float mSize;
	public float mSpeed;
	public float mTurnAngle;
	
	public MyTarget(float x, float y, float angle, float size, float speed, float turnAngle){
		
		this.mX = x;
		this.mY = y;
		this.mAngle = angle;
		this.mSize = size;
		this.mSpeed = speed;
		this.mTurnAngle = turnAngle;		
	}
	
	//�W�I��`�悷��
	public void draw(GL10 gl, int texture){
		gl.glPushMatrix();
		{
			gl.glTranslatef(mX, mY, 0.0f);
			gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
			gl.glScalef(mSize, mSize, 1.0f);
			GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, texture, 1.0f, 1.0f, 1.0f, 1.0f);
		}	
		gl.glPopMatrix();
	}
	
	
	
	
	//�|�C���g�������蔻��͈͓̔�����Ԃ�
	public boolean isPointInside(float x, float y){
		
		//�W�I�ƃ^�b�`���ꂽ�|�C���g�Ƃ̋������v�Z����
		float dx = x - mX;
		float dy = y - mY;
		float distance = (float)Math.sqrt(dx * dx + dy * dy);
		if(distance <= mSize * 0.5f){
			return true;
		}
		return false;
	}
	
	
	//�W�I���ړ�������
	public void move(){
		
		float theta = mAngle / 180.0f * (float)Math.PI;
		mX = mX + (float)Math.cos(theta) * mSpeed;
		mY = mY + (float)Math.sin(theta) * mSpeed;
		
		//���[�v����
		if(mX >= 2.0f){
			mX -= 4.0f;
		}
		if(mX <= -2.0f){
			mX += 4.0f;
		}
		if(mY >= 2.5f){
			mY -= 5.0f;
		}
		if(mY <= -2.5f){
			mY += 5.0f;
		}
	}
}
