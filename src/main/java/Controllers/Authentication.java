package Controllers;

import BusinessLogic.UserServices;
import ErrorClasses.NoSuchUsernameException;
import ErrorClasses.WrongPasswordException;
import Services.JWTService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class Authentication {

    @PostMapping(value = "/api/login")
    public ResponseEntity SignInUser (@RequestBody String requestBody) {
        try {
            JSONObject data = new JSONObject(requestBody);
            String userId = UserServices.signIn(data);
            String token = JWTService.createJWT(userId);
            return ResponseEntity.status(HttpStatus.OK).header("user-token", token).body("Logged in");
        } catch (NoSuchUsernameException | WrongPasswordException | JSONException  e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credentials are incorrect");
        }
    }

}
