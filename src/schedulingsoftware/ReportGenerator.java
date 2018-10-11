/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

import java.time.Month;
import java.util.Arrays;
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

        Map<String, Integer> typeCountMap = new HashMap<String, Integer>();

        for (Appointment appointment : appointmentData) {
            String type = appointment.getDescription();
            if (typeCountMap.containsKey(type)) {
                typeCountMap.put(type, typeCountMap.get(type) + 1);
            } else {
                //key does not exists
                typeCountMap.put(type, 1);
            }
            reportString += appointment.getDescription() + "\n";
        }

        for (final Month month : Month.values()) {
            System.out.println(month);
        }
        System.out.println(reportString);
    }

}
