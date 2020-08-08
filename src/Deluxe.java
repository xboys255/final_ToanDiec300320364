public class Deluxe extends Savings implements  Compund_Interest {
    public Deluxe(String custNum, String custName, String savType, double deposit, int year) {
        super(custNum, custName, savType, deposit, year);
    }


    @Override
    public void generateTable(int year) {
        double  fixedinterest = 0.15;


    }
}
