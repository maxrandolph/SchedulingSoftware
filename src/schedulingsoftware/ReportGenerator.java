/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;

import schedulingsoftware.Entities.Appointment;
import schedulingsoftware.Entities.Customer;

/**
 *
 * @author maxra
 */
public class ReportGenerator {

    public void GenerateApptTypesByMonth(ObservableList<Appointment> appointmentData) throws FileNotFoundException, IOException {
        String reportString = "Schedule Report - Types by Month - Generated" + DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm").format(LocalDateTime.now()) + "\r\n\r\n";
        Map<Month, String> typeMap = new EnumMap<Month, String>(Month.class);
        Map<String, Integer> typeCountMap = new HashMap<>();

        ArrayList<String> typeCountList = new ArrayList<String>();
        Calendar calAppt = Calendar.getInstance();

        for (final Month month : Month.values()) {
            typeCountList.removeAll(typeCountList);
            typeCountMap.clear();

            for (Appointment appointment : appointmentData) {
                calAppt.setTime(appointment.getStart());
                String type = appointment.getDescription();

                if (calAppt.get(Calendar.MONTH) + 1 == month.getValue()) {
                    if (typeCountMap.containsKey(type)) {
                        typeCountMap.put(type, typeCountMap.get(type) + 1);
                    } else {
                        //key does not exists
                        typeCountMap.put(type, 1);
                    }

                }

            }
            reportString += month + "\r\n" + typeCountMap.toString() + "\r\n";
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("ScheduleReport" + DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(LocalDateTime.now()) + ".txt"));

            writer.write(reportString);

            writer.close();

        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        System.out.println(reportString);
    }

    public void GenerateAllConsultantSchedules() throws SQLException, IOException {
        String reportString = "Schedule Report - Consultant Schedules - Generated " + DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm").format(LocalDateTime.now()) + "\r\n\r\n";
        Connection conn = SchedulingSoftware.conManager.open();
        // Execute query and store result in a resultset
        Map<String, String> consultantScheduleMap = new HashMap<>();
        ResultSet rs = conn.createStatement().executeQuery(
                "select title, description, `start`, `end`, createdBy from appointment order by createdBy;");
        while (rs.next()) {

            if (consultantScheduleMap.containsKey(rs.getString(5))) {
                consultantScheduleMap.put(rs.getString(5), consultantScheduleMap.get(rs.getString(5)) + "\r\n" + rs.getString(1) + " - " + rs.getString(2) + " -- Start: " + rs.getString(3) + " End: " + rs.getString(4));
            } else {
                //key does not exists
                consultantScheduleMap.put(rs.getString(5), "\r\n" + rs.getString(1) + " - " + rs.getString(2) + " -- Start: " + rs.getString(3) + " End: " + rs.getString(4));
            }

            //get customers
        }
        String mapString = consultantScheduleMap.toString();

        mapString = mapString.replace(", ", "\r\n\r\n");
        mapString = mapString.replace("{", "");
        mapString = mapString.replace("}", "");
        mapString = mapString.replace("=", "\r\n-----");

        reportString += mapString;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("ConsultantScheduleReport" + DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(LocalDateTime.now()) + ".txt"));

            writer.write(reportString);

            writer.close();

        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }

        System.out.println(mapString);

    }

    public void GenerateAllCustomersLongevity() throws SQLException, IOException {
        String reportString = "Customer Report - Customer Loyalty - Generated " + DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm").format(LocalDateTime.now()) + "\r\n\r\n";
        Connection conn = SchedulingSoftware.conManager.open();
        // Execute query and store result in a resultset
        Map<String, String> customerLocationMap = new HashMap<>();
        ResultSet rs = conn.createStatement().executeQuery(
                "select customerId, CustomerName, createDate from customer order by createDate;");
        while (rs.next()) {
            customerLocationMap.put("ID: " + rs.getString(1), "\r\n" + rs.getString(2) + " - Customer Since: " + rs.getString(3));
        }

        //get customers
        String mapString = customerLocationMap.toString();

        mapString = mapString.replace(", ", "\r\n\r\n");
        mapString = mapString.replace("{", "");
        mapString = mapString.replace("}", "");
        mapString = mapString.replace("=", "\r\n-----");

        reportString += mapString;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("CustomerLongevityReport" + DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(LocalDateTime.now()) + ".txt"));
            writer.write(reportString);
            writer.close();

        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }

        System.out.println(mapString);

    }
}
