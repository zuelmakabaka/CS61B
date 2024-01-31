package deque;


import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class ArrayDequeTest {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        ArrayDeque<Integer> test1 = new ArrayDeque<>();
        ArrayDeque<Integer> test2 = new ArrayDeque<>();
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
        ArrayDeque<Integer> correct = new ArrayDeque<>();
        ArrayDeque<Integer> broken = new ArrayDeque<>();

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

            else if (operationNumber == 3) {
                int size1 = correct.size();
                int size2 = correct.size();
                assertEquals(size1,size2);
                if (size1 > 0) {
                    if(correct.removeLast() != correct.removeLast()){
                        System.out.print("error");
                    }

                    System.out.println("removeLast(success)");
                }
            }
        }
    }
}
