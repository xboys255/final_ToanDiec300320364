public class Savings {
    private String custNum, custName, savType;
    private double deposit;
    private int year;

    public Savings(String custNum, String custName, String savType, double deposit, int year) {
        this.custNum = custNum;
        this.custName = custName;
        this.savType = savType;
        this.deposit = deposit;
        this.year = year;
    }

    public String getCustNum() {
        return custNum;
    }

    public void setCustNum(String custNum) {
        this.custNum = custNum;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getSavType() {
        return savType;
    }

    public void setSavType(String savType) {
        this.savType = savType;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
