package xyz.prinkov.mplab01;

import org.mariuszgromada.math.mxparser.Function;
import parsii.tokenizer.ParseException;

abstract public class Method {
    abstract public double[] min(Function f) throws ParseException;
}
