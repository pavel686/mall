import java.util.UUID;


public class testUUID {
	
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
        for(int i=0;i<10;i++){
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            System.out.println(uuid);
        }
    }

}
