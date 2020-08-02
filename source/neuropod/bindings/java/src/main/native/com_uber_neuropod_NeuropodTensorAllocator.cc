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
<<<<<<< HEAD
#include "utils.h"

#include <jni.h>

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
=======

#include <jni.h>

JNIEXPORT void JNICALL Java_com_uber_neuropod_NeuropodTensorAllocator_nativeDelete(JNIEnv *, jobject, jlong handle)
{
    auto allocator = reinterpret_cast<std::shared_ptr<neuropod::NeuropodTensorAllocator> *>(handle);
    (*allocator).reset();
    delete allocator;
>>>>>>> 65437be... neuropod load model
}
