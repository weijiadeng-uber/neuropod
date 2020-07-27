package com.uber.neuropod;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class NeuropodTest {
    private Neuropod model;
    private static final String MODEL_PATH = "neuropod/tests/test_data/tf_addition_model/";

    @Before
    public void setUp() throws Exception {
        LibraryLoader.load();
        LibraryLoader.setTestMode(true);
        RuntimeOptions opt = new RuntimeOptions();
        opt.useOpe = true;
        model = new Neuropod(MODEL_PATH);
    }

    @Test
    public void testCloseed() {
        model.close();
        try {
            model.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        try {
            model.getName();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void getName() {
        String name = model.getName();
        assertEquals("Keras double addition model", name);
    }

    @Test
    public void getPlatform() {
        String platform = model.getPlatform();
        assertEquals("tensorflow", platform);
    }

    @Test
    public void getInputs() {
        Set<TensorSpec> inputs = new HashSet<>(model.getInputs());
        Set<TensorSpec> expected = new HashSet<>(Arrays.asList(
                new TensorSpec("request_location_latitude", TensorType.FLOAT_TENSOR,
                        Arrays.asList(new Dimension("batch_size"), new Dimension(1))),
                new TensorSpec("request_location_longitude", TensorType.FLOAT_TENSOR,
                        Arrays.asList(new Dimension("batch_size"), new Dimension(1)))));
        assertEquals(inputs, expected);
    }

    @Test
    public void getOutputs() {
        Set<TensorSpec> outputs = new HashSet<>(model.getOutputs());
        Set<TensorSpec> expected = new HashSet<>(Arrays.asList(
                new TensorSpec("request_location_lat_plus_lng", TensorType.FLOAT_TENSOR,
                        Arrays.asList(new Dimension("batch_size"), new Dimension(1)))));
        assertEquals(outputs, expected);
    }

    @Test
    public void loadModel() {
        RuntimeOptions ope = new RuntimeOptions();
        ope.loadModelAtConstruction = false;
        try (Neuropod model = new Neuropod(MODEL_PATH, ope)) {
            String name = model.getName();
            assertEquals("Keras double addition model", name);
        }
    }

    @Test
    public void getTensorAllocator() {
        try {
            NeuropodTensorAllocator allocator = model.getTensorAllocator();
            allocator.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }

    }

    @Test
    public void getGenericTensorAllocator() {
        try {
            NeuropodTensorAllocator allocator = Neuropod.getGenericTensorAllocator();
            allocator.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }


    @After
    public void tearDown() throws Exception {
        model.close();
    }
}
