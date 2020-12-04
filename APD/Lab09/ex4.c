/******************************************************************************
* FILE: mpi_hello.c
* DESCRIPTION:
*   MPI tutorial example code: Simple hello world program
* AUTHOR: Blaise Barney
* LAST REVISED: 03/05/10
******************************************************************************/
#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#define  MASTER		0

int main (int argc, char *argv[])
{
    int   numtasks, rank, len;
    char hostname[MPI_MAX_PROCESSOR_NAME];

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);
    MPI_Get_processor_name(hostname, &len);
    int arr[100];
    int recv[25];
    if (rank == 0) {
        for (int i = 0; i < 100; i++) {
            arr[i] = i;
        }
    }
    MPI_Scatter(&arr, 25, MPI_INT, &recv, 25, MPI_INT, 0, MPI_COMM_WORLD);
    for (int i = 0; i < 25; i++) {
        recv[i] += 42;
    }
    MPI_Gather(&recv, 25, MPI_INT, &arr, 25, MPI_INT, 0, MPI_COMM_WORLD);
    if (rank == 0) {
        for (int i = 0; i < 100; i++) {
            printf("%d ", arr[i]);
        }
        printf("\n");
    }
    
    MPI_Finalize();

}

