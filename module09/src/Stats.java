// Stats.java
// 
// Author: Rahul Simha
// March, 2008
//
// A simple object to track mean, variance and confidence intervals.


public class Stats {

    double numSamples;           // Count # samples received so far.
    double sampleSum;            // Accumulated sum.
    double sampleMean;           // Mean.
    double sampleVar;            // Variance.
    double delta;                // Half the confidence interval.


    public void add(double x) {
        numSamples++;
        sampleSum += x;
        double oldMean = sampleMean;
        sampleMean = sampleSum / numSamples;
        if (numSamples <= 1) {
            // This should be treated as an improper value.
            sampleVar = 0;
        } else {
            // Note: recurrent formula for variance =>  no need to store samples.
            sampleVar = (1 - 1.0 / (double) (numSamples - 1)) * sampleVar
                    + numSamples * (sampleMean - oldMean) * (sampleMean - oldMean);
        }

        // We're using 95% confidence here. 
        delta = 1.96 * Math.sqrt(sampleVar / numSamples);
    }


    public double getMean() {
        return sampleMean;
    }


    public double getVariance() {
        return sampleVar;
    }


    public double getHalfConfidenceInterval() {
        return delta;
    }


    public String toString() {
        String results = "Statistics";
        results += "\n  Number of samples: " + numSamples;
        results += "\n  Sample mean:       " + sampleMean;
        results += "\n  Sample variance:   " + sampleVar;
        results += "\n  95% confidence: +- " + delta;
        results += "\n    as % of mean:    " + 100 * (delta / sampleMean);
        return results;
    }


}
