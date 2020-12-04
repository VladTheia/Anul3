/*
 *  * Tema 2 ASC
 *   * 2020 Spring
 *    */
#include "utils.h"
#include <cblas.h>

/* 
 *  * Add your BLAS implementation here
 *   */
double* my_solver(int N, double *A, double *B) {
        printf("BLAS SOLVER\n");
        int i;
        double *auxA = malloc(N * N * sizeof(double));
        double *auxB = malloc(N * N * sizeof(double));
	for (i = 0; i < N * N; i++) {
		auxA[i] = A[i];
		auxB[i] = B[i];
	}

	// B * A^t
        cblas_dtrmm(CblasRowMajor, CblasRight, CblasUpper,
        CblasTrans, CblasNonUnit, N, N, 1, A, N, B, N);

	// A * A
        cblas_dtrmm(CblasRowMajor, CblasLeft, CblasUpper,
        CblasNoTrans, CblasNonUnit, N, N, 1, A, N, auxA, N);

	// A^2 * B
        cblas_dtrmm(CblasRowMajor, CblasLeft, CblasUpper,
        CblasNoTrans, CblasNonUnit, N, N, 1, auxA, N, auxB, N);

        for (i = 0; i < N * N; i++)
 		auxB[i] += B[i];

	free(auxA);

        return auxB;
}
