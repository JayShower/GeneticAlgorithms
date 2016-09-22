package base;

public interface PickIndividual<T> {
	Individual<T> run(Population<T> p);
}