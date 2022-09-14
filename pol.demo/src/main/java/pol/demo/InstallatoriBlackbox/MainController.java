package pol.demo.InstallatoriBlackbox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pol.demo.InstallatoriBlackbox.Models.Request;
import pol.demo.InstallatoriBlackbox.Models.Response;
import pol.demo.InstallatoriBlackbox.Services.ViaSatService;

import java.io.IOException;

@RestController
public class MainController {

    @Autowired
    private ViaSatService viasat;

    @GetMapping("/api/GetInstallatori")
    public Response GetInstallatori() throws IOException {

        Response r =viasat.GetInstallatoriViasat(new Request());

        return r;
    }

}
