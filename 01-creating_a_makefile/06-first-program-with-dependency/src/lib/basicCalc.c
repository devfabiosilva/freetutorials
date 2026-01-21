#include <stddef.h>

double sum(double a, double b)
{
  return a + b;
}

double multiply(double a, double b)
{
  return a * b;
}

double subtract(double a, double b)
{
  return a - b;
}

double *divide(double a, double b)
{
  static double res;
  if (b != 0.0) {
    res = a / b;
    return &res;
  }

  return NULL;
}

