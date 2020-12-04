/*
 * Tema 2 ASC
 * 2020 Spring
 */
#include "utils.h"

/*
 * Add your unoptimized implementation here
 */
double* my_solver(int N, double *A, double* B) {
	printf("NEOPT SOLVER\n");
	int i, j, k;
	double *C = calloc(N * N, sizeof(double));
	double *mat1 = calloc(N * N, sizeof(double));
	double *mat2 = calloc(N * N, sizeof(double));
	
	// B * A^t
	for (i = 0; i < N; i++)
       		for (j = 0; j < N; j++)
            		for (k = j; k < N; k++)
				mat1[i * N + j] += B[i * N + k] * A[j * N + k];
	
	// A * A
	for (i = 0; i < N; i++)
        	for (j = 0; j < N; j++)
			if (j >= i)
				for (k = 0; k <= j; k++)
					mat2[i * N + j] += A[i * N + k] * A[k * N + j];

	// A^2 * B
	for (i = 0; i < N; i++)
        	for (j = 0; j < N; j++) {
			for (k = i; k < N; k++)
				C[i * N + j] += mat2[i * N + k] * B[k * N + j];
			C[i * N + j] += mat1[i * N + j];
		}

	free(mat1);
	free(mat2);

	return C;
}
