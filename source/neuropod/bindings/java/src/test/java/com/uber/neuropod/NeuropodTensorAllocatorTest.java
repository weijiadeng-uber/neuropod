package com.uber.neuropod;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class NeuropodTensorAllocatorTest {
    private NeuropodTensorAllocator allocator;
    private static final double EPSILON = 1E-6;

    @Before
    public void setUp() throws Exception {
        allocator = Neuropod.getGenericTensorAllocator();
    }

    @Test
    public void createInt() {
        IntBuffer intBuffer = IntBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            intBuffer.put(i);
        }
        intBuffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(intBuffer, shape);
        assertArrayEquals(tensor.getDims(), shape);
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getInt(i, j), intBuffer.get(i*2 + j));
            }
        }
    }

    @Test
    public void createLong() {
        LongBuffer longBuffer = LongBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            longBuffer.put(i);
        }
        longBuffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(longBuffer, shape);
        assertArrayEquals(tensor.getDims(), shape);
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getLong(i, j), longBuffer.get(i*2 + j));
            }
        }
    }

    @Test
    public void testCreateDouble() {
        DoubleBuffer doubleBuffer = DoubleBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            doubleBuffer.put(i);
        }
        doubleBuffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(doubleBuffer, shape);
        assertArrayEquals(tensor.getDims(), shape);
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getDouble(i, j), doubleBuffer.get(i*2 + j), EPSILON);
            }
        }
    }

    @Test
    public void testCreateFloat() {
        FloatBuffer floatBuffer = FloatBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            floatBuffer.put(i);
        }
        floatBuffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(floatBuffer, shape);
        assertArrayEquals(tensor.getDims(), shape);
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getFloat(i, j), floatBuffer.get(i*2 + j), EPSILON);
            }
        }
    }

    @Test
    public void testCreateDirectByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * TensorType.INT32_TENSOR.getElementByteSize()).order(ByteOrder.nativeOrder());
        for (int i=0; i<4; i++) {
            buffer.asIntBuffer().put(i);
        }
        buffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(buffer, shape, TensorType.INT32_TENSOR);
        assertArrayEquals(tensor.getDims(), shape);
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getInt(i, j), buffer.asIntBuffer().get(i*2 + j), EPSILON);
            }
        }

    }

    @Test
    public void testCreateByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(4 * TensorType.INT32_TENSOR.getElementByteSize());
        for (int i=0; i<4; i++) {
            buffer.asIntBuffer().put(i);
        }
        buffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(buffer, shape, TensorType.INT32_TENSOR);
        assertArrayEquals(tensor.getDims(), shape);
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getInt(i, j), buffer.asIntBuffer().get(i*2 + j), EPSILON);
            }
        }
    }

    @Test
    public void testCreateString() {

    }


    @After
    public void tearDown() throws Exception {
        allocator.close();
    }
}
