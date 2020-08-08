public class Regular extends Savings implements Compund_Interest {
    public Regular(String custNum, String custName, String savType, double deposit, int year) {
        super(custNum, custName, savType, deposit, year);
    }

    @Override
    public void generateTable(int year) {

    }
}
