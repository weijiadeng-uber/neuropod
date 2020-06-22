package org.neuropod;

import java.util.HashMap;
import java.util.Map;

/**
 * A global reference counter to avoid double delete for certain native objects
 */
class ReferenceCounter {
    /**
     * The constant counter.
     */
    protected static Map<Long, Integer> counter = new HashMap<>();
}