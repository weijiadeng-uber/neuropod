/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_uber_neuropod_NeuropodTensor */

#ifndef _Included_com_uber_neuropod_NeuropodTensor
#define _Included_com_uber_neuropod_NeuropodTensor
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_uber_neuropod_NeuropodTensor
 * Method:    nativeDelete
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeDelete
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_uber_neuropod_NeuropodTensor
 * Method:    nativeGetBuffer
 * Signature: (J)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetBuffer
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_uber_neuropod_NeuropodTensor
 * Method:    nativeToStringList
 * Signature: (J)Ljava/util/List;
 */
JNIEXPORT jobject JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeToStringList
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_uber_neuropod_NeuropodTensor
 * Method:    nativeGetDims
 * Signature: (J)[J
 */
JNIEXPORT jlongArray JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetDims
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_uber_neuropod_NeuropodTensor
 * Method:    nativeGetTensorType
 * Signature: (J)Lcom/uber/neuropod/TensorType;
 */
JNIEXPORT jobject JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetTensorType
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_uber_neuropod_NeuropodTensor
 * Method:    nativeGetNumberOfElements
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetNumberOfElements
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_uber_neuropod_NeuropodTensor
 * Method:    nativeGetString
 * Signature: (JJ)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetString
  (JNIEnv *, jclass, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
