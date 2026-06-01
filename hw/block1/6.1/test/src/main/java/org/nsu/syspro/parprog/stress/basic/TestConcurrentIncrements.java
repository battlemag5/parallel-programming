package org.nsu.syspro.parprog.stress.basic;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.*;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

// (1, 1) наблюдаю, это происходит из-за того что прибавление не атомарно, соответственно
// оба актора запишут в конечное значение по 1
// этого можно избежать если ++ будет атомарным
//@JCStressTest
//// @Outcome(id = "1, 1", expect = ACCEPTABLE_INTERESTING, desc = "Data race")
//@Outcome(id = "1, 2", expect = ACCEPTABLE, desc = "actor1 -> actor2.")
//@Outcome(id = "2, 1", expect = ACCEPTABLE, desc = "actor2 -> actor1.")
//@State
//public class TestConcurrentIncrements {
//    int v;
//    @Actor public void actor1(II_Result r) {
//        r.r1 = ++v;
//    }
//    @Actor public void actor2(II_Result r) {
//        r.r2 = ++v;
//    }
//}

//   0           0    0,00%   Acceptable
//       1     113 519    0,29%  Interesting
//       2     728 517    1,84%  Interesting
//       3   5 718 874   14,48%  Interesting
//       4  16 610 419   42,05%  Interesting
//       5  16 332 562   41,34%  Acceptable
// нули не встречаются поскольку arbiter начинает работать только после того, как все акторы отработали (Arbiters run after both actors, and therefore can observe the final result.)
// 1 может встретиться, поскольку это дата рейс
// 5 это нормальный вариант, когда все акторы отработали параллельно
@JCStressTest
@Outcome(id = "0", expect = ACCEPTABLE)
@Outcome(id = "1", expect = ACCEPTABLE_INTERESTING)
@Outcome(id = "2", expect = ACCEPTABLE_INTERESTING)
@Outcome(id = "3", expect = ACCEPTABLE_INTERESTING)
@Outcome(id = "4", expect = ACCEPTABLE_INTERESTING)
@Outcome(id = "5", expect = ACCEPTABLE)
@State
public class TestConcurrentIncrements {
    int v;

    @Actor
    public void actor1() {
        v++;
    }

    @Actor
    public void actor2() {
        v++;
    }

    @Actor
    public void actor3() {
        v++;
    }

    @Actor
    public void actor4() {
        v++;
    }

    @Actor
    public void actor5() {
        v++;
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = v;
    }
}