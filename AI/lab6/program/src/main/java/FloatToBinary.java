import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FloatToBinary {
    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm();
        List<Float> population = new ArrayList<>(100);
        float currentNumber;
        for (int i = 0; i < 100; i++) {
            do {
                currentNumber = (float) (Math.round((-10 + Math.random() * (30)) * 1000d) / 1000d);
            } while (population.contains(currentNumber));
            population.add(currentNumber);
        }
        for (int i = 0; i < 30; i++) {
            if (population.size() < 2) {
                break;
            }
            population = ga.doAlgorithm(population);
        }
        Map<Float, Float> minFuncValues = ga.getMinFuncValues();
        for (float funX : minFuncValues.keySet()) {
            System.out.println("f(" + funX + ") = " + minFuncValues.get(funX));
        }
        Graph graph = new Graph(minFuncValues);
        graph.setVisible(true);
    }
}