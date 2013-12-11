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
	
	//僥僉僗僠儍傪娗棟偡傞偨傔偺ID
	private int mBgTexture;				//攚宨僥僉僗僠儍
	private int mTargetTexture;			//昗揑梡僥僉僗僠儍
	private int mNumberTexture;			//悢帤梡僥僉僗僠儍
	private int mGameOverTexture;		//僎乕儉僆乕僶乕梡僥僉僗僠儍
	
	private int mParticleTexture;
	
//	private MyTarget mTarget;			//昗揑
	
	private static final int GAME_INTERVAL = 10;
	public static final int TARGET_NUM = 10;	//昗揑偺悢傪巜掕
	private MyTarget[] mTargets = new MyTarget[TARGET_NUM];
	private int mScore;					//摼揰
	
	private long mStartTime;			//奐巒帪娫
	private boolean mGameOverFlag;		//僎乕儉偺娗棟
	
	private Handler mHandler = new Handler();
	
	private MySe mSe;					//岠壥壒
	
	private ParticleSystem mParticleSystem;
	
	private long mFpsCountStartTime = System.currentTimeMillis();
	private int mFramesInSecond = 0;
	private int mFps = 0;
	
	
/*	-----------------------------------------------------
    private float mTargetAngle;			//昗揑偺妏搙
	private float mTargetX, mTargetY;	//昗揑偺埵抲
	private float mTargetSize;			//昗揑偺僒僀僘private
	private float mTargetSpeed;			//昗揑偺堏摦懍搙
	private float mTargetTurnAngle;		//昗揑偺慁夞妏搙
	
    MyTarget偵堏摦丂丂--------------------------------------*/	
	
	public MyRenderer(Context context){
		
		this.mContext = context;
		this.mSe = new MySe(context);
		this.mParticleSystem = new ParticleSystem(300, 30);
		
		startNewGame();
		
//		this.mGameOverFlag = false;		startNewGame()偵堏摦
//		this.mScore = 0;				startNewGame()偵堏摦
//		this.mStartTime = System.currentTimeMillis();	startNewGame()偵堏摦
		
//		Random rand = Global.rand;		startNewGame()偵堏摦
		
/*		//僐儞僗僩儔僋僞傪巊偭偰弶婜抣傪愝掕偡傞
		this.mTarget = new MyTarget(0.5f, -0.5f, 30.0f, 0.5f, 0.01f, 1.0f);
*/		
/*		//昗揑偺忬懺傪弶婜壔偡傞  ----------  startNewGame()偵堏摦  -----------
		for(int i = 0; i < TARGET_NUM; i++){
			//昗揑偺弶婜嵗昗偼(-1.0乣1.0, -1.0乣1.0)偺娫偺儔儞僟儉側抧揰偵偡傞
			float x = rand.nextFloat() * 2.0f - 1.0f;
			float y = rand.nextFloat() * 2.0f - 1.0f;
			
			//妏搙傪儔儞僟儉偵愝掕偡傞
			float angle = rand.nextInt(360);
			
			//昗揑偺戝偒偝傪0.25乣0.5偺娫偱儔儞僟儉偵寛掕偡傞
			float size = rand.nextFloat() * 0.25f + 0.25f;
			
			//昗揑偺堏摦懍搙傪0.01乣0.02偺娫偱儔儞僟儉偵寛掕偡傞
			float speed = rand.nextFloat() * 0.01f + 0.01f;
		
			//昗揑偺慁夞妏搙傪-2.0倖乣2.0倖偺娫偱儔儞僟儉偵寛掕偡傞
			float turnAngle = rand.nextFloat() * 4.0f - 2.0f;
			mTargets[i] = new MyTarget(x, y, angle, size, speed, turnAngle);
		}
  ----------------------------------------------------------------*/
	}
	
	public void startNewGame(){
		
		Random rand = Global.rand;
		
		//昗揑偺忬懺傪弶婜壔偡傞
		for(int i = 0; i < TARGET_NUM; i++){
			//昗揑偺弶婜嵗昗偼(-1.0乣1.0, -1.0乣1.0)偺娫偺儔儞僟儉側抧揰偵偡傞
			float x = rand.nextFloat() * 2.0f - 1.0f;
			float y = rand.nextFloat() * 2.0f - 1.0f;
			
			//妏搙傪儔儞僟儉偵愝掕偡傞
			float angle = rand.nextInt(360);
			
			//昗揑偺戝偒偝傪0.25乣0.5偺娫偱儔儞僟儉偵寛掕偡傞
			float size = rand.nextFloat() * 0.25f + 0.25f;
			
			//昗揑偺堏摦懍搙傪0.01乣0.02偺娫偱儔儞僟儉偵寛掕偡傞
			float speed = rand.nextFloat() * 0.01f + 0.01f;
		
			//昗揑偺慁夞妏搙傪-2.0倖乣2.0倖偺娫偱儔儞僟儉偵寛掕偡傞
			float turnAngle = rand.nextFloat() * 4.0f - 2.0f;
			mTargets[i] = new MyTarget(x, y, angle, size, speed, turnAngle);
			
			this.mGameOverFlag = false;
			this.mScore = 0;
			this.mStartTime = System.currentTimeMillis();
		}
	}
	
	//夋柺偑僞僢僠偝傟偨帪偵屇偽傟傞儊僜僢僪
	public void touched(float x, float y){
		
		MyTarget[] targets = mTargets;
		Log.i(getClass().toString(), String.format("touched! X = %f, y = %f", x, y));
		
/*		//昗揑偲僞僢僠偝傟偨億僀儞僩偲偺嫍棧傪寁嶼偡傞  -----  MyTarget偵堏摦  -----
		float dx = x - target.mX;
		float dy = y - target.mY;
		float distance = (float) Math.sqrt(dx * dx + dy * dy);
  ---------------------------------------------------------  */	
		Random rand = Global.rand;
		//偡傋偰偺昗揑偲偺摉傝傪敾掕偡傞
		//嫍棧偑昗揑偺僒僀僘乮敿宎乯傛傝彫偝偗傟偽摉偨偭偨偙偲偵偡傞
		if(!mGameOverFlag){
			for(int i = 0; i < TARGET_NUM; i++){
				if(targets[i].isPointInside(x, y)){
					//僷乕僥傿僋儖傪曻弌偡傞
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
	
	// 昤夋傪峴偆晹暘傪婰弎偡傞儊僢僜僢僪
	public void renderMain(GL10 gl){
		
		//Global.rand偲mTarget傪儘乕僇儖偵僉儍僢僔儏偡傞
		Random rand = Global.rand;
		MyTarget[] targets = mTargets;
		
		//偡傋偰偺昗揑傪侾偮偢偮摦偐偡
		for(int i = 0; i < TARGET_NUM; i++){
		
			//儔儞僟儉側僞僀儈儞僌偱曽岦揮姺偡傞傛偆偵偡傞
			if(rand.nextInt(100) == 0){
				//慁夞偡傞妏搙傪񳈘乣俀丅侽偺娫偱儔儞僟儉偵愝掕偡傞
				targets[i].mTurnAngle = rand.nextFloat() * 4.0f - 2.0f;
			}
			
			//偙偙偱昗揑傪慁夞偝偣傞
			targets[i].mAngle = targets[i].mAngle + targets[i].mTurnAngle;
			//昗揑傪摦偐偡乮尰嵼岦偄偰偄傞曽岦偵堏摦偝偣傞乯
			targets[i].move();
			//僷乕僥傿僋儖傪巊偭偰婳愓傪昤夋偡傞
			float moveX = (rand.nextFloat() - 0.5f) * 0.01f;
			float moveY = (rand.nextFloat() - 0.5f) * 0.01f;
			mParticleSystem.add(targets[i].mX, targets[i].mY, 0.1f, moveX, moveY);
		}
		
		//攚宨傪昤夋偡傞
		GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 2.0f, 3.0f, mBgTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//僷乕僥傿僋儖傪昤夋偡傞
		mParticleSystem.update();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		mParticleSystem.draw(gl, mParticleTexture);
		
		
		//昗揑傪昤夋偡傞
		for(int i = 0; i < TARGET_NUM; i++){

			targets[i].draw(gl, mTargetTexture);
		}
		
		gl.glDisable(GL10.GL_BLEND);
		
		//宱夁帪娫傪寁嶼偡傞
		int passedTime = (int)(System.currentTimeMillis() - mStartTime) / 1000;
		int remainTime = GAME_INTERVAL - passedTime;
		//帪娫愗傟偩偭偨傜
		if(remainTime <= 0){
			remainTime = 0;
			if(!mGameOverFlag){
				
				mGameOverFlag = true;
			
				//Global.mainActivity.showRetryButton傪UI僗儗僢僪忋偱幚峴偡傞
				mHandler.post(new Runnable(){
					
					@Override
					public void run(){
						
						Global.mainActivity.showRetryButton();
					}
				});
			}
		}	
		Log.i(getClass().toString(), "passed time = " + passedTime);
		
		//摼揰傪昤夋偡傞
		GraphicUtil.drawNumbers(gl, -0.5f, 1.25f, 0.125f, 0.125f, mNumberTexture, mScore, 8, 1.0f, 1.0f, 1.0f, 1.0f);
		//巆傝帪娫傪昤夋偡傞
		GraphicUtil.drawNumbers(gl, 0.5f, 1.2f, 0.4f, 0.4f, mNumberTexture, remainTime, 2, 1.0f, 1.0f, 1.0f, 1.0f);
		//僎乕儉僆乕僶乕僥僉僗僠儍傪昤夋偡傞
		if(mGameOverFlag){
			GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 2.0f, 0.5f, mGameOverTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		//FPS傪昞帵偡傞
		if(Global.isDebuggable){
			
			long nowTime = System.currentTimeMillis();		//尰嵼帪娫傪庢摼偡傞
			//尰嵼帪娫偲偺嵎暘傪寁嶼偡傞
			long difference = nowTime - mFpsCountStartTime;
			//1昩宱夁偟偰偄偨応崌偼丄僼儗乕儉悢偺僇僂儞僩廔椆
			if(difference >= 1000){
				
				mFps = mFramesInSecond;
				mFramesInSecond = 0;
				mFpsCountStartTime = nowTime;
			}	
			mFramesInSecond++;
			GraphicUtil.drawNumbers(gl, -0.5f, -1.25f, 0.2f, 0.2f, mNumberTexture, mFps, 2, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		
		/*		//儚乕僾張棟  --------  MyTarget偵堏摦  ----------------------
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
		
/*		gl.glPushMatrix();  -----  MyTarget撪偵堏摦  ------------------
		{
			gl.glTranslatef(target.mX, target.mY, 0.0f);
			gl.glRotatef(target.mAngle, 0.0f, 0.0f, 1.0f);
			gl.glScalef(target.mSize, target.mSize, 1.0f);
			GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, mTargetTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		gl.glPopMatrix();
  ----------------------------------------------------------------*/
		
		
/*		GraphicUtil.drawNumbers(gl, 
				0.0f, 0.0f,	// 拞怱偺嵗昗
				0.2f, 0.2f,	// 堦偮偺暥帤偺僒僀僘
				mNumberTexture,	// 悢帤偺昤夋偵巊偆僥僋僗僠儍
				12345,	// 昤夋偟偨偄悢帤
				8,	//昤夋偟偨偄寘悢
				1.0f, 1.0f, 1.0f, 1.0f	// 怓偼偦偺傑傑		
		);
*/		
		// mSampleTexture偺夋憸傪(0.0f, 0.0f)偺埵抲偵昤夋偡傞
//		GraphicUtil.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, mSampleTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		
		// 慡懱傪昤夋偡傞
//		GraphicUtil.drawTexture(gl, -0.5f, 0.5f, 1.0f, 1.0f, mNumberTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		// 0傪昤夋偡傞
//		GraphicUtil.drawNumber(gl, 0.5f, 0.5f, 1.0f, 1.0f, mNumberTexture, 0, 1.0f, 1.0f, 1.0f, 1.0f);
		// 6傪昤夋偡傞
//		GraphicUtil.drawNumber(gl, -0.5f, -0.5f, 1.0f, 1.0f, mNumberTexture, 6, 1.0f, 1.0f, 1.0f, 1.0f);
		// 9傪昤夋偡傞
//		GraphicUtil.drawNumber(gl, 0.5f, -0.5f, 1.0f, 1.0f, mNumberTexture, 9, 1.0f, 1.0f, 1.0f, 1.0f);
		
		
//		gl.glEnable(GL10.GL_BLEND);
		
//		GraphicUtil.drawCircle(gl, 0.0f, 0.0f, 8, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);
		
/*		gl.glPushMatrix();
		{
			gl.glTranslatef(0.5f, 0.0f, 0.0f);	// 慡懱傪塃偵0.5堏摦偝偣傞
			gl.glPushMatrix();
			{
				// 偙偺屻昤偔巐妏宍偩偗忋偵0.5堏摦偝偣傞
				gl.glTranslatef(0.0f, 0.5f, 0.0f);
				// 偙偺屻昤偔巐妏宍偩偗弅彫偡傞
				gl.glScalef(0.1f, 0.1f, 1.0f);
				GraphicUtil.drawSquare(gl, 1.0f, 0.0f, 0.0f, 0.2f);
			}
			gl.glPopMatrix();
			gl.glScalef(0.3f, 0.3f, 1.0f);	// 偙偺屻昤偔巐妏宍偩偗弅彫偡傞
			GraphicUtil.drawSquare(gl, 1.0f, 0.0f, 0.0f, 0.2f);
		}
		gl.glPopMatrix();
*/		
/*		// 偙傟偐傜昤夋偡傞億儕僑儞傪塃偵0.5丂忋偵0.5堏摦偝偣傞
		gl.glTranslatef(0.2f, -0.3f, 0.0f);
		// 夋柺忋偱斀帪寁夞傝偵係俆搙夞揮偝偣傞
		gl.glRotatef(45.0f, 0.0f, 0.0f, 1.0f);
		// 墶曽岦偵2.0丂廲曽岦偵0.5攞偵偡傞
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
	
	//僥僉僗僠儍傪撉傒崬傓儊僜僢僪
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
		// 昤夋張棟傪婰弎偡傞
		
		// OpenGL偺嵗昗宯側偳傪愝掕
		gl.glViewport(0, 0, mWidth, mHeight);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		
		gl.glLoadIdentity();
		gl.glOrthof(-1.0f, 1.0f, -1.5f, 1.5f, 0.5f, -0.5f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//丂夋柺傪僋儕傾偟偰偄傞
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
/*丂--------------------丂GraphicUtil偵堏摦丂---------------------
		//丂昤夋偡傞億儕僑儞偺嵗昗丄怓傪愰尵偟偰偄傞
		float[] vertices = {-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f};
		float[] colors = {1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f};
		
		// 愰尵偟偨億儕僑儞嵗昗偲怓傪僔僗僥儉忋偺儊儌儕偵妋曐偟偰偄傞丂乮徻嵶偼屻弎乯
		FloatBuffer polygonVertices = GraphicUtil.makeFloatBuffer(vertices);
		FloatBuffer polygonColors = GraphicUtil.makeFloatBuffer(colors);
		
		// 億儕僑儞傪昤夋偟偰偄傞
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
		// 夋柺惗惉帪丄夋柺岦偒曄峏帪偵屇傃弌偝傟傞丅弶婜壔張棟側偳傪峴偆
		
		this.mWidth = width;
		this.mHeight = height;
		
		Global.gl = gl;		//GL僐儞僥僉僗僩傪曐帩偡傞
		
		//僥僉僗僠儍傪儘乕僪偡傞
		loadTextures(gl);
		
/*		//僥僉僗僠儍偺惗惉傪峴偆    2復偱偼loadTexture儊僜僢僪傪捛壛偟偰偁傞偺偱巊傢側偄丂丂---------------
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
		// 夋柺惗惉帪丄夋柺岦偒曄峏帪偵屇傃弌偝傟傞丅弶婜壔張棟側偳傪峴偆
		
	}


}





























