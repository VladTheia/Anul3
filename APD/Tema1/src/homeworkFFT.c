#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <math.h>
#include <complex.h>
typedef double complex cplx;

int N; // number of input values
cplx* buf;
cplx* out;
int numThreads;
pthread_barrier_t barrier;

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

	out = (_Complex double *)malloc(sizeof(_Complex double) * N);
	buf = (_Complex double *)malloc(sizeof(_Complex double) * N);

	for (int i = 0; i < N; i++) {
		memset(buffer, 0, 20);
		if (fgets(buffer, 20, f) != NULL) {
			buf[i] = atof(buffer);
			out[i] = buf[i];
		}
	}

	fclose(f);
	free(buffer);
}

void writeOutput(char *fileName) {
	FILE *f = fopen(fileName, "w+");
	fprintf(f, "%d\n", N);
	for (int i = 0; i < N; i++) {
		fprintf(f, "%lf %lf\n", creal(buf[i]), cimag(buf[i]));
	}
	fclose(f);
}

void _fft(cplx buf[], cplx out[], int step) {
	if (step < N) {
		_fft(out, buf, step * 2);
		_fft(out + step, buf + step, step * 2);
 
		for (int i = 0; i < N; i += 2 * step) {
			cplx t = cexp(-I * M_PI * i / N) * out[i + step];
			buf[i / 2]     = out[i] + t;
			buf[(i + N)/2] = out[i] - t;
		}
	}
}

void* parFFT(void *args) {
	int thread_id = *(int*)args;

	switch (numThreads) {
	case 1:
		_fft(buf, out, 1);
		break;
	case 2:
		_fft(out + thread_id, buf + thread_id, 2);
		// the needed results are calculated first
		pthread_barrier_wait(&barrier);

		// the first level is combined
		if (thread_id == 0) {
			for (int i = 0; i < N; i += 2) {
				cplx t = cexp(-I * M_PI * i / N) * out[i + 1];
				buf[i / 2]     = out[i] + t;
				buf[(i + N)/2] = out[i] - t;
			}
		}
		break;
	case 4:
		_fft(buf + thread_id, out + thread_id, 4);
		// the needed results are calculated first
		pthread_barrier_wait(&barrier);
		
		// the second level is combined
		if (thread_id == 0) {
			for (int i = 0; i < N; i += 4) {
				cplx t = cexp(-I * M_PI * i / N) * buf[i + 2];
				out[i / 2]     = buf[i] + t;
				out[(i + N)/2] = buf[i] - t;
			}
		} else if (thread_id == 2) {
			for (int i = 0; i < N; i += 4) {
				cplx t = cexp(-I * M_PI * i / N) * (buf+1)[i + 2];
				(out + 1)[i / 2]     = (buf + 1)[i] + t;
				(out + 1)[(i + N)/2] = (buf + 1)[i] - t;
			}
		}
		pthread_barrier_wait(&barrier);

		// the first level is combined
		if (thread_id == 0) {
			for (int i = 0; i < N; i += 2) {
				cplx t = cexp(-I * M_PI * i / N) * out[i + 1];
				buf[i / 2]     = out[i] + t;
				buf[(i + N)/2] = out[i] - t;
			}
		}

		break;
	default:
		break;
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
	pthread_barrier_init(&barrier, NULL, numThreads);
	pthread_t tid[numThreads];
	int thread_id[numThreads];
	for (i = 0; i < numThreads; i++) {
		thread_id[i] = i;
	}

	for (i = 0; i < numThreads; i++) {
		pthread_create(&(tid[i]), NULL, parFFT, &(thread_id[i]));
	}

	for (i = 0; i < numThreads; i++) {
		pthread_join(tid[i], NULL);
	}	
	pthread_barrier_destroy(&barrier);

	writeOutput(argv[2]);
	free(buf);
	free(out);

	return 0;
}
