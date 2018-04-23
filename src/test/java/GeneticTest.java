import org.junit.Assert;
import org.junit.Test;
import xyz.prinkov.mplab01.genetic.Individual;

import java.util.Arrays;

public class GeneticTest {
    @Test
    public void testIndividual() {
        Individual.a = new double[]{-5, -5};
        Individual.b = new double[]{5, 5};
        Individual ind = new Individual(new double[]{0, 0});
        Individual ind2 = new Individual(new double[]{0.4, 0.1});

        String[] t = ind.getBinary();
        ind.setValue(new double[]{0.1, -0.5});
        System.out.println(Arrays.toString(ind.getBinary()));
        System.out.println(Arrays.toString(ind.getValue()));
        ind.setValue(t);
        System.out.println(Arrays.toString(ind.getValue()));
    }
}
