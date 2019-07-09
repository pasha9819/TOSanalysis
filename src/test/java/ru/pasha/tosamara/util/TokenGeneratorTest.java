package ru.pasha.tosamara.util;

import org.junit.Assert;
import org.junit.Test;

public class TokenGeneratorTest {
    @Test
    public void shaTest(){
        Assert.assertEquals(
                "0c9e95e7b04233e147ca516c183bd45ccf037c22",
                TokenGenerator.getToken());
    }
}
