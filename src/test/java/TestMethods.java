import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;
import xyz.prinkov.mplab01.Annealing;
import xyz.prinkov.mplab01.Genetic;
import xyz.prinkov.mplab01.Method;
import xyz.prinkov.mplab01.MonteCarlo;
import xyz.prinkov.mplab01.genetic.Individual;

import java.util.Arrays;

public class TestMethods {
    Method method;

    @Test
    public void testConstructorMC() {
        method = new MonteCarlo();
    }

    @Test
    public void testMC()  {
        method = new MonteCarlo();
        Function f =
                new Function("f(x, y) = 4*x^2 - 2.1*x^4 +x^6/3 + x*y-4*y^2+4*y^4");
        System.out.println(method.min(f));
    }

    @Test
    public void testConstructorA() {
        method = new Annealing();
    }

    @Test
    public void testA() {
        method = new Annealing();
        Annealing.a = new double[]{-5,-5};
        Annealing.b = new double[]{5,5};
        Function f =
                new Function("f(x, y) = 4*x^2 - 2.1*x^4 +x^6/3 + x*y-4*y^2+4*y^4");
        System.out.println(Arrays.toString(method.min(f)));
    }

    @Test
    public void testGenetic() {
        Genetic genetic = new Genetic();

        genetic.k = 500;

        Genetic.a = new double[]{-5, -5};
        Genetic.b = new double[]{5, 5};
        Function f = new Function("f(x, y) = 4*x^2 - 2.1*x^4 +x^6/3 + x*y-4*y^2+4*y^4");

        double[] answer = genetic.min(f);
        System.out.println(Arrays.toString(answer));
        System.out.println(f.calculate(answer));

    }
}
