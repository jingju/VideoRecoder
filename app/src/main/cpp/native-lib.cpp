#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_jingju_videorecorder_media_ushow_webrtcdemo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
