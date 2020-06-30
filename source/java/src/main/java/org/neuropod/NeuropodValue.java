package org.neuropod;

import java.io.Serializable;
import java.nio.*;
import java.util.List;

/**
 * This class is an one to one mapping to cpp NeuropodValue type.
 * Should not call the close function if this class does not have the ownership of the underlying native class.
 */
public class NeuropodValue extends NativeClass implements Serializable {

    /**
     * Instantiates a new Neuropod value map from existing cpp handle.
     * The native object the nativeHandle points to must have been created by the neuropod jni before, otherwise
     * it will be treated as a wild pointer and a NeuropodJNIException will be thrown in the java side if any calls
     * invoking native functions happens.
     *
     * @param nativeHandle the native handle
     */
    public NeuropodValue(long nativeHandle) {
        super(nativeHandle);
    }

    /**
     * Gets dims.
     *
     * @return the dims
     */
    public List<Long> getDims() {
        return nativeGetDims(super.getNativeHandle());
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @param model the model
     * @return the neuropod value
     */
    static NeuropodValue create(Object data,
                                       List<Long> shape,
                                       Neuropod model) {
        if (data instanceof IntBuffer) {
            return create((IntBuffer)data, shape, model);
        } else if (data instanceof LongBuffer) {
            return create((LongBuffer)data, shape, model);

        } else if (data instanceof DoubleBuffer) {
            return create((DoubleBuffer)data, shape, model);

        }else if (data instanceof FloatBuffer) {
            return create((FloatBuffer)data, shape, model);

        }else if (data instanceof List<?>) {
            // For String tensors only
            List<?> curData = (List<?>) data;
            if (!curData.isEmpty()) {
                Object obj = curData.get(0);
                if (!(obj instanceof String)) {
                    throw new NeuropodJNIException("Neuropod Exception: Unsupported input type!");
                }
            }

            return create((List<String>)data, shape, model);
        }
        throw new NeuropodJNIException("Neuropod Exception: Unsupported input type!");
    }

    /**
     * Allocate a neuropod value and copy java data to it based on the allocator of certain model.
     *
     * @param data  the data
     * @param shape the shape
     * @param model the neuropod model to allcoate the NeuropodValue in the cpp side
     * @return the al
     */
    static public NeuropodValue create(IntBuffer data,
                                       List<Long> shape,
                                       Neuropod model) {
        long retHandle = nativeCreate(data, shape, TensorType.INT32_TENSOR.getValue(), checkHandle(model));
        return new NeuropodValue(retHandle);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @param model the model
     * @return the neuropod value
     */
    static public NeuropodValue create(LongBuffer data,
                                       List<Long> shape,
                                       Neuropod model) {
        long retHandle = nativeCreate(data, shape, TensorType.INT64_TENSOR.getValue(),checkHandle(model));
        return new NeuropodValue(retHandle);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @param model the model
     * @return the neuropod value
     */
    static public NeuropodValue create(FloatBuffer data,
                                       List<Long> shape,
                                       Neuropod model) {
        long retHandle = nativeCreate(data, shape, TensorType.FLOAT_TENSOR.getValue(),checkHandle(model));
        return new NeuropodValue(retHandle);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @param model the model
     * @return the neuropod value
     */
    static public NeuropodValue create(DoubleBuffer data,
                                       List<Long> shape,
                                       Neuropod model) {

        long retHandle = nativeCreate(data, shape, TensorType.DOUBLE_TENSOR.getValue(),checkHandle(model));
        return new NeuropodValue(retHandle);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @param model the model
     * @return the neuropod value
     */
    static public NeuropodValue create(List<String> data,
                                       List<Long> shape,
                                       Neuropod model) {
        long retHandle = nativeCreate(data, shape, TensorType.STRING_TENSOR.getValue(), checkHandle(model));
        return new NeuropodValue(retHandle);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @return the neuropod value
     */
    static public NeuropodValue create(IntBuffer data,
                                       List<Long> shape) {
        return create(data, shape, null);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @return the neuropod value
     */
    static public NeuropodValue create(FloatBuffer data,
                                       List<Long> shape) {
        return create(data, shape, null);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @return the neuropod value
     */
    static public NeuropodValue create(DoubleBuffer data,
                                       List<Long> shape) {
        return create(data, shape, null);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @return the neuropod value
     */
    static public NeuropodValue create(LongBuffer data,
                                       List<Long> shape) {
        return create(data, shape, null);
    }

    /**
     * Create neuropod value.
     *
     * @param data  the data
     * @param shape the shape
     * @return the neuropod value
     */
    static public NeuropodValue create(List<String> data,
                                       List<Long> shape) {
        return create(data, shape, null);
    }

    /**
     * Gets tensor type.
     *
     * @return the tensor type
     */
    public TensorType getTensorType() {
        return nativeGetTensorType(super.getNativeHandle());
    }

    static private long checkHandle(Neuropod model) {
        long handle = 0;
        if(model!=null) {
            handle = model.getNativeHandle();
        }
        return handle;

    }

    static private void checkList(Object obj) {
        if (!(obj instanceof List<?>)) {
            throw new NeuropodJNIException("Return type is not list");
        }
    }

    /**
     * Copy all the data of the NeuropodValue and flatten it to a java list.
     * Does not apply for string tensor.
     *
     * @return the list
     */
    public Object toList() {
        return nativeToList(super.getNativeHandle());
    }

    /**
     * To int list int buffer.
     *
     * @return the int buffer
     */
    public IntBuffer toIntList() {
        TensorType type = getTensorType();
        if (type != TensorType.INT32_TENSOR) {
            throw new NeuropodJNIException("Only a int tensor can return a int list!");
        }
        return (IntBuffer)nativeToList(super.getNativeHandle());
    }

    /**
     * To long list long buffer.
     *
     * @return the long buffer
     */
    public LongBuffer toLongList() {
        TensorType type = getTensorType();
        if (type != TensorType.INT64_TENSOR) {
            throw new NeuropodJNIException("Only a long tensor can return a long list!");
        }
        return (LongBuffer)nativeToList(super.getNativeHandle());
    }

    /**
     * To float list float buffer.
     *
     * @return the float buffer
     */
    public FloatBuffer toFloatList() {
        TensorType type = getTensorType();
        if (type != TensorType.FLOAT_TENSOR) {
            throw new NeuropodJNIException("Only a float tensor can return a float list!");
        }
        return (FloatBuffer)nativeToList(super.getNativeHandle());
    }

    /**
     * To double list double buffer.
     *
     * @return the double buffer
     */
    public DoubleBuffer toDoubleList() {
        TensorType type = getTensorType();
        if (type != TensorType.DOUBLE_TENSOR) {
            throw new NeuropodJNIException("Only a double tensor can return a double list!");
        }
        return (DoubleBuffer) nativeToList(super.getNativeHandle());
    }

    /**
     * To string list list.
     *
     * @return the list
     */
    public List<String> toStringList() {
        TensorType type = getTensorType();
        if (type != TensorType.DOUBLE_TENSOR) {
            throw new NeuropodJNIException("Only a double tensor can return a double list!");
        }
        return (List<String>)nativeToList(super.getNativeHandle());
    }


    static native private Object nativeToList(long handle) throws NeuropodJNIException;

    static native private long nativeCreate(Object data, List<Long> dims, int typeNumber,long modelHandle) throws NeuropodJNIException;

    static native private List<Long> nativeGetDims(long modelHandle) throws NeuropodJNIException;

    static native private TensorType nativeGetTensorType(long modelHandle) throws NeuropodJNIException;


    /**
     * Native delete.
     *
     * @param handle the handle
     */
    @Override
    protected native void nativeDelete(long handle);
}
