compile: helloWorld.c addVectors.c addVectorsSeq.c concurrency.c
	gcc -o helloWorld helloWorld.c -lm -lpthread
	gcc -o addVectors addVectors.c -lm -lpthread
	gcc -o addVectorsSeq addVectorsSeq.c -lm -lpthread
	gcc -o concurrency concurrency.c -lm -lpthread

clean:
	rm helloWorld