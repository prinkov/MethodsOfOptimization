package xyz.prinkov.mplab01;

import org.mariuszgromada.math.mxparser.Function;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Random;

public class Annealing extends Method {
    public static float Tmax = 1000000;
    public static float r = 0.9f;
    public static float eps = 0.01f;
    public static int L = 70;

    public static double[] a;
    public static double[] b;


    Random rnd = new Random();

    @Override
    public double[] min(Function f) {

        Scope scope = Scope.create();
        int varsLength = f.getArgumentsNumber();
        Variable[] varsV = new Variable[varsLength];

        double f1 = 0;
        double f2 = 0;
        double delta = 0;

        double[] vars = new double[varsLength];
        double[] varss = new double[varsLength];

        Expression expr = null;

        try {
            expr = Parser.parse(f.getFunctionExpressionString(), scope);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        for(int i = 0; i < vars.length; i++) {
            vars[i] = rnd.nextDouble() * Math.abs(a[i] - b[i]) + a[i];
            varsV[i] = scope.getVariable(f.getArgument(i).getArgumentName());
        }
        while (Tmax > 0 + 0.00000001) {
            for(int i = 0; i < L; i++) {
                for(int z = 0; z < vars.length; z++) {
//                    varss[z] = vars[z] - eps + rnd.nextDouble() * 2 * eps;
                    varss[z] = vars[z] + rnd.nextDouble() * eps - rnd.nextDouble() * eps;
                    if(varss[z] < a[z])
                        varss[z] = a[z];
                    if(varss[z] > b[z])
                        varss[z] = b[z];
                }

                for(int l = 0; l < varsLength; l++)
                    varsV[l].setValue(vars[l]);

                f1 = expr.evaluate();

                for(int l = 0; l < varsLength; l++)
                    varsV[l].setValue(varss[l]);

                f2 = expr.evaluate();

                delta = f2 - f1;

                if (delta <= 0 || Math.exp(-delta / Tmax) > rnd.nextDouble())
                    for(int k = 0; k < vars.length; k++)
                        vars[k] = varss[k];
            }
            Tmax *= r;
        }

        double h = 0.01;
        double[] varsModify = new double[varsLength];
        for (int i = 0; i < varsLength; i++) {

            f1 = expr.evaluate();
            varsModify[i] = vars[i] + h;
            varsV[i].setValue(varsModify[i]);
            f2 = expr.evaluate();

            if(f1 > f2)
                varsModify[i] = vars[i] + h;
            else {

                varsV[i].setValue(vars[i]);
                f1 = expr.evaluate();
                varsModify[i] = vars[i] - h;
                varsV[i].setValue(varsModify[i]);
                f2 = expr.evaluate();

                if(f1 > f2)
                    varsModify[i] = vars[i] - h;
                else
                    varsModify[i] = vars[i];
            }
        }
        System.out.println(f.calculate(vars));

        System.out.println(f.calculate(varsModify));
        return varsModify;
    }


}
