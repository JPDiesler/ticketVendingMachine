public class App {
    public static void main(String[] args) throws Exception {
        double value = 1462.88;
        calculateMoneyOutput(value);
    }

    public static void calculateMoneyOutput(double input){
        int amount = (int)(input * 100);
        double[] values = {0.01,0.02,0.05,0.1,0.2,0.5,1,2,5,10,20,50,100,200,500};
        int[] asCents = {1,2,5,10,20,50,100,200,500,1000,2000,5000,10000,20000,50000};
        String result = "";
        int count = 0;
        for (int i = values.length-1; i >= 0; i--){
            double value = values[i];
            int cent = asCents[i];
            count = 0;
            while (amount - cent >= 0){
                amount -= cent;
                count++;
            }
            if (count >0){
                result += count + "x "+value+"€; ";
            } 
        }
        System.out.println(result);
    }
}
