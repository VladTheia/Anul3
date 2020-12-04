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
    int var;
    if (rank != 0) {
        var = rank;
        MPI_Send(&var, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
    } else {
        MPI_Status status;
        MPI_Recv(&var, 1, MPI_INT, MPI_ANY_SOURCE, 0, MPI_COMM_WORLD, &status);
        printf("Task %d received: %d from task %d\n", rank, var, status.MPI_SOURCE);
    }


    // printf ("Hello from task %d on %s!\n", rank, hostname);
    // if (rank == MASTER)
    //     printf("MASTER: Number of MPI tasks is: %d\n",numtasks);
    
    MPI_Finalize();

}

