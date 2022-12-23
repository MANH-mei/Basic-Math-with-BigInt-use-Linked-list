import java.util.ArrayList;

public class BigInteger {
    private DigitList digits;
    private int sign;

    public BigInteger() {
        this.digits = null;
        this.sign = 1;
    }

    public BigInteger(DigitList L) {
        this.digits = L;
        this.sign = 1;
    }

    public BigInteger(int i, DigitList L) {
        this.digits = L;
        this.sign = sgn(i);
    }

    public BigInteger(int i) {
        this.digits = DigitList.digitize(Math.abs(i));
        this.sign = sgn(i);
    }

    public BigInteger(String str) {
        if (str.charAt(0) == '-') {
            str = str.substring(1);
            this.digits = DigitList.digitize(str);
            this.sign = -1;
        } else {
            this.digits = DigitList.digitize(str);
            this.sign = 1;
        }
    }

    public DigitList getDigits() {
        return this.digits;
    }

    public int getSign() {
        return this.sign;
    }

    public void setDigits(DigitList digits) {
        this.digits = digits;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    private int sgn(int i) {
        if (i < 0)
            return -1;
        else
            return 1;
    }

    public int length() {
        if (this.digits == null)
            return 0;
        else
            return this.digits.length();
    }

    public BigInteger copy() {
        if (this.digits == null)
            return new BigInteger(0);
        else
            return new BigInteger(this.sign, this.digits.copy());
    }

    public BigInteger trimDigit() {
        return new BigInteger(this.sign, DigitList.trimDigitList(this.digits));
    }

    public boolean equals(Object obj) {
        if (obj instanceof BigInteger) {
            BigInteger other = (BigInteger) obj;
            if (this.sign == other.sign && DigitList.compareDigitLists(this.digits, other.digits) == 0) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        if (this.digits != null) {
            String integer = "";
            DigitList tmp = this.digits;
            integer = tmp.getDigit() + integer;
            tmp = tmp.getNextDigit();
            while (tmp != null) {
                integer = tmp.getDigit() + integer;
                tmp = tmp.getNextDigit();
            }
            return ((this.sign == -1) ? "-" : "") + integer;
        } else
            return "";
    }

    public BigInteger leftDigits(int n) {
        return new BigInteger(this.digits.leftDigits(n));
    }

    public BigInteger rightDigits(int n) {
        return new BigInteger(this.digits.rightDigits(n));
    }

    public BigInteger shift(int n) {
        if (n < 1)
            return this;
        else {
            BigInteger clone = this.copy();
            clone.digits = new DigitList(0, clone.digits);
            return clone.shift(n - 1);
        }
    }

    /******************************** STUDENT'S CODE ********************************/

    public BigInteger add(BigInteger other) {
        // complete this code
        if (this.sign == other.sign) {
            return new BigInteger(this.sign, DigitList.addDigitLists(0, this.digits, other.digits)).trimDigit();
        } else {
            if (DigitList.compareDigitLists(this.digits, other.digits) == 0) {
                DigitList k = new DigitList();
                return new BigInteger(k);
            }
            else if (DigitList.compareDigitLists(this.digits, other.digits) == 1)
                return new BigInteger(this.sign, DigitList.subDigitLists(0, this.digits, other.digits)).trimDigit();
            else
                return new BigInteger(other.sign, DigitList.subDigitLists(0, other.digits, this.digits)).trimDigit();
        }
    }

    public BigInteger sub(BigInteger other) {
        // code here
        if (this.sign == other.sign) {
            //4-5= 4 + (-5)
            //-4-(-5)= -4 + 5
            int t = 1;
            if (this.sign > 0)
                t = -1;
            return add(new BigInteger(t, other.digits));
        } else {
            // 4-(-5)= 4 + 5
            //-4-5= (-4)+(-5)
            return add(new BigInteger(this.sign, other.digits));
        }
    }

    public BigInteger mul(BigInteger other) {
        // code here
        BigInteger long_num = this.copy();
        BigInteger short_num = other.copy();
        if (this.length()<other.length()){
            long_num.setDigits(other.getDigits());
            long_num.setSign(other.getSign());
            short_num.setDigits(this.getDigits());
            short_num.setSign(this.getSign());
        }
        int l = long_num.length();
        if (l < 2) {
            int x = Integer.parseInt(long_num.toString());
            int y = Integer.parseInt(short_num.toString());
            return new BigInteger(Integer.toString(x * y));
        } else {
            if(l%2!=0){
                DigitList k = long_num.getDigits();
                while(k.getNextDigit()!=null){
                    k=k.getNextDigit();
                }
                DigitList a = new DigitList(0, null);
                k.setNextDigit(a);
                l++;
            }
            DigitList p = short_num.getDigits();
            while (p.getNextDigit()!=null){
                p=p.getNextDigit();
            }
            int n = l- short_num.length();
            for(int i=0; i<n;i++){
                DigitList q = new DigitList(0,null);
                p.setNextDigit(q);
                p=p.getNextDigit();
            }
            BigInteger xleft =  long_num.leftDigits(l/2);
            BigInteger xright = long_num.rightDigits(l-l/2);
            BigInteger yleft = short_num.leftDigits(l/2);
            BigInteger yright = short_num.rightDigits(l-l/2);
            BigInteger t1 = xleft.mul(yleft);
            BigInteger t2 = ((xright.mul(yleft)).add(xleft.mul(yright))).shift(l/2);
            BigInteger t3 = (xright.mul(yright)).shift(l);
            BigInteger t4 = t1.add(t2.add(t3));
            if (long_num.sign!=short_num.sign) {
                t4.setSign(-1);
            }
            return t4;
        }
    }

    public static BigInteger pow (BigInteger X, BigInteger Y){
        // code here
        int count = Integer.parseInt(Y.toString());
        if(count==0)
            return new BigInteger(1);
        BigInteger k = new BigInteger(1);
        if(count==1)
            return X;
        return X.mul(pow(X,Y.sub(k)));
    }

    public static BigInteger factorial (BigInteger X){
        // code here
        int count = Integer.parseInt(X.toString());
        if(count==0)
            return new BigInteger(1);
        BigInteger k =new BigInteger(1);
        if (count==1)
            return new BigInteger(1);
        return X.mul(factorial(X.sub(k)));
    }

    public static BigInteger computeValue(ArrayList<BigInteger> operandArr, ArrayList<Character> operatorArr) {
        // complete - and * operator
        BigInteger output = operandArr.get(0);
        for (int j = 0; j < operatorArr.size(); j++) {
            switch (operatorArr.get(j)) {
                case '+':
                    output = output.add(operandArr.get(j + 1));
                    break;
                case '-':
                    output = output.sub(operandArr.get(j + 1));
                    break;
                case '*':
                    output = output.mul(operandArr.get(j + 1));
                    break;
                default:
                    break;
            }
        }
        return output;
    }
}