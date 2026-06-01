import java.util.Objects;

class Task13 {
    static int x = 0;
    static int y = 0;
    static int z = 0;

    private static Boolean LOG = false;
    private static final StringBuilder trace = new StringBuilder();

    private static synchronized void log(String msg) {
        if (!LOG) {
            return;
        }
        if (trace.length() > 0) {
            trace.append(" - ");
        }
        trace.append(msg);
    }

    private static synchronized String trace() {
        if (!LOG) {
            return "";
        }
        return trace.toString();
    }

    static class A extends Thread {
        public void run() {
            int a_x = x;   log("A.1");
            int a_z = z;   log("A.2");
            y = a_x + a_z; log("A.3");
        }
    }

    static class B extends Thread {
        public void run() {
            int b_x = x;   log("B.1");
            x = b_x + 1;   log("B.2");
            int b_z = z;   log("B.3");
            z = b_z + 1;   log("B.4");
        }
    }

    static class C extends Thread {
        public void run() {
            int c_y = y;      log("C.1");
            if (c_y == 2) {
                log("C.2");
                int c_x = x;    log("C.3");
                x = c_x - 1;    log("C.4");
            }
        }
    }

    public static void main(String[] args) {
        LOG = Objects.equals(args[0], "1");
        trace.setLength(0);

        Thread a = new A();
        Thread b = new B();
        Thread c = new C();

        a.start();
        b.start();
        c.start();

        try {
            a.join();
            b.join();
            c.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.printf("x = %2d, y = %2d, z = %2d | %s%n", x, y, z, trace());
    }
}