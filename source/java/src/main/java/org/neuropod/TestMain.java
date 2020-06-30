package org.neuropod;

import java.nio.FloatBuffer;
import java.util.*;

public class TestMain {

    public static void main(String[] args) throws Exception {
        System.out.println("Start working......");

        String modelPath = "src/test/resources/test.zip";
        System.out.println("Loading model = " + System.getProperty("user.dir") + modelPath);
        // Load a neuropod model
        Neuropod neuropod = new Neuropod(modelPath);
        Map<String, Object> inputs = new HashMap<>();

        FloatBuffer elements1 = FloatBuffer.allocate(2);
        elements1.put(1.0f);
        elements1.put(3.0f);

        inputs.put("request_location_latitude",elements1);

        FloatBuffer elements2 = FloatBuffer.allocate(2);
        elements2.put(2.0f);
        elements2.put(5.0f);
        inputs.put("request_location_longitude",elements2);

        System.out.println(neuropod.getInputs());

        System.out.println(neuropod.getOutpus());


        System.out.println("The input is");
        System.out.println(inputs);

        Map<String, List<Long>> shapes = new HashMap<>();
        shapes.put("request_location_latitude", Arrays.asList(2L, 1L));
        shapes.put("request_location_longitude", Arrays.asList(2L, 1L));



        NeuropodValueMap valueMap = neuropod.infer(inputs, shapes);

        List<String> keyList = valueMap.getKeyList();

        System.out.println("The output is");
        System.out.println(((FloatBuffer)valueMap.toJavaMap().get("request_location_lat_plus_lng")).array()[0]);
        neuropod.close();
        // Double delete test
        neuropod.close();

        valueMap.close();
    }

}
