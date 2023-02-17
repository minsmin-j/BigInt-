import java.util.Arrays;
import java.util.Scanner;


class Compare {		//A와 B를 구분해주는 class
	public int compare(int[] numA, int[] numB) {
		int compare = 1;	//1이면 A >= B, 2이면 A < B
		int maxLength = Math.max(numA.length, numB.length);
		for(int j = maxLength - 1; j >= 0; j--) {
			if (numA[j] != numB[j]) {
				if(numA[j] > numB[j]) {
					compare = 1;
				}
				else if (numB[j] > numA[j]){
					compare = 2;
				}
				break;
			}
		}
		return compare;
	}
}


class LeadingZero {		//결과값 앞에 생성된 매우 많은 0을 제거해주는 class
	public static String removeZero(String sb) {
		String strPattern = "^0+(?!$)";
		sb = sb.replaceAll(strPattern, "");
		return sb;
	}
}


class Decimal{
	final int Maxdigit = 500;	//최대 자릿수
	String numA, numB;
	private int neg_expression;	//음수표현, 1이면 음수라는 뜻으로 -붙여서 출력
	private int maxLength;	//A와 B의 길이 중 큰 수
	private int compare;	//1이면 A >= B, 2이면 A < B

	private int[] numA_array;	//numA를 한 자리씩 담을 배열
	private int[] numB_array;	//numB를 한 자리씩 담을 배열
	private int[] result = new int[Maxdigit];	//연산 최종 결과값을 담을 배열

	Compare cpr = new Compare();


	public void setNum(String num1, String num2) {
		numA = num1;
		numB = num2;
		maxLength = Math.max(numA.length(), numB.length());
		neg_expression = 0;		//두 숫자를 초기화할 때마다 같이 초기화해야함
		Arrays.fill(result, 0);

		numA_array = new int[maxLength + 1];
		numB_array = new int[maxLength + 1];


		// A 초기화
		for(int i = numA.length() - 1, idx = 0; i >= 0; i--, idx++) {
			if (numA.charAt(i) != '-') {
				numA_array[idx] = numA.charAt(i) - '0';	// 맨 뒤 문자부터 역순으로 하나씩 저장
			}
		}


		// B 초기화
		for(int i = numB.length() - 1, idx = 0; i >= 0; i--, idx++) {
			if (numB.charAt(i) != '-') {
				numB_array[idx] = numB.charAt(i) - '0';	// 맨 뒤 문자부터 역순으로 하나씩 저장
			}
		}

		// 크기 비교 후 compare 값 결정
		compare = cpr.compare(numA_array, numB_array);
	}


	public String getResult(String num1, String num2, String operator) {
		StringBuilder sb = new StringBuilder();

		// 거꾸로 되어있는 숫자를 원래대로 바꾸어 sb에 대입
		for(int i = Maxdigit - 1; i >= 0; i--) {
			sb.append(result[i]);
		}

		String result = LeadingZero.removeZero(sb.toString());

		if (neg_expression == 1) {
			result = "-".concat(result);
		}

		// , 추가
		sb.setLength(0);
		sb.append(result);
		for(int i = result.length() - 3; i > 0; i-=3) {
			sb.insert(i, ",");
		}

		System.out.println("현재 계산 결과\n"+num1+operator+num2+" = "+sb+"\n");

		return result;
	}


	public void addDecimal() {
		if (numA.charAt(0) == '-' && numB.charAt(0) != '-') { //음수+양수
			numA = numA.replace("-", "");
			int [] temp = numA_array;
			numA_array = numB_array;
			numB_array = temp;
			subDecimal();
		}

		else if (numA.charAt(0) != '-' && numB.charAt(0) == '-') { //양수+음수
			numB = numB.replace("-", "");
			subDecimal();
		}

		else if (numA.charAt(0) == '-' && numB.charAt(0) == '-') { //음수+음수
			numA = numA.replace("-", "");
			numB = numB.replace("-", "");
			neg_expression = 1;
			addDecimal();
		}

		else {
			for(int i = 0; i < maxLength; i++) {
				int value = numA_array[i] + numB_array[i];
				result[i] += value % 10;
				result[i+1] += value / 10;
			}
		}
	}


