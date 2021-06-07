## Open MP
### More information:
```
https://github.com/critoma/dad/tree/master/lectures/c08/HPC_OpenMP

```
##### 1.Please write C code using OpenMP for addition in paralel of the items from 1 array.
##### 2.Please write C code using OpenMP for finding the minimum of items from 1 array.
##### 3. OpenMP code for calculating in parallel of average o an array

### 1.Please write C code using OpenMP for addition in paralel of the items from 1 array.

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

### 2.Please write C code using OpenMP for finding the minimum of items from 1 array.

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

### 3. OpenMP code for calculating in parallel of average o an array

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

```
#include <iostream>
#include <omp.h>

int computeArrayMeanParallel(int* vec, int len) {
    int sum = 0;
#pragma omp parallel for reduction(+:sum) num_threads(2)
    for (int i = 0; i < len; i++) {
        printf("Computing in thread %d for i = %d\n", omp_get_thread_num(), i);
        sum += vec[i];
        printf("Sum in thread %d is = %d\n", omp_get_thread_num(), sum);
    }
    printf("Total sum is %d\n", sum);

    return sum /= len;
}

int* addTwoArraysToThirdArrayParallel(int* vec1, int* vec2, int len) {
    int* resultArray = new int[len];
    memset(resultArray, 0, len * sizeof(int));
#pragma omp parallel for num_threads(2)
    for (int i = 0; i < len; i++) {
        printf("THREAD_NO %d\n", omp_get_thread_num());
        resultArray[i] += vec1[i] + vec2[i];
    }

    for (int i = 0; i < len; i++) {
        printf("Elem %d in sum array is %d\n", i, resultArray[i]);
    }
    printf("\n");
    return resultArray;
}

int addTwoArraysParallel(int* vec1, int* vec2, int len) {
    int sum = 0;
#pragma omp parallel for reduction(+:sum) num_threads(2)
    for (int i = 0; i < len; i++) {
        sum += vec1[i] + vec2[i];
    }

    printf("Total sum of the two arrays is %d\n\n", sum);
}

double mapReduceArrayAverageParallel(int* arr, int length) {
    int sum = 0;

#pragma omp parallel for reduction(+:sum) num_threads(8)
    for (int i = 0; i < length; i++) {
        printf("THREAD_NUM %d\n", omp_get_thread_num());
        sum += arr[i];
    }
    return (double)sum / length;
}

int dotProductTwoArraysParallel(int* arr1, int* arr2, int length) {
    int dotProd = 0;

#pragma omp parallel for reduction(+:dotProd)
    for (int i = 0; i < length; i++) {
        dotProd += arr1[i] * arr2[i];
    }

    return dotProd;
}

int* matrixAdditionPerLineParallel(int** matrix, int n, int m) {
    int* matrixLinesSums = new int[n];
    memset(matrixLinesSums, 0, n * sizeof(int));

#pragma omp parallel for
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            matrixLinesSums[i] += matrix[i][j];
        }
    }

    return matrixLinesSums;
}

int main()
{
    int vec1_len = 5;
    int* vec1 = new int[vec1_len] { 1, 2, 3, 4, 10 };
    int mean = computeArrayMeanParallel(vec1, 5);
    printf("Parallel mean is %d\n\n", mean);

    int vec2_len = 8;
    int* vec2 = new int[vec2_len] { 10, 10, 10, 10, 10, 10, 10, 20 };
    int* sumVect = addTwoArraysToThirdArrayParallel(vec1, vec2, vec1_len);
    free(sumVect);

    int sumOfTwoArrays = addTwoArraysParallel(vec1, vec2, vec1_len);

    // Map + reduce - calculate the average of an array using 8 threads.
    printf("Map reduce mean on 8 threads is %f\n\n", mapReduceArrayAverageParallel(vec2, vec2_len));

    int dotProduct = dotProductTwoArraysParallel(vec1, vec2, vec1_len);
    printf("Dot product of vec1 and vec2 is %d\n\n", dotProduct);

    int** matrix = new int* [2];
    for (int i = 0; i < 2; i++) {
        matrix[i] = new int[5];
        for (int j = 0; j < 5; j++) {
            matrix[i][j] = 2;
        }
    }

    int* sumsPerLine = matrixAdditionPerLineParallel(matrix, 2, 5);

    for (int i = 0; i < 2; i++) {
        printf("Sum for line %d is %d\n", i + 1, sumsPerLine[i]);
    }
    printf("\n");
}



```
