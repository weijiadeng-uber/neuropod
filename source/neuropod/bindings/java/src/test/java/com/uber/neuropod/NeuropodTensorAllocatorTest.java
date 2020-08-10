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
    public void createIntTensor() {
        IntBuffer intBuffer = IntBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            intBuffer.put(i);
        }
        intBuffer.rewind();
        long [] shape = new long[]{2,2};
        try (NeuropodTensor tensor = allocator.create(intBuffer, shape)) {
            assertArrayEquals(shape, tensor.getDims());
            assertEquals(4, tensor.getNumberOfElements());
            assertEquals(TensorType.INT32_TENSOR, tensor.getTensorType());

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    assertEquals(intBuffer.get(i * 2 + j), tensor.getInt(i, j));
                }
            }

            IntBuffer outputBuffer = tensor.toIntBuffer();
            for (int i=0; i<4; i++) {
                assertEquals(intBuffer.get(i), outputBuffer.get(i));
            }
        }
    }

    @Test
    public void createLongTensor() {
        LongBuffer longBuffer = LongBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            longBuffer.put(i);
        }
        longBuffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(longBuffer, shape);
        assertArrayEquals(shape, tensor.getDims());
        assertEquals(4, tensor.getNumberOfElements());
        assertEquals(TensorType.INT64_TENSOR, tensor.getTensorType());
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(longBuffer.get(i*2 + j), tensor.getLong(i, j));
            }
        }

        LongBuffer outputBuffer = tensor.toLongBuffer();
        for (int i=0; i<4; i++) {
            assertEquals(outputBuffer.get(i), outputBuffer.get(i));
        }
    }

    @Test
    public void testCreateDoubleTensor() {
        DoubleBuffer doubleBuffer = DoubleBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            doubleBuffer.put(i);
        }
        doubleBuffer.rewind();
        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(doubleBuffer, shape);
        assertArrayEquals(shape, tensor.getDims());
        assertEquals(4, tensor.getNumberOfElements());
        assertEquals(TensorType.DOUBLE_TENSOR, tensor.getTensorType());
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getDouble(i, j), doubleBuffer.get(i*2 + j), EPSILON);
            }
        }

        DoubleBuffer outputBuffer = tensor.toDoubleBuffer();
        for (int i=0; i<4; i++) {
            assertEquals(outputBuffer.get(i), outputBuffer.get(i), EPSILON);
        }
    }

    @Test
    public void testCreateFloatTensor() {
        FloatBuffer floatBuffer = FloatBuffer.allocate(4);
        for (int i=0; i<4; i++) {
            floatBuffer.put(i);
        }
        floatBuffer.rewind();

        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(floatBuffer, shape);
        assertArrayEquals(shape, tensor.getDims());
        assertEquals(4, tensor.getNumberOfElements());
        assertEquals(TensorType.FLOAT_TENSOR, tensor.getTensorType());
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getFloat(i, j), floatBuffer.get(i*2 + j), EPSILON);
            }
        }

        FloatBuffer outputBuffer = tensor.toFloatBuffer();
        for (int i=0; i<4; i++) {
            assertEquals(outputBuffer.get(i), outputBuffer.get(i), EPSILON);
        }
    }

    @Test
    public void testCreateFromDirectByteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * TensorType.INT32_TENSOR.getElementByteSize()).order(ByteOrder.nativeOrder());
        IntBuffer intBuffer = buffer.asIntBuffer();
        for (int i=0; i<4; i++) {
            intBuffer.put(i);
        }

        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(buffer, shape, TensorType.INT32_TENSOR);
        assertArrayEquals(shape, tensor.getDims());
        assertEquals(4, tensor.getNumberOfElements());
        assertEquals(TensorType.INT32_TENSOR, tensor.getTensorType());
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getInt(i, j), intBuffer.get(i*2 + j));
            }
        }

        IntBuffer outputBuffer = tensor.toIntBuffer();
        for (int i=0; i<4; i++) {
            assertEquals(outputBuffer.get(i), outputBuffer.get(i), EPSILON);
        }
    }

    @Test
    public void testCreateFromUndirectByteBufferTensor() {
        ByteBuffer buffer = ByteBuffer.allocate(4 * TensorType.INT32_TENSOR.getElementByteSize());
        IntBuffer intBuffer = buffer.asIntBuffer();
        for (int i=0; i<4; i++) {
            intBuffer.put(i);
        }

        long [] shape = new long[]{2,2};
        NeuropodTensor tensor = allocator.create(buffer, shape, TensorType.INT32_TENSOR);
        assertArrayEquals(shape, tensor.getDims());
        assertEquals(4, tensor.getNumberOfElements());
        assertEquals(TensorType.INT32_TENSOR, tensor.getTensorType());
        for (int i=0; i<2; i++) {
            for (int j=0; j<2; j++) {
                assertEquals(tensor.getInt(i, j), intBuffer.get(i*2 + j));
            }
        }

        IntBuffer outputBuffer = tensor.toIntBuffer();
        for (int i=0; i<4; i++) {
            assertEquals(outputBuffer.get(i), outputBuffer.get(i), EPSILON);
        }
    }

    @After
    public void tearDown() throws Exception {
        allocator.close();
    }
}
