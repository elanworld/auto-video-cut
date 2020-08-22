package com.alan.ai;


import java.util.ArrayList;

public class AiBaseTarget {
    //max chage output number when input diffrence is max
    double growth = 0.1;
    double target;
    double inputTarget;
    double distance;
    boolean positive;

    //show data
    ArrayList<Double> inputData = new ArrayList<>();
    ArrayList<Double> targetData = new ArrayList<>();

    /**
     * set ai param
     * @param target   : output expection what compute by AiBaseTarget
     * @param distance : input distance
     * @param inputTarget : input expecton target
     * @param positive : correlation of input and output
     */
    public AiBaseTarget(double target, double distance, double inputTarget, boolean positive) {
        this.target = target;
        this.distance = distance;
        this.inputTarget = inputTarget;
        this.positive = positive;
    }

    public double input(double currentInput) {
        double gap = growth * Math.abs(currentInput - inputTarget) / distance;
        boolean pstv = currentInput > inputTarget;
        if (!positive) {
            pstv = !pstv;
        }
        if (pstv) {
            target += gap;
        } else {
            target -= gap;
        }
        inputData.add(currentInput);
        targetData.add(target);
        return target;
    }

    public double getTarget() {
        return target;
    }

    public ArrayList<Double> getInputData() {
        return inputData;
    }

    public ArrayList<Double> getTargetData() {
        return targetData;
    }

    @Override
    public String toString() {
        return targetData + "\n" + inputData;
    }
}
