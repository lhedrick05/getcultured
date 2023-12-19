package liftoff.atlas.getcultured.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import liftoff.atlas.getcultured.models.data.UserRepository;
import liftoff.atlas.getcultured.models.User;
import liftoff.atlas.getcultured.models.dto.LoginFormDTO;
import liftoff.atlas.getcultured.models.dto.SignUpFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    // TODO: Add config to handle all requests with "/" in path - https://stackoverflow.com/a/2515533
    @GetMapping(path= {"","/"}) // Handles requests for "user" and "user/",
    public String handleRootRequest() {
        /* TODO: Add logic to differentiate accessing user's authorization and route traffic in accordance.
        *   Examples: Not logged in? Serve login page. Logged in? Go to personal user profile page. */

        return "redirect:/user/login";
    }

    @GetMapping("login")
    public String displayUserLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        return "user/login";
    }

    @GetMapping("sign-up")
    public String displayUserCreationForm(Model model)  {
        model.addAttribute(new SignUpFormDTO());
        return "user/create-user";
    }

    @PostMapping("sign-up")
    public String processRegistrationForm(@ModelAttribute @Valid SignUpFormDTO signUpFormDTO, Errors errors, HttpServletRequest request, Model model) {

        if (errors.hasErrors()) {
            return "user/create-user";
        }

        User existingUsername = userRepository.findByUsername(signUpFormDTO.getUsername());
        User existingEmailAddress = userRepository.findByEmailAddress(signUpFormDTO.getEmailAddress());

        if (existingEmailAddress != null) {
            errors.rejectValue("emailAddress", "emailAddress.alreadyregistered", "An account using that email address has already been registered");
            return "user/create-user";
        }

        if (existingUsername != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            return "user/create-user";
        }

        String password = signUpFormDTO.getPassword();
        String verifyPassword = signUpFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            return "user/create-user";
        }

        User newUser = new User(signUpFormDTO.getUsername(), signUpFormDTO.getEmailAddress(), signUpFormDTO.getPassword());
        userRepository.save(newUser);
        AuthenticationController.setUserInSession(request.getSession(), newUser);

        // TODO: Take to personal profile page to add profile data
        return "redirect:";
    }

    @GetMapping("profile")
    public String displayPersonalUserProfile(Model model)  {
        return "user/user-profile";
    }

    @GetMapping("profile/view/{userId}")
    public String displayPublicUserProfile(Model model)  {
        return "user/public-profile";
    }
}