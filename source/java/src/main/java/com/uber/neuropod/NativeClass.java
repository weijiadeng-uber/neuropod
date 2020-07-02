package com.uber.neuropod;

/**
 * This is a base class for all class with a binding to native class.
 * Need to call close() method after usage to free memory in C++ side.
 */
abstract class NativeClass implements AutoCloseable {
    // Load native library
    static {
        LibraryLoader.load();
    }

    // The pointer to the native object
    private Long nativeHandle_;

    /**
     * Instantiates a new Native class.
     */
    public NativeClass() {
    }

    /**
     * Instantiates a new Native class.
     *
     * @param nativeHandle the native handle
     */
    public NativeClass(long nativeHandle) {
        if (ReferenceCounter.counter.containsKey(nativeHandle_)) {
            ReferenceCounter.counter.put(nativeHandle_, ReferenceCounter.counter.get(nativeHandle_) + 1);
        } else {
            ReferenceCounter.counter.put(nativeHandle_, 1);
        }
        this.nativeHandle_ = nativeHandle;
    }

    /**
     * Gets native handle.
     *
     * @return the native handle
     */
    protected long getNativeHandle() {
        if (nativeHandle_ == null) {
            throw new NeuropodJNIException("Object is not allocated");
        }
        return nativeHandle_;
    }

    /**
     * Sets native handle.
     *
     * @param handle the handle
     */
    protected void setNativeHandle(long handle) {
        nativeHandle_ = handle;
        if (ReferenceCounter.counter.containsKey(nativeHandle_)) {
            ReferenceCounter.counter.put(nativeHandle_, ReferenceCounter.counter.get(nativeHandle_) + 1);
        } else {
            ReferenceCounter.counter.put(nativeHandle_, 1);
        }

    }

    /**
     * Delete the underlying native class
     *
     * @param handle the handle
     */
    abstract protected void nativeDelete(long handle);

    @Override
    public void close() throws Exception {
        // Wrap the nativeDelete to close method so that the IDE will have a warning
        // if the object is not deleted, and will be auto deleted in a try catch block.
        if (nativeHandle_ == null) {
            return;
        }
        // Check the reference counter to avoid delete early
        if (ReferenceCounter.counter.containsKey(nativeHandle_)) {
            ReferenceCounter.counter.put(nativeHandle_, ReferenceCounter.counter.get(nativeHandle_) - 1);
            if (ReferenceCounter.counter.get(nativeHandle_) == 0) {
                ReferenceCounter.counter.remove(nativeHandle_);
                nativeDelete(nativeHandle_);
            }
        }
        nativeHandle_ = null;
    }
}
