#include <stdio.h>  
#include <stdlib.h>

int rows = 3, cols = 4;
int array[3][4] = { {5,8,7,6,}, {1,4,3,2}, {11,12,10,9} };
int transpose[4][3];

int compare(const void *a, const void *b)
{
  int x = *(int *)a;
  int y = *(int *)b;

  return (x - y);
}

int sortLines() {
  int j;
  // Sort lines of matrix using qsort:
  for(int j = 0; j < rows; j++)
    qsort(array[j], cols, sizeof(int), compare);
}

int sortColumns() {
  int i, j;
  // Find the transpose of matrix
  for (i = 0; i < rows; i++) {
    for (j = 0; j < cols; j++) {
      transpose[j][i] = array[i][j];
    }
  }

  // Sort lines of transpose (columns in orig) using qsort:
  for (int j = 0; j < cols; j++)
    qsort(transpose[j], rows, sizeof(int), compare);

  // Return to original shape
  for (i = 0; i < cols; i++) {
    for (j = 0; j < rows; j++) {
      array[j][i] = transpose[i][j];
    }
  }  
}

int main()
{
  
  // Print the matrix unsorted:
  printf("\nUnsorted matrix:\n");
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      printf("%2d, ", array[i][j]);
    }
    printf("\n");
  }

  sortLines();

  // Print the matrix sorted:
  printf("\nSorted rows:\n");
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      printf("%2d, ", array[i][j]);
    }
    printf("\n");
  }

  sortColumns();

  // Print the matrix sorted:
  printf("\nSorted rows+columns:\n");
  for (int i = 0; i < rows; i++) {
    for (int j = 0; j < cols; j++) {
      printf("%2d, ", array[i][j]);
    }
    printf("\n");
  }

  return 0;
}