	public void subDecimal() {
		if (numA.charAt(0) == '-' && numB.charAt(0) != '-') { //음수-양수
			numA = numA.replace("-", "");
			neg_expression = 1;
			addDecimal();
		}

		else if (numA.charAt(0) != '-' && numB.charAt(0) == '-') { // 양수-음수
			numB = numB.replace("-", "");
			subDecimal();
		}

		else if (numA.charAt(0) == '-' && numB.charAt(0) == '-') { // 음수-음수
			numA = numA.replace("-", "");
			numB = numB.replace("-", "");
			neg_expression = 1;
			addDecimal();
		}

		else {
			compare = cpr.compare(numA_array, numB_array);
			if (compare == 1) {		//A-B
				for(int i = 0; i < maxLength; i++) {
					if(numA_array[i] < numB_array[i]) {
						numA_array[i] += 10;
						numA_array[i + 1] -= 1;
					}
					result[i] = numA_array[i] - numB_array[i];
				}
			}
			else if (compare == 2) {	//-(B-A)
				for(int i = 0; i < maxLength; i++) {
					if(numA_array[i] > numB_array[i]) {
						numB_array[i] += 10;
						numB_array[i + 1] -= 1;
					}
					result[i] = numB_array[i] - numA_array[i];
				}
				neg_expression = 1;
			}
		}
	}


	public void mulDecimal( ) {
		if (numA.charAt(0) == '-' && numB.charAt(0) != '-') { // 음수*양수
			neg_expression = 1;
		}

		else if (numA.charAt(0) != '-' && numB.charAt(0) == '-') { // 양수*음수
			neg_expression = 1;
		}

		int[] temp_result = new int[500];
		int carry = 0;

		for(int i = 0; i <= numB.length(); i++) {
			for(int j = 0; j <= numA.length(); j++) {

				int value = numA_array[j] * numB_array[i] + carry;
				carry = value / 10;
				temp_result[i+j] = value % 10;

				result[i+j] = result[i+j] + temp_result[i+j] % 10;	// temp_result의 값을 바로 result에 대입
				if ( result[i+j]/10 > 0) {
					result[i+j+1] += result[i+j] / 10;
					result[i+j] -= 10;
				}
			}
		}
	}


	public void divDecimal() {
		if (numA.charAt(0) == '-' && numB.charAt(0) != '-') { // 음수/양수
			neg_expression = 1;
		}

		else if (numA.charAt(0) != '-' && numB.charAt(0) == '-') { // 양수/음수
			neg_expression = 1;
		}

		int[] quo = new int[500];
		quo[0] = 1;

		while (cpr.compare(numA_array, numB_array) == 1) {
			for(int i = 0; i < maxLength; i++) {	// A - B를 하고 나온 값을 A에 대입
				if(numA_array[i] < numB_array[i]) {
					numA_array[i] += 10;
					numA_array[i + 1] -= 1;
				}
				numA_array[i] = numA_array[i] - numB_array[i];
			}

			for(int i = 0; i < result.length - 1; i++) {	// 한 번 뺄셈을 할 때마다 몫이 +1
				int value = result[i] + quo[i];
				result[i] = value % 10;
				result[i+1] = value / 10;
			}
		}
	}
}



class Binary{
	final int Maxdigit = 500;
	String numA, numB;
	private int neg_expression;
	private int maxLength;
	private int carry;
	private int compare;

	private int[] numA_array;
	private int[] numB_array;
	private int[] result = new int[Maxdigit];

	Compare cpr = new Compare();


	public void setNum(String num1, String num2) {
		numA = num1;
		numB = num2;
		maxLength = Math.max(numA.length(), numB.length());
		neg_expression = 0;
		carry = 0;
		Arrays.fill(result, 0);

		numA_array = new int[maxLength+1];
		numB_array = new int[maxLength+1];


		// A 초기화
		for(int i = numA.length() - 1, idx = 0; i >= 0; i--, idx++) {
			numA_array[idx] = numA.charAt(i) - '0';	// 맨 뒤 문자부터 역순으로 하나씩 저장
		}


		// B 초기화
		for(int i = numB.length() - 1, idx = 0; i >= 0; i--, idx++) {
			numB_array[idx] = numB.charAt(i) - '0';	// 맨 뒤 문자부터 역순으로 하나씩 저장
		}


		// 크기 비교 후 compare 값 결정
		compare = cpr.compare(numA_array, numB_array);


	}


