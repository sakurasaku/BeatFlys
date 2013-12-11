package com.maffi.book2;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button mRetryButton;	//���g���C�{�^��
	private MyBgm mBgm;				//BGM
	private long mPauseTime = 0L;
	private MyRenderer mRenderer;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //�t���X�N���[���\��
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //�^�C�g���o�[���\��
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //���ʃ{�^�����{�����[���{�^���ŏo����悤�ɂ���
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        //�f�o�b�O���[�h���ǂ������肷��
       try{
        	
        	PackageManager pm = getPackageManager();
        	ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
        	Global.isDebuggable = (ApplicationInfo.FLAG_DEBUGGABLE == (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        }catch(NameNotFoundException e){
        	
        	e.printStackTrace();
        }
       
        
        
        
        
        Global.mainActivity = this;
        
        this.mRenderer = new MyRenderer(this);
        
        //MyGLSurfaceView�̐���
        MyGLSurfaceView glSurfaceView = new MyGLSurfaceView(this);
        
        //GLSurfaceView��MyRenderer��K�p
        glSurfaceView.setRenderer(mRenderer);
        
        //�r���[��GLSurfaceView�ɕύX
        setContentView(glSurfaceView);
        
        
        //�{�^���̃��C�A�E�g
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        params.setMargins(0, 150, 0, 0);
        //�{�^���̍쐬
        this.mRetryButton = new Button(this);
        this.mRetryButton.setText("Retry");
       	hideRetryButton();
        addContentView(mRetryButton, params);
        //�C�x���g�̒ǉ�
        this.mRetryButton.setOnClickListener(new Button.OnClickListener(){
        	
        	@Override
        	public void onClick(View v){
        		hideRetryButton();
        		mRenderer.startNewGame();
        	}
        });
        //MyBgm�̐���
        this.mBgm = new MyBgm(this);
        //�ۑ�������Ԃɖ߂�
        if(savedInstanceState != null){
        	
        	long startTime = savedInstanceState.getLong("startTime");
        	long pauseTime = savedInstanceState.getLong("pauseTime");
        	int score = savedInstanceState.getInt("score");
        	long pausedTime = pauseTime - startTime;
        	mRenderer.subtractPausedTime(-pausedTime);
        	mRenderer.setScore(score);
        }
    }
    
    //���g���C�{�^����\������
    public void showRetryButton(){
    	
    	mRetryButton.setVisibility(View.VISIBLE);
    }
    //���g���C�{�^�����\���ɂ���
    public void hideRetryButton(){
    	
    	mRetryButton.setVisibility(View.GONE);
    }
    @Override
    public void onResume(){
    	
    	super.onResume();
    	if(mPauseTime != 0L){
    		//�o�b�N�O���E���h�ɂȂ��Ă������Ԃ��v�Z����
    		long pausedTime = System.currentTimeMillis() - mPauseTime;
    		mRenderer.subtractPausedTime(pausedTime);
    	}
    	mBgm.start();			//BGM�Đ�
    }
    @Override
    public void onPause(){
    	super.onPause();
    	mBgm.stop();			//BGM��~	
    	//�e�L�X�`�����폜����
    	GL10 gl = Global.gl;
    	TextureManager.deleteAll(gl);

    	mPauseTime = System.currentTimeMillis();	//�o�b�N�O�����h�ɂȂ������Ԃ��o���Ă���
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
    	
    	super.onSaveInstanceState(outState);		//��Ԃ�ۑ�����
    	//�J�n����
    	outState.putLong("startTime", mRenderer.getStartTime());
    	//onPause��������
    	outState.putLong("pauseTime", System.currentTimeMillis());
    	outState.putInt("score", mRenderer.getScore());	//�X�R�A
    }   
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
    	//�����L�[�������ꂽ��
    	if(event.getAction() == KeyEvent.ACTION_DOWN){
    		
    		switch (event.getKeyCode()){
    		
    		case KeyEvent.KEYCODE_BACK:	//Back�{�^��
    		return false;
    		default:
    		}
    	}
    	return super.dispatchKeyEvent(event);
    }
    
    
}






















