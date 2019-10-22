package view;

import java.sql.SQLException;

import oracle.jbo.domain.Number;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;

public class DoctorRateBean {
    Number appointmentId, rating;
    String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        System.out.println("AppointmentID" + getAppointmentId());
        String        amDef = "model.module.AppModuleAM";
        String        config = "AppModuleAMLocal";
        ApplicationModule am =
        Configuration.createRootApplicationModule(amDef,config);
        ViewObject vo = am.findViewObject("DoctorRatingValidation1");
        vo.setNamedWhereClauseParam("appId", getAppointmentId());
        vo.executeQuery();
        Row row = vo.first();
        String value = "";
        if(row != null){
                value = (String) row.getAttribute("RatingScore").toString();
        }
        Configuration.releaseRootApplicationModule(am,true);
        status = value;   
        return status;
    }

    public void setAppointmentId(Number appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Number getAppointmentId() {
        return appointmentId;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }

    public Number getRating() {
        return rating;
    }

    public DoctorRateBean() {
        super();
    }
    
    public String navigate(){
            System.out.println("AppointmentID" + getAppointmentId());
            String        amDef = "model.module.AppModuleAM";
            String        config = "AppModuleAMLocal";
            ApplicationModule am =
            Configuration.createRootApplicationModule(amDef,config);
            ViewObject vo = am.findViewObject("DoctorRating1");
            Row row = vo.createRow();
                  row.setAttribute("AppointmentId", getAppointmentId());
                  row.setAttribute("RatingScore", getRating());
                  vo.insertRow(row);
                  am.getTransaction().commit();   
            Configuration.releaseRootApplicationModule(am,true);
            return "back";
        }
}
