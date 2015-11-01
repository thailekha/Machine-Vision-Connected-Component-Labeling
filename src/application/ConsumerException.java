package application;

/**
 * To replace the Consumer functional interface since it cannot throw Exception
 * @author Thai Kha le
 *
 * @param <DataType> data type the interface acts upon
 */
@FunctionalInterface
public interface ConsumerException<DataType> {
	void doTask(DataType param) throws Exception;
}
