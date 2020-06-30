#include "org_neuropod_NeuropodValue.h"

#include "jclass_register.h"
#include "reference_manager.h"
#include "utils.h"

#include <neuropod/neuropod.hh>

#include <string>

#include <jni.h>

using namespace neuropod::jni;

const int FLOAT = 0;
const int DOUBLE = 1;
const int INT = 2;
const int LONG = 3;
const int STRING = 5;

JNIEXPORT jobject JNICALL Java_org_neuropod_NeuropodValue_nativeToList(JNIEnv *env, jclass, jlong nativeHandle) {
    // This function copy the data twice, first from neuropod to cpp vector, then from cpp vector to java list.
    // This is not very efficient.
    try {
        ReferenceManager<neuropod::NeuropodValue>::check(nativeHandle);
        auto neuropodTensor = reinterpret_cast<neuropod::NeuropodValue *>(nativeHandle)->as_tensor();
        auto size = neuropodTensor->get_num_elements();
        auto tensorType = neuropodTensor->get_tensor_type();

        switch (tensorType) {
            case neuropod::FLOAT_TENSOR: {
                auto typedTensor = neuropodTensor->as_typed_tensor<float>();
                auto rawPtr = typedTensor->get_raw_data_ptr();
                jobject ret = env->CallStaticObjectMethod(java_nio_FloatBuffer, java_nio_FloatBuffer_allocate, size);
                jfloatArray floatArray = static_cast<jfloatArray>(env->CallObjectMethod(ret,
                                                                                        java_nio_FloatBuffer_array));
                float *body = reinterpret_cast<float *>(env->GetFloatArrayElements(floatArray, 0));
                memcpy(body, rawPtr, sizeof(float) * size);
                env->ReleaseFloatArrayElements(floatArray, body, 0);
                return ret;
            }
            case neuropod::DOUBLE_TENSOR: {
                auto typedTensor = neuropodTensor->as_typed_tensor<double>();
                auto rawPtr = typedTensor->get_raw_data_ptr();
                jobject ret = env->CallStaticObjectMethod(java_nio_DoubleBuffer, java_nio_DoubleBuffer_allocate, size);
                jdoubleArray doubleArray = static_cast<jdoubleArray>(env->CallObjectMethod(ret,
                                                                                           java_nio_DoubleBuffer_array));
                double *body = reinterpret_cast<double *>(env->GetDoubleArrayElements(doubleArray, 0));
                memcpy(body, rawPtr, sizeof(double) * size);
                env->ReleaseDoubleArrayElements(doubleArray, body, 0);
                return ret;
            }
            case neuropod::INT32_TENSOR: {
                auto typedTensor = neuropodTensor->as_typed_tensor<int32_t>();
                auto rawPtr = typedTensor->get_raw_data_ptr();
                jobject ret = env->CallStaticObjectMethod(java_nio_IntBuffer, java_nio_IntBuffer_allocate, size);
                jintArray intArray = static_cast<jintArray>(env->CallObjectMethod(ret, java_nio_IntBuffer_array));
                int32_t *body = reinterpret_cast<int32_t *>(env->GetIntArrayElements(intArray, 0));
                memcpy(body, rawPtr, sizeof(int32_t) * size);
                env->ReleaseIntArrayElements(intArray, body, 0);
                return ret;
            }
            case neuropod::INT64_TENSOR: {
                auto typedTensor = neuropodTensor->as_typed_tensor<int64_t>();
                auto rawPtr = typedTensor->get_raw_data_ptr();
                jobject ret = env->CallStaticObjectMethod(java_nio_LongBuffer, java_nio_LongBuffer_allocate, size);
                jlongArray longArray = static_cast<jlongArray>(env->CallObjectMethod(ret, java_nio_LongBuffer_array));
                int64_t *body = reinterpret_cast<int64_t *>(env->GetLongArrayElements(longArray, 0));
                memcpy(body, rawPtr, sizeof(int64_t) * size);
                env->ReleaseLongArrayElements(longArray, reinterpret_cast<jlong *>(body), 0);
                return ret;
            }
            case neuropod::STRING_TENSOR: {
                jobject ret = env->NewObject(java_util_ArrayList, java_util_ArrayList_, size);
                auto typedTensor = neuropodTensor->as_typed_tensor<std::string>();
                auto elementList = typedTensor->get_data_as_vector();
                for (auto const &ele: elementList) {
                    jstring convertedEle = env->NewStringUTF(ele.c_str());
                    env->CallBooleanMethod(ret, java_util_ArrayList_add, convertedEle);
                    env->DeleteLocalRef(convertedEle);
                }
                return ret;
            }

            default:
                throw std::runtime_error(std::string("Unsupported tensor type:") + tensorTypeToString(tensorType));
        }
    }
    catch (const std::exception &e) {
        throwJavaException(env, e.what());
    }
    return nullptr;
}

