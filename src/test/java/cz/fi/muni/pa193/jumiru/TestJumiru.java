package cz.fi.muni.pa193.jumiru;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestJumiru {
    @Test
    public void testValid(){
        assertTrue(true);
        Assert.assertEquals("A1LQFN3A", TestVector.VALID_BECH32M[0]);
    }
}
