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

#pragma once

#include <string>

#include "neuropod/neuropod.hh"

#include <jni.h>

namespace neuropod
{
namespace jni
{

// Convert jstring to cpp string
std::string toString(JNIEnv *env, jstring target);

// A wrapper for env->FindClass, will throw a cpp exception if the find fails.
jclass findClass(JNIEnv *env, const char *name);

// A wrapper for env->GetMethodID, will throw a cpp exception if the get fails.
jmethodID getMethodID(JNIEnv *env, jclass clazz, const char *name, const char *sig);

// A wrapper for env->GetStaticMethodID, will throw a cpp exception if the get fails.
jmethodID getStaticMethodID(JNIEnv *env, jclass clazz, const char *name, const char *sig);

// Get the name of a Java class
std::string getJclassName(JNIEnv *env, jclass clazz);

// Get the corresponding Java enum based on the given field name.
jobject getFieldObject(JNIEnv *env, jclass enumClass, std::string fieldName);

// Convert cpp tensor type to string
std::string tensorTypeToString(TensorType type);

// Throw a Java NeuropodJNIException exception. This throw is only valid for the Java side, in the cpp side it is just
// like a normal function: it won't interupt the normal program work flow.
void throwJavaException(JNIEnv *env, const char *message);

} // namespace jni
} // namespace neuropod
