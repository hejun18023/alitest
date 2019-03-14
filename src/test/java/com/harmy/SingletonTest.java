package com.harmy;

import com.harmy.singleton.Singleton;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Unit test for Singleton.
 */
public class SingletonTest
{
    @Test
    public void testSingleton()
    {

        final Set<Integer> hashcodes = new HashSet<>();

        IntStream.range(0, 99).forEach(i ->{
            new Thread(() -> hashcodes.add(Singleton.getInstance().hashCode())).start();
        });
        Assert.assertTrue("singleton error.", hashcodes.size() != 1);

    }
}
