package com.jingju.videorecorder.songstudio.recording.camera.preview;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class RecordingPreviewView extends SurfaceView implements Callback {
	private static final String TAG = "RecordingPreviewView";
	
	public RecordingPreviewView(Context context) {
		super(context);
		SurfaceHolder surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Surface surface = holder.getSurface();
		int width = getWidth();
		int height = getHeight();
		if(null != mCallback){
			mCallback.createSurface(surface, width, height);
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if(null != mCallback){
			mCallback.resetRenderSize(width, height);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if(null != mCallback){
			mCallback.destroySurface();
		}
	}
	
	private RecordingPreviewViewCallback mCallback;
	public void setCallback(RecordingPreviewViewCallback callback){
		this.mCallback = callback;
	}
	public interface RecordingPreviewViewCallback {
		public void createSurface(Surface surface, int width, int height);
		public void resetRenderSize(int width, int height);
		public void destroySurface();
	}
}
