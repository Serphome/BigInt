public class Main
{
    public static void main(String[] args) {
        final BigInt firstBigInt = new BigInt("933935967366139758691011374");
        final BigInt secondBigInt = new BigInt("518707862453917924243987");
        final BigInt res = firstBigInt.divide(secondBigInt);
        System.out.println(res);
    }
}