package deque;

import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {

    public static class IntComparator implements Comparator<Integer>{
        public int compare(Integer a,Integer b){
            return a-b;
        }
    }

    public static class StringComparator implements Comparator<String>{
        public int compare(String a,String b){
            return a.length()-b.length();
        }
    }
    @Test
    public void noParameterMaxTest(){
        MaxArrayDeque<Integer> maxArray0=new MaxArrayDeque<>(new IntComparator());
        for (int i=0;i<32;i++){
            maxArray0.addLast(i);
        }
        assertEquals(31,(int)maxArray0.max());
    }

    @Test
    public void oneParameterMaxTest(){
        MaxArrayDeque<String> maxArray1=new MaxArrayDeque<>(new StringComparator());
        maxArray1.addLast("123456");
        maxArray1.addLast("12345");
        maxArray1.addLast("1234");
        maxArray1.addLast("123");
        maxArray1.addLast("12");
        maxArray1.addLast("1");

        assertEquals("123456",maxArray1.max());
    }
}
