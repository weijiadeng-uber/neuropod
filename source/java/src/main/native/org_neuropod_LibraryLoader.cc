#include "org_neuropod_LibraryLoader.h"

#include <jni.h>

JNIEXPORT jboolean JNICALL Java_org_neuropod_LibraryLoader_nativeIsLoaded(JNIEnv *, jclass)
{
    return JNI_TRUE;
}