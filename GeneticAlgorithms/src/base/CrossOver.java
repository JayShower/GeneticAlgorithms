package base;

public interface CrossOver<T> {
	void run(Individual<T> i1, Individual<T> i2);
}