package view;

import java.math.BigInteger;

import java.security.MessageDigest;

import java.sql.SQLException;

import java.security.*;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;

public class PatientRegistrationBean {
    String firstName, lastName, email, password, contact, street, area, city, state, country, pin;

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email.toUpperCase();
    }

    public String getEmail() {
        return email;
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

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public PatientRegistrationBean() {
        super();
    }
    
    public String navigate(){
       String        amDef = "model.module.AppModuleAM";
       String        config = "AppModuleAMLocal";
       ApplicationModule am =
       Configuration.createRootApplicationModule(amDef,config);
       ViewObject vo = am.findViewObject("PatientValidation1");
       
       // to see user does not already exists
       vo.setNamedWhereClauseParam("username", getEmail());
       vo.executeQuery();
       Row row = vo.first();
       if(row != null)
       return "error";
       
       //to insert in address
       ViewObject vo1 = am.findViewObject("Address1");
       Row row1 = vo1.createRow();
               row1.setAttribute("Street", getStreet());
               row1.setAttribute("Area", getArea());
               row1.setAttribute("City", getCity());
               row1.setAttribute("StateProvince", getState());
               row1.setAttribute("Country", getCountry());
               row1.setAttribute("Pin", getPin());
               vo1.insertRow(row1);
               am.getTransaction().commit();
               
      //to get address id
       ViewObject vo2 = am.findViewObject("AddressValidation1");
       vo2.setNamedWhereClauseParam("vstreet", getStreet());
       vo2.setNamedWhereClauseParam("varea", getArea());
       vo2.setNamedWhereClauseParam("vcity", getCity());
       vo2.setNamedWhereClauseParam("vstate", getState());
       vo2.setNamedWhereClauseParam("vcountry", getCountry());
       vo2.setNamedWhereClauseParam("vpin", getPin());
       vo2.executeQuery();
       Row row2= vo2.first();
        try {
            System.out.println("Checkif" + row2.getAttribute("AddressId")); 
            Number addressId = new Number((String)row2.getAttribute("AddressId").toString());
           System.out.println("AddressID : " + addressId);
            //insert into patient table
            ViewObject vo3 = am.findViewObject("InsertPatient1");
            Row row3 = vo3.createRow();
                  row3.setAttribute("FirstName", getFirstName());
                  row3.setAttribute("LastName", getLastName());
                  row3.setAttribute("ContactNumber", getContact());
                  row3.setAttribute("Email", getEmail());
                  row3.setAttribute("Password", getPassword());
                  row3.setAttribute("AddressId", addressId);
                  vo3.insertRow(row3);
                  am.getTransaction().commit();   
            Configuration.releaseRootApplicationModule(am,true);
            return "successRegister";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       
        return "error";
   }
}
