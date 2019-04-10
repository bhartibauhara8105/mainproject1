package com.niit.jobmanagement.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.niit.jobmanagement.entities.Jobs;
import com.niit.jobmanagement.entities.Resume;
import com.niit.jobmanagement.entities.User;
import com.niit.jobmanagement.repos.JobsRepository;
import com.niit.jobmanagement.repos.ResumeRepository;
import com.niit.jobmanagement.repos.UserRepository;

@Controller
public class UserController {
int count=0;
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	JobsRepository jobRepository;
	
	@Autowired
	ResumeRepository resumeRepository;
	
	@RequestMapping("/showReg") 
	public String showRegisterationPage()
	{
		System.out.println(" %%%%%%%%%%%%%%%%  showing register");
		return "login/registerUser";
	}
	
	

	@RequestMapping("/userError") 
	public String showError(ModelMap modelMap)
	{
		return"login/userError";
	}
	
	
	
	@RequestMapping("/userModule") 
	public String showuserModule(ModelMap modelMap)
	{
		if(count!=0) {
			List<Jobs> jobs=jobRepository.findAll();
			modelMap.put("jobs", jobs);
		return "login/userModule";
		}
		else
			return showLoginPage();
	}
	
	
	
	
	@RequestMapping("/showLogin") 
	public String showLoginPage()
	{
		System.out.println(" %%%%%%%%%%%%%%%%  showing login");
		return "login/login";
	}
	
	
	@RequestMapping(value="/registerUser",method=RequestMethod.POST)
	public String register(@RequestParam("email")String email,@ModelAttribute("user") User user, ModelMap modelMap)
	{
		
		System.out.println("lklklaklaklaka:   "+user.getEmail());
		
		userRepository.save(user);
		User u=userRepository.findByEmail(email);
		
		System.out.println("email is: "+u.getId());
		modelMap.addAttribute("userid",u.getId());
		return "register/resume";
	}
	
	
	//@RequestMapping(value="/login",method=RequestMethod.POST)
	@RequestMapping("/login") 
	public String login(@RequestParam("email")String email,@RequestParam("password")String password,ModelMap modelMap,HttpSession session)
	{
		System.out.println("in login module");
		
		User user=userRepository.findByEmail(email);
		
		
		
		if(user==null) {
			modelMap.addAttribute("msg", "Invalid username or password.Please try again...");
			return"login/login";
		}
		else {
			
		System.out.println("user "+user);
		System.out.println("***********************************************    "+user.getPassword()+"    "+password);
		
		if(user.getPassword().equals(password))
		{
			if(user.getRole().equals("Job Seeker"))
			{	count++;
				
				System.out.println("listinggg "+user.getId());
				
				List<Resume> resume=resumeRepository.findAll();
				
				session.setAttribute("username", email);
				session.setAttribute("userid", user.getId());
				List<Jobs> jobs=jobRepository.findAll();
				for(int i=0;i<resume.size();i++)
				{
					System.out.println("-------- "+user.getId()+" "+resume.get(i).getUser().getId());
					if(user.getId().equals(resume.get(i).getUser().getId()))
					{
						session.setAttribute("resume", resume.get(i).getId());
					}
					
					
				}
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$      "+resume.size()+" "+resume.get(0).getId());
				
				
				System.out.println("jobbbsss "+jobs.get(0).getCategory());
				
				 modelMap.put("jobs", jobs);
				//session.setAttribute("flag1", 0);
				return "login/userModule";
			}
			else {
				count++;
				return "hrModule";
			}
			
		}
		else {
			count=0;
			modelMap.addAttribute("msg", "Invalid username or password.Please try again...");
		
		}
		
		return"login/login";
	}
	
	}
	
	
	
}
