package view;


import java.io.IOException;

import java.math.BigInteger;

import javax.faces.context.FacesContext;

import oracle.adf.model.BindingContext;

import oracle.binding.OperationBinding;

import java.security.*;

public class LoginBean {
    String username, password, role;
    Number patientId, doctorId, adminId;

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setAdminId(Number adminId) {
        this.adminId = adminId;
    }

    public Number getAdminId() {
        return adminId;
    }

    public void setUsername(String username) {
        this.username = username.toUpperCase();
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(password.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
              hashtext = "0"+hashtext;
            }
            password = hashtext;
        } catch (Exception e) {
             System.out.println(e.getMessage());
        }      
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPatientId(Number patientId) {
        this.patientId = patientId;
    }

    public Number getPatientId() {
        return patientId;
    }

    public void setDoctorId(Number doctorId) {
        this.doctorId = doctorId;
    }

    public Number getDoctorId() {
        return doctorId;
    }

    public LoginBean() {
        super();
    }
    
    public String navigate(){
        if(role.equals("patient")){
            OperationBinding ob = BindingContext.getCurrent().getCurrentBindingsEntry().getOperationBinding("loginAuthentication");
            ob.getParamsMap().put("Username", username);
            ob.getParamsMap().put("Password", password);
            
            String userRole = (String) ob.execute();
            System.out.println("userRole " + userRole);
            if(userRole == null)
                return "error";
            else{
                int d = Integer.valueOf(userRole);
                Number userId = (Integer) d;
                System.out.println("UserID : " + userId);
                setPatientId(userId);
                System.out.println("PatientID IN LOGIN : " + getPatientId());
                return "successPatient";
            }
        } else if(role.equals("doctor")){
             OperationBinding ob = BindingContext.getCurrent().getCurrentBindingsEntry().getOperationBinding("loginAuthenticationDoctor");
             ob.getParamsMap().put("Username", username);
             ob.getParamsMap().put("Password", password);
             
             String userRole = (String) ob.execute();
             System.out.println("userRole " + userRole);
             if(userRole == null)
                 return "error";
             else{
                 int d = Integer.valueOf(userRole);
                 Number userId = (Integer) d;
                 System.out.println("UserID : " + userId);
                 setDoctorId(userId);
                 System.out.println("DoctorID IN LOGIN : " + getDoctorId());
                 return "successDoctor";
             }
         } else if(role.equals("admin")){
            System.out.println("hello0");
             OperationBinding ob = BindingContext.getCurrent().getCurrentBindingsEntry().getOperationBinding("loginAuthenticationAdmin");
             ob.getParamsMap().put("Username", username);
             ob.getParamsMap().put("Password", password);
             System.out.println("hello1");
             String userRole = (String) ob.execute();
             System.out.println("userRole " + userRole);
             if(userRole == null)
                 return "error";
             else{
                 int d = Integer.valueOf(userRole);
                 Number userId = (Integer) d;
                 System.out.println("UserID : " + userId);
                 setAdminId(userId);
                 System.out.println("AdminID IN LOGIN : " + getAdminId());
                 return "successAdmin";
             }
        } else{
             return "error";
        }
    }
    
    public String logout() {
         FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Health-ViewController-context-root/faces/LoginUser");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
       return "logout";
    }
}
