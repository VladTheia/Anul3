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
    if (rank == 0) {
        var = 0;
    } else {
        MPI_Recv(&var, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        printf("Process %d received var %d from process %d\n",
        rank, var, rank - 1);
    }
    var += 2;
    MPI_Send(&var, 1, MPI_INT, (rank + 1) % numtasks, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        MPI_Recv(&var, 1, MPI_INT, numtasks - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        printf("Process %d received var %d from process %d\n",
        rank, var, numtasks - 1);
    }
    
    MPI_Finalize();

}

