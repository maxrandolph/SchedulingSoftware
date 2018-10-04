/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import schedulingsoftware.Entities.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TablePosition;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

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
    public TableColumn<Customer, String> columnAddress;
    @FXML
    public TableColumn<Customer, String> columnPhone;

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
        initializeTableViewWithDatabase();

    }

    @FXML
    public void initializeTableViewWithDatabase() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            customerData = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT customer.customerId,customer.customerName,address.phone,address.address,address.address2,address.postalCode,city.city, country.country FROM customer \n"
                    + "left join address on address.addressId = customer.addressId\n"
                    + "left join city on address.cityId = city.cityId\n"
                    + "left join country on country.countryId = city.countryId;");
            while (rs.next()) {
                //get customers
                customerData.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8)));
            }

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }

        //Set cell values to tableCustomer.
        tableCustomer.setEditable(true);
        tableCustomer.getSelectionModel().setCellSelectionEnabled(true);

        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        columnPhone.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set all columns editable
        columnName.setEditable(true);
        columnAddress.setEditable(true);
        columnPhone.setEditable(true);
        //makes columns editable
        columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("addressStr"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phoneStr"));

        columnName.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCustomerName(event.getNewValue());
            updateData("customer", event.getNewValue(), customer.getCustomerId(), "customerName", "customerId");
        }
        );
        columnAddress.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCustomerName(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "address", "AddressId");
        }
        );
        columnPhone.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCustomerName(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "phone", "AddressId");
        }
        );
        tableCustomer.setItems(null);
        tableCustomer.setItems(customerData);

    }

    public void updateData(String tableName, String newValue, Integer id, String columnToUpdate, String entityIdName) {
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
            PreparedStatement statement = connection.prepareStatement("UPDATE " + tableName + " SET " + columnToUpdate + "=? WHERE " + entityIdName + "=?");

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

    public void handleBtnAddCustomerAction(ActionEvent event) throws SQLException, IOException {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            customerData = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            conn.createStatement().executeUpdate(
                    "insert into customer (customerName, addressId, active, createdBy, lastUpdateBy, createDate)values('new customer', 0, 1,'"
                    + SchedulingSoftware.currentUserName + "'" + SchedulingSoftware.currentUserName + "', now());");
//            customerData.add(new Customer(rs.getInt(1), "New Customer",
//                    rs.getInt(3), rs.getBoolean(4), rs.getDate(5),
//                    rs.getString(6), rs.getDate(7), rs.getString(8), rs.getString(9), rs.getString(10)));

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

}
