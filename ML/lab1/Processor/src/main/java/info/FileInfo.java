package info;

public class FileInfo {

    private String lastWrittenFilename;

    private String initialInputDataFilename;
    private String outputDataFilename;
    private String secondOutputDataFilename;

    private String anomaliesDataFilename;
    private String afterAnomaliesProcessedDataFilename;
    private String afterEmptyAndErrorInputsProcessedDataFilename;
    private String afterContradictoriesProcessedDataFilename;
    private String afterOptimizationDataFilename;

    public String getInitialInputDataFilename() {
        return initialInputDataFilename;
    }

    public void setInitialInputDataFilename(String initialInputDataFilename) {
        this.initialInputDataFilename = initialInputDataFilename;
    }

    public String getOutputDataFilename() {
        return outputDataFilename;
    }

    public void setOutputDataFilename(String outputDataFilename) {
        this.outputDataFilename = outputDataFilename;
    }

    public String getAnomaliesDataFilename() {
        return anomaliesDataFilename;
    }

    public void setAnomaliesDataFilename(String anomaliesDataFilename) {
        this.anomaliesDataFilename = anomaliesDataFilename;
    }

    public String getAfterAnomaliesProcessedDataFilename() {
        return afterAnomaliesProcessedDataFilename;
    }

    public void setAfterAnomaliesProcessedDataFilename(String afterAnomaliesProcessedDataFilename) {
        this.afterAnomaliesProcessedDataFilename = afterAnomaliesProcessedDataFilename;
    }

    public String getAfterEmptyAndErrorInputsProcessedDataFilename() {
        return afterEmptyAndErrorInputsProcessedDataFilename;
    }

    public void setAfterEmptyAndErrorInputsProcessedDataFilename(String afterEmptyAndErrorInputsProcessedDataFilename) {
        this.afterEmptyAndErrorInputsProcessedDataFilename = afterEmptyAndErrorInputsProcessedDataFilename;
    }

    public String getAfterContradictoriesProcessedDataFilename() {
        return afterContradictoriesProcessedDataFilename;
    }

    public void setAfterContradictoriesProcessedDataFilename(String afterContradictoriesProcessedDataFilename) {
        this.afterContradictoriesProcessedDataFilename = afterContradictoriesProcessedDataFilename;
    }

    public String getAfterOptimizationDataFilename() {
        return afterOptimizationDataFilename;
    }

    public void setAfterOptimizationDataFilename(String afterOptimizationDataFilename) {
        this.afterOptimizationDataFilename = afterOptimizationDataFilename;
    }

    public String getLastWrittenFilename() {
        return lastWrittenFilename;
    }

    public void setLastWrittenFilename(String lastWrittenFilename) {
        this.lastWrittenFilename = lastWrittenFilename;
    }

    public String getSecondOutputDataFilename() {
        return secondOutputDataFilename;
    }

    public void setSecondOutputDataFilename(String secondOutputDataFilename) {
        this.secondOutputDataFilename = secondOutputDataFilename;
    }
}
