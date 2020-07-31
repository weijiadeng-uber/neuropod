package com.uber.neuropod;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NeuropodTensorTest {

    @Before
    public void setUp() throws Exception {
        Map<String, NeuropodTensor> tensors = new HashMap<>();
        NeuropodTensorAllocator allocator = Neuropod.getGenericTensorAllocator();
        ByteBuffer buffer = new
        NeuropodTensor x = allocator.create(FloatBuffer.wrap(new float[]{1.0f, 3.0f}), new long[]{1L, 2L});
        tensors.put("x", x);
        tensors.put("y", allocator.create(FloatBuffer.wrap(new float[]{2.0f, 4.0f}), new long[]{1L, 2L}));
        Map<String, NeuropodTensor> res = model.infer(tensors);
        assertTrue(res.containsKey("out"));
        NeuropodTensor out = res.get("out");
        FloatBuffer buffer = out.toFloatBuffer();
        assertEquals(3.0f , buffer.get(0), 1E-6f);
        assertEquals(7.0f , buffer.get(1), 1E-6f);
        x.close();
        out.close();
    }

    @Test
    public void close() {
    }

    @Test
    public void getDims() {
    }

    @Test
    public void getNumberOfElements() {
    }

    @Test
    public void getTensorType() {
    }

    @Test
    public void toLongBuffer() {
    }

    @Test
    public void toIntBuffer() {
    }

    @Test
    public void toFloatBuffer() {
    }

    @Test
    public void toDoubleBuffer() {
    }

    @Test
    public void toStringList() {
    }

    @Test
    public void getInt() {
    }

    @Test
    public void getLong() {
    }

    @Test
    public void getDouble() {
    }

    @Test
    public void getFloat() {
    }

    @Test
    public void getString() {
    }

    @After
    public void tearDown() throws Exception {
    }
}
