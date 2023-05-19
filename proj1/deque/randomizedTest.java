package deque;
import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
public class randomizedTest {
    @Test
    public void test(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        ArrayDeque<Integer> B = new ArrayDeque<>();
        int N = 100000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // addFirst
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 2) {
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
