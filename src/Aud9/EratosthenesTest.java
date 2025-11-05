package Aud9;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class EratosthenesSieve {

    public static boolean isPrime(int number) {
        for(int i=2;i<=number/2;i++) {
            if(number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getPrimes(int n) {
        List<Integer> primes = new ArrayList<Integer>();
        IntStream.rangeClosed(2, n).forEach(primes::add);
        for(int i=0;i<primes.size();i++) {
            int current = primes.get(i);
            if(isPrime(primes.get(i))) {
                primes.remove(current);
            }
        }
        return primes;
    }

}

public class EratosthenesTest {
    public static void main(String[] args) {
        EratosthenesSieve sieve = new EratosthenesSieve();
        List<Integer> primes = sieve.getPrimes(1000);
        primes.forEach(System.out::println);
    }
}
