package multipleProducersMultipleConsumers;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author cristian.chilipirea
 *
 */
public class Buffer  {
	int a;
	ArrayBlockingQueue<Integer> list = new ArrayBlockingQueue<Integer>(10);

	void put(int value) throws InterruptedException {
		a = value;
		list.put(a);
	}

	int get() throws InterruptedException {
		a = list.take();
		return a;
	}
}
