##Open MP

#####1.Please write C code using OpenMP for addition in paralel of the items from 1 array.
#####2.Please write C code using OpenMP for finding the minimum of items from 1 array.
#####3. OpenMP code for calculating in parallel of average o an array

###1.Please write C code using OpenMP for addition in paralel of the items from 1 array.

```
#include<stdio.h>
#include <stdlib.h>
#include<omp.h>
#include<limits>
using namespace std;
#define N 1000
int main()
{
	int ar[N];
	int sumParallel=0;
	int sumSq = 0;
	srand(1);
	for (int i = 0; i < N; i++) {
		ar[i] = rand();
		sumSq += ar[i];
	}
#pragma omp parallel for reduction(+:sumParallel)
	for (int i = 0; i < N; i++) {

		sumParallel += ar[i];
	}
	printf("Sum S= %d\n", sumSq);
	printf("Sum P= %d\n", sumParallel);

	return 0;
}
```

###2.Please write C code using OpenMP for finding the minimum of items from 1 array.

```
#include<stdio.h>
#include <stdlib.h>
#include<omp.h>
#include<limits>
using namespace std;
#define N 1000
int main()
{
	int ar[N];
	srand(1);
	int minS=INT_MAX;
	int minP= INT_MAX;
	int noThreads = 4;
	for (int i = 0; i < N; i++) {
		ar[i] = rand();
		minS = minS > ar[i] ? ar[i] : minS;
	}
	int partialMin[4][100];
	for (int i = 0; i < noThreads; i++)
	{
		partialMin[i][0] = INT_MAX;
	}
#pragma omp parallel num_threads(noThreads)
	{
		int id = omp_get_thread_num();
		int chunks = N / noThreads;
		int lowLim = id * chunks;
		int upLim = id == (noThreads - 1) ? N : (id + 1) * chunks;
		for (int i = lowLim; i < upLim; i++) {
			partialMin[id][0] = partialMin[id][0]> ar[i]? ar[i]: partialMin[id][0];
		}
	}
	for (int i = 0; i < noThreads; i++)
	{
		minP = minP > partialMin[i][0] ? partialMin[i][0] : minP;
	}
	printf("Min S= %d\n", minS);
	printf("Min P= %d\n", minP);
	return 0;
}

```

###3. OpenMP code for calculating in parallel of average o an array

```
float arrayAverage(int* a, int n){
    float avg=0;
    int localSum=0;
#pragma omp parallel for scheduled(static) reduction(+:localSum)
    localSum=0;
    for(int i=0;i<n;i++){
        localSum+=a[i];
    }
    avg=localSum;
    return avg/n;
}
```
