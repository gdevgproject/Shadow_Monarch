package dev.umbra.core.contract.config;

/**
 * Server/world-scoped configuration settings.
 */
public final class ServerConfig {
    private double gateFrequencyMultiplier = 1.0;
    private int maxSummonLimit = 12;

    public double getGateFrequencyMultiplier() {
        return gateFrequencyMultiplier;
    }

    public void setGateFrequencyMultiplier(double gateFrequencyMultiplier) {
        this.gateFrequencyMultiplier = gateFrequencyMultiplier;
    }

    public int getMaxSummonLimit() {
        return maxSummonLimit;
    }

    public void setMaxSummonLimit(int maxSummonLimit) {
        this.maxSummonLimit = maxSummonLimit;
    }
}
