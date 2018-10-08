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
    public TableColumn<Customer, String> columnAddress2;
    @FXML
    public TableColumn<Customer, String> columnZip;
    @FXML
    public TableColumn<Customer, String> columnCity;
    @FXML
    public TableColumn<Customer, String> columnCountry;
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
        GetDataFromDatabase();

        //Set cell values to tableCustomer.
        tableCustomer.setEditable(true);
        tableCustomer.getSelectionModel().setCellSelectionEnabled(true);

        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAddress2.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCountry.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCity.setCellFactory(TextFieldTableCell.forTableColumn());
        columnZip.setCellFactory(TextFieldTableCell.forTableColumn());
        columnPhone.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set all columns editable
        columnName.setEditable(true);
        columnAddress.setEditable(true);
        columnPhone.setEditable(true);
        columnAddress2.setEditable(true);
        columnCity.setEditable(true);
        columnCountry.setEditable(true);
        columnZip.setEditable(true);

        //makes columns editable
        columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnAddress2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        columnZip.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        columnName.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCustomerName(event.getNewValue());
            updateData("customer", event.getNewValue(), customer.getCustomerId(), "customerName", "customerId");
            RefreshCustomerTable();

        }
        );
        columnAddress2.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setAddress2(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "address2", "AddressId");
            RefreshCustomerTable();

        }
        );
        columnCity.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCity(event.getNewValue());
            updateData("city", event.getNewValue(), customer.getCityId(), "city", "CityId");
            RefreshCustomerTable();
        }
        );
        columnAddress.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setAddress(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "address", "AddressId");
            RefreshCustomerTable();
        }
        );
        columnPhone.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setPhone(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "phone", "AddressId");
            RefreshCustomerTable();
        }
        );
        columnZip.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setPostalCode(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "postalCode", "AddressId");
            RefreshCustomerTable();
        }
        );
        columnCountry.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCountry(event.getNewValue());
            updateData("country", event.getNewValue(), customer.getCountryId(), "country", "countryId");
            RefreshCustomerTable();
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

    // Event handle for Add User button click.
    public void handleBtnAddCustomerAction(ActionEvent event) throws SQLException, IOException {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            customerData = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            CreateNewCustomer();
            conn.createStatement().executeUpdate(
                    "insert into country (country, createdBy, lastUpdateBy, createDate)values('New City','"
                    + SchedulingSoftware.currentUserName + "','" + SchedulingSoftware.currentUserName + "', now());"
            );
            System.out.println("insert into country (country, createdBy, lastUpdateBy, createDate)values('New Country','"
                    + SchedulingSoftware.currentUserName + "','" + SchedulingSoftware.currentUserName + "', now());"
            );
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    @FXML
    private void handleBtnSaveChanges(ActionEvent event) {
        RefreshCustomerTable();
    }

    @FXML
    private void handleBtnDeleteUser(ActionEvent event) throws SQLException {
        try {
            Connection connection = SchedulingSoftware.conManager.open();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM country where countryId=?");
            Customer selectedCustomer = tableCustomer.getSelectionModel().getSelectedItem();

            statement.setInt(1, selectedCustomer.getCountryId());
            System.out.println(statement);

            statement.executeUpdate();
            RefreshCustomerTable();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    public void RefreshCustomerTable() {
        System.out.println("refreshing dat");
        customerData.removeAll(customerData);
        GetDataFromDatabase();
        tableCustomer.setItems(null);
        tableCustomer.setItems(customerData);

    }

    private void GetDataFromDatabase() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            customerData = FXCollections.observableArrayList();
            // Execute query and store result in a resultset
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT customer.customerId,customer.customerName,address.phone,address.address,address.address2,address.postalCode,city.city, country.country, address.addressId, city.cityId, country.countryId FROM customer \n"
                    + "left join address on address.addressId = customer.addressId\n"
                    + "left join city on address.cityId = city.cityId\n"
                    + "left join country on country.countryId = city.countryId;");
            while (rs.next()) {
                //get customers
                customerData.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getInt(9), rs.getInt(10), rs.getInt(11)));
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    // Returns Id of country just created.
    private int CreateNewCountry() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            // Execute query and store result in a resultset
            conn.createStatement().executeUpdate(
                    "insert into country (country, createdBy, lastUpdateBy, createDate)values('New City','"
                    + SchedulingSoftware.currentUserName + "','" + SchedulingSoftware.currentUserName + "', now());"
            );
//            System.out.println("insert into country (country, createdBy, lastUpdateBy, createDate)values('New Country','"
//                    + SchedulingSoftware.currentUserName + "','" + SchedulingSoftware.currentUserName + "', now());"
//            );

            ResultSet rs = conn.createStatement().executeQuery(
                    "select max(countryId) from country;");
            rs.next();
            return (int) ((Number) rs.getObject(1)).intValue();

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
            return 0;
        }
    }

    private int CreateNewCity() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            // Execute query and store result in a resultset
            String query
                    = "insert into city (city, createdBy, lastUpdateBy, createDate, countryId)values('New City','"
                    + SchedulingSoftware.currentUserName
                    + "','" + SchedulingSoftware.currentUserName
                    + "', now(),"
                    + Integer.toString(CreateNewCountry())
                    + ");";
            System.out.println(query);
            conn.createStatement().executeUpdate(query);

            ResultSet rs = conn.createStatement().executeQuery(
                    "select max(cityId) from city;");
            rs.next();
            return (int) ((Number) rs.getObject(1)).intValue();

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
            return 0;
        }
    }

    private int CreateNewAddress() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            // Execute query and store result in a resultset

            String query = "insert into address (address, address2, cityId, postalCode, phone, createdBy, lastUpdateBy, createDate)values('New Address',"
                    + "'',"
                    + Integer.toString(CreateNewCity())
                    + ",'00000'"
                    + ",'0000000000','"
                    + SchedulingSoftware.currentUserName
                    + "','" + SchedulingSoftware.currentUserName
                    + "', now());";
            System.out.println(query);

            conn.createStatement().executeUpdate(
                    query
            );

            ResultSet rs = conn.createStatement().executeQuery(
                    "select max(addressId) from address;");
            rs.next();
            return (int) ((Number) rs.getObject(1)).intValue();

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
            return 0;
        }
    }

    private void CreateNewCustomer() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            // Execute query and store result in a resultset
            String query = "insert into customer (customerName, addressId, active, createdBy, lastUpdateBy, createDate)values('new customer',"
                    + CreateNewAddress() + ",1,'"
                    + SchedulingSoftware.currentUserName + "','" + SchedulingSoftware.currentUserName + "', now());";
            System.out.println(query);
            conn.createStatement().executeUpdate(query);
            RefreshCustomerTable();

        } catch (SQLException ex) {
            System.err.println("Error" + ex);

        }
    }
}
