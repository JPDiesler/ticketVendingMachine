public class App {
    public static void main(String[] args) throws Exception {
        double value = 1462.88;
        calculateMoneyOutput(value);
    }
    /*
     * Calculates the representation of a value in the euro currencysysten.
     * Converts the input into cents, to avoid a wired issue with rounding....
     * Tries to subtract the greatest possible value from the give input, then
     * adds one to the counter. If the resukt of the subtarct is negative, add count
     * and value to result string and repeat with the next smaller value.
     * If the result of the subtarct is 0 the algorithm is finished
     */
    public static void calculateMoneyOutput(double input){
        //Convert to cents to avoid rounding errors by Java....
        int amount = (int)(input * 100);
        int amountInCents = (int) (input * 100);
    
    int[] denominationsInCents = {50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1};
    String result = "";

    for (int i = 0; i < denominationsInCents.length; i++) {
        int denomination = denominationsInCents[i];
        int count = amountInCents / denomination;
        
        if (count > 0) {
            result += count + "x " + (denomination / 100.0) + "€; ";
            amountInCents %= denomination; // Update the remaining amount
        }
    }
    
    System.out.println(result);
    }
}
