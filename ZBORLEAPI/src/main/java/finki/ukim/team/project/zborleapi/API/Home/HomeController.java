package finki.ukim.team.project.zborleapi.API.Home;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping("/admin/hello")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<String> helloWorldAdmin(){
        return ResponseEntity.ok("Hello there you did it !!!");
    }

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<String> helloWorldUser(){
        return ResponseEntity.ok("Hello there you did it !!!");
    }
}
