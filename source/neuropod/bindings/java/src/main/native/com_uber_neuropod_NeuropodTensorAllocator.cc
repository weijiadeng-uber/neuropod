/* Copyright (c) 2020 UATC, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

#include "com_uber_neuropod_NeuropodTensorAllocator.h"

#include "neuropod/neuropod.hh"
#include "utils.h"
#include "jclass_register.h"
#include "utils.h"

#include <jni.h>

const int FLOAT  = 0;
const int DOUBLE = 1;
const int INT    = 2;
const int LONG   = 3;

using namespace neuropod::jni;

JNIEXPORT void JNICALL Java_com_uber_neuropod_NeuropodTensorAllocator_nativeDelete(JNIEnv *env, jobject, jlong handle)
{
    try
    {
        auto allocator = reinterpret_cast<std::shared_ptr<neuropod::NeuropodTensorAllocator> *>(handle);
        (*allocator).reset();
        delete allocator;
    }
    catch (const std::exception &e)
    {
        neuropod::jni::throwJavaException(env, e.what());
    }
}

JNIEXPORT jlong JNICALL Java_com_uber_neuropod_NeuropodTensorAllocator_nativeAllocate
  (JNIEnv * env, jclass, jlongArray dims , jint typeNumber, jobject buffer, jlong handle) {
    try
    {
    auto allocator = *reinterpret_cast<std::shared_ptr<neuropod::NeuropodTensorAllocator> *>(handle);

        // Prepare shape
        jsize                size = env->GetArrayLength(dims);
        jlong *              arr  = env->GetLongArrayElements(dims, 0);
        std::vector<int64_t> shapes(arr, arr + size);
        env->ReleaseLongArrayElements(dims, arr, JNI_ABORT);
        // Prepare Buffer
        auto globalBufferRef = env->NewGlobalRef(buffer);
        auto bufferAddress = env->GetDirectBufferAddress(buffer);
        const neuropod::Deleter deleter         = [globalBufferRef, env](void *unused) mutable { env->DeleteGlobalRef(globalBufferRef); };
        std::shared_ptr<neuropod::NeuropodValue> tensor;
        switch (typeNumber)
        {
        case INT: {
            tensor = allocator->tensor_from_memory<int32_t>(shapes, reinterpret_cast<int32_t *>(bufferAddress), deleter);
            break;
        }
        case LONG: {
            tensor = allocator->tensor_from_memory<int64_t>(shapes, reinterpret_cast<int64_t *>(bufferAddress), deleter);
            break;
        }
        case FLOAT: {
            tensor = allocator->tensor_from_memory<float>(shapes, reinterpret_cast<float *>(bufferAddress), deleter);
            break;
        }
        case DOUBLE: {
            tensor = allocator->tensor_from_memory<double>(shapes, reinterpret_cast<double *>(bufferAddress), deleter);
            break;
        }
        default:
            throw std::runtime_error("Unsupported tensor type!");
        }
        return reinterpret_cast<jlong>(toHeap(std::move(tensor)));

    }
    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return reinterpret_cast<jlong>(nullptr);

  }


JNIEXPORT jlong JNICALL Java_com_uber_neuropod_NeuropodTensorAllocator_nativeCreateStringTensor
  (JNIEnv * env, jclass, jobject data, jlongArray dims, jlong allocatorHandle) {
    try
    {
    auto allocator = *reinterpret_cast<std::shared_ptr<neuropod::NeuropodTensorAllocator> *>(allocatorHandle);

        // Prepare shape
        jsize                shapeSize = env->GetArrayLength(dims);
        jlong *              arr  = env->GetLongArrayElements(dims, 0);
        std::vector<int64_t> shapes(arr, arr + shapeSize);
        env->ReleaseLongArrayElements(dims, arr, JNI_ABORT);
        auto tensor = allocator->allocate_tensor<std::string>(shapes);
        jlong                                    size   = env->CallIntMethod(data, java_util_ArrayList_size);
        auto                                     accessor = tensor->accessor<1>();
        for (jlong i = 0; i < size; i++)
        {
            jstring element = static_cast<jstring>(env->CallObjectMethod(data, java_util_ArrayList_get, i));
            accessor.operator[](i) = toString(env, element);
            env->DeleteLocalRef(element);
        }
        std::shared_ptr<neuropod::NeuropodValue> ret    = tensor;
        return reinterpret_cast<jlong>(toHeap(ret));
    }
    catch (const std::exception &e)
    {
        throwJavaException(env, e.what());
    }
    return reinterpret_cast<jlong>(nullptr);
  }
