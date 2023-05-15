package randomizedtest;
import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
public class randomizedTest {
    @Test
    public void test(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                assertEquals(L.size(),B.size());
            } else if (operationNumber == 2) {
                //getLast
                if(L.size() > 0) {
                    int Last = L.getLast();
                    assertEquals(L.getLast(),B.getLast());
                }
            } else if (operationNumber == 3) {
                //removeLast
                if(L.size() > 0){
                    int remove = L.removeLast();
                    int remove1 = B.removeLast();
                    assertEquals(remove,remove1);
                }
            }
        }
    }
}
