//
// Created by yan on 2020/11/29.
//

#include "apicompat.h"
#include "utils/logcompat.h"
#include "tracepath/tracepath6.h"
#include <jni.h>
#include <stdio.h>

typedef struct {
    JNIEnv* jniEnv;
    jobject jObject;

    jmethodID onStartMethodId;
    jmethodID onUpdateMethodId;
    jmethodID onEndMethodId;
} Callback2Java;

Callback2Java callback2Java;

void callbackOnStart() {
    (*callback2Java.jniEnv)->CallVoidMethod(callback2Java.jniEnv, callback2Java.jObject, callback2Java.onStartMethodId);
}

int callbackOnUpdate(const char* fmt, ...) {
    if (fmt == NULL) {
        return -1;
    }

    char buffer[512] = { 0 };
    va_list aptr;
    int ret;

    va_start(aptr, fmt);
    ret = vsprintf(buffer, fmt, aptr);
    va_end(aptr);

    if (ret > 0) {
        jstring value = (*callback2Java.jniEnv)->NewStringUTF(callback2Java.jniEnv, buffer);
        (*callback2Java.jniEnv)->CallVoidMethod(callback2Java.jniEnv, callback2Java.jObject, callback2Java.onUpdateMethodId, value);
    }
    return ret;
}

void callbackOnEnd() {
    (*callback2Java.jniEnv)->CallVoidMethod(callback2Java.jniEnv, callback2Java.jObject, callback2Java.onEndMethodId);
}

jclass getJavaClass(JNIEnv* env) {
    return (*env)->FindClass(env, "cn/yan/android/tracepath/AndroidTracePath");
}

JNIEXPORT void JNICALL nativeInit(JNIEnv * env, jobject thiz) {
    LOGD("nativeInit enter.");
    callback2Java.jniEnv = env;
    callback2Java.jObject = thiz;

    jclass jClass = getJavaClass(env);

    callback2Java.onStartMethodId = (*env)->GetMethodID(env, jClass, "nativeOnStart", "()V");
    callback2Java.onUpdateMethodId = (*env)->GetMethodID(env, jClass, "nativeOnUpdate", "(Ljava/lang/String;)V");
    callback2Java.onEndMethodId = (*env)->GetMethodID(env, jClass, "nativeOnEnd", "()V");
    LOGD("nativeInit end.");
}

JNIEXPORT void JNICALL nativeStartTrace(JNIEnv * env, jobject thiz, jstring host_name) {
    LOGD("nativeStartTrace enter.");
    const char * domin = (*env)->GetStringUTFChars(env, host_name, NULL);
    LOGD("host name is: %s", domin);

    char* args[] = { "tracepath", domin };
    tracepath(2, args);

    (*env)->ReleaseStringUTFChars(env, host_name, domin);
    LOGD("nativeStartTrace end.");
}

static const JNINativeMethod gMethods[] = {
        { "nativeInit", "()V", (void*)nativeInit },
        { "nativeStartTrace", "(Ljava/lang/String;)V", (void*)nativeStartTrace }
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv *env;
    if ((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_4) != JNI_OK){
        return -1;
    }

    (*env)->RegisterNatives(env, getJavaClass(env), gMethods, sizeof(gMethods) / sizeof(gMethods[0]));
    LOGD("JNI_OnLoad");
    return JNI_VERSION_1_4;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {
    LOGD("JNI_OnUnload");
}