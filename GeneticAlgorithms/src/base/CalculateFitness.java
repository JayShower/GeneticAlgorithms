package base;

public interface CalculateFitness<T> {

	double run(Individual<T> i, T targetValue);
}