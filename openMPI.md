## Open MPI 

### More information:
```
https://github.com/critoma/dad/tree/master/lectures/c08/openmpi

```
#### The sum of two arrays in MPI

```
#include<stdio.h>
#include<mpi.h>
#define SIZE 2
int main(){
    MPI_Init(NULL,NULL);
    int world_size;
    int rank;
    int finalSum = 0;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &world_size);

    int array[SIZE][3*SIZE] ={
        {1,2,3,4,5,6},
        {1,2,3,4,5,6},
    };
    int received[3*SIZE];
    int partialSums[SIZE] = {0,0};
    int nodeSum = 0;
    if(world_size == SIZE){
        MPI_Scatter(array, 3*SIZE, MPI_INT, received, 3*SIZE,MPI_INT, 0, MPI_COMM_WORLD);

        for(int i=0;i<3*SIZE;i++){
            nodeSum += received[i];
        }
        MPI_Gather(&nodeSum,1,MPI_INT, partialSums,1,MPI_INT,0,MPI_COMM_WORLD);

        if(rank == 0){
            for(int i=0;i<SIZE;i++){
                finalSum += partialSums[i];
            }
            printf("%d",finalSum);
        }
    }

    MPI_Finalize();
    return 0;
}

```
