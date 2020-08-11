package com.alan.ai;

public class AiBaseTarget {
    double growth = 0.1;
    double target;
    double inputTarget;
    double distance;

    /**
     * set ai param
     * @param target   : output expection what compute by AiBaseTarget
     * @param distance : input distance
     * @param inputTarget : input expecton target
     */
    public AiBaseTarget(double target, double distance, double inputTarget) {
        this.target = target;
        this.distance = distance;
        this.inputTarget = inputTarget;
    }

    public double input(double currentInput) {
        double gap = growth * Math.abs(currentInput - inputTarget) / distance;
        if (inputTarget > target) {
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
