package com.maffi.book2;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class ParticleSystem {
	
	public final int mCapacity;
	//�p�[�e�B�N�����P�O�O��������
	public static final int PARTICLE_COUNT = 100;
	
	//�p�[�e�B�N��
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
			
			if(!particles[i].mIsActive){		//��A�N�e�B�u�̃p�[�e�B�N����T��
				
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
		//���_�̔z��@�@��̃p�[�e�B�N��������U���_X�Q�v�f
		float[] vertices = GraphicUtil.getVertices(6 * 2 * mCapacity);
		//�F�̔z��@�P�̃p�[�e�B�N��������U���_X�S�v�f�ir,g,b,a�jX�ő�̃p�[�e�B�N����
		float[] colors = GraphicUtil.getColors(6 * 4 * mCapacity);
		//�e�L�X�`���}�b�s���O�@�@�P�̃p�[�e�B�N��������U���_X�Q�v�f�i���C���jX�ő�̃p�[�e�B�N����
		float[] coords = GraphicUtil.getCoords(6 * 2 * mCapacity);
		
		//�A�N�e�B�u�ȃp�[�e�B�N���̃J�E���g
		int vertexIndex = 0;
		int colorIndex = 0;
		int texCoordIndex = 0;
		
		int activeParticleCount = 0;
		
		for(int i = 0; i < PARTICLE_COUNT; i++){
			
			//�����ŃA�N�e�B�u�ȃp�[�e�B�N������Draw���\�b�h���Ăяo���悤�ɂ���
			if(particles[i].mIsActive){
				
				//���_���W��ǉ�����
				float centerX = particles[i].mX;
				float centerY = particles[i].mY;
				float size = particles[i].mSize;
				float vLeft = -0.5f * size + centerX;
				float vRight = 0.5f * size + centerX;
				float vTop = 0.5f * size + centerY;
				float vBottom = -0.5f * size + centerY;
				
				//�|���S���P
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vBottom;
				
				//�|���S���Q
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vTop;
				vertices[vertexIndex++] = vLeft;
				vertices[vertexIndex++] = vBottom;
				vertices[vertexIndex++] = vRight;
				vertices[vertexIndex++] = vBottom;
				
				//�F
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
				//�}�b�s���O���W
				//�|���S���P
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				//�|���S���Q
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 0.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 1.0f;
				coords[texCoordIndex++] = 1.0f;
				
				//�A�N�e�B�u�p�[�e�B�N���̐��𐔂���
				activeParticleCount++;
			}
		}
		//��C�ɑS���`�悷��
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
