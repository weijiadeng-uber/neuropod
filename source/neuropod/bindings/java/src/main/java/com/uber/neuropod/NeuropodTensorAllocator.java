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

package com.uber.neuropod;

import java.nio.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The factory type for NeuropodTensor, can be obtained from a neuropod model or as a generic allocator.
 * Should call close method when finish using it.
 */
public class NeuropodTensorAllocator extends NativeClass {
    protected NeuropodTensorAllocator(long handle) {
        super(handle);
    }
    private static final Set<TensorType> SUPPORTED_TENSOR_TYPES = new HashSet<>(Arrays.asList(TensorType.INT32_TENSOR,TensorType.INT64_TENSOR,TensorType.DOUBLE_TENSOR,TensorType.FLOAT_TENSOR));

    /**
     * Create a NeuropodTensor based on given ByteBuffer, dims array and tensorType. The created tensor
     * will have the input tensorType
     * <p>
     * Will not trigger a copy if the buffer is a direct bytebuffer in native order and the backend of
     * the allocator does not have alignment requirement. Otherwise it will trigger a copy.
     *
     * @param byteBuffer the buffer that contains tensor data
     * @param dims       the shape of the tensor
     * @param tensorType the tensor type
     * @return the created NeuropodTensor
     */
    public NeuropodTensor copyFrom(ByteBuffer byteBuffer, long[] dims, TensorType tensorType) {
        if (!SUPPORTED_TENSOR_TYPES.contains(tensorType)) {
            throw new NeuropodJNIException("Unsupported tensor type!");
        }
        switch (tensorType) {
            case INT32_TENSOR : return copyFrom(byteBuffer.asIntBuffer(), dims);
            case INT64_TENSOR: return copyFrom(byteBuffer.asLongBuffer(), dims);
            case FLOAT_TENSOR: return copyFrom(byteBuffer.asFloatBuffer(), dims);
            case DOUBLE_TENSOR: return copyFrom(byteBuffer.asDoubleBuffer(), dims);
        }
        return null;
    }

    public NeuropodTensor tensorFromMemory(ByteBuffer byteBuffer, long[] dims, TensorType tensorType) {
        if (!SUPPORTED_TENSOR_TYPES.contains(tensorType)) {
            throw new NeuropodJNIException("unsupported tensor type: " + tensorType.name());
        }
        if (!byteBuffer.isDirect()) {
            throw new NeuropodJNIException("the input byteBuffer is not direct");
        }
        if (byteBuffer.order() != ByteOrder.nativeOrder()) {
            throw new NeuropodJNIException("the input byteBuffer is not in a native order");
        }
        if (byteBuffer.isReadOnly()) {
            throw new NeuropodJNIException("the input byteBuffer is read-only");
        }
        return createTensorFromBuffer(byteBuffer, dims, tensorType);
    }


    /**
     * Create a NeuropodTensor based on given LongBuffer, dims array. The created tensor
     * will have tensor type INT64_TENSOR.
     * <p>
     * Will trigger a copy.
     *
     * @param longBuffer the buffer that contains tensor data
     * @param dims       the shape of the tensor
     * @return the created NeuropodTensor
     */
    public NeuropodTensor copyFrom(LongBuffer longBuffer, long[] dims) {
        TensorType type =TensorType.INT64_TENSOR;
        ByteBuffer tensorBuffer = allocateJavaBuffer(dims, type);
        tensorBuffer.asLongBuffer().put(longBuffer);
        return createTensorFromBuffer(tensorBuffer, dims, type);
    }

    /**
     * Create a NeuropodTensor based on given IntBuffer, dims array. The created tensor
     * will have type INT32_TENSOR.
     * <p>
     * Will trigger a copy.
     *
     * @param intBuffer the buffer that contains tensor data
     * @param dims      the shape of the tensor
     * @return the created NeuropodTensor
     */
    public NeuropodTensor copyFrom(IntBuffer intBuffer, long[] dims) {
        TensorType type =TensorType.INT32_TENSOR;
        ByteBuffer tensorBuffer = allocateJavaBuffer(dims, type);
        tensorBuffer.asIntBuffer().put(intBuffer);
        return createTensorFromBuffer(tensorBuffer, dims, type);
    }

    /**
     * Create a NeuropodTensor based on given DoubleBuffer, dims array. The created tensor
     * will have type DOUBLE_TENSOR.
     * <p>
     * Will trigger a copy.
     *
     * @param doubleBuffer the buffer that contains tensor data
     * @param dims         the shape of the tensor
     * @return the created NeuropodTensor
     */
    public NeuropodTensor copyFrom(DoubleBuffer doubleBuffer, long[] dims) {
        TensorType type =TensorType.DOUBLE_TENSOR;
        ByteBuffer tensorBuffer = allocateJavaBuffer(dims, type);
        tensorBuffer.asDoubleBuffer().put(doubleBuffer);
        return createTensorFromBuffer(tensorBuffer, dims, type);
    }

    /**
     * Create a NeuropodTensor based on given FloatBuffer, dims array. The created tensor
     * will have type FLOAT_TENSOR.
     * <p>
     * Will trigger a copy.
     *
     * @param floatBuffer the buffer that contains tensor data
     * @param dims        the shape of the tensor
     * @return the neuropod NeuropodTensor
     */
    public NeuropodTensor copyFrom(FloatBuffer floatBuffer, long[] dims) {
        TensorType type =TensorType.FLOAT_TENSOR;
        ByteBuffer tensorBuffer = allocateJavaBuffer(dims, type);
        tensorBuffer.asFloatBuffer().put(floatBuffer);
        return createTensorFromBuffer(tensorBuffer, dims, type);
    }

    /**
     * Create a NeuropodTensor based on given string list, dims array. The created tensor
     * will have type STRING_TENSOR.
     * <p>
     * Will trigger a copy.
     *
     * @param stringList the string list
     * @param dims       the dims
     * @return the created NeuropodTensor
     */
    public NeuropodTensor copyFrom(List<String> stringList, long[] dims) {
        NeuropodTensor tensor = new NeuropodTensor();
        tensor.setNativeHandle(nativeCreateStringTensor(stringList, dims, super.getNativeHandle()));
        return tensor;
    }

    private static ByteBuffer allocateJavaBuffer(long[] dims, TensorType tensorType) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(
                (int)(tensorType.getElementByteSize() *
                        Arrays.stream(dims).reduce(1, (ele1, ele2) -> ele1*ele2)));
        return buffer.order(ByteOrder.nativeOrder());
    }

    private NeuropodTensor createTensorFromBuffer(ByteBuffer buffer, long[] dims, TensorType tensorType) {
        //buffer.rewind();
        NeuropodTensor tensor = new NeuropodTensor();
        tensor.buffer = buffer;
        tensor.setNativeHandle(nativeAllocate(dims, tensorType.getValue(), tensor.buffer, super.getNativeHandle()));
        return tensor;
    }

    private static native long nativeAllocate(long[] dims, int tensorType, ByteBuffer buffer, long allocatorHandle) throws NeuropodJNIException;

    private static native long nativeCreateStringTensor(List<String> data, long[] dims, long allocatorHandle) throws NeuropodJNIException;


    @Override
    protected native void nativeDelete(long handle) throws NeuropodJNIException;
}
