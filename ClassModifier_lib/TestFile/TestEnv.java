//This is a practise Java file which can help you out in obtaining smali commands for a given java command. After making changes save this file to check its corresponding smali version

public class TestEnv {

	public static void main(String[] args) {

	}

	public TestEnv(){
		
	}

	public void sayHello(){
		System.out.println("Hello");
	}

	public String returnHello(){
		return "Hello";
	}

	public int returnNumber(){
		return 5;
	}

	public void checkEqualOperation(int i){
		if(i==9999999){
			System.out.println("They are equal");
		}
	}

	public void checkNotEqualOperation(int i){
		if(i!=9999999){
			System.out.println("They are not equal");
		}
	}
	
}
