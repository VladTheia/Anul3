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
    int root = 0;
    // int var = 5;
    int arr[100];
    if (rank == root) {
        for (int i = 0; i < 100; i++) {
            arr[i] = i;
        }
    }
    MPI_Bcast(&arr, 100, MPI_INT, root, MPI_COMM_WORLD);
    if (rank != root) {
        printf("%d got:\n", rank);
        for (int i = 0; i < 100; i++) {
            printf("%d\n", arr[i]);
        }
    }

    // printf ("Hello from task %d on %s!\n", rank, hostname);
    // if (rank == MASTER)
    //     printf("MASTER: Number of MPI tasks is: %d\n",numtasks);
    
    MPI_Finalize();

}

