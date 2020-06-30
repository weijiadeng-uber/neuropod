package org.neuropod;

import java.util.HashMap;
import java.util.Map;

/**
 * The enum Neuropod device.
 */
public enum NeuropodDevice {
    /**
     * Cpu neuropod device.
     */
    CPU(-1),
    /**
     * Gpu 0 neuropod device.
     */
    GPU0(0),
    /**
     * Gpu 1 neuropod device.
     */
    GPU1(1),
    /**
     * Gpu 2 neuropod device.
     */
    GPU2(2),
    /**
     * Gpu 3 neuropod device.
     */
    GPU3(3),
    /**
     * Gpu 4 neuropod device.
     */
    GPU4(4),
    /**
     * Gpu 5 neuropod device.
     */
    GPU5(5),
    /**
     * Gpu 6 neuropod device.
     */
    GPU6(6),
    /**
     * Gpu 7 neuropod device.
     */
    GPU7(7);

    // These helper functions are for support int to enum and enum to int conversion
    private int value;
    private static Map map = new HashMap<>();

    private NeuropodDevice(int value) {
        this.value = value;
    }

    static {
        for (NeuropodDevice deviceType : NeuropodDevice.values()) {
            map.put(deviceType.value, deviceType);
        }
    }

    /**
     * Value of neuropod device.
     *
     * @param deviceType the device type
     * @return the neuropod device
     */
    public static NeuropodDevice valueOf(int deviceType) {
        return (NeuropodDevice) map.get(deviceType);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

}
