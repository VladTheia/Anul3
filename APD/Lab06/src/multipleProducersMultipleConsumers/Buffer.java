package multipleProducersMultipleConsumers;

import java.util.LinkedList;

/**
 * @author cristian.chilipirea
 *
 */
public class Buffer {
	int a;
	LinkedList<Integer> list = new LinkedList<>();
	int capacity = 3;

	void put(int value) throws InterruptedException {
		synchronized (this)
		{
			while (list.size()==capacity)
				wait();
			notify();
			list.add(value);

			//Thread.sleep(1000);
		}
	}

	int get() throws InterruptedException {
		int val = 0;


			synchronized (this)
			{
				while (list.size()==0)
					wait();
				notify();

				val = list.removeFirst();
				//Thread.sleep(1000);
			}
			return val;
	}
}
