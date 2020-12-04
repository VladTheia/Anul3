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
    int arr[5];
    if (rank == 0) {
        for (int i = 0; i < 5; i++) {
            arr[i] = i;
            MPI_Send(&i, 1, MPI_INT, 1, i, MPI_COMM_WORLD);
        }
    } else {
        MPI_Status status;
        for (int i = 0; i < 5; i++) {
            MPI_Recv(&arr + i, 1, MPI_INT, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
            printf("Task %d received: %d with tag %d\n", rank, arr[i], status.MPI_TAG);
        }
    }


    // printf ("Hello from task %d on %s!\n", rank, hostname);
    // if (rank == MASTER)
    //     printf("MASTER: Number of MPI tasks is: %d\n",numtasks);
    
    MPI_Finalize();

}

