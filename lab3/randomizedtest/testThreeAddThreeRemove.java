package randomizedtest;
import org.junit.Test;
import static org.junit.Assert.*;


public class testThreeAddThreeRemove {
    @Test
    public void test1(){
        AListNoResizing<Integer> AL= new AListNoResizing<>();
        BuggyAList<Integer> BuggyAL= new BuggyAList<>();
        AL.addLast(4);
        AL.addLast(5);
        AL.addLast(6);
        BuggyAL.addLast(4);
        BuggyAL.addLast(5);
        BuggyAL.addLast(6);

        assertEquals(AL.removeLast(),BuggyAL.removeLast());
        assertEquals(AL.removeLast(),BuggyAL.removeLast());
        assertEquals(AL.removeLast(),BuggyAL.removeLast());
    }
}
