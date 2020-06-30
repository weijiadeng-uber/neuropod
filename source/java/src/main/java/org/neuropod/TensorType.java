package org.neuropod;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an one to one mapping to cpp TensorType enum
 */
public enum TensorType {
    /**
     * The Float tensor.
     */
// Supported Types
    FLOAT_TENSOR(0),
    /**
     * Double tensor data type.
     */
    DOUBLE_TENSOR(1),
    /**
     * Int 32 tensor data type.
     */
    INT32_TENSOR(2),
    /**
     * Int 64 tensor data type.
     */
    INT64_TENSOR(3),
    /**
     * String tensor data type.
     */
    STRING_TENSOR(4),

    /**
     * The Int 8 tensor.
     */
// UnSupported Types, the types below are not supported by the java api
    INT8_TENSOR(5),
    /**
     * Int 16 tensor data type.
     */
    INT16_TENSOR(6),
    /**
     * Uint 8 tensor data type.
     */
    UINT8_TENSOR(7),
    /**
     * Uint 16 tensor data type.
     */
    UINT16_TENSOR(8),
    /**
     * Uint 32 tensor data type.
     */
    UINT32_TENSOR(9),
    /**
     * Uint 64 tensor data type.
     */
    UINT64_TENSOR(10);

    // These helper functions are for support int to enum and enum to int conversion
    private int value;
    private static Map map = new HashMap<>();

    private TensorType(int value) {
        this.value = value;
    }

    static {
        for (TensorType tensorType : TensorType.values()) {
            map.put(tensorType.value, tensorType);
        }
    }

    public static TensorType valueOf(int dataType) {
        return (TensorType) map.get(dataType);
    }

    public int getValue() {
        return value;
    }

    }
