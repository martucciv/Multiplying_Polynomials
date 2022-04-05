/*Veronica Martucci
* COP3503 Spring 2022
* P5: Polynomial Multiplication
*/
import java.util.*;

public class poly {

    private int length;
    private long[] coeff;

    public static void main(String[] args){

        Scanner stdin = new Scanner(System.in);

        int n = stdin.nextInt();
        int len = (int)Math.pow(2,n) - 1;

        long[] temp1 = new long[len + 1];
        long[] temp2 = new long[len + 1];

        //create first object
        for(int i = len; i >= 0; i--){

            temp1[i] = stdin.nextInt();
        }
        poly poly1 = new poly(temp1);

        //create second object
        for(int i = len; i >= 0; i--){

            temp2[i] = stdin.nextInt();
        }
        poly poly2 = new poly(temp2);

        //calculate product
        poly res = poly1.mult(poly2);

        //print result
        StringBuilder sb = new StringBuilder();
        for(int i = res.length - 1; i >= 0; i--){
            sb.append(res.coeff[i]).append("\n");
        }
        String result = sb.toString();
        System.out.print(result);
    }

    // Creates a polynomial from the coefficients stored in vals. The polynomial created must store
    // exactly (1<<k) coefficients for some integer k.
    public poly(long[] vals){

        length = vals.length;
        coeff = vals;
    }

    // Both this and other must be of the same size and the corresponding lengths must be powers of 2.
    // Returns the sum of this and other in a newly created poly.
    public poly add(poly other){

        long[] temp = new long[this.length];
        poly res = new poly(temp);

        for(int i = 0; i < this.length; i++){
            res.coeff[i] = this.coeff[i] + other.coeff[i];
        }

        return res;
    }

    // Both this and other must be of the same size and the corresponding lengths must be powers of 2.
    // Returns the difference of this and other in a new poly.
    public poly sub(poly other){

        for(int i = 0; i < this.length; i++){

            this.coeff[i] -= other.coeff[i];
        }

        return this;
    }

    // Both this and other must be of the same size and the corresponding lengths must be powers of 2.
    // Returns the product of this and other in a newly created poly, using the regular nested loop algorithm,
    // with the length being the next power of 2.
    public poly multSlow(poly other){

        long[] temp = new long[this.length + other.length - 1];
        poly res = new poly(temp);

        for(int i = 0; i < this.length; i++){
            for(int j = 0; j < other.length; j++){
                res.coeff[i + j] += (this.coeff[i] * other.coeff[j]);
            }
        }

        return res;
    }

    // Both this and other must be of the same size and the corresponding lengths must be powers of 2.
    // Returns the product of this and other in a newly created poly, using Karatsuba’s algorithm, with
    // the length being the next power of 2.
    public poly mult(poly other){

        //Base case
        if(this.length < 32){

            return multSlow(other);
        }

        poly poly1Low = new poly(Arrays.copyOf(this.coeff, this.coeff.length / 2));
        poly poly2Low = new poly(Arrays.copyOf(other.coeff, other.coeff.length / 2));
        poly poly1High = new poly(Arrays.copyOfRange(this.coeff, this.length / 2, this.length));
        poly poly2High = new poly(Arrays.copyOfRange(other.coeff, other.length / 2, other.length));

        //multiply left and right side of the polynomial
        poly left = poly1Low.mult(poly2Low);
        poly right = poly1High.mult(poly2High);

        //middle part of the foiling
        poly poly1Sum = poly1High.add(poly1Low);
        poly poly2Sum = poly2High.add(poly2Low);
        poly mid = poly1Sum.mult(poly2Sum);

        long[] t = new long[left.length];
        poly temp = new poly(t);
        for(int i = 0; i < left.length; i++){
            temp.coeff[i] = left.coeff[i] + right.coeff[i];
        }
        mid = mid.sub(temp);

        //Put result back together
        long[] r = new long[this.length << 1];
        poly res = new poly(r);

        //put result back together
        res = res.getLeft(left);
        res = res.getRight(right, this.length);

        int shift = this.length / 2;
        for (int i = shift; i < shift + mid.length; i++)
            res.coeff[i] += mid.coeff[i - shift];

        return res;
    }

    // Returns the left half of this poly in its own poly.
    private poly getLeft(poly other){

        for (int i = 0; i < other.length; i++)
            this.coeff[i] += other.coeff[i];

        return this;
    }

    // Returns the right half of this poly in its own poly.
    private poly getRight(poly other, int shift){

        for (int i = shift; i < shift + other.length; i++)
            this.coeff[i] += other.coeff[i];

        return this;
    }
}
