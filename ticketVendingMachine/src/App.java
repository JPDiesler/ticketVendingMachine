import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.List;
public class App {

    //Variables for colored console output
    private static String ANSI_RED = "\u001B[31m";
    private static String ANSI_GREEN = "\u001B[32m";
    private static String ANSI_BLUE = "\u001B[34m";
    private static String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {

        //Input scanner will be passed on to each function, since java 11 cannot handle multiple scanners correctly
        Scanner inputScanner = new Scanner(System.in);
        try {
            while(true){
            printSuccess("Welcome to FDS ticketVendingMachine!");
            printSuccess("We sell tickets for your train ride.");
            double value = ticketSelectionProcess(inputScanner);
            double overpay = paymentProcess(inputScanner, value);
            handleOverpayment(inputScanner, overpay);
            System.out.println("=====================================");
            System.out.println("Machine restarting, please standby...");
            System.out.println("=====================================");
            TimeUnit.SECONDS.sleep(3);
            System.out.println();
            System.out.println();
        }
        } catch (Exception e) {
            printInfo("FDS ticket vending machine will shut down!");
            System.out.println(e);
        }
        
        
        inputScanner.close();
    }

    /*
     * Displays the tickets available for selection
     */
    public static void displayTicketOptions(){
        printInfo("Available tickets:");
        System.out.println("A: 2,50€");
        System.out.println("B: 3,70€");
        System.out.println("C: 4,50€");
    }

    /*
     * Used to print red colored text to the console
     */
    public static void printError(String message){
        System.out.println(ANSI_RED+message+ANSI_RESET);
    }

    /*
     * Used to print red colored text to the console
     */
    public static void printSuccess(String message){
        System.out.println(ANSI_GREEN+message+ANSI_RESET);
    }

    /*
     * Used to print red colored text to the console
     */
    public static void printInfo(String message){
        System.out.println(ANSI_BLUE+message+ANSI_RESET);
    }

    /*
     * Guides the user to the ticket selection process
     * A ticket can either be for an adult or a child (50% off for children)
     * Can add a variable amount of tickets a once
     * Applies Bahncard dicount 20% if necessary
     */
    public static double ticketSelectionProcess(Scanner inputScanner){
        double total = 0;
        while(true){
            displayTicketOptions();
            System.out.println("Enter A,B or C:");
            String userInput = inputScanner.nextLine();
            boolean success = false;
            double ticketprice = 0;
            switch (userInput) {
                case "A":
                    ticketprice = 2.5;
                    success = true;
                    break;
                case "B":
                    ticketprice = 3.7;
                    success = true;
                    break;
                case "C":
                    ticketprice = 4.50;
                    success = true;
                    break;
                default:
                    printError("Invalid input, please try again.");
                    System.out.println();
            }

            if (success){
                System.out.println();
                printInfo("Ticket "+userInput+" has been selected");
                System.out.println();
                boolean child = false;
                String question = "Child ticket?";
                if (handleBooleanInput(question,inputScanner)){
                    ticketprice = (ticketprice/2);
                    child = true;
                    System.out.println();
                    printInfo("Child ticket selected");
                    System.out.println();
                }else{
                    System.out.println();
                    printInfo("Adult ticket selected");
                    System.out.println();
                }
                question = "How many tickets do you want to buy?";
                int count = handleIntegerInput(question, inputScanner, 1, Integer.MAX_VALUE);
                double price = count * ticketprice;
                total += price;
                System.out.println();
                String info = "Added x"+count+" Ticket "+userInput+" ";
                if (child){
                    info += "Children";
                } else {
                    info +="Adult";
                }
                printInfo(info);
                String priceInfo = count+" x "+String.format("%.2f€",ticketprice)+" = "+String.format("%.2f€",price);
                printInfo(priceInfo);
                String totalPriceInfo = "TOTAL: "+String.format("%.2f€",total);
                printInfo(totalPriceInfo);
                System.out.println();
                question = "Do you want to add more tickets?";
                if(!handleBooleanInput(question,inputScanner)){
                    System.out.println();
                    printInfo("No more tickets to add");
                    System.out.println();
                    break;
                }else{
                    System.out.println();
                    printInfo("Adding more tickets");
                    System.out.println();
                }
                
                
            }
        }
        String question ="Do you have a BahnCard?";
        if (handleBooleanInput(question,inputScanner)){
            System.out.println();
            printInfo("Applying 20% Bahncard discount.");
            
            total -= 0.20*total;
        }else{
            printInfo("User does not poses a Bahncard");
        }
        printInfo("Total: "+String.format("%.2f€", total));
        System.out.println();
        return total;
    }

