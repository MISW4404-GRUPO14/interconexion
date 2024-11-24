package model.data_structures;

import java.text.DecimalFormat;

public class RehashUtil {
    

	public static boolean isPrime(int n) {
	    if (n <= 1) return false;
	    if (n <= 3) return true; // Esto incluye n == 2 y n == 3

	    if (n % 2 == 0 || n % 3 == 0) return false;

	    return isPrimeByTrialDivision(n);
	}

	private static boolean isPrimeByTrialDivision(int n) {
	    for (int i = 5; i * i <= n; i += 6) {
	        if (n % i == 0 || n % (i + 2) == 0) {
	            return false;
	        }
	    }
	    return true;
	}

	public int nextPrime(int n) {
	    if (n <= 1) return 2;

	    int candidate = n + 1;

	    while (!isPrime(candidate)) {
	        candidate++;
	    }

	    return candidate;
	}
	
}