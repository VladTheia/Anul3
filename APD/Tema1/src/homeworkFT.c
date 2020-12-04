#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <math.h>
#include <complex.h>

int N; // number of input values
double complex* input;
double complex* output;
int numThreads;

void readInput(char *fileName) {
	char *buffer = (char *) malloc(20);
	memset(buffer, 0, 20);
	FILE* f = fopen(fileName, "r");

	// check the file
	if (f == NULL) {
		perror("Unable to open file!");
        exit(1);
	}

	// get number of values
	if (fgets(buffer, 20, f) != NULL)
		N = atoi(buffer);

	// allocate array and fill with values
	input = (_Complex double *)malloc(sizeof(_Complex double) * N);
	for (int i = 0; i < N; i++) {
		memset(buffer, 0, 20);
		if (fgets(buffer, 20, f) != NULL)
			input[i] = atof(buffer);
	}

	fclose(f);
	free(buffer);

	// also allocate output array
	output = (_Complex double *)malloc(sizeof(_Complex double) * N);
}

void writeOutput(char *fileName) {
	FILE *f = fopen(fileName, "w+");
	fprintf(f, "%d\n", N);
	for (int i = 0; i < N; i++) {
		fprintf(f, "%lf %lf\n", creal(output[i]), cimag(output[i]));
	}
	fclose(f);
}

void* parFT(void *args) {
	int thread_id = *(int*)args;

	int start = thread_id * ceil((double)N/(double)numThreads);
	int end = fmin(N, (thread_id + 1) * ceil((double)N/(double)numThreads));

	for (int i = start; i < end; i++) {
		output[i] = 0;
		for (int j = 0; j < N; j++) {
			double angle = 2 * M_PI * j * i / N;
			output[i] += input[j] * cexp(-angle * I);
		}
	}	

	return NULL;
}

int main(int argc, char * argv[]) {
	int i;
	// check args
	if(argc < 4) {
		printf("Not enough paramters: ./program inputValues.txt outputValues.txt numThreads\n");
		exit(1);
	}
	readInput(argv[1]);

	numThreads = atoi(argv[3]);
	pthread_t tid[numThreads];
	int thread_id[numThreads];
	for (i = 0; i < numThreads; i++) {
		thread_id[i] = i;
	}

	for (i = 0; i < numThreads; i++) {
		pthread_create(&(tid[i]), NULL, parFT, &(thread_id[i]));
	}

	for (i = 0; i < numThreads; i++) {
		pthread_join(tid[i], NULL);
	}

	writeOutput(argv[2]);
	free(input);
	free(output);

	return 0;
}
