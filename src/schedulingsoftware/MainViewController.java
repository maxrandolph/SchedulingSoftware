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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import schedulingsoftware.Entities.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import schedulingsoftware.Entities.Appointment;

/**
 * FXML Controller class
 *
 * @author maxra
 */
public class MainViewController implements Initializable {

    // Tables
    @FXML
    public TableView<Customer> tableCustomer;
    @FXML
    public TableView<Appointment> tableAppointment;
    @FXML
    public TableView<Appointment> tableAppointmentDateFiltered;

    // Customer columns
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

    // Appointment columns
    @FXML
    public TableColumn<Appointment, String> columnAptCurrentTitle;
    @FXML
    public TableColumn<Appointment, String> columnAptCurrentType;
    @FXML
    public TableColumn<Appointment, Date> columnAptCurrentStart;
    @FXML
    public TableColumn<Appointment, Date> columnAptCurrentEnd;

    // Appointment date filtered columns
    @FXML
    public TableColumn<Customer, String> columnAptDateTitle;
    @FXML
    public TableColumn<Customer, String> columnAptDateType;
    @FXML
    public TableColumn<Customer, Date> columnAptDateStart;
    @FXML
    public TableColumn<Customer, Date> columnAptDateEnd;

    // Buttons
    @FXML
    public Button btnExit;
    @FXML
    public Button btnAddCustomer;
    @FXML
    public Button btnDeleteCustomer;
    @FXML
    public Button btnAddAppointment;
    @FXML
    public Button btnDeleteAppointment;
    @FXML
    public Button btnClearFilter;
    @FXML
    public Button btnGenTypesByMonthReport;
    @FXML
    public Button btnGenUserSchedules;

    @FXML
    public ToggleButton btnFilterButton;
    // Labels
    @FXML
    public Label lblAppointments;

    // Datepicker
    @FXML
    public DatePicker dpFilter;

    public Customer selectedCustomer;
    // Main data lists.
    private ObservableList<Customer> customerData;
    private ObservableList<Appointment> appointmentData;
    private FilteredList<Appointment> appointmentCurrentData; // Lists that populate the table views based on what is selected.

    private FilteredList<Appointment> appointmentDateData; // Lists that populate the table views based on what is selected.

