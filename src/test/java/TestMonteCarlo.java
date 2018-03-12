import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;
import xyz.prinkov.mplab01.MonteCarlo;

public class TestMonteCarlo {
    MonteCarlo method;

    @Test
    public void testConstructor() {
        method = new MonteCarlo();
    }

    @Test
    public void testMethod()  {
        method = new MonteCarlo();
        Function f =
                new Function("f(x, y) = 4*x^2 - 2.1*x^4 +x^6/3 + x*y-4*y^2+4*y^4");
        System.out.println(method.min(f));
    }
}
