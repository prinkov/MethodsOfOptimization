import org.junit.Test;
import org.mariuszgromada.math.mxparser.Function;
import parsii.tokenizer.ParseException;
import xyz.prinkov.mplab01.*;
import xyz.prinkov.mplab01.genetic.Individual;

import java.util.Arrays;

public class TestMethods {
    Method method;

    @Test
    public void testConstructorMC() {
        method = new MonteCarlo();
    }

    @Test
    public void testMC() throws ParseException {
        MonteCarlo.a = new double[]{-5, -5};
        MonteCarlo.b = new double[]{5, 5};
        method = new MonteCarlo();
        Function f =
                new Function("f(x, y) = 4*x^2 - 2.1*x^4 +x^6/3 + x*y-4*y^2+4*y^4");
        double[] answer = method.min(f);
        System.out.println(Arrays.toString(answer));
        System.out.println(f.calculate(answer));
    }

    @Test
    public void testConstructorA() {
        method = new Annealing();
    }

    @Test
    public void testA() throws ParseException {
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

    @Test
    public void testInterval() throws ParseException {
        Interval interval = new Interval();
        Interval.a = new double[]{-50, -50};
        Interval.b = new double[]{50, 50};
//        Function f =
//                new Function("f(x, y) = 4 * x^2 - 2.1 * x^4 +" +
//                        " x^6 / 3 + x * y - 4 * y^2 + 4 * y^4");
         Function f =
                new Function("f(x,y) = 0.26*(x^2+y^2) -0.48*x*y");

        double[] answer = interval.min(f);

        System.out.println(Arrays.toString(answer));
        System.out.println(f.calculate(answer));
    }
}
