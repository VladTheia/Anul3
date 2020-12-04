#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>

int a;
pthread_mutex_t mutex;

void* threadFunction(void *var)
{
    for (int i = 0; i < 100; i++) {
        a += 2;
    }
}

int main(int argc, char **argv)
{
	int P = 2;
	int i;

	pthread_t tid[P];

	for(i = 0; i < P; i++) {
		pthread_create(&(tid[i]), NULL, threadFunction, NULL);
	}

	for(i = 0; i < P; i++) {
		pthread_join(tid[i], NULL);
	}

	printf("a value is: %d\n", a);

	return 0;
}

// In cel mai rau caz, thread-urile sunt in concurentea la fiecare iteratie, rezultatul final fiind 200.