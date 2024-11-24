package Ecommerce;

import java.io.FileOutputStream;
import java.io.PrintStream;

import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		try {
            FileOutputStream fos = new FileOutputStream("errors.log", true); // Append mode
            PrintStream printStream = new PrintStream(fos);
            System.setErr(printStream);
            System.err.println("Error logging initialized."); // Test log
        } catch (Exception e) {
            e.printStackTrace();
        }
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
