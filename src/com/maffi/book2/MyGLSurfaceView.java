package com.maffi.book2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MyGLSurfaceView extends GLSurfaceView{

	//��ʃT�C�Y
	private float mWidth;
	private float mHeight;
	
	//MyRenderer��ێ�����
	private MyRenderer mMyRenderer;

	public MyGLSurfaceView(Context context) {
		super(context);
		
		setFocusable(true);
	}
	
	@Override
	public void setRenderer(Renderer renderer){
		
		super.setRenderer(renderer);
		this.mMyRenderer = (MyRenderer)renderer;
	}
	
	
	@Override		//��ʂ̃T�C�Y��ۑ����Ă����i�T�[�t�F�C�X�z���_�[�j
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
		
		super.surfaceChanged(holder, format, w, h);
		this.mWidth =w;
		this.mHeight = h;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		//��ʃT�C�Y�����ɍ��W��ϊ�����
		float x = event.getX() / (float)mWidth * 2.0f - 1.0f;	
		float y = event.getY() / (float)mHeight * -3.0f + 1.5f;
		//�����_���[�ɂɒʒm����
		mMyRenderer.touched(x, y);
		
		Log.i(getClass().toString(), String.format("touched! x = %f, y = %f", x, y));
		return false;
	}
}
