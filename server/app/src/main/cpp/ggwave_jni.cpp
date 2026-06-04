#include <android/log.h>
#include <jni.h>
#include <string.h>

#include "ggwave.h"

namespace {
    JavaVM* g_jvm;
    jobject g_mainObject;
    ggwave_Instance g_ggwave;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_borne_MainActivity_initNative(JNIEnv * env, jobject obj) {
    __android_log_print(ANDROID_LOG_DEBUG, "ggwave (native)", "Initializing native module");

    ggwave_Parameters parameters = ggwave_getDefaultParameters();
    parameters.sampleFormatInp = GGWAVE_SAMPLE_FORMAT_I16;
    parameters.sampleFormatOut = GGWAVE_SAMPLE_FORMAT_I16;
    parameters.sampleRateInp = 48000;
    g_ggwave = ggwave_init(parameters);

    env->GetJavaVM(&g_jvm);
    g_mainObject = env->NewGlobalRef(obj);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_borne_MainActivity_processCaptureData(JNIEnv *env, jobject thiz, jshortArray data) {
    jsize dataSize = env->GetArrayLength(data);

    jboolean isCopy = false;
    jshort * cData = env->GetShortArrayElements(data, &isCopy);

    char output[256];
    int ret = ggwave_decode(g_ggwave, (char *) cData, 2*dataSize, output);

    if (ret != 0) {
        __android_log_print(ANDROID_LOG_DEBUG, "ggwave (native)", "Received message: '%s'", output);

        jclass handlerClass = env->GetObjectClass(g_mainObject);
        jmethodID mid_onReceivedMessage = env->GetMethodID(handlerClass, "onNativeReceivedMessage", "([B)V");
        jbyteArray jba_message = env->NewByteArray(strlen(output));

        env->SetByteArrayRegion(jba_message, 0, strlen(output), (jbyte*) output);
        env->CallVoidMethod(g_mainObject, mid_onReceivedMessage, jba_message);
        env->DeleteLocalRef(jba_message);
    }
}