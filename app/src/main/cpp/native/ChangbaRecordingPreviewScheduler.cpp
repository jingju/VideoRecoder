#include "com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler.h"

#define LOG_TAG "RecordingPreviewScheduler"

static MVRecordingPreviewController *previewController = 0;
static jobject g_obj = 0;

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_switchPreviewFilter__ILandroid_content_res_AssetManager_2Ljava_lang_String_2(
        JNIEnv *env, jobject obj, jint filterType, jobject assetManager, jstring filename) {
    if (NULL != previewController) {
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_switchCameraFacing(
        JNIEnv *env, jobject obj) {
    if (NULL != previewController) {
        previewController->switchCameraFacing();
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_adaptiveVideoQuality(
        JNIEnv *env, jobject obj, jint maxBitRate, jint avgBitRate, jint fps) {
    if (NULL != previewController) {
        previewController->adaptiveVideoQuality(maxBitRate, avgBitRate, fps);
    }
}

JNIEXPORT void JNICALL
Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_hotConfigQuality(
        JNIEnv *env, jobject instance, jint max, jint avg, jint fps) {
    if (NULL != previewController) {
        previewController->hotConfigQuality(max, avg, fps);
    }

}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_startEncoding(
        JNIEnv *env, jobject obj, jint width, jint height, jint videoBitRate, jint frameRate,
        jboolean useHardWareEncoding, jint strategy) {
    if (NULL != previewController) {
        previewController->startEncoding(width, height, videoBitRate, frameRate,
                                         useHardWareEncoding, strategy);
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_stopEncoding(
        JNIEnv *env, jobject obj) {
    if (NULL != previewController) {
        previewController->stopEncoding();
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_createWindowSurface(
        JNIEnv *env, jobject obj, jobject surface) {
    if (surface != 0 && NULL != previewController) {
        ANativeWindow *window = ANativeWindow_fromSurface(env, surface);
        if (window != NULL) {
            previewController->createWindowSurface(window);
        }
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_destroyWindowSurface(
        JNIEnv *env, jobject obj) {
    if (NULL != previewController) {
        previewController->destroyWindowSurface();
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_prepareEGLContext(
        JNIEnv *env, jobject obj, jobject surface, jint screenWidth, jint screenHeight,
        jint cameraFacingId) {
    previewController = new MVRecordingPreviewController();
    JavaVM *g_jvm = NULL;
    env->GetJavaVM(&g_jvm);
    g_obj = env->NewGlobalRef(obj);
    if (surface != 0 && NULL != previewController) {
        ANativeWindow *window = ANativeWindow_fromSurface(env, surface);
        if (window != NULL) {
            previewController->prepareEGLContext(window, g_jvm, g_obj, screenWidth, screenHeight,
                                                 cameraFacingId);
        }
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_resetRenderSize(
        JNIEnv *env, jobject obj, jint screenWidth, jint screenHeight) {
    if (NULL != previewController) {
        previewController->resetRenderSize(screenWidth, screenHeight);
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_destroyEGLContext(
        JNIEnv *env, jobject obj) {
    if (NULL != previewController) {
        previewController->destroyEGLContext();
        delete previewController;
        previewController = NULL;

        if (g_obj != 0) {
            env->DeleteGlobalRef(g_obj);
            g_obj = 0;
        }
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_notifyFrameAvailable(
        JNIEnv *env, jobject obj) {
    if (NULL != previewController) {
        previewController->notifyFrameAvailable();
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_updateTexMatrix(
        JNIEnv *env, jobject obj, jfloatArray array) {
    if (NULL != previewController) {
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_switchPauseRecordingPreviewState(
        JNIEnv *env, jobject obj) {
    if (NULL != previewController) {
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_switchCommonPreviewState(
        JNIEnv *env, jobject obj) {
    if (NULL != previewController) {
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_hotConfig(
        JNIEnv *env, jobject instance, jint bitRate, jint fps, jint gopSize) {
    LOGI(" %s", "Java_com_jingju_videorecorder_songstudio_recording_camera_preview_ChangbaRecordingPreviewScheduler_hotConfig");
    if (NULL != previewController) {
        previewController->hotConfig(bitRate, fps, gopSize);
    }
}

JNIEXPORT void JNICALL Java_com_jingju_videorecorder_songstudio_recording_camera_preview_RecordingPreviewScheduler_setBeautifyParam(JNIEnv* env, jobject obj, jint key, jfloat value) {
	LOGI("Java_com_jingju_videorecorder_songstudio_recording_camera_preview_ChangbaRecordingPreviewScheduler_setBeautifyParam");
	LOGI("setbeautify: %d, %f", key, value);
	if(NULL != previewController) {
	}
}


