#include <jni.h>
#include "com_uber_neuropod_NeuropodTensor.h"
#include "neuropod/neuropod.hh"
#include "utils.h"
#include "jclass_register.h"
#include <memory>

using namespace neuropod::jni;


JNIEXPORT void JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeDoDelete
  (JNIEnv *, jobject, jlong handle) {
    auto tensor = reinterpret_cast<std::shared_ptr<neuropod::NeuropodValue> *>(handle);
    (*tensor).reset();
    delete tensor;
}

JNIEXPORT jobject JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetBuffer
  (JNIEnv *env, jclass, jlong nativeHandle) {
    try
    {
        auto neuropodTensor = (*reinterpret_cast<std::shared_ptr<neuropod::NeuropodValue> *>(nativeHandle))->as_tensor();
        auto tensorType     = neuropodTensor->get_tensor_type();
        switch (tensorType)
        {
        case neuropod::FLOAT_TENSOR: {
            return createDirectBuffer<float>(env, neuropodTensor);
        }
        case neuropod::DOUBLE_TENSOR: {
            return createDirectBuffer<double>(env, neuropodTensor);
        }
        case neuropod::INT32_TENSOR: {
            return createDirectBuffer<int32_t>(env, neuropodTensor);
        }
        case neuropod::INT64_TENSOR: {
            return createDirectBuffer<int64_t>(env, neuropodTensor);
        }
        default:
            throw std::runtime_error(std::string("Unsupported tensor type: ") + tensorTypeToString(tensorType));
        }
    }
    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return nullptr;
  }


JNIEXPORT jobject JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeToStringList
  (JNIEnv *env, jclass, jlong nativeHandle) {
    try
    {
        auto neuropodTensor = (*reinterpret_cast<std::shared_ptr<neuropod::NeuropodValue> *>(nativeHandle))->as_tensor();
        auto size           = neuropodTensor->get_num_elements();
        auto tensorType     = neuropodTensor->get_tensor_type();

        switch (tensorType)
        {
        case neuropod::STRING_TENSOR: {
            // This function copy data twice
            jobject ret         = env->NewObject(java_util_ArrayList, java_util_ArrayList_, size);
            auto    typedTensor = neuropodTensor->as_typed_tensor<std::string>();
            auto    elementList = typedTensor->get_data_as_vector();
            for (auto const &ele : elementList)
            {
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
    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return nullptr;

  }

JNIEXPORT jlongArray JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetDims
  (JNIEnv *env, jclass, jlong handle)
{
    try
    {
        auto tensor = (*reinterpret_cast<std::shared_ptr<neuropod::NeuropodValue> *>(handle))->as_tensor();
        auto       dims   = tensor->as_tensor()->get_dims();
        jlongArray result = env->NewLongArray(dims.size());
        if (result == NULL)
        {
            throw std::runtime_error("Out of memory!");
        }
        env->SetLongArrayRegion(result, 0, dims.size(), reinterpret_cast<jlong *>(dims.data()));
        return result;
    }

    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return nullptr;
}


JNIEXPORT jobject JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetTensorType
  (JNIEnv *env, jclass, jlong handle) {
    try
    {
        auto tensor = (*reinterpret_cast<std::shared_ptr<neuropod::NeuropodValue> *>(handle))->as_tensor();
        auto type   = tensor->as_tensor()->get_tensor_type();
        return getFieldObject(env, com_uber_neuropod_TensorType, tensorTypeToString(type).c_str());
    }
    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return nullptr;

  }



JNIEXPORT jlong JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetNumberOfElements
  (JNIEnv *env, jclass, jlong handle)
{
    try
    {
        auto tensor = (*reinterpret_cast<std::shared_ptr<neuropod::NeuropodValue> *>(handle))->as_tensor();
        return tensor->get_num_elements();
    }
    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return 0;
}


JNIEXPORT jstring JNICALL Java_com_uber_neuropod_NeuropodTensor_nativeGetString
  (JNIEnv *env, jclass, jlong index, jlong handle)
  {
    try
    {
        auto stringTensor = (*reinterpret_cast<std::shared_ptr<neuropod::NeuropodValue> *>(handle))
                                ->as_tensor()
                                ->as_typed_tensor<std::string>();
        std::string ele;
        // TODO: Improve the effiency
        auto strList = stringTensor->get_data_as_vector();
        ele          = strList[index];
        return env->NewStringUTF(ele.c_str());
    }
    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return nullptr;
  }
