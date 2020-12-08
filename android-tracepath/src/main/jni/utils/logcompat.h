//
// Created by yan on 2020/11/29.
//

#ifndef ANDROIDTRACEPATH_LOGCOMPAT_H
#define ANDROIDTRACEPATH_LOGCOMPAT_H

#include <android/log.h>

#define TAG "AndroidTracePathCompat"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG,  __VA_ARGS__)

#endif //ANDROIDTRACEPATH_LOGCOMPAT_H
