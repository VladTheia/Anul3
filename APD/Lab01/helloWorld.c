#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>
#include <unistd.h>

void* threadFunction(void *var)
{
	int thread_id = *(int*)var;
	// for (int i = 0; i < 100; i++) {
		printf("Hello world from thread %i\n", thread_id);
	// }
}

void* threadFunction2(void *var)
{
	int thread_id = *(int*)var;
	// for (int i = 0; i < 100; i++) {
		printf("Hello world from thread %i\n", thread_id);
	// }
}

void* threadAdd(void *var) {

}

int main(int argc, char **argv)
{
	int P = 2;// sysconf(_SC_NPROCESSORS_ONLN);
	int i;

	pthread_t tid[P];
	int thread_id[P];
	for(i = 0;i < P; i++)
		thread_id[i] = i;

	// for(i = 0; i < P; i++) {
		// pthread_create(&(tid[0]), NULL, threadFunction, &(thread_id[0]));
	// }

		// pthread_create(&(tid[1]), NULL, threadFunction2, &(thread_id[1]));

	for(i = 0; i < P; i++) {
		pthread_join(tid[i], NULL);
	}

	int a[12];
	int b[12];
	int c[12];

	for (i = 0; i < 12; i++) {
		a[i] = i;
		b[i] = 2 * i;
	}

	pthread_t tid[4];
	int thread_id[4];
	for(i = 0;i < 4; i++)
		thread_id[i] = i;

	for(i = 0; i < 4; i++) {
		pthread_create(&(tid[i]), NULL, threadAdd, &(thread_id[i]));
	}

	return 0;
}
