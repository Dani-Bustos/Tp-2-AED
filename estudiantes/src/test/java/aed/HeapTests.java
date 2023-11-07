package aed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class HeapTests {

    /*@Timeout(value=100, unit=TimeUnit.MILLISECONDS)
    @Test
    public void randomGrande() {
        Random rand = new Random();
        int[] test = new int[(int)100000000];
        for (int i = 0; i < test.length; i++) {
            test[i] = rand.nextInt((int)10);
        }
        Heap res = new Heap(test);
        System.out.println(res.cardinal());
    }*/

    @Test
    public void prueba() {
        
    }


}