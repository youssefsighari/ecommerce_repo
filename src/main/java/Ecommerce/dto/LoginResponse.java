package Ecommerce.dto;
  
 

public class LoginResponse {
    private String jwt;
    private Long userId;  
    private String firstName;

    public LoginResponse(String jwt, Long userId, String firstName) {
        this.jwt = jwt;
        this.userId = userId;
        this.firstName = firstName;
    }
 
   
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long customerId) {
        this.userId = customerId;
    } 

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}

