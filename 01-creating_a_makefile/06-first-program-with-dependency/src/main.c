#include <stdio.h>
#include <basicCalc.h>

int main(int argc, char **argv)
{
  double a = 1, b = 2, c = 3, d = 12, *divRes;

  printf("\nCalculating values ...\n");
  printf("\na = %f and b = %f with sum a + b = %f", a, b, sum(a, b));
  printf("\nb = %f and c = %f with multiply b x c = %f", b, c, multiply(b, c));
  printf("\nc = %f and d = %f with subtract c - d = %f", c, d, subtract(c, d));

  divRes = divide(d, c);

  if (divRes)
    printf("\nd = %f and c = %f with divide d / c = %f", d, c, (double)*divRes);
  else
    printf("\nDivision by Zero");

  printf("\n\nThank you :)\n\n");

  return 0;
}

