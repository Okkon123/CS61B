package flik;

import org.junit.Test;
import static org.junit.Assert.*;
public class FlikTest {
    @Test
    public void test1(){
        for(int i=0;i<500;i++) {
            assertTrue("i ="+ i, Flik.isSameNumber(i,i));
        }
    }
}
