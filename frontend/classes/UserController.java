package grapevin;

import java.sql.SQLException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller("/controller")
class UserController {

    @GetMapping("/register")
    public String userCredentials(Model model){
       model.addAttribute("user", new User());
       return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) throws SQLException, ClassNotFoundException {
        SQLConnection conn = new SQLConnection("jdbc:mysql://localhost:3306/winedb?user=root&password=Foolcavetown201:3306/winedb", 
                                            "root", 
                                            "Foolcavetown201");
        String uname = user.getUsername();
        if (conn.findByUsername(uname) == user.getUsername()) {
            user.setUsername(uname);
            user.setPassword(user.getPassword());
            conn.saveUser(user);
            conn.closeConection();
            model.addAttribute("message",  "Registration successful");
            return "index";
        }
        conn.closeConection();
        model.addAttribute("message",  "Username already exists");
        return "index";
    }

    @GetMapping("/login")
    public String userLoginCredentials(Model model){
       model.addAttribute("user", new User());
       return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) throws SQLException, ClassNotFoundException {
        SQLConnection conn = new SQLConnection("jdbc:mysql://localhost:3306/winedb?user=root&password=Foolcavetown201:3306/winedb", 
                                                "root", 
                                                "Foolcavetown201");
        if ((conn.findByUsername(user.getUsername()) == user.getUsername()) && 
            (conn.findByPassword(user.getPassword()) == user.getPassword())) {
                conn.closeConection();
                model.addAttribute("message",  "Login successful");
                return "index";
        }
        conn.closeConection();
        model.addAttribute("message",  "Username or password wrong");
        return "index";                           
    }
}
