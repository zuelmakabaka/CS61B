package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> ops = new AList<>();
        int i = 1000;
        while(i <= 128000) {
            Ns.addLast(i);
            ops.addLast(10000);
            i = i * 2;
        }
        for(int m = 0;m < Ns.size();m += 1){
            SLList<Integer> test = new SLList<>();
            for(int n = 1;n <= Ns.get(m);n += 1){
                test.addLast(1);
            }
            Stopwatch sw = new Stopwatch();
            for(int k = 1;k <= 10000;k += 1){
                test.getLast();
            }
            times.addLast(sw.elapsedTime());
        }
        printTimingTable(Ns,times,ops);
    }

}