    /* Handles the payment process
     * First asks to user for vouchers and handels them
     * Only accepts euro coins => 0.01€-2.00€
     * If any invalid currency is "insterted" it will be "ejected" and error will be displayed
     * If toPay <= 0 the payment process is finished successfully
     * If abort is pressed the payment process will be aborted and the already payed money will be "ejected"
     * If finished successfully overpay is returned, max. overpay 1.99€
     */
    public static double paymentProcess(Scanner inputScanner, double toPay){
        String question = "Do you have a voucher?";
        while((handleBooleanInput(question, inputScanner))){
            System.out.println();
            printInfo("User has a voucher");
            System.out.println();
            boolean success = false;
            while(!success){
                System.out.println("Please enter your voucher:");
                String input =  inputScanner.nextLine();
                String[] splitted = input.split("%");
                if(input.length() != 11 || !input.contains("FDS_TVM%")){
                    printError("Invalid voucher");
                    System.out.println();
                }else{
                    double value = Double.parseDouble(splitted[1])/100;
                    printSuccess("Voucher valid! Value:" + String.format("%.2f€", value));
                    System.out.println();
                    toPay -= value;
                    success = true;
                }
            }
        }
        System.out.println();
        printInfo("User has no a voucher");
        System.out.println();
        List<Double> VALID_COINS = List.of(2.00, 1.00, 0.50, 0.20, 0.10, 0.05, 0.02, 0.01);
        double payed = 0;
        while (toPay > 0){
            System.out.println("Remaining amount: "+String.format("%.2f€", toPay));
            printInfo("Please insert coins, valid coins: "+coinListToString(VALID_COINS));
            printInfo("Or type 'Abort' to abort the transaction.");
            String input = inputScanner.nextLine();
            double coin = -1.00;
            try{
                coin = Double.parseDouble(input);
            }catch(Exception e){
                if (input.equals("Abort")){
                    printInfo("Your ticked order has been aborted, retrive your money from the tray!");
                    calculateMoneyOutput(payed);
                    toPay = -2;
                    coin = -1;
                }else{
                    printError("Invalid coin! Ejected coin: "+coin);
                    printError("You should insert one of these coins "+coinListToString(VALID_COINS));
                }
                
            }finally{
                if (VALID_COINS.contains(coin)){
                    toPay -= coin;
                    payed += coin;
                }else{
                    if (coin != -1){ //Prevent error message on abortion
                    printError("Invalid coin! Ejected coin: "+coin);
                    printError("You should insert one of these coins "+coinListToString(VALID_COINS));
                }
                    
                }
            }
            System.out.println();
        }
        if (toPay != -2){
            printSuccess("Payment successful! Tickets will be printed shortly...");
            double overpay = -toPay;
            return overpay;
        }else{
            return 0;
        }
        
    }
    /*
     * Handles overpayment if necessary
     * User can choose between an voucher or his change
     * If a voucher is selected 'FDS_TVM%'+value in cents will be printed
     * If change is selected the machine will "eject" the change as coins
     */
    public static void handleOverpayment(Scanner inputScanner, double overpay){
        if(overpay > 0.0){
                String question = "You payed " + String.format("%.2f€", overpay)+" too much do you like a voucher?";
                if(handleBooleanInput(question, inputScanner)){
                    printSuccess("===========================");
                    printSuccess("Your voucher: FDS_TVM%"+(int)(overpay*100));
                    printSuccess("===========================");
                }else{
                    printInfo("Please retrieve your change:");
                    calculateMoneyOutput(overpay);
                }
            }
        System.out.println();
        System.out.println();
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

    /* Handles a boolean user input 
     * The user will be asked a given question
     * The user should answer with the typicall Y/n wich is converted to a boolean if successful
     * If the user fails to input Y/n, he will be prompted to try again
     */
    public static boolean handleBooleanInput(String question, Scanner inputScanner){
        while (true) {
            System.out.println(question);
            System.out.println("Enter Y/n:");
            String userInput = inputScanner.nextLine();
            if (userInput.equals("Y")){
                System.out.println();
                return true;
            }else if (userInput.equals("n")){
                System.out.println();
                return false;
            }else{
                printError("Invalid input, please try again.");
            }
            System.out.println();
        }
    }

    /* Handles a integer user input 
     * The user will be asked a given question
     * The user should answer with an integer,wich is then converted to int
     * If the user fails to input an integer, he will be prompted to try again
     */
    public static int handleIntegerInput(String question, Scanner inputScanner, int minimumValue, int maximumValue){
        while (true) {
            System.out.println(question);
            System.out.println("Enter a Number:");
            String userInput = inputScanner.nextLine();
            int value = minimumValue-1;
            boolean success = false;
            try {
                value = Integer.parseInt(userInput);
                success = true;
            } catch (Exception e) {
                printError("You should enter a Number!");
            }finally{
                if (success == true){
                    if (minimumValue <= value && value <= maximumValue){
                        System.out.println();
                        return value;
                    }else{
                        printError("Enter a number between "+minimumValue+" and "+maximumValue);
                    }
                }
            }
            System.out.println();
        }
    }

    /*
     * Formats a list of doubles to a string using the euro format
     */
    
    public static String coinListToString(List<Double> coins) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < coins.size(); i++) {
            result.append(String.format("%.2f€", coins.get(i)));
            if (i < coins.size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }
}
