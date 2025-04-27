package ch.zhaw.mdm.energy.classification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTcontroller {
    @GetMapping("/ping")
    public String ping() {
    return "Sentiment app is up and running!";
    }    
    @GetMapping("/count")
    public int count() {
    return 42;
    }
    
}
