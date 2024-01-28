package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> test1 = new AListNoResizing<>();
        BuggyAList<Integer> test2 = new BuggyAList<>();
        test1.addLast(3);
        test1.addLast(4);
        test1.addLast(5);
        test2.addLast(3);
        test2.addLast(4);
        test2.addLast(5);
        assertEquals(test1.size(), test2.size());
        assertEquals(test1.removeLast(),test2.removeLast());
        assertEquals(test1.removeLast(),test2.removeLast());
        assertEquals(test1.removeLast(),test2.removeLast());
    }
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                correct.addLast(randVal);
                broken.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size1 = correct.size();
                int size2 = correct.size();
                assertEquals(size1,size2);
                System.out.println("size: " + size1);
                System.out.println("size: " + size2);
            }
            else if (operationNumber == 2){
                int size1 = correct.size();
                int size2 = broken.size();
                assertEquals(size1,size2);
                if(size1 > 0){
                    int x = correct.getLast();
                    int y = broken.getLast();
                    assertEquals(x,y);
                    System.out.println("getLast(" + x + ")");
                }
            }
            else if (operationNumber == 3) {
                int size1 = correct.size();
                int size2 = correct.size();
                assertEquals(size1,size2);
                if (size1 > 0) {
                    int x = correct.removeLast();
                    int y = broken.removeLast();
                    assertEquals(x,y);
                    System.out.println("removeLast(" + x + ")");
                }
            }
        }
    }
}
