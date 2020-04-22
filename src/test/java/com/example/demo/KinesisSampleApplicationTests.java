package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

class KinesisSampleApplicationTests {

    List<String> list = Arrays.asList("1D2S#10S","1D2S0T","1S*2T*3S","1D#2S*3S","1T2D3D#","1D2S3T*2");

    @Test
    void contextLoads() {
        String dartResult = "1S2D*3T";

        Pattern pattern = Pattern.compile("(^[0-9]+[SDT][*#]?$){3}");

        list.forEach(a -> System.out.println(Pattern.matches("^([0-9]+[SDT][*#]?){3}$", a)));

        String[] split = "1D2S#10S".split("^[0-9]+[SDT][*#]?$");
        Arrays.asList(split).forEach(a -> System.out.println(a));
    }

}
