package com.hca.oauth.controller;

import com.hca.oauth.model.InactiveUser;
import com.hca.oauth.model.ResetPassword;
import com.hca.oauth.model.User;
import com.hca.oauth.service.UserService;
import com.hca.oauth.service.impl.EmailService;
import com.hca.oauth.service.impl.InactiveUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import static com.hca.oauth.common.Constants.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private InactiveUserService inactiveUserService;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	private String emailhost = InetAddress.getLoopbackAddress().getHostAddress();;

	/**
	 * Received when user clicks on email sent
	 * 
	 * @param activationToken
	 * @param model
	 * @param httpServletResponse
	 */
	@GetMapping("/activateUser")
	public void activateUser(@RequestParam String activationToken, Model model,
			HttpServletResponse httpServletResponse) {
		System.out.println("*********** request received to actiavate activationToken=" + activationToken);
		InactiveUser inactiveUser = inactiveUserService.findById(activationToken);
		System.out.println(" ****** inactiveuser received =" + inactiveUser);
		if (inactiveUser == null) {
			System.out.println(" ****** inactiveuser received is null =");
			throw new RuntimeException("Malformed Token received.");
		} else {
			System.out.println(" ****** inactiveuser received is not null =");
			model.addAttribute("inactiveUser", inactiveUser);
			try {
				httpServletResponse.sendRedirect(changepwd + activationToken);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@GetMapping("/custom-login")
	public String loadLoginPage() {
		return "login";
	}

	@GetMapping("/changepwd")
	public String changepwd(@RequestParam String code, Model model) {
		InactiveUser inactiveUser = inactiveUserService.findById(code);
		model.addAttribute("inactiveUser", inactiveUser);
		return "change-password";
	}

	/**
	 * After email validation , user sets the password inactive record is deleted
	 * insert into user table with encrypted password.
	 * 
	 * @param inactiveuser
	 * @param httpServletResponse
	 */
	@PostMapping("/regcom")
	public void regcom(@ModelAttribute("inactiveUser") InactiveUser inactiveuser,
			HttpServletResponse httpServletResponse) {
		User user = new User();
		user.setEmail(inactiveuser.getEmail());
		user.setMobilenumber(inactiveuser.getMobilenumber());
		user.setName(inactiveuser.getName());
		user.setPassword(inactiveuser.getPassword());
		user.setVerified("Y");

		try {
			String token = userService.signUp(user);
			inactiveUserService.removeUserByEmail(inactiveuser.getEmail());
			httpServletResponse.sendRedirect("/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e.getMessage());
		}
	}

	@PostMapping("/signup")
	public String singup(@ModelAttribute("signup") InactiveUser user, HttpServletResponse httpServletResponse) {

		System.out.println("*****************in signup generated token not generated=");
		System.out.println("getEmail " + user.getEmail());
		System.out.println("getName " + user.getName());
		System.out.println("getMobilenumber " + user.getMobilenumber());
		inactiveUserService.findDuplicateByEmail(user.getEmail());
		userService.findDuplicateByEmail(user.getEmail());
		UUID randomid = UUID.randomUUID();
		user.setCode(randomid.toString());
		inactiveUserService.saveUser(user);

		// String token = userService.signUp(user);

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		System.out.println("email host=" + emailhost);
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Complete Registration!");
		mailMessage.setFrom("sales@mdstechsol.com");
		mailMessage.setText("To confirm your account, please click here : " + "http://" + emailhost
				+ ":8000/auth/activateUser?activationToken=" + randomid.toString());
		System.out.println("******* sending email =");

		emailService.sendEmail(mailMessage);

		/*
		 * return UriComponentsBuilder.fromUriString(homeUrl) .queryParam("auth_token",
		 * token) .build().toUriString();
		 */

		return "signin";

	}

	@PostMapping("/login")
	public String login(@ModelAttribute("signup") User user, Model model, HttpServletResponse httpServletResponse)
			throws IOException {

		System.out.println("Entered email= " + user.getEmail() + "  Entered password=" + user.getPassword());

		User userId = userService.fetchPassword(user.getEmail());
		 
		if (userId == null)
			throw new RuntimeException("Invalid Credentials");
		String entecpwd = bcryptEncoder.encode(user.getPassword());
		System.out.println(" System Password=" + userId.getPassword());
		System.out.println(" Entered Password=" + entecpwd);

		if (bcryptEncoder.matches(user.getPassword(), userId.getPassword())) {
			String token = userService.login(user);
			model.addAttribute("redirecturl", homeUrl + "?auth_token=" + token);
			return "login :: redirecturl";
		} else
			throw new RuntimeException("Invalid Credentials");

	}

	@PostMapping("/resetpwd")
	public String resetpwd(ResetPassword resetPassword, HttpServletResponse httpServletResponse) throws IOException {
		try {
	    System.out.println("recieved resetPassword="+resetPassword);
		User curpwd = userService.fetchPassword(resetPassword.getEmail());
		System.out.println("recieved curpwd="+curpwd);
		if(curpwd==null) {
			throw new RuntimeException("Invalid User! Password not reset!");
		}
		if (bcryptEncoder.matches(resetPassword.getCpwd(), curpwd.getPassword())) {
			System.out.println("* ** Passwords are same .** ");
			String entecpwd = bcryptEncoder.encode(resetPassword.getCpwd());
			curpwd.setPassword(entecpwd);
			userService.updateUser(curpwd);
		} else {
			System.out.println("* ** Passwords did not match .** ");
			return  "login :: resetpassword";
		}

		return "login :: resetpassword";
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

}