	public String getResult(String num1, String num2, String operator) {
		StringBuilder sb = new StringBuilder();

		// 2의 보수 취하기
		if (neg_expression == 1) {
			// result 뒤집기
			for (int i = 0; i < result.length; i++) {
				if (result[i] == 0) {
					result[i] = 1;
				}
				else if (result[i] == 1) {
					result[i] = 0;
				}
			}

			//뒤집은 result + 1
			int[] x = new int[result.length];	// result.length = 500
			x[0] = 1;
			carry = 0;
			for(int i = 0; i < result.length; i++) {
				int value = result[i] + x[i] + carry;
				switch (value) {
					case 0:
						result[i] = 0;
						carry = 0;
						break;
					case 1:
						result[i] = 1;
						carry = 0;
						break;
					case 2:
						result[i] = 0;
						carry = 1;
						break;
					case 3:
						result[i] = 1;
						carry = 1;
						break;
					default:
				}
			}
		}

		for(int i = result.length - 1; i >= 0; i--) {
			sb.append(result[i]);
		}


		String result = LeadingZero.removeZero(sb.toString());

		// 공백 추가
		sb.setLength(0);
		sb.append(result);
		for(int i = sb.length() - 4; i > 0; i-=4) {
			sb.insert(i, " ");
		}

		System.out.println("현재 계산 결과\n"+num1+operator+num2+" = "+sb+"\n");

		return result;
	}


	public void addBinary() {
		for(int i = 0; i <= maxLength; i++) {
			int value = numA_array[i] + numB_array[i] + carry;
			if (i == 500) {		//result의 최대 인덱스는 499
				break;
			}

			switch (value) {
				case 0:
					result[i] = 0;
					carry = 0;
					break;
				case 1:
					result[i] = 1;
					carry = 0;
					break;
				case 2:
					result[i] = 0;
					carry = 1;
					break;
				case 3:
					result[i] = 1;
					carry = 1;
					break;
				default:
			}

		}
	}


	public void subBinary() {
		if (compare == 1) {
			for(int i = 0; i < maxLength; i++) {
				if(numA_array[i] < numB_array[i]) {
					numA_array[i] += 2;
					numA_array[i+1] -= 1;
				}
				result[i] = numA_array[i]-numB_array[i];
			}
		}
		else if (compare == 2) {
			for(int i = 0; i < maxLength; i++) {
				if(numB_array[i] < numA_array[i]) {
					numB_array[i] += 2;
					numB_array[i+1] -= 1;
				}
				result[i] = numB_array[i]-numA_array[i];
			}
			neg_expression = 1;
		}
	}


	public void mulBinary() {
		andBinary();	//2진수의 곱 = AND
	}


	public void divBinary() {
		int[] quo = new int[500];
		quo[0] = 1;

		while (cpr.compare(numA_array, numB_array) == 1) {
			// A-B
			for(int i = 0; i < maxLength; i++) {
				if(numA_array[i] < numB_array[i]) {
					numA_array[i] += 2;
					numA_array[i + 1] -= 1;
				}
				numA_array[i] = numA_array[i] - numB_array[i];
			}

			// 한 번 뺄때마다 몫 +1
			carry = 0;
			for(int i = 0; i < result.length; i++) {
				int value = result[i] + quo[i] + carry;
				switch (value) {
					case 0:
						result[i] = 0;
						carry = 0;
						break;
					case 1:
						result[i] = 1;
						carry = 0;
						break;
					case 2:
						result[i] = 0;
						carry = 1;
						break;
					case 3:
						result[i] = 1;
						carry = 1;
						break;
					default:
				}
			}
		}
	}


	public void andBinary() {
		for (int i = 0; i < maxLength; i++) {
			int andCompare = numA_array[i] + numB_array[i];
			switch (andCompare) {
				case 0:
					result[i] = 0;
					break;
				case 1:
					result[i] = 0;
					break;
				case 2:
					result[i] = 1;
					break;
			}
		}
	}


	public void orBinary() {
		for (int i = 0; i < maxLength; i++) {
			int orCompare = numA_array[i] + numB_array[i];
			switch (orCompare) {
				case 0:
					result[i] = 0;
					break;
				case 1:
					result[i] = 1;
					break;
				case 2:
					result[i] = 1;
					break;
			}
		}
	}


