package org.nsu.syspro.parprog.stress.basic;
import org.openjdk.jcstress.infra.results.*;
import org.openjdk.jcstress.annotations.*;
//   RESULT     SAMPLES     FREQ       EXPECT  DESCRIPTION
//  0, 2, 1  22625366   37,12%  Interesting
//  1, 0, 1  1355685    2,22%  Interesting
//  1, 1, 1  38976      0,06%  Interesting
//  1, 2, 1  36936664   60,59%  Interesting
@JCStressTest
@Outcome(id = "1, 0, 1", expect = Expect.ACCEPTABLE)
@Outcome(id = "1, 1, 1", expect = Expect.ACCEPTABLE)
@Outcome(id = "0, 2, 1", expect = Expect.ACCEPTABLE)
@Outcome(id = "1, 2, 1", expect = Expect.ACCEPTABLE)
//z всегда 1, y - 0\1\2, x 1 или 0 с y = 2
// это полный достиижмый список, более полное объяснение в комментариях к пру

@State
public class FindStates {
    int x, y, z;

    @Actor
    public void a() {
        int a_x = x;   // A.1
        int a_z = z;   // A.2
        y = a_x + a_z; // A.3
    }

    @Actor
    public void b() {
        int b_x = x; // B.1
        x = b_x + 1; // B.2
        int b_z = z; // B.3
        z = b_z + 1; // B.4
    }

    @Actor
    public void c() {
        int c_y = y;    // C.1
        if (c_y == 2) { // C.2
            int c_x = x;  // C.3
            x = c_x - 1;  // C.4
        }
    }

    @Arbiter
    public void main(III_Result r) {
        r.r1 = x;
        r.r2 = y;
        r.r3 = z;
    }
}