import info.DataInfo;
import info.FileInfo;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import preprocessor.AnomalyPreprocessor;
import preprocessor.DataPreprocessor;
import stats.DataStatsCollector;

public class Application {

    public static void main(String... args) throws IOException, InterruptedException {
        BigDecimal beforeUpdateColumnsCount;
        BigDecimal afterUpdateColumnsCount;

        BigDecimal beforeUpdateRowsCount;
        BigDecimal afterUpdateRowsCount;

        BigDecimal beforeUpdateNumbersCount;
        BigDecimal afterUpdateNumbersCount;

        BigDecimal beforeUpdateFileSize;
        BigDecimal afterUpdateFileSize;

        DataInfo dataInfo = new DataInfo(new BigDecimal(1.5f), 1, 120);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setInitialInputDataFilename(
                "C:\\Users\\Oleksandr_Storozhuk\\Documents\\Institute\\ML\\lab1\\Generator\\completeData.csv");
        fileInfo.setAnomaliesDataFilename(
                "C:\\Users\\Oleksandr_Storozhuk\\Documents\\Institute\\ML\\lab1\\Generator\\anomaly.txt");
        DataStatsCollector dataStatsCollector = new DataStatsCollector(dataInfo, fileInfo);

/*
        dataStatsCollector.readAnomalyData();
*/

        dataStatsCollector.analyzeDataFile(fileInfo.getInitialInputDataFilename(), false);
        System.gc();

/*        dataStatsCollector.calculateCurrentAnomalies();
        System.gc();*/

        dataStatsCollector.calculateColumns(fileInfo.getInitialInputDataFilename());

        beforeUpdateColumnsCount = BigDecimal.valueOf(dataInfo.getColumnCount());
        beforeUpdateRowsCount = BigDecimal.valueOf(dataInfo.getRowsCount());
        beforeUpdateFileSize = BigDecimal.valueOf(
                dataStatsCollector.getFileSize(fileInfo.getInitialInputDataFilename()));
        beforeUpdateNumbersCount = BigDecimal.valueOf(dataStatsCollector.getTotalNumbersCount());

        dataStatsCollector.printData();

        DataPreprocessor dataPreprocessor = new DataPreprocessor(dataInfo, fileInfo);
        fileInfo.setAfterAnomaliesProcessedDataFilename("afterAnomaliesRemoved.csv");
        fileInfo.setAfterEmptyAndErrorInputsProcessedDataFilename("afterEmptyAndErrorsProc.csv");
        fileInfo.setAfterContradictoriesProcessedDataFilename("afterContradictoriesProc.csv");
        fileInfo.setAfterOptimizationDataFilename("afterOptimizationProc.csv");

        dataPreprocessor.processEmptyAndErrorInputs(fileInfo.getInitialInputDataFilename());
        System.gc();

        dataPreprocessor.processContradictoryInfo();
        System.gc();

        dataPreprocessor.optimizeCriteria();
        System.gc();

        AnomalyPreprocessor anomalyPreprocessor = new AnomalyPreprocessor(dataInfo, fileInfo);
        anomalyPreprocessor.preprocessAnomalies(fileInfo.getAfterOptimizationDataFilename());
        System.gc();

        dataPreprocessor.getMinAndMaxValues(fileInfo.getAfterAnomaliesProcessedDataFilename());
        System.gc();

        dataPreprocessor.normalizeData(fileInfo.getAfterAnomaliesProcessedDataFilename());

        System.out.println("-------------");

        dataStatsCollector.resetFileInfo();

        dataStatsCollector.analyzeDataFile("result.csv", true);

        dataStatsCollector.calculateColumns("result.csv");

        afterUpdateColumnsCount = BigDecimal.valueOf(dataInfo.getColumnCount());
        afterUpdateRowsCount = BigDecimal.valueOf(dataInfo.getRowsCount());
        afterUpdateFileSize = BigDecimal.valueOf(
                dataStatsCollector.getFileSize("result.csv"));
        afterUpdateNumbersCount = BigDecimal.valueOf(dataStatsCollector.getTotalNumbersCount());

        dataStatsCollector.printData();

        BigDecimal sizeDiff = afterUpdateFileSize.divide(beforeUpdateFileSize, 1, RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(100));
        BigDecimal rowsDiff = afterUpdateRowsCount.divide(beforeUpdateRowsCount, 1, RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(100));
        BigDecimal colsDiff = afterUpdateColumnsCount.divide(beforeUpdateColumnsCount, 1, RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(100));
        BigDecimal numsDiff = afterUpdateNumbersCount.divide(beforeUpdateNumbersCount, 1, RoundingMode.HALF_DOWN)
                .multiply(BigDecimal.valueOf(100));

        System.out.println("After preprocessing the result has the following changes over the initial data set:");
        System.out.printf("Size Difference before-to-after = %s%%\n", sizeDiff);
        System.out.printf("Rows count Difference before-to-after = %s%%\n", rowsDiff);
        System.out.printf("Columns count Difference before-to-after = %s%%\n", colsDiff);
        System.out.printf("Numbers count Difference before-to-after = %s%%\n", numsDiff);
    }
}
