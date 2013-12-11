package com.maffi.book2;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ParticleSystem {
	
	public final int mCapacity;
	//パーティクルを１００個生成する
	public static final int PARTICLE_COUNT = 100;
	
	//パーティクル
	private Particle[] mParticles = new Particle[PARTICLE_COUNT];
	
	public ParticleSystem(int capacity, int particleLifeSpan){
		
		this.mCapacity = capacity;
		mParticles = new Particle[mCapacity];
		Particle[] particles = mParticles;
		for(int i = 0; i < mCapacity; i++){
			
			particles[i] = new Particle();
			particles[i].mLifeSpan = particleLifeSpan;
		}
	}
	
	public void add(float x, float y, float size, float moveX, float moveY){
		
		Particle[] particles = mParticles;
		for(int i = 0; i < PARTICLE_COUNT; i++){
			
			if(!particles[i].mIsActive){		//非アクティブのパーティクルを探す
				
				particles[i].mIsActive = true;
				particles[i].mX = x;
				particles[i].mY = y;
				particles[i].mSize = size;
				particles[i].mMoveX = moveX;
				particles[i].mMoveY = moveY;
				
				particles[i].mFrameNumber = 0;
				break;
			}
		}
	}
	public void draw(GL10 gl, int texture){
		
		Particle[] particles = mParticles;
		//頂点の配列　　一つのパーティクルあたり６頂点X２要素
		float[] vertices = GraphicUtil.getVertices(6 * 2 * mCapacity);
		//色の配列　１つのパーティクルあたり６頂点X４要素（r,g,b,a）X最大のパーティクル数
		float[] colors = GraphicUtil.getColors(6 * 4 * mCapacity);
		//テキスチャマッピング　　１つのパーティクルあたり６頂点X２要素（ｘ，ｙ）X最大のパーティクル数
		float[] coords = GraphicUtil.getCoords(6 * 2 * mCapacity);
		
		//アクティブなパーティクルのカウント
		int vertexIndex = 0;
		int colorIndex = 0;
		int texCoordIndex = 0;
		
		int activeParticleCount = 0;
		
		for(int i = 0; i < PARTICLE_COUNT; i++){
			
			//ここでアクティブなパーティクルだけDrawメソッドを呼び出すようにする
			if(particles[i].mIsActive){
				
				//頂点座標を追加する
				float centerX = particles[i].mX;
				float centerY = particles[i].mY;
				float size = particles[i].mSize;
				float vLeft = -0.5f * size + centerX;
				float vRight = 0.5f * size + centerX;
				float vTop = 0.5f * size + centerY;
				float vBottom = -0.5f * size + centerY;
				
				//ポリゴン１
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vBottom;
				
				//ポリゴン２
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vBottom;
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vBottom;
				
				//色
				float lifePercentage = (float)particles[i].mFrameNumber / (float)particles[i].mLifeSpan;
				float alpha;
				if(lifePercentage <= 0.5f){
					
					alpha = lifePercentage * 2.0f;
				}else{
					
					alpha = 1.0f - (lifePercentage - 0.5f) * 2.0f;
				}
				
				for(int j = 0; j < 6; j++){
					
					colors[colorIndex++] = 1.0f;
					colors[colorIndex++] = 1.0f;
					colors[colorIndex++] = 1.0f;
					colors[colorIndex++] = alpha;
				}
				//マッピング座標
				//ポリゴン１
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				//ポリゴン２
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 1.0f;
				
				//アクティブパーティクルの数を数える
				activeParticleCount++;
			}
		}
		//一気に全部描画する
		FloatBuffer verticesBuffer = GraphicUtil.makeVerticesBuffer(vertices);
		FloatBuffer colorBuffer = GraphicUtil.makeColorsBuffer(colors);
		FloatBuffer coordBuffer = GraphicUtil.makeTexCoordsBuffer(coords);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticesBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, activeParticleCount * 6);
		
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	
	
	
	public void update(){
		Particle[] particles = mParticles;
		for(int i = 0; i < PARTICLE_COUNT; i++){
			
			if(particles[i].mIsActive){
				particles[i].update();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
