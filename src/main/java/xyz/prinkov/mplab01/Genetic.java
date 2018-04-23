package xyz.prinkov.mplab01;

import org.apache.commons.lang3.ArrayUtils;
import org.mariuszgromada.math.mxparser.Function;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;
import xyz.prinkov.mplab01.genetic.Individual;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;


public class Genetic extends Method {
    public static int k;
    public static int numOfEpoch;
    public static double probMutation;
    public static double[] a;
    public static double[] b;

    Random rnd = new Random();
    Scope scope = Scope.create();
    double[] dot_min;
    double fmin= Double.POSITIVE_INFINITY;

    @Override
    public double[] min(Function f) {
        Individual.a = a;
        Individual.b = b;

        int varsLength = f.getArgumentsNumber();
        Variable[] vars = new Variable[varsLength];
        dot_min = new double[varsLength];

        Expression expr = null;

        try {
            expr = Parser.parse(f.getFunctionExpressionString(), scope);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Individual[] population = new Individual[k];
        double[] populationValue = new double[varsLength];

        Individual.vars = vars;
        Individual.exp = expr;

        for(int i = 0; i < vars.length; i++) {
            vars[i] = scope.getVariable(f.getArgument(i).getArgumentName());
        }

        for(int i = 0; i < population.length; i++) {
            for(int j = 0; j < varsLength; j++)
                populationValue[j] = rnd.nextDouble() * (b[j] - a[j]) + a[j];
            population[i] = new Individual(populationValue);
        }


        for(int epoch = 0; epoch < numOfEpoch; epoch++) {
            Arrays.parallelSort(population);
            population = Arrays.copyOfRange(population, 0, k);
            double[] probability =
                    getProbabilityForReproductation(expr, vars, population);
            population = ArrayUtils.addAll(population, reproduction(population, probability));

//            Arrays.stream(population).forEach(o -> o.mutation(probMutation));
        }

        getProbabilityForReproductation(expr, vars, population);

        return dot_min;
    }

    public double[] getProbabilityForReproductation(Expression exp,
                                                  Variable[] vars,
                                                  Individual[] population
                                                  ) {
        double[] fxi = new double[population.length];
        double[] probabilityReproductation = new double[population.length];

        double[] mbdot_min = population[0].getValue();
        double[] mbdot_max = population[population.length - 1].getValue();

        for(int i = 0; i < vars.length; i++)
            vars[i].setValue(mbdot_min[i]);

        double isFmin = exp.evaluate();
        if(isFmin < fmin) {
            fmin = isFmin;
            dot_min = mbdot_min;
        }

        for(int i = 0; i < vars.length; i++)
            vars[i].setValue(mbdot_max[i]);

        final double fmax = exp.evaluate();

        for(int k = 0; k < population.length; k++) {
            double[] varValue = population[k].getValue();
            for(int i = 0; i < vars.length; i++)
                vars[i].setValue(varValue[i]);
            fxi[k] = exp.evaluate();
        }

        double fmaxMinusFi  = DoubleStream.of(fxi).mapToObj(e -> e = (fmax - e + 1)).
                mapToDouble(Double::valueOf).
                sum();

        for(int i = 0; i < population.length; i++) {
            probabilityReproductation[i] = (fmax - fxi[i] + 1) / fmaxMinusFi;
        }

        return probabilityReproductation;
    }

    private Individual[] reproduction(Individual[] population, double[] probability) {
        Individual[] children = new Individual[population.length / 2];
        for(int i = 0; i < children.length; i++) {
            int one = getRandomResult(probability);
            int two;
            int controlIndex = 0;
            while((two = getRandomResult(probability)) == one)
                if(controlIndex++ > 100)
                    break;
            try {
                children[i] =
                        Individual.coition(population[one], population[two]);
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }

        return children;
    }

    int getRandomResult(double prob[]) {
        double coin = rnd.nextDouble();
        double sum = 0.00000;
        int index = 0;
        while(sum < coin)
            sum += prob[index++];
//        System.out.println(index-1);
        return index-1;
    }
}
