/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

import java.net.URL;
import java.sql.Connection;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import schedulingsoftware.Entities.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TablePosition;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author maxra
 */
public class MainViewController implements Initializable {

    @FXML
    public TableView<Customer> tableCustomer;

    @FXML
    public TableColumn<Customer, String> columnName;
    @FXML
    public TableColumn<Customer, Integer> columnAddress;

    @FXML
    public Button btnSaveCustomer;
    @FXML
    public Button btnAddCustomer;
    @FXML
    public Button btnDeleteCustomer;

    private ObservableList<Customer> customerData;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDataFromDatabase();

    }

    @FXML
    public void loadDataFromDatabase() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            customerData = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                //get customers
                customerData.add(new Customer(rs.getInt(1), rs.getString(2),
                        rs.getInt(3), rs.getBoolean(4), rs.getDate(5),
                        rs.getString(6), rs.getDate(7), rs.getString(8)));
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }

        //Set cell values to tableCustomer.
        tableCustomer.setEditable(true);
        tableCustomer.getSelectionModel().setCellSelectionEnabled(true);

        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAddress.setCellFactory(TextFieldTableCell.<Customer, Integer>forTableColumn(new IntegerStringConverter()));
        columnName.setEditable(true);
        
        //makes columns editable
        columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("addressId"));

        columnName.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCustomerName(event.getNewValue());
            updateData("customer", event.getNewValue(), customer.getCustomerId());
        }
        );

        tableCustomer.setItems(null);
        tableCustomer.setItems(customerData);

    }

    public void updateData(String columnName, String newValue, Integer id) {
        Connection connection = null;
        try {
            connection = SchedulingSoftware.conManager.open();
            Statement con = connection.createStatement();
            //connection
            TablePosition pos = tableCustomer.getSelectionModel().getSelectedCells().get(0);
            int row = pos.getRow();
            TableColumn col = pos.getTableColumn();
            String data1 = (String) col.getCellObservableValue(row).getValue();
            //cell
            Customer row1 = tableCustomer.getSelectionModel().getSelectedItem();
            Integer c1 = row1.getCustomerId();
            //row
            //tableview variables

            PreparedStatement statement = connection.prepareStatement("UPDATE " + columnName + " SET customerName=? WHERE customerId=?");

            statement.setString(1, newValue);
            statement.setInt(2, id);
            System.out.println(statement);
            statement.executeUpdate();
            con.close();
            //Query       
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }
}
