/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;

import schedulingsoftware.Entities.Appointment;

/**
 *
 * @author maxra
 */
public class ReportGenerator {

    public void GenerateApptTypesByMonth(ObservableList<Appointment> appointmentData) {
        String reportString = "";
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

                if (calAppt.get(Calendar.MONTH)+1 == month.getValue()) {
                    if (typeCountMap.containsKey(type)) {
                        typeCountMap.put(type, typeCountMap.get(type) + 1);
                    } else {
                        //key does not exists
                        typeCountMap.put(type, 1);
                    }

                }

            }
            reportString += month + "\n" + typeCountMap.toString() + "\n";
        }
        System.out.println(reportString);
    }

}
