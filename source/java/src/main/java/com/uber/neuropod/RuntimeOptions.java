package com.uber.neuropod;

/**
 * An One to One mapping to RuntimeOptions native class.
 * TODO: Implement this class.
 */
public class RuntimeOptions  {

    /**
     * The Free memory every cycle.
     */
    private boolean freeMemoryEveryCycle = true;
    /**
     * The Control queue name.
     */
    private String controlQueueName = "";
    /**
     * The Visible device.
     */
    private NeuropodDevice visibleDevice = NeuropodDevice.GPU0;
    /**
     * The Load model at construction.
     */
    private boolean loadModelAtConstruction = true;
    /**
     * The Disable shape and type checking.
     */
    private boolean disableShapeAndTypeChecking = false;

    /**
     * Instantiates a new Runtime options.
     */
    public RuntimeOptions() {
    }

    /**
     * To native runtime options native.
     *
     * @return the runtime options native
     */
    RuntimeOptionsNative toNative() {
        return new RuntimeOptionsNative(freeMemoryEveryCycle, controlQueueName, visibleDevice, loadModelAtConstruction, disableShapeAndTypeChecking);
    }

    /**
     * Is free memory every cycle boolean.
     *
     * @return the boolean
     */
    public boolean isFreeMemoryEveryCycle() {
        return freeMemoryEveryCycle;
    }

    /**
     * Sets free memory every cycle.
     *
     * @param freeMemoryEveryCycle the free memory every cycle
     */
    public void setFreeMemoryEveryCycle(boolean freeMemoryEveryCycle) {
        this.freeMemoryEveryCycle = freeMemoryEveryCycle;
    }

    /**
     * Gets control queue name.
     *
     * @return the control queue name
     */
    public String getControlQueueName() {
        return controlQueueName;
    }

    /**
     * Sets control queue name.
     *
     * @param controlQueueName the control queue name
     */
    public void setControlQueueName(String controlQueueName) {
        this.controlQueueName = controlQueueName;
    }

    /**
     * Gets visible device.
     *
     * @return the visible device
     */
    public NeuropodDevice getVisibleDevice() {
        return visibleDevice;
    }

    /**
     * Sets visible device.
     *
     * @param visibleDevice the visible device
     */
    public void setVisibleDevice(NeuropodDevice visibleDevice) {
        this.visibleDevice = visibleDevice;
    }

    /**
     * Is load model at construction boolean.
     *
     * @return the boolean
     */
    public boolean isLoadModelAtConstruction() {
        return loadModelAtConstruction;
    }

    /**
     * Sets load model at construction.
     *
     * @param loadModelAtConstruction the load model at construction
     */
    public void setLoadModelAtConstruction(boolean loadModelAtConstruction) {
        this.loadModelAtConstruction = loadModelAtConstruction;
    }

    /**
     * Is disable shape and type checking boolean.
     *
     * @return the boolean
     */
    public boolean isDisableShapeAndTypeChecking() {
        return disableShapeAndTypeChecking;
    }

    /**
     * Sets disable shape and type checking.
     *
     * @param disableShapeAndTypeChecking the disable shape and type checking
     */
    public void setDisableShapeAndTypeChecking(boolean disableShapeAndTypeChecking) {
        this.disableShapeAndTypeChecking = disableShapeAndTypeChecking;
    }

    /**
     * The corresponding native type for RuntimeOptions. Is not exposed to user, only used inside other public
     * method.
     */
    static class RuntimeOptionsNative extends NativeClass {
        static {
            LibraryLoader.load();
        }
        /**
         * Instantiates a new Runtime options native.
         *
         * @param freeMemoryEveryCycle        the free memory every cycle
         * @param controlQueueName            the control queue name
         * @param visibleDevice               the visible device
         * @param loadModelAtConstruction     the load model at construction
         * @param disableShapeAndTypeChecking the disable shape and type checking
         */
        RuntimeOptionsNative(boolean freeMemoryEveryCycle, String controlQueueName, NeuropodDevice visibleDevice, boolean loadModelAtConstruction, boolean disableShapeAndTypeChecking) {
            super(nativeCreate(freeMemoryEveryCycle, controlQueueName, visibleDevice.getValue(), loadModelAtConstruction, disableShapeAndTypeChecking));
        }

        static private native long nativeCreate(boolean freeMemoeryEverySycle, String controlQueueName, int visibleDevice, boolean loadModelAtConstruction, boolean disableShapeAndTypeChecking);

        @Override
        protected native void nativeDelete(long handle);
    }
}