JNIEXPORT jlong JNICALL Java_org_neuropod_NeuropodValue_nativeCreate(
        JNIEnv *env, jclass, jobject data, jobject dims, jint typeNumer, jlong modelHandle) {
    try {
        ReferenceManager<neuropod::Neuropod>::check(modelHandle);
        // Prepare allocator
        auto model = reinterpret_cast<neuropod::Neuropod *>(modelHandle);
        auto allocator = model->get_tensor_allocator();

        // Prepare shape
        jlong size = env->CallIntMethod(dims, java_util_ArrayList_size);
        std::vector<int64_t> shapes(size);
        for (int i = 0; i < size; i++) {
            jobject element = env->CallObjectMethod(dims, java_util_ArrayList_get, i);
            shapes[i] = env->CallLongMethod(element, java_lang_Long_longValue);
            env->DeleteLocalRef(element);
        }

        switch (typeNumer) {
            case INT: {
                auto intArray = static_cast<jintArray>(env->CallObjectMethod(data, java_nio_IntBuffer_array));
                jsize len = env->GetArrayLength(intArray);
                jint *body = env->GetIntArrayElements(intArray, 0);
                auto tensor = allocator->allocate_tensor<int32_t>(shapes);
                tensor->copy_from(body, len);
                ReferenceManager<neuropod::NeuropodValue>::put(tensor);
                return reinterpret_cast<jlong>(tensor.get());
            }
            case LONG: {
                auto longArray = static_cast<jlongArray>(env->CallObjectMethod(data, java_nio_LongBuffer_array));
                jsize len = env->GetArrayLength(longArray);
                int64_t *body = reinterpret_cast<int64_t *>(env->GetLongArrayElements(longArray, 0));
                auto tensor = allocator->allocate_tensor<int64_t>(shapes);
                tensor->copy_from(body, len);
                ReferenceManager<neuropod::NeuropodValue>::put(tensor);
                return reinterpret_cast<jlong>(tensor.get());
            }
            case FLOAT: {
                auto floatArray = static_cast<jfloatArray>(env->CallObjectMethod(data, java_nio_FloatBuffer_array));
                jsize len = env->GetArrayLength(floatArray);
                float *body = env->GetFloatArrayElements(floatArray, 0);
                auto tensor = allocator->allocate_tensor<float>(shapes);
                tensor->copy_from(body, len);
                ReferenceManager<neuropod::NeuropodValue>::put(tensor);
                return reinterpret_cast<jlong>(tensor.get());
            }
            case DOUBLE: {
                auto doubleArray = static_cast<jdoubleArray>(env->CallObjectMethod(data, java_nio_DoubleBuffer_array));
                jsize len = env->GetArrayLength(doubleArray);
                double *body = env->GetDoubleArrayElements(doubleArray, 0);
                auto tensor = allocator->allocate_tensor<double>(shapes);
                tensor->copy_from(body, len);
                ReferenceManager<neuropod::NeuropodValue>::put(tensor);
                return reinterpret_cast<jlong>(tensor.get());
            }
            case STRING: {
                jlong size = env->CallIntMethod(data, java_util_ArrayList_size);
                std::vector<std::string> intermediate;
                for (jlong i = 0; i < size; i++) {
                    jstring element = static_cast<jstring>( env->CallObjectMethod(data, java_util_ArrayList_get, i));
                    intermediate.emplace_back(toString(env, element));
                    env->DeleteLocalRef(element);
                }
                auto tensor = allocator->allocate_tensor<std::string>(shapes);
                tensor->copy_from(intermediate);
                ReferenceManager<neuropod::NeuropodValue>::put(tensor);
                return reinterpret_cast<jlong>(tensor.get());

            }
            default:
                throw std::runtime_error("Unsupported tensor type!");
        }
    }
    catch (const std::exception &e) {
        throwJavaException(env, e.what());
    }
    return reinterpret_cast<jlong>(nullptr);
}

JNIEXPORT jobject JNICALL Java_org_neuropod_NeuropodValue_nativeGetDims(JNIEnv *env, jclass, jlong handle) {
    try {
        ReferenceManager<neuropod::NeuropodValue>::check(handle);
        auto tensor = reinterpret_cast<neuropod::NeuropodValue *>(handle);
        auto dims = tensor->as_tensor()->get_dims();
        jobject ret = env->NewObject(java_util_ArrayList, java_util_ArrayList_, dims.size());
        for (int64_t dim : dims) {
            jobject javaEle = env->CallStaticObjectMethod(java_lang_Long, java_lang_Long_valueOf, dim);
            env->CallBooleanMethod(ret, java_util_ArrayList_add, javaEle);
            env->DeleteLocalRef(javaEle);
        }
        return ret;
    }

    catch (const std::exception &e) {
        throwJavaException(env, e.what());
    }
    return nullptr;
}

JNIEXPORT jobject JNICALL Java_org_neuropod_NeuropodValue_nativeGetTensorType(JNIEnv *env, jclass, jlong handle) {
    try {
        ReferenceManager<neuropod::NeuropodValue>::check(handle);
        auto tensor = reinterpret_cast<neuropod::NeuropodValue *>(handle);
        auto type = tensor->as_tensor()->get_tensor_type();
        return getFieldObject(env, org_neuropod_TensorType, tensorTypeToString(type).c_str());
    }
    catch (const std::exception &e) {
        throwJavaException(env, e.what());
    }
    return nullptr;
}

JNIEXPORT void JNICALL Java_org_neuropod_NeuropodValue_nativeDelete(JNIEnv *, jobject, jlong nativeHandle) {
    ReferenceManager<neuropod::NeuropodValue>::remove(nativeHandle);
}