	public void xorBinary() {
		for (int i = 0; i < maxLength; i++) {
			int xorCompare = numA_array[i] + numB_array[i];
			switch (xorCompare) {
				case 0:
					result[i] = 0;
					break;
				case 1:
					result[i] = 1;
					break;
				case 2:
					result[i] = 0;
					break;
			}
		}
	}


	public void leftshift() {
		int i = numA.length() - 1;
		for(int j = 0; j < numB.length(); j++) {	//i는 result의 시작 인덱스, A+B
			int k = 1;
			i += numB_array[j]*k;
			k = k * 10;
		}

		while (i >= 0) {
			int idx = i;
			for (int j = 0; j < numB.length(); j++) {	//idx는 A의 시작 인덱스, idx = i-B
				int k = 1;
				idx -= numB_array[j]*k;
				k = k * 10;
			}

			if (idx < 0) {
				result[i] = 0;
			}
			else {
				result[i] = numA_array[idx];
			}
			i--;
		}
	}


	public void rightshift() {
		for (int i = 0; i < numA.length(); i++) {
			int idx = i; // idx는 A의 시작 인덱스, idx = i + B
			for (int j = 0; j < numB.length(); j++) {
				int k = 1;
				idx += numB_array[j]*k;
				k = k * 10;
			}

			if (idx >= numA.length()) {
				result[i] = 0;
			}
			else {
				result[i] = numA_array[idx];
			}
		}
	}

}


public class PGcalculator {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("2진수 계산은 1, 10진수 계산은 2를 입력하여라.");
		int x = sc.nextInt();

		//10진수 계산
		if (x == 2) {
			System.out.println("10진수 계산 시작!");

			Decimal dec = new Decimal();

			System.out.print("첫번째 숫자 입력: ");
			String num1 = sc.next();
			if (num1.charAt(0) == '-') {
				System.out.println("현재 계산 결과\n0" + num1 + "=" + num1);
			}


			while (true) {
				System.out.print("연산자 입력 (+, -, *, /, 0입력시 종료): ");
				String operator = sc.next();
				if (operator.equals("0")) {
					System.out.println("계산 종료");
					break;
				}

				System.out.print("두번째 숫자 입력: ");
				String num2 = sc.next();
				dec.setNum(num1, num2);		//두 숫자 초기화


				//연산자 판단
				if (operator.equals("+")) {
					dec.addDecimal();
					num1 = dec.getResult(num1, num2, operator);
				}
				else if (operator.equals("-")) {
					dec.subDecimal();
					num1 = dec.getResult(num1, num2, operator);
				}
				else if (operator.equals("*")) {
					dec.mulDecimal();
					num1 = dec.getResult(num1, num2, operator);
				}
				else if (operator.equals("/")) {
					dec.divDecimal();
					num1 = dec.getResult(num1, num2, operator);
				}
				else {
					System.out.println("연산자 입력이 잘못 되었습니다.");
				}

			}
		}

		//2진수 계산
		if (x == 1) {
			System.out.println("2진수 계산 시작!");

			Binary bin = new Binary();

			System.out.print("첫번째 숫자 입력: ");
			String num1 = sc.next();


			while (true) {
				System.out.print("연산자 입력 (+, -, *, /, &, |, ^, <<, >>>, 0입력시 종료): ");
				String operator = sc.next();
				if (operator.equals("0")) {
					System.out.println("계산 종료");
					break;
				}

				System.out.print("두번째 숫자 입력: ");
				String num2 = sc.next();
				bin.setNum(num1, num2);


				//연산자 판단
				if (operator.equals("+")) {
					bin.addBinary();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals("-")) {
					bin.subBinary();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals("*")) {
					bin.mulBinary();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals("/")) {
					bin.divBinary();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals("&")) {
					bin.andBinary();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals("|")) {
					bin.orBinary();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals("^")) {
					bin.xorBinary();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals("<<")) {
					bin.leftshift();
					num1 = bin.getResult(num1, num2, operator);
				}
				else if (operator.equals(">>>")) {
					bin.rightshift();
					num1 = bin.getResult(num1, num2, operator);
				}
				else {
					System.out.println("연산자 입력이 잘못 되었습니다.");
				}

			}
		}
	}
}