    //  FilteredList<Appointment> appointmentDateData = new FilteredList<>(appointmentData);
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableViewWithDatabase();
        SchedulingSoftware.stage.setMinWidth(1016);
        SchedulingSoftware.stage.setMinHeight(576);
        SchedulingSoftware.stage.setTitle("Max Randolph Consultancy - Scheduler App");
    }

    @FXML
    public void initializeTableViewWithDatabase() {
        appointmentData = FXCollections.observableArrayList();
        customerData = FXCollections.observableArrayList();

        RefreshCustomerData();
        RefreshAppointmentData();

        //Set cell values to tableCustomer.
        tableCustomer.setEditable(true);
        tableCustomer.getSelectionModel().setCellSelectionEnabled(true);
        tableCustomer.setPlaceholder(new Label("No customers exist."));

        tableAppointment.setEditable(true);
        tableAppointment.getSelectionModel().setCellSelectionEnabled(true);
        tableAppointment.setPlaceholder(new Label("No appointments exist."));

        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAddress2.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCountry.setCellFactory(TextFieldTableCell.forTableColumn());
        columnCity.setCellFactory(TextFieldTableCell.forTableColumn());
        columnZip.setCellFactory(TextFieldTableCell.forTableColumn());
        columnPhone.setCellFactory(TextFieldTableCell.forTableColumn());

        columnAptCurrentTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAptCurrentType.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAptCurrentStart.setCellFactory(column -> { // Lambda function used here since this cell has unique properties that
            // would make definining an entire function overkill.
            TableCell<Appointment, Date> cell = new TextFieldTableCell<Appointment, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                private TextField textField;

                @Override
                public void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null) {
                            Timestamp sq = new Timestamp(item.getTime());
                            setText(sq.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
                        }
                    }
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    Timestamp sq = new Timestamp(getItem().getTime());
                    setText(sq.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    if (textField == null) {
                        createDateTextField();
                    }
                    setGraphic(textField);
                    textField.selectAll();

                }

                private void createDateTextField() {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

                    textField = new TextField(getItem() == null ? "" : format.format(getItem()));
                    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                    textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent t) {

                            if (t.getCode() == KeyCode.ENTER) {
                                try {
                                    Date newDate = (Date) dateFormatter.parse(textField.getText());
                                    commitEdit(new java.sql.Timestamp(newDate.getTime()));
                                } catch (ParseException ex) {
                                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                                    cancelEdit();
                                }
                            } else if (t.getCode() == KeyCode.ESCAPE) {
                                cancelEdit();
                            }
                        }
                    });
                    this.setGraphic(textField);
                }
            };

            return cell;
        });

        columnAptCurrentEnd.setCellFactory(column -> { // Lambda function used here since this cell has unique properties that
            // would make definining an entire function overkill.
            TableCell<Appointment, Date> cell = new TextFieldTableCell<Appointment, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
                private TextField textField;

                @Override
                public void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        if (item != null) {
                            Timestamp sq = new Timestamp(item.getTime());
                            setText(sq.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
                        }
                    }
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    Timestamp sq = new Timestamp(getItem().getTime());
                    setText(sq.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a")));
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    createDateTextField();
                    textField.selectAll();

                }

                private void createDateTextField() {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

                    textField = new TextField(getItem() == null ? "" : format.format(getItem()));
                    textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);

                    textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent t) {

                            if (t.getCode() == KeyCode.ENTER) {
                                try {
                                    Date newDate = (Date) dateFormatter.parse(textField.getText());
                                    commitEdit(new java.sql.Timestamp(newDate.getTime()));
                                } catch (ParseException ex) {
                                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                                    cancelEdit();
                                }
                            } else if (t.getCode() == KeyCode.ESCAPE) {
                                cancelEdit();
                            }
                        }
                    });
                    this.setGraphic(textField);
                }
            };

            return cell;
        });
        appointmentCurrentData = new FilteredList<>(appointmentData);
        appointmentDateData = new FilteredList<>(appointmentData);

        tableCustomer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println(newSelection);
                btnAddAppointment.setDisable(false);
                btnDeleteCustomer.setDisable(false);
                lblAppointments.setText(newSelection.getCustomerName() + "'s Appointments");
                selectedCustomer = tableCustomer.getSelectionModel().getSelectedItem();
                appointmentCurrentData.setPredicate(
                        new Predicate<Appointment>() {
                    public boolean test(Appointment t) {
                        return t.getCustomerId() == newSelection.getCustomerId();
                    }
                }
                );
            }
        });

        tableAppointment.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnDeleteCustomer.setDisable(false);
            } else {
                btnDeleteCustomer.setDisable(true);
            }
        });

        dpFilter.valueProperty().addListener((ov, oldVal, newVal) -> {
            appointmentDateData.setPredicate(
                    new Predicate<Appointment>() {
                public boolean test(Appointment t) {
                    if (dpFilter.getValue() == null) {
                        return true;
                    }
                    Calendar calT = Calendar.getInstance();
                    Calendar calCompare = Calendar.getInstance();

                    calT.setTime(t.getStart());
                    calCompare.setTime(java.util.Date.from(dpFilter.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    if (btnFilterButton.isSelected()) {
                        return calT.get(Calendar.WEEK_OF_YEAR) == calCompare.get(Calendar.WEEK_OF_YEAR);
                    }
                    return calT.get(Calendar.MONTH) == calCompare.get(Calendar.MONTH);
                }
            }
            );
        });

        // Set all columns editable
        columnName.setEditable(true);
        columnAddress.setEditable(true);
        columnPhone.setEditable(true);
        columnAddress2.setEditable(true);
        columnCity.setEditable(true);
        columnCountry.setEditable(true);
        columnZip.setEditable(true);

        columnAptCurrentTitle.setEditable(true);
        columnAptCurrentType.setEditable(true);
        columnAptCurrentStart.setEditable(true);
        columnAptCurrentEnd.setEditable(true);

        //makes columns editable
        columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnAddress2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        columnCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        columnCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        columnZip.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        columnAptCurrentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAptCurrentType.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnAptCurrentStart.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("start"));
        columnAptCurrentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));

        columnAptDateTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAptDateType.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnAptDateStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        columnAptDateEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        columnName.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCustomerName(event.getNewValue());
            updateData("customer", event.getNewValue(), customer.getCustomerId(), "customerName", "customerId");
            RefreshTables();

        }
        );
        columnAddress2.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setAddress2(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "address2", "AddressId");
            RefreshTables();

        }
        );
        columnCity.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCity(event.getNewValue());
            updateData("city", event.getNewValue(), customer.getCityId(), "city", "CityId");
            RefreshTables();
        }
        );
        columnAddress.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setAddress(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "address", "AddressId");
            RefreshTables();
        }
        );
        columnPhone.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setPhone(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "phone", "AddressId");
            RefreshTables();
        }
        );
        columnZip.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setPostalCode(event.getNewValue());
            updateData("address", event.getNewValue(), customer.getAddressId(), "postalCode", "AddressId");
            RefreshTables();
        }
        );
        columnCountry.setOnEditCommit(event
                -> {
            Customer customer = event.getRowValue();
            customer.setCountry(event.getNewValue());
            updateData("country", event.getNewValue(), customer.getCountryId(), "country", "countryId");
            RefreshTables();
        }
        );

        // Appointment table event handling
        columnAptCurrentTitle.setOnEditCommit(event
                -> {
            Appointment appointment = event.getRowValue();
            appointment.setTitle(event.getNewValue());
            updateData("appointment", event.getNewValue(), appointment.getAppointmentId(), "title", "appointmentId");
            RefreshTables();
        }
        );
        columnAptCurrentType.setOnEditCommit(event
                -> {
            Appointment appointment = event.getRowValue();
            appointment.setDescription(event.getNewValue());
            updateData("appointment", event.getNewValue(), appointment.getAppointmentId(), "description", "appointmentId");
            RefreshTables();
        }
        );
        columnAptCurrentStart.setOnEditCommit(event
                -> {
            Appointment appointment = event.getRowValue();
            appointment.setStart(event.getNewValue());
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            updateData("appointment", dateFormatter.format(event.getNewValue()), appointment.getAppointmentId(), "start", "appointmentId");
            RefreshTables();
        }
        );

        columnAptCurrentEnd.setOnEditCommit(event
                -> {
            Appointment appointment = event.getRowValue();
            appointment.setEnd(event.getNewValue());
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            updateData("appointment", dateFormatter.format(event.getNewValue()), appointment.getAppointmentId(), "end", "appointmentId");
            RefreshTables();
        }
        );
        tableCustomer.setItems(null);
        tableCustomer.setItems(customerData);

        tableAppointment.setItems(null);
        tableAppointment.setItems(appointmentCurrentData);

        tableAppointmentDateFiltered.setItems(null);
        tableAppointmentDateFiltered.setItems(appointmentDateData);

    }

    public void updateData(String tableName, String newValue, Integer id, String columnToUpdate, String entityIdName) {
        Connection connection;
        try {
            connection = SchedulingSoftware.conManager.open();
            PreparedStatement statement = connection.prepareStatement("UPDATE " + tableName + " SET " + columnToUpdate + "=? WHERE " + entityIdName + "=?");

            statement.setString(1, newValue);
            statement.setInt(2, id);
            System.out.println(statement);
            statement.executeUpdate();
            connection.close();
            //Query       
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    // Event handle for Add User button click.
    public void handleBtnAddCustomerAction(ActionEvent event) throws SQLException, IOException {
        try {
            // Execute query and store result in a resultset
            CreateNewCustomer();

        } catch (Exception ex) {
            System.err.println("Error" + ex);
        }
    }

    // Event handle for Add User button click.
    public void handleBtnAddAppointmentAction(ActionEvent event) throws SQLException, IOException {
        try {

            // Execute query and store result in a resultset
            CreateNewAppointment();

        } catch (Exception ex) {
            System.out.println("error" + ex);
        }
    }

    @FXML
    private void HandleBtnExitAction(ActionEvent event) {
        System.exit(0);
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
            btnDeleteCustomer.setDisable(true);
            RefreshTables();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    @FXML
    private void handleBtnDeleteAppointment(ActionEvent event) throws SQLException {
        try {
            Connection connection = SchedulingSoftware.conManager.open();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM appointment where appointmentId=?");
            Appointment selectedAppointment = tableAppointment.getSelectionModel().getSelectedItem();

            statement.setInt(1, selectedAppointment.getAppointmentId());
            System.out.println(statement);

            statement.executeUpdate();
            RefreshTables();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    @FXML
    private void handleBtnDateFilter(ActionEvent event) {
        btnFilterButton.setText(btnFilterButton.isSelected() ? "Filter on Month" : "Filter on Week");
        dpFilter.setPromptText(btnFilterButton.isSelected() ? "Select Date to Filter Appts by Month" : "Select Date to Filter Appts by Week");
        if (dpFilter.getValue() != null) {
            appointmentDateData.setPredicate(
                    new Predicate<Appointment>() {
                public boolean test(Appointment t) {
                    Calendar calT = Calendar.getInstance();
                    Calendar calCompare = Calendar.getInstance();

                    calT.setTime(t.getStart());
                    calCompare.setTime(java.util.Date.from(dpFilter.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    if (btnFilterButton.isSelected()) {
                        return calT.get(Calendar.WEEK_OF_YEAR) == calCompare.get(Calendar.WEEK_OF_YEAR);
                    }
                    return calT.get(Calendar.MONTH) == calCompare.get(Calendar.MONTH);
                }
            }
            );
        }
    }

    @FXML
    private void handleBtnGenTypesByMonthReport(ActionEvent event) {
        ReportGenerator generator = new ReportGenerator();
        generator.GenerateApptTypesByMonth(appointmentData);
    }

    @FXML
    private void handleBtnClearFilter(ActionEvent event) {
        dpFilter.setValue(null);
    }

    public void RefreshTables() {
        System.out.println("refreshing data");
        appointmentData.removeAll(appointmentData);
        customerData.removeAll(customerData);
        RefreshCustomerData();
        RefreshAppointmentData();
//        tableCustomer.setItems(null);
//        tableCustomer.setItems(customerData);
    }

    private void RefreshCustomerData() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
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

    private void RefreshAppointmentData() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();

            // Execute query and store result in a resultset
            ResultSet rs = conn.createStatement().executeQuery(
                    "select appointmentId, customerId, title, description, `start`, `end` from appointment");
            while (rs.next()) {
                //get customers
                appointmentData.add(
                        new Appointment(
                                rs.getInt(1),
                                rs.getInt(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getTimestamp(5) != null ? new Date(rs.getTimestamp(5).getTime()) : rs.getDate(5),
                                rs.getTimestamp(6) != null ? new Date(rs.getTimestamp(6).getTime()) : rs.getDate(6),
                                SchedulingSoftware.currentUserName, SchedulingSoftware.currentUserName
                        )
                );
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
                    "insert into country (country, createdBy, lastUpdateBy, createDate)values('New Country','"
                    + SchedulingSoftware.currentUserName + "','" + SchedulingSoftware.currentUserName + "', now());"
            );
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
            RefreshTables();

        } catch (SQLException ex) {
            System.err.println("Error" + ex);

        }
    }

    private void CreateNewAppointment() {
        try {
            Connection conn = SchedulingSoftware.conManager.open();
            // Execute query and store result in a resultset
            String query = "insert into appointment (customerId, title, description, createdBy, lastUpdateBy, start, end, location, contact, url, createDate)values("
                    + selectedCustomer.getCustomerId() + ",'New Appointment','Appointment Type','"
                    + SchedulingSoftware.currentUserName + "','" + SchedulingSoftware.currentUserName + "', now(), now(), 'empty', 'empty', 'empty', now());";
            System.out.println(query);
            conn.createStatement().executeUpdate(query);
            RefreshTables();
        } catch (SQLException ex) {
            System.err.println("Error" + ex);

        }
    }

}
