package flik;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class fliktest {
    @Test
    public void testflik() {
        int i = 0;
        int j = 0;
        while(i < 5000){
            assertEquals(i,j);
            assertTrue(i == j);
            i += 1;
            j += 1;
        }
    }
}
