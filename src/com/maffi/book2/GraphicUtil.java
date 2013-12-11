package com.maffi.book2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class GraphicUtil {
	
	//配列オブジェクトを保持する
	private static Hashtable<Integer, float[]> verticesPool = new Hashtable<Integer, float[]>();
	private static Hashtable<Integer, float[]> colorsPool = new Hashtable<Integer, float[]>();
	private static Hashtable<Integer, float[]> coordsPool = new Hashtable<Integer, float[]>();

	
	
	private static Hashtable<Integer, FloatBuffer> polygonVerticesPool = new Hashtable<Integer, FloatBuffer>();
	private static Hashtable<Integer, FloatBuffer> polygonColorsPool = new Hashtable<Integer, FloatBuffer>();
	private static Hashtable<Integer, FloatBuffer> texCoordsPool = new Hashtable<Integer, FloatBuffer>();
	
	public static float[] getVertices(int n){
		
		if(polygonVerticesPool.containsKey(n)){
			
			return verticesPool.get(n);
		}
		float[] vertices = new float[n];
		verticesPool.put(n, vertices);
		return vertices;
	}
	
	public static float[] getColors(int n){
		
		if(colorsPool.containsKey(n)){
			
			return colorsPool.get(n);
		}
		float[] colors = new float[n];
		colorsPool.put(n, colors);
		return colors;
	}
	
	public static float[] getCoords(int n){
		
		if(coordsPool.containsKey(n)){
			
			return coordsPool.get(n);
		}
		float[] coords = new float[n];
		coordsPool.put(n, coords);
		return coords;
	}
	
	// システム上のメモリ領域を確保するためのメソッド
	public static final FloatBuffer makeFloatBuffer(float[] arr){
		

		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);

		return fb;
	}
	
	
	
	
	//　性能改善で使い回し出来るように、キャッシュし３つに分けたバッファーのメソッド達
	public static final FloatBuffer makeVerticesBuffer(float[] arr){
		
		FloatBuffer fb = null;
		if(polygonVerticesPool.containsKey(arr.length)){
			
			fb = polygonVerticesPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		
		fb = makeFloatBuffer(arr);
		polygonVerticesPool.put(arr.length, fb);
		return fb;
	}
	
	public static final FloatBuffer makeColorsBuffer(float[] arr){
		
		FloatBuffer fb = null;
		if(polygonColorsPool.containsKey(arr.length)){
			
			fb = polygonColorsPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
		
		fb = makeFloatBuffer(arr);
		polygonColorsPool.put(arr.length, fb);
		return fb;
	}
	
	public static final FloatBuffer makeTexCoordsBuffer(float[] arr){
	
		FloatBuffer fb = null;
		if(texCoordsPool.containsKey(arr.length)){
			
			fb = texCoordsPool.get(arr.length);
			fb.clear();
			fb.put(arr);
			fb.position(0);
			return fb;
		}
	
		fb = makeFloatBuffer(arr);
		texCoordsPool.put(arr.length, fb);
		return fb;
	}
	
	public static final void drawNumbers(GL10 gl,float x, float y, float width, float height,	int texture,
			int number, int figures, float r, float g, float b, float a){
		
		float totalWidth = width * (float)figures;	// ｎ文字分の横幅
		float rightX = x + (totalWidth * 0.5f);		// 右端のX座標
		float figlX = rightX - width * 0.5f;		// １番右の桁の中心のｘ座標
		
		for(int i = 0; i < figures; i++){
			float figNX = figlX - (float)i * width;	// ｎ桁目の中心のｘ座標
			int numberToDraw = number % 10;
			number = number / 10;
			drawNumber(gl, figNX, y, width, height, texture, numberToDraw, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		
		
		
		
	}
	
	public static final void drawNumber(GL10 gl,float x, float y, float w, float h,	int texture,
			int number, float r, float g, float b, float a){
		float u = (float)(number % 4) * 0.25f;
		float v = (float)(number / 4) * 0.25f;
		drawTexture(gl, x, y, w, h, texture, u, v, 0.25f, 0.25f, r, g, b, a);
	}
	
	public static final void drawTexture(GL10 gl,float x, float y, float width, float height,
			int texture, float u, float v, float tex_w, float tex_h, float r, float g, float b, float a){
		
		//　描画するポリゴンの座標、色を宣言している		
//		float[] vertices = {-0.5f * width + x, -0.5f * height + y, 0.5f * width + x, -0.5f * height + y,
//							-0.5f * width + x, 0.5f * height + y, 0.5f * width + x, 0.5f * height + y,};
		//上記の宣言からHashtable適用後に変更
		float[] vertices = getVertices(8);
		vertices[0] = -0.5f * width + x; vertices[1] = -0.5f * height + y;
		vertices[2] =  0.5f * width + x; vertices[3] = -0.5f * height + y;
		vertices[4] = -0.5f * width + x; vertices[5] =  0.5f * height + y;
		vertices[6] =  0.5f * width + x; vertices[7] =  0.5f * height + y;
		
//		float[] colors = {r, g, b, a,   r, g, b, a,   r, g, b, a,   r, g, b, a,};
		//Hashtable私用後に変更
		float[] colors = getColors(16);
		for(int i = 0; i < 16; i++){
			
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i] = a;
		}
		
//		float[] coords = {u, v + tex_h, u + tex_w, v + tex_h, u, v, u + tex_w, v,}; 
		//Hashtable使用後に変更
		float[] coords = getCoords(8);
		coords[0] = u;				coords[1] = v + tex_h;
		coords[2] = u + tex_w;		coords[3] = v + tex_h;
		coords[4] = u;				coords[5] = v;
		coords[6] = u + tex_w;		coords[7] = v;

		
		
		// 宣言したポリゴン座標と色をシステム上のメモリに確保している　（詳細は後述）
		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		FloatBuffer texCoords = makeTexCoordsBuffer(coords);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		//テキスチャオブジェクトの指定
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		// ポリゴンを描画している
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}
	public static final void drawTexture(GL10 gl,float x, float y, float width, float height,
			int texture, float r, float g, float b, float a){
		drawTexture(gl, x, y, width, height, texture, 0.0f, 0.0f, 1.0f, 1.0f, r, g, b, a);
	}
	
	
	public static void drawCircle(GL10 gl, float x, float y, int divides, float radius,
										   float r, float g, float b, float a){
		
		float[] vertices = getVertices(divides * 3 * 2);
		int vertexId = 0;		// 頂点配列の要素の番号を記憶しておくための変数
		for(int i = 0; i < divides; i++){
			// 円周上のi番目の頂点の角度（ラジアン）を計算する
			float theta1 = 2.0f / (float)divides * (float)i * (float)Math.PI;
			// 円周上の(i + 1)番目の頂点の角度（ラジアン）を計算する
			float theta2 = 2.0f / (float)divides * (float)(i + 1) * (float)Math.PI;
			// i番目の三角形の0番目の頂点情報をセットする
			vertices[vertexId++] = x;
			vertices[vertexId++] = y;
			// i番目の三角形の１番目の頂点情報をセットする（円周上のi番目の頂点）
			vertices[vertexId++] = (float)Math.cos((double)theta1) * radius + x;	// x座標
			vertices[vertexId++] = (float)Math.sin((double)theta1) * radius + y;	// y座標
			// i番目の三角形の2番目の頂点情報をセットする（円周上のi+1番目の頂点）
			vertices[vertexId++] = (float)Math.cos((double)theta2) * radius + x;	// x座標
			vertices[vertexId++] = (float)Math.sin((double)theta2) * radius + y;	// y座標	
		}
		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		
		gl.glColor4f(r, g, b, a);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, divides * 3);
		
	}
		
	//正方形のポリゴンを描画する
	public static final void drawSquare(GL10 gl, float r, float g, float b, float a){
		
		// 黄色い四角形を描画する
		drawSquare(gl, 0.0f, 0.0f, r, g, b, a);
//		drawRectangle(gl, 0.0f, 0.0f, 0.0f, 0.0f, r, g, b, a);
	}
	// 指定した座標（x, y）にRGBAで指定した色の正方形を描画する
	public static final void drawSquare(GL10 gl,float x, float y,
			float r, float g, float b, float a){
		
		//　描画するポリゴンの座標、色を宣言している		
		float[] vertices = {-0.5f + x, -0.5f + y, 0.5f + x, -0.5f + y,
							-0.5f + x, 0.5f + y, 0.5f + x, 0.5f + y,};
		float[] colors = {
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
				r, g, b, a,
		};
		
		// 宣言したポリゴン座標と色をシステム上のメモリに確保している　（詳細は後述）
		FloatBuffer squareVertices = makeFloatBuffer(vertices);
		FloatBuffer squareColors = makeFloatBuffer(colors);
		
		// ポリゴンを描画している
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, squareVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, squareColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
	// 指定した座標（x, y）にRGBAで指定した色の正方形を描画する
	public static final void drawRectangle(GL10 gl,float x, float y, float width, float height,
			float r, float g, float b, float a){
		
		//　描画するポリゴンの座標、色を宣言している		
//		float[] vertices = {-0.5f * width + x, -0.5f * height + y, 0.5f * width + x, -0.5f * height + y,
//							-0.5f * width + x, 0.5f * height + y, 0.5f * width + x, 0.5f * height + y,};
		//Hashtable使用後に変更
		float[] vertices = getVertices(8);
		vertices[0] = -0.5f * width + x; vertices[1] = -0.5f * height + y;
		vertices[2] =  0.5f * width + x; vertices[3] = -0.5f * height + y;
		vertices[4] = -0.5f * width + x; vertices[5] =  0.5f * height + y;
		vertices[6] =  0.5f * width + x; vertices[7] =  0.5f * height + y;
		
		
//		float[] colors = {r, g, b, a,    r, g, b, a,    r, g, b, a,    r, g, b, a,};
		//Hashtable私用後に変更
		float[] colors = getColors(16);
		for(int i = 0; i < 16; i++){
			
			colors[i++] = r;
			colors[i++] = g;
			colors[i++] = b;
			colors[i] = a;
		}
		
		
		
		// 宣言したポリゴン座標と色をシステム上のメモリに確保している　（詳細は後述）
		FloatBuffer polygonVertices = makeVerticesBuffer(vertices);
		FloatBuffer polygonColors = makeColorsBuffer(colors);
		
		// ポリゴンを描画している
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	public static final int loadTexture(GL10 gl, Resources resources, int resId){
		
		int[] textures = new int[1];
		
		//Bitmapの作成
		Bitmap bmp = BitmapFactory.decodeResource(resources, resId, options);
		if(bmp == null){
			return 0;
		}
		
		//OpenGL用のテキスチャを生成する
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		
		//OpenGLへの転送が完了したので、VMメモリ上に作成したBitmapを破棄する
		bmp.recycle();
		
		//TextureManagerに登録する
		TextureManager.addTexture(resId, textures[0]);
		
		return textures[0];
	}
	private static final BitmapFactory.Options options = new BitmapFactory.Options();
	
	static{
		
		//リソースの自動リサイズをしない
		options.inScaled = false;
		//32bit画像として読み込む
		options.inPreferredConfig = Config.ARGB_8888;
	}
	
}





























