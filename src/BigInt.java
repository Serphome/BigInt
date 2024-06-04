import java.util.*;

class BigInt {
    private final ArrayList<Integer> digits;
    private final boolean isNegative;

    public BigInt(String data) { //основной конструктор
        isNegative = data.startsWith("-");
        String number;
        if (isNegative) {
            number = data.substring(1);
        } else {
            number = data;
        }
        digits = new ArrayList<>(data.length());
        for (char digit : number.toCharArray()) {
            this.digits.add(digit - '0');
        }
    }

    private BigInt(ArrayList<Integer> digits, boolean isNegative) { //вспомогательный конструктор
        this.digits = digits;
        this.isNegative = isNegative;
    }

    @Override
    public String toString() { // метод toString возвращает строку преобразовывая знак и массив digits в строку
        StringBuilder sb = new StringBuilder();
        if (isNegative) {
            sb.append("-");
        }
        for (int digit : digits) {
            sb.append(digit);
        }
        return sb.toString();
    }

    public static BigInt valueOf(long data) {// переводит интовое(long) значение в биг инт
        return new BigInt(String.valueOf(data));
    }

    private void removeZeros(ArrayList<Integer> result) { // вспомогательная функция убирающие нули(если они есть) из ArrayList
        while (!result.isEmpty() && result.get(0) == 0) {
            result.remove(0);
        }
    }

    private int compareTo(ArrayList<Integer> A, ArrayList<Integer> B) {// вспомогательная функция compareTo, необходима для сравнения двух чисел через ArrayList внутри циклов
        if (A.size() < B.size()) {
            return -1;
        } else if (A.size() > B.size()) {
            return 1;
        } else {
            int res = 0;
            for (int i = 0; i < A.size(); i++) {
                if (A.get(i) > B.get(i)) {
                    return 1;
                } else if (A.get(i) < B.get(i)) {
                    return -1;
                } else {
                    continue;
                }
            }
            return res;
        }
    }

