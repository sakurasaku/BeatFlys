package com.maffi.book2;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;

public class MyRenderer 
implements GLSurfaceView.Renderer{

	private Context mContext;
	private int mWidth;
	private int mHeight;
	
	//�e�L�X�`�����Ǘ����邽�߂�ID
	private int mBgTexture;				//�w�i�e�L�X�`��
	private int mTargetTexture;			//�W�I�p�e�L�X�`��
	private int mNumberTexture;			//�����p�e�L�X�`��
	private int mGameOverTexture;		//�Q�[���I�[�o�[�p�e�L�X�`��
	
	private int mParticleTexture;
	
//	private MyTarget mTarget;			//�W�I
	
	private static final int GAME_INTERVAL = 10;
	public static final int TARGET_NUM = 10;	//�W�I�̐����w��
	private MyTarget[] mTargets = new MyTarget[TARGET_NUM];
	private int mScore;					//���_
	
	private long mStartTime;			//�J�n����
	private boolean mGameOverFlag;		//�Q�[���̊Ǘ�
	
	private Handler mHandler = new Handler();
	
	private MySe mSe;					//���ʉ�
	
	private ParticleSystem mParticleSystem;
	
	private long mFpsCountStartTime = System.currentTimeMillis();
	private int mFramesInSecond = 0;
	private int mFps = 0;
	
	
/*	-----------------------------------------------------
    private float mTargetAngle;			//�W�I�̊p�x
	private float mTargetX, mTargetY;	//�W�I�̈ʒu
	private float mTargetSize;			//�W�I�̃T�C�Yprivate
	private float mTargetSpeed;			//�W�I�̈ړ����x
	private float mTargetTurnAngle;		//�W�I�̐���p�x
	
    MyTarget�Ɉړ��@�@--------------------------------------*/	
	
	public MyRenderer(Context context){
		
		this.mContext = context;
		this.mSe = new MySe(context);
		this.mParticleSystem = new ParticleSystem(300, 30);
		
		startNewGame();
		
//		this.mGameOverFlag = false;		startNewGame()�Ɉړ�
//		this.mScore = 0;				startNewGame()�Ɉړ�
//		this.mStartTime = System.currentTimeMillis();	startNewGame()�Ɉړ�
		
//		Random rand = Global.rand;		startNewGame()�Ɉړ�
		
/*		//�R���X�g���N�^���g���ď����l��ݒ肷��
		this.mTarget = new MyTarget(0.5f, -0.5f, 30.0f, 0.5f, 0.01f, 1.0f);
*/		
/*		//�W�I�̏�Ԃ�����������  ----------  startNewGame()�Ɉړ�  -----------
		for(int i = 0; i < TARGET_NUM; i++){
			//�W�I�̏������W��(-1.0�`1.0, -1.0�`1.0)�̊Ԃ̃����_���Ȓn�_�ɂ���
			float x = rand.nextFloat() * 2.0f - 1.0f;
			float y = rand.nextFloat() * 2.0f - 1.0f;
			
			//�p�x�������_���ɐݒ肷��
			float angle = rand.nextInt(360);
			
			//�W�I�̑傫����0.25�`0.5�̊ԂŃ����_���Ɍ��肷��
			float size = rand.nextFloat() * 0.25f + 0.25f;
			
			//�W�I�̈ړ����x��0.01�`0.02�̊ԂŃ����_���Ɍ��肷��
			float speed = rand.nextFloat() * 0.01f + 0.01f;
		
			//�W�I�̐���p�x��-2.0���`2.0���̊ԂŃ����_���Ɍ��肷��
			float turnAngle = rand.nextFloat() * 4.0f - 2.0f;
			mTargets[i] = new MyTarget(x, y, angle, size, speed, turnAngle);
		}
  ----------------------------------------------------------------*/
	}
	
	public void startNewGame(){
		
		Random rand = Global.rand;
		
		//�W�I�̏�Ԃ�����������
		for(int i = 0; i < TARGET_NUM; i++){
			//�W�I�̏������W��(-1.0�`1.0, -1.0�`1.0)�̊Ԃ̃����_���Ȓn�_�ɂ���
			float x = rand.nextFloat() * 2.0f - 1.0f;
			float y = rand.nextFloat() * 2.0f - 1.0f;
			
			//�p�x�������_���ɐݒ肷��
			float angle = rand.nextInt(360);
			
			//�W�I�̑傫����0.25�`0.5�̊ԂŃ����_���Ɍ��肷��
			float size = rand.nextFloat() * 0.25f + 0.25f;
			
			//�W�I�̈ړ����x��0.01�`0.02�̊ԂŃ����_���Ɍ��肷��
			float speed = rand.nextFloat() * 0.01f + 0.01f;
		
			//�W�I�̐���p�x��-2.0���`2.0���̊ԂŃ����_���Ɍ��肷��
			float turnAngle = rand.nextFloat() * 4.0f - 2.0f;
			mTargets[i] = new MyTarget(x, y, angle, size, speed, turnAngle);
			
			this.mGameOverFlag = false;
			this.mScore = 0;
			this.mStartTime = System.currentTimeMillis();
		}
	}
	
	//��ʂ��^�b�`���ꂽ���ɌĂ΂�郁�\�b�h
	public void touched(float x, float y){
		
		MyTarget[] targets = mTargets;
		Log.i(getClass().toString(), String.format("touched! X = %f, y = %f", x, y));
		
/*		//�W�I�ƃ^�b�`���ꂽ�|�C���g�Ƃ̋������v�Z����  -----  MyTarget�Ɉړ�  -----
		float dx = x - target.mX;
		float dy = y - target.mY;
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
  ---------------------------------------------------------  */	
		Random rand = Global.rand;
		//���ׂĂ̕W�I�Ƃ̓���𔻒肷��
		//�������W�I�̃T�C�Y�i���a�j��菬������Γ����������Ƃɂ���
		if(!mGameOverFlag){
			for(int i = 0; i < TARGET_NUM; i++){
				if(targets[i].isPointInside(x, y)){
					//�p�[�e�B�N������o����
					for(int j = 0; j < 40; j++){
						
						float moveX = (rand.nextFloat() - 0.5f) * 0.05f;
						float moveY = (rand.nextFloat() - 0.5f) * 0.05f;
						mParticleSystem.add(targets[i].mX, targets[i].mY, 0.2f, moveX, moveY);
						
					}
					float dist = 2.0f;
					float theta = rand.nextFloat() * 360.0f / 180.0f * (float)Math.PI;
					targets[i].mX = (float)Math.cos(theta) * dist;
					targets[i].mY = (float)Math.sin(theta) * dist;
					
					mSe.playHitSound();
					mScore += 100;
					Log.i(getClass().toString(), "Hit!");
				}
			}
		}		
	}
	public void subtractPausedTime(long pausedTime){
    	
    	mStartTime += pausedTime;
    }
	public long getStartTime(){
    	
    	return mStartTime;
    }
	public int getScore(){
		
		return mScore;
	}
	public void setScore(int score){
		
		mScore = score;
	}
	
	// �`����s���������L�q���郁�b�\�b�h
	public void renderMain(GL10 gl){
		
		//Global.rand��mTarget�����[�J���ɃL���b�V������
		Random rand = Global.rand;
		MyTarget[] targets = mTargets;
		
		//���ׂĂ̕W�I���P��������
		for(int i = 0; i < TARGET_NUM; i++){
		
			//�����_���ȃ^�C�~���O�ŕ����]������悤�ɂ���
			if(rand.nextInt(100) == 0){
				//���񂷂�p�x��2�0�`�Q�B�O�̊ԂŃ����_���ɐݒ肷��
				targets[i].mTurnAngle = rand.nextFloat() * 4.0f - 2.0f;
			}
			
			//�����ŕW�I����񂳂���
			targets[i].mAngle = targets[i].mAngle + targets[i].mTurnAngle;
			//�W�I�𓮂����i���݌����Ă�������Ɉړ�������j
			targets[i].move();
			//�p�[�e�B�N�����g���ċO�Ղ�`�悷��
			float moveX = (rand.nextFloat() - 0.5f) * 0.01f;
			float moveY = (rand.nextFloat() - 0.5f) * 0.01f;
			mParticleSystem.add(targets[i].mX, targets[i].mY, 0.1f, moveX, moveY);
		}
		
		//�w�i��`�悷��
		GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 2.0f, 3.0f, mBgTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//�p�[�e�B�N����`�悷��
		mParticleSystem.update();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		mParticleSystem.draw(gl, mParticleTexture);
		
		
		//�W�I��`�悷��
		for(int i = 0; i < TARGET_NUM; i++){

			targets[i].draw(gl, mTargetTexture);
		}
		
		gl.glDisable(GL10.GL_BLEND);
		
		//�o�ߎ��Ԃ��v�Z����
		int passedTime = (int)(System.currentTimeMillis() - mStartTime) / 1000;
		int remainTime = GAME_INTERVAL - passedTime;
		//���Ԑ؂ꂾ������
		if(remainTime <= 0){
			remainTime = 0;
			if(!mGameOverFlag){
				
				mGameOverFlag = true;
			
				//Global.mainActivity.showRetryButton��UI�X���b�h��Ŏ��s����
				mHandler.post(new Runnable(){
					
					@Override
					public void run(){
						
						Global.mainActivity.showRetryButton();
					}
				});
			}
		}	
		Log.i(getClass().toString(), "passed time = " + passedTime);
		
		//���_��`�悷��
		GraphicUtil.drawNumbers(gl, -0.5f, 1.25f, 0.125f, 0.125f, mNumberTexture, mScore, 8, 1.0f, 1.0f, 1.0f, 1.0f);
		//�c�莞�Ԃ�`�悷��
		GraphicUtil.drawNumbers(gl, 0.5f, 1.2f, 0.4f, 0.4f, mNumberTexture, remainTime, 2, 1.0f, 1.0f, 1.0f, 1.0f);
		//�Q�[���I�[�o�[�e�L�X�`����`�悷��
		if(mGameOverFlag){
			GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 2.0f, 0.5f, mGameOverTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		//FPS��\������
		if(Global.isDebuggable){
			
			long nowTime = System.currentTimeMillis();		//���ݎ��Ԃ��擾����
			//���ݎ��ԂƂ̍������v�Z����
			long difference = nowTime - mFpsCountStartTime;
			//1�b�o�߂��Ă����ꍇ�́A�t���[�����̃J�E���g�I��
			if(difference >= 1000){
				
				mFps = mFramesInSecond;
				mFramesInSecond = 0;
				mFpsCountStartTime = nowTime;
			}	
			mFramesInSecond++;
			GraphicUtil.drawNumbers(gl, -0.5f, -1.25f, 0.2f, 0.2f, mNumberTexture, mFps, 2, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		
		/*		//���[�v����  --------  MyTarget�Ɉړ�  ----------------------
		if(target.mX >= 2.0f){
			target.mX -= 4.0f;
		}
		if(target.mX <= -2.0f){
			target.mX += 4.0f;
		}
		if(target.mY >= 2.5f){
			target.mY -= 5.0f;
		}
		if(target.mY <= -2.5f){
			target.mY += 5.0f;
		}
-------------------------------------------------------------*/		
		
/*		gl.glPushMatrix();  -----  MyTarget���Ɉړ�  ------------------
		{
			gl.glTranslatef(target.mX, target.mY, 0.0f);
			gl.glRotatef(target.mAngle, 0.0f, 0.0f, 1.0f);
			gl.glScalef(target.mSize, target.mSize, 1.0f);
			GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, mTargetTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		gl.glPopMatrix();
  ----------------------------------------------------------------*/
		
		
/*		GraphicUtil.drawNumbers(gl, 
				0.0f, 0.0f,	// ���S�̍��W
				0.2f, 0.2f,	// ��̕����̃T�C�Y
				mNumberTexture,	// �����̕`��Ɏg���e�N�X�`��
				12345,	// �`�悵��������
				8,	//�`�悵��������
				1.0f, 1.0f, 1.0f, 1.0f	// �F�͂��̂܂�		
		);
*/		
		// mSampleTexture�̉摜��(0.0f, 0.0f)�̈ʒu�ɕ`�悷��
//		GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, mSampleTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		
		// �S�̂�`�悷��
//		GraphicUtil.drawTexture(gl, -0.5f, 0.5f, 1.0f, 1.0f, mNumberTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		// 0��`�悷��
//		GraphicUtil.drawNumber(gl, 0.5f, 0.5f, 1.0f, 1.0f, mNumberTexture, 0, 1.0f, 1.0f, 1.0f, 1.0f);
		// 6��`�悷��
//		GraphicUtil.drawNumber(gl, -0.5f, -0.5f, 1.0f, 1.0f, mNumberTexture, 6, 1.0f, 1.0f, 1.0f, 1.0f);
		// 9��`�悷��
//		GraphicUtil.drawNumber(gl, 0.5f, -0.5f, 1.0f, 1.0f, mNumberTexture, 9, 1.0f, 1.0f, 1.0f, 1.0f);
		
		
//		gl.glEnable(GL10.GL_BLEND);
		
//		GraphicUtil.drawCircle(gl, 0.0f, 0.0f, 8, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);
		
/*		gl.glPushMatrix();
		{
			gl.glTranslatef(0.5f, 0.0f, 0.0f);	// �S�̂��E��0.5�ړ�������
			gl.glPushMatrix();
			{
				// ���̌�`���l�p�`�������0.5�ړ�������
				gl.glTranslatef(0.0f, 0.5f, 0.0f);
				// ���̌�`���l�p�`�����k������
				gl.glScalef(0.1f, 0.1f, 1.0f);
				GraphicUtil.drawSquare(gl, 1.0f, 0.0f, 0.0f, 0.2f);
			}
			gl.glPopMatrix();
			gl.glScalef(0.3f, 0.3f, 1.0f);	// ���̌�`���l�p�`�����k������
			GraphicUtil.drawSquare(gl, 1.0f, 0.0f, 0.0f, 0.2f);
		}
		gl.glPopMatrix();
*/		
/*		// ���ꂩ��`�悷��|���S�����E��0.5�@���0.5�ړ�������
		gl.glTranslatef(0.2f, -0.3f, 0.0f);
		// ��ʏ�Ŕ����v���ɂS�T�x��]������
		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
		// ��������2.0�@�c������0.5�{�ɂ���
		gl.glScalef(2.0f, 0.5f, 1.0f);
*/		
//		GraphicUtil.drawSquare(gl);
//		GraphicUtil.drawSquare(gl, 1.0f, 0.0f, 0.0f, 0.2f);

//		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
//		GraphicUtil.drawRectangle(gl, 0.75f, 0.25f, 0.5f, 2.0f, 1.0f, 1.0f, 0.0f, 1.0f);

		/*		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				float brightness = (i + j) % 2;
				GraphicUtil.drawRectangle(gl, (float) i * 0.4f - 0.8f,
											  (float) j * 0.4f - 0.8f, 0.4f, 0.4f,
											  brightness, brightness, brightness, 1.0f);
			}
		}
*/
//	gl.glDisable(GL10.GL_BLEND);
	}
	
	//�e�L�X�`����ǂݍ��ރ��\�b�h
	private void loadTextures(GL10 gl){
		
		Resources res = mContext.getResources();
		this.mBgTexture = GraphicUtil.loadTexture(gl, res, R.drawable.circuit);
		if(mBgTexture == 0){
			Log.e(getClass().toString(), "load Texture error! circuit");
		}
		this.mTargetTexture = GraphicUtil.loadTexture(gl, res, R.drawable.fly);
		if(mTargetTexture == 0){
			Log.e(getClass().toString(), "load Texture error! fly");
		}
		this.mNumberTexture = GraphicUtil.loadTexture(gl, res, R.drawable.number_texture);
		if(mNumberTexture == 0){
			Log.e(getClass().toString(), "load Texture error! number_texture");
		}
		this.mGameOverTexture = GraphicUtil.loadTexture(gl, res, R.drawable.game_over);
		if(mGameOverTexture == 0){
			Log.e(getClass().toString(), "load Texture error! game_over");
		}
		
		this.mParticleTexture = GraphicUtil.loadTexture(gl, res, R.drawable.particle_blue);
		if(mParticleTexture == 0){
			
			Log.e(getClass().toString(), "load texture error! particle_blue");
		}
	}
	
	
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// �`�揈�����L�q����
		
		// OpenGL�̍��W�n�Ȃǂ�ݒ�
		gl.glViewport(0, 0, mWidth, mHeight);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		
		gl.glLoadIdentity();
		gl.glOrthof(-1.0f, 1.0f, -1.5f, 1.5f, 0.5f, -0.5f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//�@��ʂ��N���A���Ă���
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
/*�@--------------------�@GraphicUtil�Ɉړ��@---------------------
		//�@�`�悷��|���S���̍��W�A�F��錾���Ă���
		float[] vertices = {-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f};
		float[] colors = {1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f};
		
		// �錾�����|���S�����W�ƐF���V�X�e����̃������Ɋm�ۂ��Ă���@�i�ڍׂ͌�q�j
		FloatBuffer polygonVertices = GraphicUtil.makeFloatBuffer(vertices);
		FloatBuffer polygonColors = GraphicUtil.makeFloatBuffer(colors);
		
		// �|���S����`�悵�Ă���
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
--------------------------------------------------------------*/
		
		renderMain(gl);
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// ��ʐ������A��ʌ����ύX���ɌĂяo�����B�����������Ȃǂ��s��
		
		this.mWidth = width;
		this.mHeight = height;
		
		Global.gl = gl;		//GL�R���e�L�X�g��ێ�����
		
		//�e�L�X�`�������[�h����
		loadTextures(gl);
		
/*		//�e�L�X�`���̐������s��    2�͂ł�loadTexture���\�b�h��ǉ����Ă���̂Ŏg��Ȃ��@�@---------------
		this.mSampleTexture = GraphicUtil.loadTexture(gl, mContext.getResources(), R.drawable.sample_tex);
		if(mSampleTexture != 0){
			
			Log.e(getClass().toString(), "texture load success! sample_tex");
		}
		this.mNumberTexture = GraphicUtil.loadTexture(gl, mContext.getResources(), R.drawable.number_texture);
		if(mNumberTexture != 0){
			
			Log.e(getClass().toString(), "texture load success! number_texture");
		}
----------------------------------------------------------------------------  */
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// ��ʐ������A��ʌ����ύX���ɌĂяo�����B�����������Ȃǂ��s��
		
	}


}





























