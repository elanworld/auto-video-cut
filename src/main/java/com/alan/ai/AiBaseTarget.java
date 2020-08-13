package com.alan.ai;

public class AiBaseTarget {
    //max chage output number when input diffrence is max
    double growth = 0.1;
    double target;
    double inputTarget;
    double distance;
    boolean positive;

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
        return target;
    }

    public double getTarget() {
        return target;
    }
}