    public int compareTo(BigInt data) {// основная функция compareTo сравнивающая два BigInt
        if (!isNegative && !data.isNegative) {
            return compareTo(digits, data.digits);
        } else if (!isNegative) {
            return 1;
        } else if (!data.isNegative) {
            return -1;
        } else {
            if (compareTo(digits, data.digits) == 1) {
                return -1;
            } else if (compareTo(digits, data.digits) == -1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private ArrayList<Integer> substrateHelp(ArrayList<Integer> A, ArrayList<Integer> B) { //вспомогательная функция substract оеализующая вычетание
        ArrayList<Integer> result = new ArrayList<Integer>();
        int carry = 0;
        int maxLength = Math.max(A.size(), B.size());
        while (A.size() < maxLength) {
            A.add(0, 0);
        }
        while (B.size() < maxLength) {
            B.add(0, 0);
        }
        for (int i = maxLength - 1; i >= 0; i--) {
            int digit1 = A.get(i);
            int digit2 = B.get(i);
            int diff = carry + digit1 - digit2;
            if (diff < 0) {
                diff += 10;
                carry = -1;
            } else {
                carry = 0;
            }
            result.add(diff);
        }
        Collections.reverse(result);
        removeZeros(result);
        removeZeros(A);
        removeZeros(B);
        return result;
    }

    private ArrayList<Integer> sum(ArrayList<Integer> A, ArrayList<Integer> B) {// вспомогательная функция sum реализующая сложение
        ArrayList<Integer> result = new ArrayList<Integer>();
        int carry = 0;
        int i = A.size() - 1;
        int j = B.size() - 1;
        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) {
                sum += A.get(i);
                i--;
            }
            if (j >= 0) {
                sum += B.get(j);
                j--;
            }
            carry = sum / 10;
            sum = sum % 10;
            result.add(sum);
        }
        Collections.reverse(result);
        return result;
    }

    public BigInt add(BigInt data) { // функция возвращающий новое число, являющееся суммой исходного и переданного чисел
        if (isNegative == data.isNegative) {
            return new BigInt(sum(digits, data.digits), isNegative);
        } else {
            int compare = compareTo(digits, data.digits);
            if (compare == 0) {
                return new BigInt("0");
            } else if (compare == 1) {
                return new BigInt(substrateHelp(digits, data.digits), isNegative);
            } else {
                return new BigInt(substrateHelp(data.digits, digits), data.isNegative);
            }
        }
    }

    public BigInt subtract(BigInt data) { // функция возвращающий новое число, являющееся разностью исходного и переданного чисел
        if (!this.isNegative && !data.isNegative) {
            if (compareTo(digits, data.digits) == 1) {
                return new BigInt(substrateHelp(digits, data.digits), false);
            } else if (compareTo(digits, data.digits) == -1) {
                return new BigInt(substrateHelp(data.digits, digits), true);
            } else {
                return new BigInt(substrateHelp(data.digits, digits), false);
            }
        } else if (isNegative && !data.isNegative) {
            return new BigInt(sum(digits, data.digits), true);
        } else if (!isNegative) {
            return new BigInt(sum(digits, data.digits), false);
        } else {
            if (compareTo(digits, data.digits) == 1) {
                return new BigInt(substrateHelp(digits, data.digits), true);
            } else {
                return new BigInt(substrateHelp(data.digits, this.digits), false);
            }
        }
    }

    private ArrayList<Integer> multi(ArrayList<Integer> A, ArrayList<Integer> B) { // вспомогательная фукнция реализующая умножение
        ArrayList<Integer> result = new ArrayList<Integer>(Collections.nCopies(A.size() + B.size(), 0));
        for (int j = B.size() - 1; j >= 0; j--) {
            for (int i = A.size() - 1; i >= 0; i--) {
                int digit_a = A.get(i);
                int digit_b = B.get(j);
                int dif = digit_a * digit_b + result.get(i + j + 1);
                result.set(i + j + 1, dif % 10);
                result.set(i + j, (dif - dif % 10) / 10 + result.get(i + j));
            }
        }
        removeZeros(result);
        return result;
    }

    public BigInt multiply(BigInt data) { // функция возвращающая новое число, являющееся произведением исходного и переданного чисел
        if((digits.size() == 1 && digits.get(0) == 0) || (data.digits.size() == 1 && data.digits.get(0) == 0))
        {
            return new BigInt("0");
        }
        return new BigInt(multi(digits, data.digits), isNegative ^ data.isNegative);
    }

    private int dev(ArrayList<Integer> A, ArrayList<Integer> B) {//вспомогательная функция которая вычитает одно число их драгого пока это возможно
        ArrayList<Integer> result = new ArrayList<>(A);
        int count = 0;
        while (compareTo(result, B) >= 0) {
            result = substrateHelp(result, B);
            count++;
            removeZeros(B);
            removeZeros(result);
        }
        return count;
    }

    private ArrayList<Integer> divideDigits(ArrayList<Integer> A, ArrayList<Integer> B) { // вспомогательная функция которая реализует деление в столбик
        ArrayList<Integer> part = new ArrayList<Integer>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        int buffer = 0;
        int buffer2 = 0;
        int index = 0;
        boolean flag = true;
        while (index < A.size()) {
            part.add(A.get(index));
            if (!result.isEmpty()) {
                buffer2++;
            }
            while (compareTo(part, B) < 0 && index + 1 < A.size()) {
                index++;
                part.add(A.get(index));
                if (!result.isEmpty()) {
                    buffer++;
                }
            }
            if (compareTo(part, B) >= 0) {
                int temp = dev(part, B);
                if (!flag) {
                    for (int i = 0; i < buffer; i++) {
                        result.add(0);
                    }
                }
                buffer = 0;
                buffer2 = 0;
                result.add(temp);
                flag = false;
                for (int i = 0; i < temp; i++) {
                    removeZeros(B);
                    part = substrateHelp(part, B);
                }
                index++;
            } else {
                break;
            }
        }
        for (int i = 0; i < buffer + buffer2; i++) {
            result.add(0);
        }
        return result;
    }

    public BigInt divide(BigInt data) {// функция возвращающая новое число, являющееся частным исходного и переданного чисел
        if (compareTo(digits, data.digits) < 0) {
            return new BigInt("0");
        } else {
            return new BigInt(divideDigits(digits, data.digits), isNegative ^ data.isNegative);
        }
    }
}