package com.maffi.book2;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

public class TextureManager {
	
	//�e�L�X�`����ێ�����
	private static Map<Integer, Integer> mTextures = new Hashtable<Integer, Integer>();
	
	//���[�h�����e�L�X�`����ǉ�����
	public static final void addTexture(int resId, int texId){
		mTextures.put(resId, texId);
	}
	//�e�L�X�`�����폜����
	public static final void deleteTexture(GL10 gl, int resId){
		if(mTextures.containsKey(resId)){
			int[] texId = new int[1];
			texId[0] = mTextures.get(resId);
			gl.glDeleteTextures(1, texId, 0);
			mTextures.remove(resId);
		}
	}
	//���ׂẴe�L�X�`�����폜����
	public static final void deleteAll(GL10 gl){
		List<Integer> keys = new ArrayList<Integer>(mTextures.keySet());
		for(Integer key : keys){
			deleteTexture(gl, key);
		}
	}
	
}
