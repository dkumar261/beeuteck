package com.forecastera.service.usersettingsmanagement.services;
/*
 * @Author Kanishk Vats
 * @Create 13-08-2023
 * @Description
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forecastera.service.usersettingsmanagement.commonResponseUtil.Error;
import com.forecastera.service.usersettingsmanagement.dto.request.PostLoginCredentials;
import com.forecastera.service.usersettingsmanagement.dto.request.PostUserRegistration;
import com.forecastera.service.usersettingsmanagement.entity.UserLoginHistory;
import com.forecastera.service.usersettingsmanagement.entity.UserRegistration;
import com.forecastera.service.usersettingsmanagement.repository.UserLoginHistoryRepo;
import com.forecastera.service.usersettingsmanagement.repository.UserRegistrationRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class UserSettingsLoginService {

    private final RestTemplate restTemplate;

    private final WebClient webClient;

    @Autowired
    private UserLoginHistoryRepo userLoginHistoryRepo;

    @Autowired
    private UserRegistrationRepo userRegistrationRepo;

    @Autowired
    public UserSettingsLoginService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.webClient = WebClient.create();
    }

    @Value("${azure.ad.url}")
    private String url;

    @Value("${azure.ad.grant-type}")
    private String grant_type;

    @Value("${azure.ad.client-id}")
    private String client_id;

    @Value("${azure.ad.client-secret}")
    private String client_secret;

    @Value("${azure.ad.scope}")
    private String scope;

    private static final long TOKEN_EXPIRATION_TIME_IN_MINUTES = 240;
    private static final int SECRET_KEY_LENGTH = 256;

    /*
    Login Settings API
     */

    private String generateAccessToken(String username){

//        generating random secret key
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[SECRET_KEY_LENGTH / 8];
        secureRandom.nextBytes(bytes);
        Key key = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(bytes).getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME_IN_MINUTES * 60000)) // in milliseconds
                .signWith(key)
                .compact();
    }

    public Object newUserRegistration(PostUserRegistration postUserRegistration){
        List<UserRegistration> userRegistrationByEmailId = userRegistrationRepo.getUserRegistrationByEmailId(postUserRegistration.getEmailId());
        if(userRegistrationByEmailId!=null && !userRegistrationByEmailId.isEmpty()){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage("User already exist");
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }
        else{
            try {
                UserRegistration newUser = new UserRegistration(postUserRegistration);
                userRegistrationRepo.save(newUser);
            }
            catch (Exception e) {
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage("Error while creating new user");
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
        }
        return true;
    }

    public Object login(PostLoginCredentials postLoginCredentials, Boolean isAzureLogin){
        Map<String, Object> returnData = new HashMap<>();
        if(isAzureLogin!=null && isAzureLogin){
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            parameters.add("grant_type", grant_type);
            parameters.add("client_id", client_id);
            parameters.add("client_secret", client_secret);
            parameters.add("scope", scope);
            parameters.add("username", postLoginCredentials.getUsername());
            parameters.add("password", postLoginCredentials.getPassword());

            Map response;

            try {
                response = webClient.post()
                        .uri(url)
                        .body(BodyInserters.fromValue(parameters))
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();
            }
            catch(Exception e){

                log.info("Failed user login with email: " + postLoginCredentials.getUsername());

                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage("Please enter a valid email and password");
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }

            assert response != null;
            String accessToken = String.valueOf(response.get("access_token"));

            String[] chunks = accessToken.split("\\.");

            Base64.Decoder decoder = Base64.getUrlDecoder();

            String header = new String(decoder.decode(chunks[0]));
            String payload = new String(decoder.decode(chunks[1]));
            String signature = new String(decoder.decode(chunks[2]));

            String[] role = null;
            String name = null;
            String email = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(payload);

                // Retrieve a specific value from the payload
//            role = jsonNode.get("roles").asText();
                name = jsonNode.get("name").asText();
                email = jsonNode.get("upn").asText();
                JsonNode roleData = jsonNode.get("roles");
                if (roleData != null && roleData.isArray() && roleData.size() > 0) {
                    role = objectMapper.convertValue(roleData, String[].class);
                }
//            System.out.println(role);
            } catch (Exception e) {
                role = null;
            }
//      Map<String, Object> returnData = new HashMap<>();
            returnData.put("Role", role);
            returnData.put("Name", name);
            returnData.put("Email", email);
            returnData.put("Access Token", accessToken);

            log.info("User " + name + " logged in at " + new Date());

            String roleAll;

            if (role == null || role.length == 0) {
                roleAll =  "";
            }
            else {
                StringBuilder result = new StringBuilder(role[0]);

                for (int i = 1; i < role.length; i++) {
                    result.append(",").append(role[i]);
                }
                roleAll = result.toString();
            }

            UserLoginHistory currentUser = new UserLoginHistory(null, name, roleAll, accessToken, new Date(), null);

            userLoginHistoryRepo.save(currentUser);

            return returnData;

        }
        else{
            List<UserRegistration> userRegistrationData = userRegistrationRepo.getUserRegistrationByEmailId(postLoginCredentials.getUsername());

            if(userRegistrationData==null || userRegistrationData.isEmpty()){
                Error error = new Error();
                error.setRequestAt(new Date());
                error.setMessage("Please enter a valid email and password");
                error.setStatus(HttpStatus.BAD_REQUEST.value());
                return error;
            }
            else {
                if(userRegistrationData.get(0).getPassword().equals(postLoginCredentials.getPassword())){

                    UserRegistration currentUser = userRegistrationData.get(0);

                    String email = currentUser.getEmailId();
                    String role = currentUser.getRole();
                    String name = currentUser.getFirstName() + " " + currentUser.getLastName();
                    String accessToken = generateAccessToken(name);
                    returnData.put("Role", role);
                    returnData.put("Name", name);
                    returnData.put("Email", email);
                    returnData.put("Access Token", accessToken);

                    UserLoginHistory currentUserLoginRecord = new UserLoginHistory(null, name, role, accessToken, new Date(), null);

                    userLoginHistoryRepo.save(currentUserLoginRecord);
                }
                else{
                    Error error = new Error();
                    error.setRequestAt(new Date());
                    error.setMessage("Please enter a valid email and password");
                    error.setStatus(HttpStatus.BAD_REQUEST.value());
                    return error;
                }
            }

        }
        return returnData;
//        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

//        if (response.getStatusCode() == HttpStatus.OK) {
//            // Successful authentication, you can further validate user data if needed
//            // For example, you can make another request to the Microsoft Graph API to get user details
//            return true;
//        } else {
//            // Failed authentication
//            return false;
//        }
    }

    public Object logoutProcess(String username, String accessToken){

        List<UserLoginHistory> getUserLoginHistory = userLoginHistoryRepo.findLoginDetailsByUsernameAndAccessToken(username, accessToken);
        if(getUserLoginHistory==null || getUserLoginHistory.isEmpty()){
            Error error = new Error();
            error.setRequestAt(new Date());
            error.setMessage("Invalid access token");
            error.setStatus(HttpStatus.BAD_REQUEST.value());
            return error;
        }
        else{
            UserLoginHistory currentUser = getUserLoginHistory.get(0);
            currentUser.setLogoutTime(new Date());
            log.info("User " + getUserLoginHistory.get(0).getUsername() + " logged out at " + new Date());
            userLoginHistoryRepo.save(currentUser);
        }
        return true;
    }
}
