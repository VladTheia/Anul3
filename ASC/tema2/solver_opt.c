/*
 * Tema 2 ASC
 * 2020 Spring
 */
#include "utils.h"

/*
 * Add your optimized implementation here
 */
double* my_solver(int N, double *A, double* B) {
	printf("OPT SOLVER\n");

	int i, j, k;
	double *C = malloc(N * N * sizeof(double));
	double *mat1 = malloc(N * N * sizeof(double));
	double *mat2 = malloc(N * N * sizeof(double));
	register double suma;
	double *origin_pa;
	double *origin_pb;
	double *pa1;
	double *pb1;
	double *pa2;
	double *pb2;
	
	for (i = 0; i < N; i++) {
		origin_pa = &A[i * N];
		origin_pb = &B[i * N];
		for (j = 0; j < N; j++) {
			// B * A^t
			pa1 = &A[j * N] + j;
			pb1 = origin_pb + j;
			suma = 0.0;
			for (k = j; k < N; k++) {
				suma += *pa1 * *pb1;
				pa1++;
				pb1++;
			}
			mat1[i * N + j] = suma;

			// A * A
			pa2 = origin_pa;
			pb2 = &A[j];
			suma = 0.0;
			if (j >= i)
				for (k = 0; k <= j; k++) {
					suma += *pa2 * *pb2;
					pa2++;
					pb2 += N;
				}
			mat2[i * N + j] = suma;
		}
	}

	// A^2 * B
	for (i = 0; i < N; i++) {
		origin_pa = &mat2[i * N];
		for (j = 0; j < N; j++) {
			pa1 = origin_pa + i;
			pb1 = &B[j] + N * i;
			suma = 0.0;
			for (k = i; k < N; k++) {
				suma += *pa1 * *pb1;
				pa1++;
				pb1 += N;
			}
			suma += mat1[i * N + j];
			C[i * N + j] = suma;
		}
	}

	free(mat1);
	free(mat2);

	return C;	
}
