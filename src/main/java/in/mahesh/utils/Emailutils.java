package in.mahesh.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

@Controller
public class Emailutils {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void mailSent(String to , String body , String subject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("hellridermahesh@gmail.com");
		message.setText(body);
		message.setTo(to);
		message.setSubject(subject);
		
		mailSender.send(message);
	}

}
