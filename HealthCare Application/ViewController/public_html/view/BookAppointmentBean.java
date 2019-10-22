package view;

import java.sql.SQLException;

import model.module.AppModuleAMImpl;

import oracle.adf.share.ADFContext;

import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Date;

import oracle.jbo.domain.Number;
import oracle.jbo.Row;
import oracle.jbo.server.DBTransactionImpl;

public class BookAppointmentBean extends AppModuleAMImpl{
    Number doctorId, patientId,timeHour, timeMin;
    Date appointmentDate;
    String timeMeridian,status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {

        System.out.println("DoctorID" + getDoctorId());
        System.out.println("Date" + getAppointmentDate());
        System.out.println("TimeHour" + getTimeHour());
        System.out.println("TimeMinute" + getTimeMin());
        System.out.println("TimeMeridian" + getTimeMeridian());
       
        if(getAppointmentDate() == null && getTimeHour() == null && getTimeMin() == null && getTimeMeridian()==null)
            status = "Unknown";
        else{
            String        amDef = "model.module.AppModuleAM";
            String        config = "AppModuleAMLocal";
            ApplicationModule am =
            Configuration.createRootApplicationModule(amDef,config);
            ViewObject vo = am.findViewObject("AppointmentAvailable1");
            vo.setNamedWhereClauseParam("docId", getDoctorId());
            vo.setNamedWhereClauseParam("appDate", getAppointmentDate());
            vo.setNamedWhereClauseParam("hour", getTimeHour());
            vo.setNamedWhereClauseParam("min", getTimeMin());
            vo.setNamedWhereClauseParam("med", getTimeMeridian());
            vo.executeQuery();
            Row row = vo.first();
            String value;
            if(row == null)
                value = "Available";
            else
             value = "Unavailable";
            Configuration.releaseRootApplicationModule(am,true);
            status = value;
        }
        return status;
    }

    public void setDoctorId(Number doctorId) {
        this.doctorId = doctorId;
    }

    public Number getDoctorId() {
        return doctorId;
    }

    public void setPatientId(Number patientId) {
        this.patientId = patientId;
    }

    public Number getPatientId() {
        return patientId;
    }

    public void setTimeHour(Number timeHour) {
        this.timeHour = timeHour;
    }

    public Number getTimeHour() {
        return timeHour;
    }

    public void setTimeMin(Number timeMin) {
        this.timeMin = timeMin;
    }

    public Number getTimeMin() {
        return timeMin;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setTimeMeridian(String timeMeridian) {
        this.timeMeridian = timeMeridian;
    }

    public String getTimeMeridian() {
        return timeMeridian;
    }

    public BookAppointmentBean() {
        super();
    }
    
    public String  navigateTocreateAappointment(){
        System.out.println("DoctorID" + getDoctorId());
        return "createAppointment";
    }
    
    public String navigateConfirm(){
            String        amDef = "model.module.AppModuleAM";
            String        config = "AppModuleAMLocal";
            ApplicationModule am =
            Configuration.createRootApplicationModule(amDef,config);
            ViewObject vo = am.findViewObject("AppointmentVO1");
            Row row = vo.createRow();
            Number patientId;
                try {
                    patientId = new Number(ADFContext.getCurrent().getSessionScope().get("patientId"));
                    System.out.println("PatientID" + patientId);
                    row.setAttribute("AppointmentDate", getAppointmentDate());
                    row.setAttribute("AppointmentStatus", "SCHEDULED");
                    row.setAttribute("DoctorId", getDoctorId());
                    row.setAttribute("PatientId", patientId);
                    row.setAttribute("TimeHour", getTimeHour());
                    row.setAttribute("TimeMin", getTimeMin());
                    row.setAttribute("TimeMeridian", getTimeMeridian());
                    vo.insertRow(row);
                    am.getTransaction().commit();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                
            return "commit";
        }
    
}
