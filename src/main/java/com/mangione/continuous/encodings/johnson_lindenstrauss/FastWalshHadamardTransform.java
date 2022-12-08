package com.mangione.continuous.encodings.johnson_lindenstrauss;

import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

public class FastWalshHadamardTransform {

    private final DMatrixRMaj
            output;

    FastWalshHadamardTransform(int rows){
        if (!is_power_of_two(rows)) throw new IllegalArgumentException();
        output = new DMatrixRMaj(rows,1);
    }

    public DMatrixRMaj apply(DMatrixRMaj input){
        output.set(input);
        apply_in_place(output);
        return output;
    }

    static int sum_bits(int p){
        int c = 0;
        while(p>0){
            c+=p%2;
            p/=2;
        }
        return c;
    }

    static boolean is_power_of_two(int p){
        return sum_bits(p)==1;
    }

    static double hadamard_coefficient(int i, int j){
        double p = 1.0;
        return sum_bits(i&j)%2==0?1.0:-1.0;
    }

    public static void apply_in_place(DMatrixRMaj a){
        if (a.data.length != a.getNumRows()) throw new IllegalArgumentException();
        apply_in_place(a,a.getNumRows());
    }

    public static void apply_in_place(DMatrixRMaj a, int extent){
        if (a.getNumCols() != 1) throw new IllegalArgumentException();
        apply_in_place(a.getData(), a.getNumRows(), extent);
    }

    public static void apply_in_place(double[] a, int size, int initialExtent){
        apply_in_place(a,size,initialExtent,0);
    }

    public static void apply_in_place(double[] a, int size, int initialExtent,int offset) {
        int extent = initialExtent;
        int butterflySize=2;
        while(butterflySize <= size)
        {
            int butterfliesToCover = extent/butterflySize + (extent%butterflySize> 0?1:0);
            for(int i = 0; i< butterfliesToCover;i+=1){
                butterfly(a, i*butterflySize+offset, butterflySize/2);
            }
            extent = butterflySize*butterfliesToCover;
            butterflySize *= 2;
        }
    }

    public static void populateWHColumn(double[] a, int size,int columnNumber){
        populateWHColumn(a,size,columnNumber,0);
    }

    public static void populateWHColumn(double[] a, int size,int columnNumber,int offset){
        for(int i=0;i<size;i++){
            a[i+offset] = i==columnNumber?1.0:0.0;
        }
        int butterflySize=2;
        while(butterflySize<=size){
            int i = columnNumber/butterflySize;
            butterfly(a, i*butterflySize+offset, butterflySize/2);
            butterflySize*=2;
        }
    }

    private static void butterfly(double[] a, int i, int h) {
        for(int j=i; j< i+h;j++){
            double x = a[j];
            double y = a[j+h];
            a[j] = x+y;
            a[j+h] = x-y;
        }
    }
}
