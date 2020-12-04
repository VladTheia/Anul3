#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>

// pentru a obtine rezultatul 14,
// ne asiguram ca a = 5 este executat dupa
// a = 3, urmat ambele adunari.

int a;
pthread_barrier_t barrier1;
pthread_barrier_t barrier2;

void* threadFunction1(void *var)
{
    pthread_barrier_wait(&barrier1);
	a = 5;
	a += 7;
    // se executa dupa a = 3
	pthread_barrier_wait(&barrier2);
}

void* threadFunction2(void *var)
{
    a = 3;
    // se executa primul
	pthread_barrier_wait(&barrier1);
	pthread_barrier_wait(&barrier2);
	a += 2;
    // se executa la final
}

int main(int argc, char **argv)
{
	int P = 2;
	int i;

	
	int num_threads = 2;
	pthread_barrier_init(&barrier1, NULL, num_threads);
	pthread_barrier_init(&barrier2, NULL, num_threads);

	pthread_t tid[P];

	pthread_create(&(tid[0]), NULL, threadFunction1, NULL);
	pthread_create(&(tid[1]), NULL, threadFunction2, NULL);
	

	for(i = 0; i < P; i++) {
		pthread_join(tid[i], NULL);
	}

	pthread_barrier_destroy(&barrier1);
	pthread_barrier_destroy(&barrier2);

	printf("a value is: %d\n", a);

	return 0;
}
