import java.util.*;
public class VowelSequence
{
	public static void VowelSequence(String[] args) {
		Scanner in = new Scanner(System.in);
		String str = in.nextLine();
		System.out.println(getRequired(str));
	}
	
	public static String getRequired(String a){
	    String temp="",maxStr="";
	    int c =0,max=0;
	    a = a.toLowerCase();
	    for(int i=0;i<a.length();i++){
	        
	        if(a.charAt(i)=='a' || a.charAt(i)=='e' || a.charAt(i)=='i' || a.charAt(i)=='o' || a.charAt(i)=='u'){
	            temp+=a.charAt(i);
	            c++;
	        }
	        else{
	            if(max<c){
	                max = c;
	                maxStr=temp;
	            }
	            
	                c=0;
	                temp="";
	        }
	        
	       
	    }
	     if(max<c){
	            return temp;
	        }
	        else{
	            return maxStr;
	        }
	        
	}
}