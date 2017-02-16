package base;

public interface Function<T, U, R> {

	R apply(T t, U u);

}