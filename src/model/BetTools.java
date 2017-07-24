package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import exceptions.ErrorsList;
import model.bet.SessionInfo;

public class BetTools {
	
	private static ArrayList<String> fibList = new ArrayList<>(101);
	
	static{
		loadFibList();
	}
	
	public static List<String> getFibList(){
		return fibList;
	}
	
	private static void loadFibList(){
	    	fibList.add("1");
			fibList.add("1");
			fibList.add("2");
			fibList.add("3");
			fibList.add("5");
			fibList.add("8");
			fibList.add("13");
			fibList.add("21");
			fibList.add("34");
			fibList.add("55");
			fibList.add("89");
			fibList.add("144");
			fibList.add("233");
			fibList.add("377");
			fibList.add("610");
			fibList.add("987");
			fibList.add("1597");
			fibList.add("2584");
			fibList.add("4181");
			fibList.add("6765");
			fibList.add("10946");
			fibList.add("17711");
			fibList.add("28657");
			fibList.add("46368");
			fibList.add("75025");
			fibList.add("121393");
			fibList.add("196418");
			fibList.add("317811");
			fibList.add("514229");
			fibList.add("832040");
			fibList.add("1346269");
			fibList.add("2178309");
			fibList.add("3524578");
			fibList.add("5702887");
			fibList.add("9227465");
			fibList.add("14930352");
			fibList.add("24157817");
			fibList.add("39088169");
			fibList.add("63245986");
			fibList.add("102334155");
			fibList.add("165580141");
			fibList.add("267914296");
			fibList.add("433494437");
			fibList.add("701408733");
			fibList.add("1134903170");
			fibList.add("1836311903");
			fibList.add("2971215073");
			fibList.add("4807526976");
			fibList.add("7778742049");
			fibList.add("12586269025");
			fibList.add("20365011074");
			fibList.add("32951280099");
			fibList.add("53316291173");
			fibList.add("86267571272");
			fibList.add("139583862445");
			fibList.add("225851433717");
			fibList.add("365435296162");
			fibList.add("591286729879");
			fibList.add("956722026041");
			fibList.add("1548008755920");
			fibList.add("2504730781961");
			fibList.add("4052739537881");
			fibList.add("6557470319842");
			fibList.add("10610209857723");
			fibList.add("17167680177565");
			fibList.add("27777890035288");
			fibList.add("44945570212853");
			fibList.add("72723460248141");
			fibList.add("117669030460994");
			fibList.add("190392490709135");
			fibList.add("308061521170129");
			fibList.add("498454011879264");
			fibList.add("806515533049393");
			fibList.add("1304969544928657");
			fibList.add("2111485077978050");
			fibList.add("3416454622906707");
			fibList.add("5527939700884757");
			fibList.add("8944394323791464");
			fibList.add("14472334024676221");
			fibList.add("23416728348467685");
			fibList.add("37889062373143906");
			fibList.add("61305790721611591");
			fibList.add("99194853094755497");
			fibList.add("160500643816367088");
			fibList.add("259695496911122585");
			fibList.add("420196140727489673");
			fibList.add("679891637638612258");
			fibList.add("1100087778366101931");
			fibList.add("1779979416004714189");
			fibList.add("2880067194370816120");
			fibList.add("4660046610375530309");
			fibList.add("7540113804746346429");
			fibList.add("12200160415121876738");
			fibList.add("19740274219868223167");
			fibList.add("31940434634990099905");
			fibList.add("51680708854858323072");
			fibList.add("83621143489848422977");
			fibList.add("135301852344706746049");
			fibList.add("218922995834555169026");
			fibList.add("354224848179261915075");
	}
	
	public static String verifyBet(SessionInfo info,BigDecimal startBet){
		if(info == null)
			return ErrorsList.CONNECTION_ERROR;
		if(info.getBalance().compareTo(startBet) <= 0)
			return ErrorsList.INSUFFICIENT_FUNDS;
		return "unknown Error";
	}
	
	public static int fibonacci(int n)  {
		if(n == 0)
			return 0;
	    else if(n == 1)
	        return 1;
	    else
	        return fibonacci(n - 1) + fibonacci(n - 2);
	 }
}
