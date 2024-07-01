package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.DatePicker;
import entity.Guest;
import entity.Maid;
import entity.Pricelist;
import entity.Receptionist;
import entity.Reservation;
import entity.Room;
import entity.RoomAdditionalService;
import entity.RoomType;
import entity.AService;
import entity.Staff;
import entity.User;
import enums.Gender;
import enums.LevelOfEducation;
import enums.ReservationStatus;
import enums.Role;
import enums.RoomStatus;
import enums.ServiceType;
import manage.GuestManager;
import manage.HotelManager;
import manage.RoomAdditionalServiceManager;
import manage.RoomManager;
import manage.AServiceManager;
import net.miginfocom.swing.MigLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;

public class AdminFrame extends JFrame {
	private JFrame window;
    private JTabbedPane tabPanel;
    private DefaultTableModel staffTableModelM;
    private DefaultTableModel staffTableModelR;
    private DefaultTableModel pricelistTableModelR;
    private DefaultTableModel pricelistTableModelA;
    private DefaultTableModel guestTableModel;
    private DefaultTableModel roomTableModel;
    private DefaultTableModel reservationTableModel;
    private DefaultTableModel pricelistsTableModel;
    private HotelManager hotelManager;
    
    @Override
	public void dispose() {
		hotelManager.saveData();
		super.dispose();
	}
    
    public AdminFrame(User user, HotelManager hotelManager) {
        this.hotelManager = hotelManager;
        GuestManager gm = hotelManager.getGuestManager();
        RoomManager rm = hotelManager.getRoomManager();
        AServiceManager sm = hotelManager.getServiceManager();
        gm.loadData();
       
    	initializeWindow(user);
        addLogoutButton(user);
        initializeTabPanel();
        populateStaffTable(hotelManager);
        populatePricelistTable(hotelManager);
        populateGuestTable(hotelManager);
        populateRoomTable(hotelManager);
        populateReservationTable(hotelManager, rm, gm, sm, hotelManager.getRoomAdditionalServiceManager());
        populatePricelistsTable(hotelManager);
        populateReportsTabPanel(hotelManager);
        populateChartTabPanel(hotelManager);
       
        window.setVisible(true);
    }

    private void initializeWindow(User user) {
        window = new JFrame("Admin Panel - " + user.getFirstName() + " " + user.getLastName());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1200, 650);
        ImageIcon icon = new ImageIcon("icon.png");
        window.setIconImage(icon.getImage());
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setLayout(new MigLayout("", "[grow]", "[][grow]"));
    }

    private void addLogoutButton(User user) {
        JButton logoutButton = new JButton("Izloguj se");
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> {
            user.logout();
            window.dispose();
            hotelManager.saveData();
            new LoginFrame(hotelManager);
        });
        window.add(logoutButton, "align left, wrap");
    }

    private void initializeTabPanel() {
        tabPanel = new JTabbedPane();
        JPanel page1 = new JPanel();
        page1.add(new JLabel());
        JPanel page2 = new JPanel();
        JPanel page3 = new JPanel();
        JPanel page4 = new JPanel();
        JPanel page5 = new JPanel();
        JPanel page6 = new JPanel();
        JPanel page7 = new JPanel();
        JPanel page8 = new JPanel();
        page3.add(new JLabel("Gosti hotela"));
        tabPanel.addTab("Usluge", page1);
        tabPanel.addTab("Zaposleni", page2);
        tabPanel.addTab("Gosti", page3);
        tabPanel.addTab("Rezervacije", page4);
        tabPanel.addTab("Sobe", page5);
        tabPanel.addTab("Cenovnici", page6);
        tabPanel.addTab("Izveštaji", page7);
        tabPanel.addTab("Grafikoni", page8);
        window.add(tabPanel, "grow, push, wrap");
    }
    
    private JPanel createFormPanel(HotelManager hotelManager, JTable tableR, JTable tableM) {
    	JLabel form = new JLabel("Forma za dodavanje zaposlenih");
        JLabel nameLabel = new JLabel("Ime:");
        JTextField nameField = new JTextField(29);
        JLabel lastNameLabel = new JLabel("Prezime:");
        JTextField lastNameField = new JTextField(29);
        Gender[] genderValues = Gender.values();
        String[] genderOptions = new String[genderValues.length + 1];
        genderOptions[0] = "";
        for (int i = 0; i < genderValues.length; i++) {
            genderOptions[i + 1] = genderValues[i].toString();
        }
        JLabel genderLabel = new JLabel("Pol:");
        JComboBox<String> genderComboBox = new JComboBox<>(genderOptions);
        JLabel dobLabel = new JLabel("Datum rođenja:");
        DatePicker dobChooser = new DatePicker();
        JLabel phoneLabel = new JLabel("Telefon:");
        JTextField phoneField = new JTextField(29);
        JLabel addressLabel = new JLabel("Adresa:");
        JTextField addressField = new JTextField(29);
        JLabel usernameLabel = new JLabel("Korisničko ime:");
        JTextField usernameField = new JTextField(29);
        JLabel passwordLabel = new JLabel("Lozinka:");
        JPasswordField passwordField = new JPasswordField(29);
        JLabel levelOfEducationLabel = new JLabel("Nivo obrazovanja:");
        LevelOfEducation[] levelOfEducationValues = LevelOfEducation.values();
        String[] levelOfEducationOptions = new String[levelOfEducationValues.length + 1];
        levelOfEducationOptions[0] = "";
        for (int i = 0; i < levelOfEducationValues.length; i++) {
            levelOfEducationOptions[i + 1] = levelOfEducationValues[i].toString();
        }
        JComboBox<String> levelOfEducationComboBox = new JComboBox<>(levelOfEducationOptions);
        JLabel yearsOfServiceLabel = new JLabel("Godine staza:");
        JTextField yearsOfServiceField = new JTextField(29);
        
        JLabel positionLabel = new JLabel("Pozicija:");
        Role[] jobValues = Role.values();
        String[] jobOptions = new String[3];
        jobOptions[0] = ""; 
        for (int i = 2; i <= 3; i++) {
            jobOptions[i-1] = jobValues[i-1].toString();
        }
        JComboBox<String> jobComboBox = new JComboBox<>(jobOptions);
		        

        JButton addButton = new JButton("Dodaj");
        addButton.setPreferredSize(new Dimension(70, addButton.getPreferredSize().height));
        addButton.addActionListener(e -> {
			String name = nameField.getText();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite ime.");
				return;
			}
			String lastName = lastNameField.getText();
			if (lastName.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite prezime.");
				return;
			}
			Gender gender = Gender.values()[genderComboBox.getSelectedIndex() - 1];
			if (gender == null) {
				JOptionPane.showMessageDialog(window, "Molimo odaberite pol.");
				return;
			}
			LocalDate dob = dobChooser.getDate();
			if (dob == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite datum rođenja.");
				return;
			}
			String phone = phoneField.getText();
			if (phone.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite telefon.");
				return;
			}
			String address = addressField.getText();
			if (address.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite adresu.");
				return;
			}
			String username = usernameField.getText();
			if (username.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite korisničko ime.");
				return;
			}
			for (Staff staff : hotelManager.getStaffManager().getReceptionistManager().getReceptionistList().values()) {
				if (staff.getUsername().equals(username)) {
					JOptionPane.showMessageDialog(window, "Korisničko ime već postoji.");
					return;
				}
			}
			for (Staff staff : hotelManager.getStaffManager().getMaidManager().getMaidList().values()) {
				if (staff.getUsername().equals(username)) {
					JOptionPane.showMessageDialog(window, "Korisničko ime već postoji.");
					return;
				}
			}
			String password = new String(passwordField.getPassword());
			if (password.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite lozinku.");
				return;
			}
			LevelOfEducation levelOfEducation = LevelOfEducation.values()[levelOfEducationComboBox.getSelectedIndex() - 1];
			if (levelOfEducation == null) {
				JOptionPane.showMessageDialog(window, "Molimo odaberite nivo obrazovanja.");
				return;
			}
			int yearsOfService = Integer.parseInt(yearsOfServiceField.getText());
			if (yearsOfService <= 0) {
				JOptionPane.showMessageDialog(window, "Molimo unesite ispravan broj godina staža.");
				return;
			}
			if (yearsOfServiceField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite godine staža.");
				return;
			}
			Role job = Role.values()[jobComboBox.getSelectedIndex()];
			if (job == null) {
				JOptionPane.showMessageDialog(window, "Molimo odaberite poziciju.");
				return;
			}
			double salary = hotelManager.getStaffManager().getSalary(levelOfEducation, yearsOfService, job);
			
			if (job == Role.RECEPTIONIST) {
				int r = hotelManager.getStaffManager().getReceptionistManager().addReceptionist(name, lastName, gender, dob, phone, address, username, password, levelOfEducation, yearsOfService, salary, Role.RECEPTIONIST, false);
				addRowToRTable(hotelManager.getStaffManager().getReceptionistManager().getReceptionistByID(r));
			} else if (job == Role.MAID) {
					int m = hotelManager.getStaffManager().getMaidManager().addMaid(name, lastName, gender, dob, phone, address, username, password, levelOfEducation, yearsOfService, salary, Role.MAID, false, LocalDate.now(), 0);
					addRowToMTable(hotelManager.getStaffManager().getMaidManager().getMaidByID(m));
			} else {
				JOptionPane.showMessageDialog(window, "Molimo odaberite poziciju.");
			}
			JOptionPane.showMessageDialog(window, "Zaposleni " + name + " " + lastName + " je uspešno dodat.");
			hotelManager.saveData();
			
        });
        
        JButton changeButton = new JButton("Izmeni");
        	tableR.addMouseListener(new MouseAdapter() {
        	
            @Override
            public void mousePressed(MouseEvent e) {
            	tableM.clearSelection();
        	int selectedRow = tableR.getSelectedRow();
        	if (tableR.getSelectedRow() != -1) {
        		String name = staffTableModelR.getValueAt(selectedRow, 0).toString();
        		String lastName = staffTableModelR.getValueAt(selectedRow, 1).toString();
        		Gender gender = Gender.valueOf(staffTableModelR.getValueAt(selectedRow, 2).toString());
        		LocalDate dob = LocalDate.parse(staffTableModelR.getValueAt(selectedRow, 3).toString());
        		String phone = staffTableModelR.getValueAt(selectedRow, 4).toString();
        		String address = staffTableModelR.getValueAt(selectedRow, 5).toString();
        		String username = staffTableModelR.getValueAt(selectedRow, 6).toString();
        		String password = staffTableModelR.getValueAt(selectedRow, 7).toString();
        		Role job = Role.RECEPTIONIST;
        		LevelOfEducation levelOfEducation = LevelOfEducation.valueOf(staffTableModelR.getValueAt(selectedRow, 8).toString());
        		int yearsOfService = Integer.parseInt(staffTableModelR.getValueAt(selectedRow, 9).toString());
        		
        		Staff staff = hotelManager.getStaffManager().getReceptionistManager().getReceptionistByID(selectedRow);
        		
        		nameField.setText(name);
        		lastNameField.setText(lastName);
        		genderComboBox.setSelectedItem(gender.toString());
        		dobChooser.setDate(dob);
        		phoneField.setText(phone);
        		addressField.setText(address);
        		usernameField.setText(username);
        		passwordField.setText(password);
        		levelOfEducationComboBox.setSelectedItem(levelOfEducation.toString());
        		yearsOfServiceField.setText(String.valueOf(yearsOfService));
        		jobComboBox.setSelectedItem(job.toString());
        		
        		changeButton.setEnabled(true);
        		
        	}
            }
        	});		
        	
        	changeButton.addActionListener(e -> {
        		
        		boolean isReceptionist = false;
        		int selectedRow;
				if (tableR.getSelectedRow() != -1) {
					selectedRow = tableR.getSelectedRow();
					isReceptionist = true;
				} else {
					selectedRow = tableM.getSelectedRow();
				}
        		String name2 = nameField.getText();
        		if (name2.isEmpty()) {
					JOptionPane.showMessageDialog(window, "Molimo unesite ime.");
					return;
        		}
                String lastName2 = lastNameField.getText();
                if (lastName2.isEmpty()) {
                	JOptionPane.showMessageDialog(window, "Molimo unesite prezime.");
                	return;
                }
                Gender gender2 = Gender.valueOf(genderComboBox.getSelectedItem().toString());
				if (gender2 == null) {
					JOptionPane.showMessageDialog(window, "Molimo odaberite pol.");
					return;
				}
                LocalDate dob2 = dobChooser.getDate();
				if (dob2 == null) {
					JOptionPane.showMessageDialog(window, "Molimo unesite datum rođenja.");
					return;
				}
                String phone2 = phoneField.getText();
                if (phone2.isEmpty()) {
                	JOptionPane.showMessageDialog(window, "Molimo unesite telefon.");
                	return;
                }
                String address2 = addressField.getText();
				if (address2.isEmpty()) {
					JOptionPane.showMessageDialog(window, "Molimo unesite adresu.");
					return;
				}
                String username2 = usernameField.getText();
                if (username2.isEmpty()) {
                	JOptionPane.showMessageDialog(window, "Molimo unesite korisničko ime.");
                	return;
                }
                for (Staff staff : hotelManager.getStaffManager().getReceptionistManager().getReceptionistList().values()) {
					if (staff.getUsername().equals(username2)) {
						JOptionPane.showMessageDialog(window, "Korisničko ime već postoji.");
						return;
					}
                }
                for (Staff staff : hotelManager.getStaffManager().getMaidManager().getMaidList().values()) {
                	if (staff.getUsername().equals(username2)) {
						JOptionPane.showMessageDialog(window, "Korisničko ime već postoji.");
						return;
                	}
                }
                String password2 = new String(passwordField.getPassword());
				if (password2.isEmpty()) {
					JOptionPane.showMessageDialog(window, "Molimo unesite lozinku.");
					return;
				}
                LevelOfEducation levelOfEducation2 = LevelOfEducation.valueOf(levelOfEducationComboBox.getSelectedItem().toString());
                if (levelOfEducation2 == null) {
					JOptionPane.showMessageDialog(window, "Molimo odaberite nivo obrazovanja.");
					return;
                }
                if (yearsOfServiceField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(window, "Molimo unesite godine staža.");
					return;
                }
                int yearsOfService2 = Integer.parseInt(yearsOfServiceField.getText());
                Role job = Role.MAID;
                if (isReceptionist) {
                	job = Role.RECEPTIONIST;
                }
                double salary = hotelManager.getStaffManager().getSalary(levelOfEducation2, yearsOfService2, job);
                
                Staff staff;
                if (isReceptionist) {
                	staff = hotelManager.getStaffManager().getReceptionistManager().getReceptionistByID(selectedRow);
                } else {
                	staff = hotelManager.getStaffManager().getMaidManager().getMaidByID(selectedRow);
                }
                
                staff.setFirstName(name2);
                staff.setLastName(lastName2);
                staff.setGender(gender2);
                staff.setDateOfBirth(dob2);
                staff.setPhone(phone2);
                staff.setAddress(address2);
                staff.setUsername(username2);
                staff.setPassword(password2);
                staff.setLevelOfEducation(levelOfEducation2);
                staff.setYearsOfService(yearsOfService2);
                staff.setSalary(salary);
                hotelManager.saveData();
                JOptionPane.showMessageDialog(window, "Zaposleni " + name2 + " " + lastName2 + " je uspešno izmenjen.");
                JTable taable;
                if (isReceptionist) {
                	taable = tableR;
                }
				else {
					taable = tableM;
				}
                taable.setValueAt(name2, selectedRow, 0);
                taable.setValueAt(lastName2, selectedRow, 1);
                taable.setValueAt(gender2, selectedRow, 2);
                taable.setValueAt(dob2, selectedRow, 3);
                taable.setValueAt(phone2, selectedRow, 4);
                taable.setValueAt(address2, selectedRow, 5);
                taable.setValueAt(username2, selectedRow, 6);
                taable.setValueAt(password2, selectedRow, 7);
                taable.setValueAt(levelOfEducation2, selectedRow, 8);
                	
                taable.setValueAt(yearsOfService2, selectedRow, 9);
                taable.setValueAt(salary, selectedRow, 10);
                taable.repaint();
                
                nameField.setText("");
                lastNameField.setText("");
                genderComboBox.setSelectedIndex(0);
                dobChooser.setDate(null);
                phoneField.setText("");
                addressField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                levelOfEducationComboBox.setSelectedIndex(0);
                yearsOfServiceField.setText("");
                jobComboBox.setSelectedIndex(0);
                
                
                		
                
        	});

        	
        	
        	tableM.addMouseListener(new MouseAdapter() {
            	
                @Override
                public void mousePressed(MouseEvent e) {
                tableR.clearSelection();
            	int selectedRow = tableM.getSelectedRow();
            	if (tableM.getSelectedRow() != -1) {
            		String name = staffTableModelM.getValueAt(selectedRow, 0).toString();
            		String lastName = staffTableModelM.getValueAt(selectedRow, 1).toString();
            		Gender gender = Gender.valueOf(staffTableModelM.getValueAt(selectedRow, 2).toString());
            		LocalDate dob = LocalDate.parse(staffTableModelM.getValueAt(selectedRow, 3).toString());
            		String phone = staffTableModelM.getValueAt(selectedRow, 4).toString();
            		String address = staffTableModelM.getValueAt(selectedRow, 5).toString();
            		String username = staffTableModelM.getValueAt(selectedRow, 6).toString();
            		String password = staffTableModelM.getValueAt(selectedRow, 7).toString();
            		Role job = Role.MAID;
            		LevelOfEducation levelOfEducation = LevelOfEducation.valueOf(staffTableModelM.getValueAt(selectedRow, 8).toString());
            		int yearsOfService = Integer.parseInt(staffTableModelM.getValueAt(selectedRow, 9).toString());
            		
            		Staff staff = hotelManager.getStaffManager().getMaidManager().getMaidByID(selectedRow);
            		
            		nameField.setText(name);
            		lastNameField.setText(lastName);
            		genderComboBox.setSelectedItem(gender.toString());
            		dobChooser.setDate(dob);
            		phoneField.setText(phone);
            		addressField.setText(address);
            		usernameField.setText(username);
            		passwordField.setText(password);
            		levelOfEducationComboBox.setSelectedItem(levelOfEducation.toString());
            		yearsOfServiceField.setText(String.valueOf(yearsOfService));
            		jobComboBox.setSelectedItem(job.toString());
            		
            		changeButton.setEnabled(true);
            		
            	}
                }
            	});		
            	

        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][]"));
        formPanel.add(form, "align left, span 2, wrap");
        formPanel.add(nameLabel, "align right");
        formPanel.add(nameField, "wrap");
        formPanel.add(lastNameLabel, "align right");
        formPanel.add(lastNameField, "wrap");
        formPanel.add(genderLabel, "align right");
        formPanel.add(genderComboBox, "wrap");
        formPanel.add(dobLabel, "align right");
        formPanel.add(dobChooser, "wrap");
        formPanel.add(phoneLabel, "align right");
        formPanel.add(phoneField, "wrap");
        formPanel.add(addressLabel, "align right");
        formPanel.add(addressField, "wrap");
        formPanel.add(usernameLabel, "align right");
        formPanel.add(usernameField, "wrap");
        formPanel.add(passwordLabel, "align right");
        formPanel.add(passwordField, "wrap");
        formPanel.add(levelOfEducationLabel, "align right");
        formPanel.add(levelOfEducationComboBox, "wrap");
        formPanel.add(yearsOfServiceLabel, "align right");
        formPanel.add(yearsOfServiceField, "wrap");
        
        formPanel.add(positionLabel, "align right");
        formPanel.add(jobComboBox, "wrap");
        formPanel.add(changeButton, "align right");
        formPanel.add(addButton, "align right");
        

        return formPanel;
    }

    private void populateStaffData(HotelManager hotelManager) {
        for (Receptionist receptionist : hotelManager.getStaffManager().getReceptionistManager().getReceptionistList().values()) {
			
        	if (receptionist.isDeleted()) {
				continue;
			}
			else {
        	addRowToRTable(receptionist);
        }}

        for (Maid maid : hotelManager.getStaffManager().getMaidManager().getMaidList().values()) {
			if (maid.isDeleted()) {
				continue;
			}
			else{addRowToMTable(maid);}
        }
    }

    private void addRowToMTable(Staff user) {
        Object[] rowData = {
                user.getFirstName(),
                user.getLastName(),
                user.getGender().toString(),
                user.getDateOfBirth().toString(),
                user.getPhone(),
                user.getAddress(),
                user.getUsername(),
                user.getPassword(),
                user.getLevelOfEducation().toString(),
                String.valueOf(user.getYearsOfService()),
                String.valueOf(user.getSalary())
        };
        staffTableModelM.addRow(rowData);
    }    
    private void addRowToRTable(Staff user) {
    	if (!user.isDeleted()) {
        Object[] rowData = {
                user.getFirstName(),
                user.getLastName(),
                user.getGender().toString(),
                user.getDateOfBirth().toString(),
                user.getPhone(),
                user.getAddress(),
                user.getUsername(),
                user.getPassword(),
                user.getLevelOfEducation().toString(),
                String.valueOf(user.getYearsOfService()),
                String.valueOf(user.getSalary())
        };
        staffTableModelR.addRow(rowData);}
    }

    private void populateStaffTable(HotelManager hotelManager) {
        String[] columnNames = {"Ime", "Prezime", "Pol", "Datum rođenja", "Telefon", "Adresa", "Korisničko ime", "Lozinka", "Nivo obrazovanja", "Godine staža", "Plata"};
        staffTableModelM = new DefaultTableModel(columnNames, 0);
        staffTableModelR = new DefaultTableModel(columnNames, 0);

        
        
        JTable tableR = new JTable(staffTableModelR);
        JTable tableM = new JTable(staffTableModelM);
        JPanel formPanel = createFormPanel(hotelManager, tableR, tableM);
        tableR.setRowSelectionAllowed(true);
        tableR.setColumnSelectionAllowed(false);
        tableR.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableR.setDefaultEditor(Object.class, null);
        tableR.getTableHeader().setReorderingAllowed(false);
        tableM.setRowSelectionAllowed(true);
        tableM.setColumnSelectionAllowed(false);
        tableM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableM.setDefaultEditor(Object.class, null);
        tableM.getTableHeader().setReorderingAllowed(false);
        
        populateStaffData(hotelManager);
        
        JButton deleteButton = new JButton("Obriši");
                deleteButton.addActionListener(e -> {
        	int selectedRow = tableR.getSelectedRow();
            if (tableR.getSelectedRow() != -1) {
                staffTableModelR.removeRow(selectedRow);
                staffTableModelR.fireTableRowsDeleted(selectedRow, selectedRow);
                hotelManager.getStaffManager().getReceptionistManager().getReceptionistByID(selectedRow).setDeleted(true);
                hotelManager.saveData();
                JOptionPane.showMessageDialog(window, "Zaposleni " + hotelManager.getStaffManager().getReceptionistManager().getReceptionistByID(selectedRow).getFirstName() + " " + hotelManager.getStaffManager().getReceptionistManager().getReceptionistByID(selectedRow).getLastName() + " je uspešno obrisan iz sistema.");
                
            } else if (tableM.getSelectedRow() != -1) { 
                selectedRow = tableM.getSelectedRow();
                staffTableModelM.removeRow(selectedRow);
                staffTableModelM.fireTableRowsDeleted(selectedRow, selectedRow);
                hotelManager.getStaffManager().getMaidManager().getMaidByID(selectedRow).setDeleted(true);
                hotelManager.saveData();
                JOptionPane.showMessageDialog(window, "Zaposleni " + hotelManager.getStaffManager().getMaidManager().getMaidByID(selectedRow).getFirstName() + " " + hotelManager.getStaffManager().getMaidManager().getMaidByID(selectedRow).getLastName() + " je uspešno obrisan iz sistema.");
            } else {
                JOptionPane.showMessageDialog(window, "Izaberite zaposlenog iz tabele kojeg želite da obrišete.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        });

        JPanel buttonPanel = new JPanel(new MigLayout("wrap 2", "[]push[][]", ""));
        buttonPanel.add(deleteButton, "grow");
        
        JScrollPane scrollPaneR = new JScrollPane(tableR);
        JScrollPane scrollPaneM = new JScrollPane(tableM);

        JPanel labeledTablePanelR = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanelR.add(scrollPaneR, "cell 0 0, grow");
        JLabel tableLabelR = new JLabel("Recepcioneri");
        tableLabelR.setHorizontalAlignment(SwingConstants.CENTER);
        

        JPanel labeledTablePanelM = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanelM.add(scrollPaneM, "cell 0 0, grow");
        JLabel tableLabelM = new JLabel("Sobarice");
        tableLabelM.setHorizontalAlignment(SwingConstants.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, labeledTablePanelR, labeledTablePanelM);
        splitPane.setResizeWeight(0.5);
        splitPane.setEnabled(false);

        JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        buttonAndTablesPanel.add(tableLabelR, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanelR, "grow, pushy");
        buttonAndTablesPanel.add(tableLabelM, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanelM, "grow, pushy");
        buttonAndTablesPanel.add(buttonPanel, "growx");	

        JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
        tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);
        tablesAndFormPanel.add(formPanel, BorderLayout.EAST);

        JPanel page2 = (JPanel) tabPanel.getComponentAt(1);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
    }

    
    
    
    
    
    
    private void populatePricelistTable(HotelManager hotelManager) {
        String[] columnNames = {"ID","Naziv","Cena","Cenovnik", "Tip usluge"};
        String[] columnNames2 = {"ID","Tip sobe","Broj kreveta", "Kapacitet", "Cena","Cenovnik"};
        pricelistTableModelR = new DefaultTableModel(columnNames2, 0);
        pricelistTableModelA = new DefaultTableModel(columnNames, 0);
        
        
        JTable tableR = new JTable(pricelistTableModelR);
        JTable tableA = new JTable(pricelistTableModelA);
        tableR.setRowSelectionAllowed(true);
        tableR.setColumnSelectionAllowed(false);
        tableR.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableR.setDefaultEditor(Object.class, null);
        tableR.getTableHeader().setReorderingAllowed(false);
        tableA.setRowSelectionAllowed(true);
        tableA.setColumnSelectionAllowed(false);
        tableA.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableA.setDefaultEditor(Object.class, null);
        tableA.getTableHeader().setReorderingAllowed(false);
        
        JButton deleteButton = new JButton("Obriši");
        deleteButton.addActionListener(e -> {
        	int selectedRow;
            if (tableR.getSelectedRow() != -1) {
                selectedRow = tableR.getSelectedRow();
                
                hotelManager.getRoomManager().getRoomType(Integer.parseInt(tableR.getValueAt(selectedRow, 0).toString())).setDeleted(true);
                hotelManager.saveData();
                JOptionPane.showMessageDialog(window, "Tip sobe " + hotelManager.getRoomManager().getRoomType(Integer.parseInt(tableR.getValueAt(selectedRow, 0).toString())).getServiceName()+ " je uspešno obrisan iz sistema.");
                pricelistTableModelR.removeRow(selectedRow);
                pricelistTableModelR.fireTableRowsDeleted(selectedRow, selectedRow);
                
            } else if (tableA.getSelectedRow() != -1) { 
            	selectedRow = tableA.getSelectedRow();
                ServiceType type = ServiceType.valueOf(tableA.getValueAt(selectedRow, 4).toString());
				if (type == ServiceType.ADDITIONAL_SERVICE) {
					int id = Integer.parseInt(tableA.getValueAt(selectedRow, 0).toString());
					hotelManager.getServiceManager().getService(id).setDeleted(true);
					hotelManager.saveData();
	                JOptionPane.showMessageDialog(window, "Usluga " + hotelManager.getServiceManager().getService(id).getServiceName()+ " je uspešno obrisana iz sistema.");
				} else {
					int id = Integer.parseInt(tableA.getValueAt(selectedRow, 0).toString());
					hotelManager.getRoomAdditionalServiceManager().getRServiceList().get(id).setDeleted(true);
					hotelManager.saveData();
					JOptionPane.showMessageDialog(window,
							"Usluga " + hotelManager.getRoomAdditionalServiceManager().getRServiceList()
									.get(id)
									.getServiceName() + " je uspešno obrisana iz sistema.");
				}
				pricelistTableModelA.removeRow(selectedRow);
				pricelistTableModelA.fireTableRowsDeleted(selectedRow, selectedRow);
                
                
            } else {
                JOptionPane.showMessageDialog(window, "Izaberite uslugu iz tabele koju želite da obrišete.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        });
        
        
        JPanel buttonPanel = new JPanel(new MigLayout("wrap 2", "[]push[][]", ""));
        buttonPanel.add(deleteButton, "grow, align right");
        
        JPanel formPanel = createPriceFormPanel(hotelManager, tableA);
        JPanel formPanel2 = createRoomTypeFormPanel(hotelManager, tableR);
        
        JPanel formP = new JPanel(new MigLayout("wrap 1", "[]push[][]", ""));
        formP.add(formPanel2, "grow, pushy, center");
        formP.add(formPanel, "grow, pushy, center");

        populatePricelistData(hotelManager);

        JScrollPane scrollPaneR = new JScrollPane(tableR);
        JScrollPane scrollPaneA = new JScrollPane(tableA);

        JPanel labeledTablePanelR = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanelR.add(scrollPaneR, "cell 0 0, grow");
        JLabel tableLabelR = new JLabel("Tipovi soba");
        tableLabelR.setHorizontalAlignment(SwingConstants.CENTER);
        

        JPanel labeledTablePanelA = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanelA.add(scrollPaneA, "cell 0 0, grow");
        JLabel tableLabelA = new JLabel("Dodatne usluge");
        tableLabelA.setHorizontalAlignment(SwingConstants.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, labeledTablePanelR, labeledTablePanelA);
        splitPane.setResizeWeight(0.5);
        splitPane.setEnabled(false);

        JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        buttonAndTablesPanel.add(tableLabelR, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanelR, "grow, pushy");
        buttonAndTablesPanel.add(tableLabelA, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanelA, "grow, pushy");
        buttonAndTablesPanel.add(buttonPanel, "growx");	

        JPanel tablesAndFormPanel = new JPanel(new MigLayout("", "[grow][]", "[grow]"));
        tablesAndFormPanel.add(buttonAndTablesPanel, "grow");
        tablesAndFormPanel.add(formP, "grow, pushy"); 
        JPanel page1 = (JPanel) tabPanel.getComponentAt(0);
        page1.setLayout(new BorderLayout());
        page1.add(tablesAndFormPanel, BorderLayout.CENTER);
    }
    
    private JPanel createPriceFormPanel(HotelManager hotelManager, JTable table) {
    	JLabel form = new JLabel("Forma za dodavanje i izmenu dodatnih usluga");
        JLabel nameLabel = new JLabel("Naziv:");
        JTextField nameField = new JTextField(18);
        
        JLabel priceLabel = new JLabel("Cena:");
        JTextField priceField = new JTextField(18);
        
        JLabel typeLabel = new JLabel("Tip usluge:");
        JComboBox<String> typeComboBox = new JComboBox<>();
        typeComboBox.addItem("");
		for (ServiceType type : ServiceType.values()) {
			if (type != ServiceType.ROOM_SERVICE) {
				typeComboBox.addItem(type.toString());
			}
		}
        
        JLabel pricelistLabel = new JLabel("Cenovnik:");
        JComboBox<String> pricelistComboBox = new JComboBox<>();
        pricelistComboBox.addItem("");
        for (Pricelist pricelist : hotelManager.getPricelistManager().getPricelistList().values()) {
        	if (!pricelist.isDeleted())
        	 pricelistComboBox.addItem(pricelist.getName());
        }
        
        JButton addButton = new JButton("Dodaj");
        JButton okButton = new JButton("Izmeni");
        addButton.setPreferredSize(new Dimension(70, addButton.getPreferredSize().height));
        addButton.addActionListener(e -> {
		String name = nameField.getText();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite naziv.");
				return;
			}
			if (priceField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite cenu.");
				return;
			}
		double price = Double.parseDouble(priceField.getText());
		int pricelistID = pricelistComboBox.getSelectedIndex() - 1;
			if (pricelistID == -1) {
				JOptionPane.showMessageDialog(window, "Molimo odaberite cenovnik.");
				return;
			}
		ServiceType type = ServiceType.valueOf(typeComboBox.getSelectedItem().toString());
			if (type == null) {
				JOptionPane.showMessageDialog(window, "Molimo odaberite tip usluge.");
				return;
			}
		int s = hotelManager.getServiceManager().addServiceType(name, type , price, false, pricelistID);
		
        addRowToPricelistTableA(hotelManager.getServiceManager().getService(s));
        
		
		JOptionPane.showMessageDialog(window, "Usluga " + name + " je uspešno dodata.");
		hotelManager.saveData();
		
		nameField.setText("");
		priceField.setText("");
		pricelistComboBox.setSelectedIndex(0);
		
		        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                
                
                ServiceType type = ServiceType.valueOf(table.getValueAt(selectedRow, 4).toString());
    			if (type == ServiceType.ADDITIONAL_SERVICE) {
	        	AService service = hotelManager.getServiceManager().getServiceList().get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
	        	nameField.setText(service.getServiceName());
	        	priceField.setText(String.valueOf(service.getPrice()));
	        	typeComboBox.setSelectedItem(service.getServiceType().toString());
	        	pricelistComboBox.setSelectedItem(hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getName());
	        	
    			}
    			else if (type == ServiceType.ROOM_ADDITIONAL_SERVICE) {
    				RoomAdditionalService service = hotelManager.getRoomAdditionalServiceManager().getRServiceList().get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
    				nameField.setText(service.getServiceName());
    	        	priceField.setText(String.valueOf(service.getPrice()));
    	        	typeComboBox.setSelectedItem(service.getServiceType().toString());
    	        	pricelistComboBox.setSelectedItem(hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getName());
    			}
	        	
	        
	        	okButton.setEnabled(true);
            }
        });
        	
        okButton.addActionListener(e1 -> {
        	int selectedRow = table.getSelectedRow();
        	if (nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(window, "Molimo unesite naziv.");
                return;
        	}
        	String name2 = nameField.getText();
            String price2 = priceField.getText();
			if (price2.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite cenu.");
				return;
			}
            ServiceType type = ServiceType.valueOf(typeComboBox.getSelectedItem().toString());
            if (type == null) {
                JOptionPane.showMessageDialog(window, "Molimo odaberite tip usluge.");
                return;
            }
			if (type == ServiceType.ADDITIONAL_SERVICE) {
				AService service = hotelManager.getServiceManager().getServiceList().get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
				service.setServiceName(name2);
	        	service.setPrice(Double.parseDouble(price2));
	        	
				DefaultTableModel model = (DefaultTableModel) table.getModel();
			    model.setValueAt(service.getServiceName(), selectedRow, 1);
			    model.setValueAt(service.getPrice(), selectedRow,2);
			    model.fireTableDataChanged();
			    hotelManager.getServiceManager().saveData();
				JOptionPane.showMessageDialog(window, "Usluga " + service.getServiceName() + " je uspešno izmenjena.");

				hotelManager.saveData();
			}
			else if (type == ServiceType.ROOM_ADDITIONAL_SERVICE) {
				RoomAdditionalService service = hotelManager.getRoomAdditionalServiceManager().getRServiceList().get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
				service.setServiceName(name2);
				service.setPrice(Double.parseDouble(price2));
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				model.setValueAt(service.getServiceName(), selectedRow, 1);
				model.setValueAt(service.getPrice(), selectedRow, 2);
				model.fireTableDataChanged();
				hotelManager.getRoomAdditionalServiceManager().saveData();
				JOptionPane.showMessageDialog(window, "Usluga " + service.getServiceName() + " je uspešno izmenjena.");
			}
        	
        	

		});
			

        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][]"));
        formPanel.add(form, "align left, span 2, wrap");
        formPanel.add(nameLabel, "align right");
        formPanel.add(nameField, "wrap");
        formPanel.add(priceLabel, "align right");
        formPanel.add(priceField, "wrap");
        formPanel.add(pricelistLabel, "align right");
        formPanel.add(pricelistComboBox, "wrap");
        formPanel.add(typeLabel, "align right");
        formPanel.add(typeComboBox, "wrap");
        formPanel.add(addButton, "align right");
        formPanel.add(okButton, "align right");

        return formPanel;
    }
    
	private JPanel createRoomTypeFormPanel(HotelManager hotelManager, JTable table) {
    	JLabel form = new JLabel("Forma za dodavanje i izmenu tipova soba");
        JLabel nameLabel = new JLabel("Naziv:");
        JTextField nameField = new JTextField(18);
        JLabel bedsLabel = new JLabel("Broj kreveta:");
        JTextField bedsField = new JTextField(18);
        JLabel capacityLabel = new JLabel("Kapacitet:");
        JTextField capacityField = new JTextField(18);
        JLabel priceLabel = new JLabel("Cena:");
        JTextField priceField = new JTextField(18);
        JLabel pricelistLabel = new JLabel("Cenovnik:");
        JComboBox<String> pricelistComboBox = new JComboBox<>();
        pricelistComboBox.addItem("");
		for (Pricelist pricelist : hotelManager.getPricelistManager().getPricelistList().values()) {
			if (!pricelist.isDeleted())
			pricelistComboBox.addItem(pricelist.getName());
		}
        

        JButton addButton = new JButton("Dodaj");
        
        JButton okButton = new JButton("Izmeni");
        addButton.setPreferredSize(new Dimension(70, addButton.getPreferredSize().height));
        addButton.addActionListener(e -> {
        	String name = nameField.getText();
        	if (name.isEmpty()) {
	 			JOptionPane.showMessageDialog(window, "Molimo unesite naziv.");
	 			return;
        	}
        	if (bedsField.getText().isEmpty()) {
	 			JOptionPane.showMessageDialog(window, "Molimo unesite broj kreveta.");
	 			return;
        	}
    		int beds = Integer.parseInt(bedsField.getText());
    		if (capacityField.getText().isEmpty()) {
	 			JOptionPane.showMessageDialog(window, "Molimo unesite kapacitet.");
	 			return;
    		}
    		int capacity = Integer.parseInt(capacityField.getText());
    		if (priceField.getText().isEmpty()) {
	 			JOptionPane.showMessageDialog(window, "Molimo unesite cenu.");
	 			return;
    		}
    		double price = Double.parseDouble(priceField.getText());
    		int pricelistID = pricelistComboBox.getSelectedIndex() - 1;
    		if (pricelistID == -1) {
	 			JOptionPane.showMessageDialog(window, "Molimo odaberite cenovnik.");
	 			return;
    		}
    		int s = hotelManager.getRoomManager().addRoomType(name, beds,capacity, price, false, pricelistID, ServiceType.ROOM_SERVICE);
    		
    		
            addRowToPricelistTableR(hotelManager.getRoomManager().getRoomType(s));
    		
    		JOptionPane.showMessageDialog(window, "Tip sobe " + name + " je uspešno dodat.");
    		hotelManager.saveData();
    		
    		nameField.setText("");
    		bedsField.setText("");
    		capacityField.setText("");
    		priceField.setText("");
    		pricelistComboBox.setSelectedIndex(0);
        });
        

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                RoomType roomType = hotelManager.getRoomManager().getRoomTypeList().get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
	        	nameField.setText(roomType.getServiceName());
	        	bedsField.setText(String.valueOf(roomType.getBeds()));
	        	capacityField.setText(String.valueOf(roomType.getCapacity()));
	        	priceField.setText(String.valueOf(roomType.getPrice()));
	        	pricelistComboBox.setSelectedItem(hotelManager.getPricelistManager().getPricelistList().get(roomType.getPricelistID()).getName());
	        	okButton.setEnabled(true);
            }
        });
        	
        okButton.addActionListener(e1 -> {
        	int selectedRow = table.getSelectedRow();
			if (nameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite naziv.");
				return;
			}
        	String name = nameField.getText();
        	 if (bedsField.getText().isEmpty()) {
        		      			JOptionPane.showMessageDialog(window, "Molimo unesite broj kreveta.");
        		      			return;
        	 }
            String beds = bedsField.getText();
			if (capacityField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite kapacitet.");
				return;
			}
            String capacity = capacityField.getText();
                        if (priceField.getText().isEmpty()) {
                        	                	JOptionPane.showMessageDialog(window, "Molimo unesite cenu.");
                        	                	return;
                        }
            String price = priceField.getText();
        	Room room = hotelManager.getRoomManager().getRoomList().get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
        	
        	room.setRoomType(hotelManager.getRoomManager().getRoomType(Integer.parseInt(table.getValueAt(selectedRow, 0).toString())));
        	room.getRoomType().setServiceName(name);
        	room.getRoomType().setBeds(Integer.parseInt(beds));
        	room.getRoomType().setCapacity(Integer.parseInt(capacity));
        	room.getRoomType().setPrice(Double.parseDouble(price));
        	
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
		    model.setValueAt(room.getRoomType().getServiceName(), selectedRow, 1);
		    model.setValueAt(room.getRoomType().getBeds(), selectedRow,2);
		    model.setValueAt(room.getRoomType().getCapacity(), selectedRow,3);
		    model.setValueAt(room.getRoomType().getPrice(), selectedRow,4);
		    model.setValueAt(hotelManager.getPricelistManager().getPricelistList().get(room.getRoomType().getPricelistID()).getName(), selectedRow,5);
		    model.fireTableDataChanged();
		    hotelManager.getRoomManager().saveRTData();
			JOptionPane.showMessageDialog(window, "Tip sobe " + room.getRoomType().getServiceName() + " je uspešno izmenjen.");
		});
        
        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][]"));
        formPanel.add(form, "align left, span 2, wrap");
        formPanel.add(nameLabel, "align right");
        formPanel.add(nameField, "wrap");
        formPanel.add(bedsLabel, "align right");
        formPanel.add(bedsField, "wrap");
        formPanel.add(capacityLabel, "align right");
        formPanel.add(capacityField, "wrap");
        formPanel.add(priceLabel, "align right");
        formPanel.add(priceField, "wrap");
        formPanel.add(pricelistLabel, "align right");
        formPanel.add(pricelistComboBox, "wrap");
        formPanel.add(addButton, "align right");
        formPanel.add(okButton, "align right");
        
        return formPanel;
        
	}
    
    private void populatePricelistData(HotelManager hotelManager) {
		for (AService service : hotelManager.getServiceManager().getServiceList().values()) {
			if (service.isDeleted()) { continue; }
            addRowToPricelistTableA(service);
            
        }
		for (RoomAdditionalService service : hotelManager.getRoomAdditionalServiceManager().getRServiceList().values()) {
			if (service.isDeleted()) { continue; }
            
			addRowToPricelistTableRA(service);
            
		}
		for (RoomType room : hotelManager.getRoomManager().getRoomTypeList().values()) {
			if (room.isDeleted()) { continue; }
			addRowToPricelistTableR(room);
		}
	}
	
	private void addRowToPricelistTableA(AService service) {
		Object[] rowData = {service.getId(), service.getServiceName(),
				String.valueOf(service.getPrice()), hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getName(), service.getServiceType().toString()};
		pricelistTableModelA.addRow(rowData);
	}
	
	private void addRowToPricelistTableRA(RoomAdditionalService service) {
		Object[] rowData = {service.getId(), service.getServiceName(), String.valueOf(service.getPrice()),
				hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getName(), service.getServiceType().toString() };
		pricelistTableModelA.addRow(rowData);
	}
	
	private void addRowToPricelistTableR(RoomType room) {
		Object[] rowData = {room.getId(), room.getServiceName(), room.getBeds(), room.getCapacity(),
				String.valueOf(room.getPrice()), hotelManager.getPricelistManager().getPricelistList().get(room.getPricelistID()).getName()};
		pricelistTableModelR.addRow(rowData);
	}
  
	
	private void addRowToGuestTable(Guest user) {
		Object[] rowData = {
                user.getFirstName(),
                user.getLastName(),
                user.getGender().toString(),
                user.getDateOfBirth().toString(),
                user.getPhone(),
                user.getAddress(),
                user.getUsername(),
                user.getPassword(),
        };
        guestTableModel.addRow(rowData);
	}
	
	private void populateGuestData(HotelManager hotelManager) {
		for (Guest guest : hotelManager.getReceptionistManager().getGuestManager().getGuestList().values()) {
			if (guest.isDeleted()) {
				continue;
			}
			addRowToGuestTable(guest);
        }
	}
	
	
	
	
	private void populateGuestTable(HotelManager hotelManager) {
        String[] columnNames = {"Ime", "Prezime", "Pol", "Datum rođenja", "Telefon", "Adresa", "Korisničko ime", "Lozinka"};
        guestTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(guestTableModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        hotelManager.getReceptionistManager().getGuestManager().loadData();
        populateGuestData(hotelManager);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel labeledTablePanel = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanel.add(scrollPane, "cell 0 0, grow");
        JLabel tableLabel = new JLabel("Gosti hotela");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        buttonAndTablesPanel.add(tableLabel, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanel, "grow, pushy");
        JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
        tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);
        JPanel page2 = (JPanel) tabPanel.getComponentAt(2);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
    }
	
	
	
	
	private void addRowToRoomTable(Room room) {
		if (room.isDeleted()) {
			return;
		}
		String roomAdditionalServices = "";
		if (room.getAdditionalServices() != null) {
			
		for (RoomAdditionalService ras : room.getAdditionalServices()) {
			if (ras == null) {
				continue;
			}
			RoomAdditionalService service = hotelManager.getRoomAdditionalServiceManager().getService(ras.getId());
			if (service == null) {
				continue;
			} else {
				String name = service.getServiceName();
				roomAdditionalServices += name + ", ";
			}
		}}
		hotelManager.getRoomManager().loadRTData();
		hotelManager.getRoomManager().loadData();
		Object[] rowData = {
			room.getRoomNumber(),
			room.getRoomType().getServiceName(),
			room.getRoomStatus().toString(),
			roomAdditionalServices
        };
        roomTableModel.addRow(rowData);
	}
	
	private void populateRoomData(HotelManager hotelManager) {
		for (Room room : hotelManager.getRoomManager().getRoomList().values()) {
			addRowToRoomTable(room);
        }
	}
	
	private JPanel createRoomFormPanel(HotelManager hotelManager, JTable table) {
    	JLabel form = new JLabel("Forma za dodavanje i izmenu soba");
        JLabel number = new JLabel("Broj sobe: ");
        JLabel number2 = new JLabel();
        JLabel type = new JLabel("Tip sobe:");
        ArrayList<String> roomValues = new ArrayList<String>();
		for (RoomType room : hotelManager.getRoomManager().getRoomTypeList().values()) {
			if (room.isDeleted()) {
				continue;
			}
			roomValues.add(room.getServiceName());
		}
        ArrayList<String> roomOptions = new ArrayList<String>();
        roomOptions.add("");
        for (int i = 0; i < roomValues.size(); i++) {
        	if (roomOptions.contains(roomValues.get(i)))
        		continue;
        	roomOptions.add(roomValues.get(i));
        }
        JComboBox<String> roomComboBox = new JComboBox<>();
		for (String room : roomOptions) {
			roomComboBox.addItem(room);
		}
		JPanel additionalServicesPanel2 = new JPanel(new MigLayout("wrap 3", "[][][]", "[][][]"));
		Set<String> addedServiceNames2 = new HashSet<>();
        int checkboxCount2 = 0;
        for (RoomAdditionalService service : hotelManager.getRoomAdditionalServiceManager().getRServiceList().values()) {
            if (!service.isDeleted() && service.getServiceType() == ServiceType.ROOM_ADDITIONAL_SERVICE) {
                String serviceName = service.getServiceName();

                if (!addedServiceNames2.contains(serviceName)) {
                    JCheckBox checkBox = new JCheckBox(serviceName);
                    checkboxCount2++;

                    if (checkboxCount2 % 3 == 0) {
                        additionalServicesPanel2.add(checkBox, "wrap 1");
                    } else {
                        additionalServicesPanel2.add(checkBox);
                    }

                    addedServiceNames2.add(serviceName);
                }
            }
        }
        JScrollPane roomScrollPane = new JScrollPane(roomComboBox);
        roomScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JLabel nameLabel = new JLabel("Status:");
        RoomStatus[] stValues = RoomStatus.values();
        String[] stOptions = new String[stValues.length + 1];
        stOptions[0] = "";
        for (int i = 0; i < stValues.length; i++) {
            stOptions[i + 1] = stValues[i].toString();
        }
        JComboBox<String> stComboBox = new JComboBox<>(stOptions);
        JButton okButton = new JButton("Izmeni");
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
            	for (int i = 0; i < additionalServicesPanel2.getComponentCount(); i++) {
					JCheckBox checkBox = (JCheckBox) additionalServicesPanel2.getComponent(i);
					checkBox.setSelected(false);
            	}
                int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(window, "Molimo izaberite sobu.");
					return;
				}
                String roomType = table.getValueAt(selectedRow, 1).toString();
				if (roomType.equals("")) {
					JOptionPane.showMessageDialog(window, "Molimo odaberite tip sobe.");
					return;
				}
                String roomStatus = table.getValueAt(selectedRow, 2).toString();
                if (roomStatus.equals("")) {
	                JOptionPane.showMessageDialog(window, "Molimo odaberite status sobe.");
	                return;
                }
                int roomTypeComboIndex = 0;
                for(int i = 0; i < roomOptions.size(); i++) {
                	if (roomOptions.get(i).equals(roomType)) {
                		roomTypeComboIndex = i;
                		break;
                	}
                }
                int roomStatusComboIndex = 0;
                for(int i = 0; i < stOptions.length; i++) {
                	 if (stOptions[i].equals(roomStatus)) {
                		roomStatusComboIndex = i;
                		break;
                	}
                }
	        	Room room = hotelManager.getRoomManager().getRoomList().get(selectedRow + 1);
	        	number2.setText(String.valueOf(room.getRoomNumber()));
	        	stComboBox.setSelectedIndex(roomStatusComboIndex);
	        	roomComboBox.setSelectedIndex(roomTypeComboIndex);
	        	String additionalServices2 = table.getValueAt(selectedRow, 3).toString();
				String[] additionalServicesArray2 = additionalServices2.split(", ");
				for (int i = 0; i < additionalServicesArray2.length; i++) {
					for (int j = 0; j < additionalServicesPanel2.getComponentCount(); j++) {
						JCheckBox checkBox = (JCheckBox) additionalServicesPanel2.getComponent(j);
						
						if (checkBox.getText().equals(additionalServicesArray2[i])) {
							checkBox.setSelected(true);

						}
						
					}
				
				}
	        	okButton.setEnabled(true);
            }
        });
        	
        okButton.addActionListener(e1 -> {
        	int selectedRow = table.getSelectedRow();
			if (roomComboBox.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(window, "Molimo odaberite sobu.");
				return;
			}
        	Room room = hotelManager.getRoomManager().getRoomList().get(selectedRow + 1);
        	int indeksTipaSobe = roomComboBox.getSelectedIndex() - 1;
			RoomType rt = hotelManager.getRoomManager().getRoomType(indeksTipaSobe);
			int indeksStatusaSobe = stComboBox.getSelectedIndex();
			RoomStatus status = RoomStatus.valueOf(stOptions[indeksStatusaSobe]);
			ArrayList<RoomAdditionalService> adRoom = new ArrayList<RoomAdditionalService>();
	        Set<String> selectedServiceNames2 = new HashSet<>();
				for (int i = 0; i < additionalServicesPanel2.getComponentCount(); i++) {
					if (additionalServicesPanel2.getComponent(i) instanceof JCheckBox) {
						JCheckBox checkBox = (JCheckBox) additionalServicesPanel2.getComponent(i);

						if (checkBox.isSelected()) {
							selectedServiceNames2.add(checkBox.getText());
						}
					}
				}
				
				for (RoomAdditionalService service : hotelManager.getRoomAdditionalServiceManager().getRServiceList()
						.values()) {
					if (selectedServiceNames2.contains(service.getServiceName())) {
						adRoom.add(service);
						selectedServiceNames2.remove(service.getServiceName());
					}
				}
			room.setRoomType(rt);
			room.setRoomStatus(status);
			room.setAdditionalServices(adRoom);
			DefaultTableModel model = (DefaultTableModel) table.getModel();
		    model.setValueAt(rt.getServiceName(), selectedRow, 1);
		    model.setValueAt(status.toString(), selectedRow,2);
		    String additionalServices = "";
		    for (RoomAdditionalService ras : adRoom) {
		    	additionalServices += ras.getServiceName() + ", ";
		    }
		    model.setValueAt(additionalServices, selectedRow, 3);
			JOptionPane.showMessageDialog(window, "Soba " + room.getRoomNumber() + " je uspešno izmenjena.");
			hotelManager.saveData();
		});
        
        JButton add = new JButton("Dodaj");
        add.addActionListener(e -> {
        	int newId = hotelManager.getRoomManager().getRoomList().size() + 1;
        	int indeksTipaSobe = roomComboBox.getSelectedIndex() - 1;
            if (indeksTipaSobe == -1) {
	    		JOptionPane.showMessageDialog(window, "Molimo odaberite tip sobe.");
	    		return;
            }
			RoomType rt = hotelManager.getRoomManager().getRoomType(indeksTipaSobe);
			int indeksStatusaSobe = stComboBox.getSelectedIndex();
			if (indeksStatusaSobe == 0) {
				JOptionPane.showMessageDialog(window, "Molimo odaberite status sobe.");
				return;
			}
			RoomStatus status = RoomStatus.valueOf(stOptions[indeksStatusaSobe]);
			ArrayList<RoomAdditionalService> adRoom = new ArrayList<RoomAdditionalService>();
	        Set<String> selectedServiceNames2 = new HashSet<>();
				for (int i = 0; i < additionalServicesPanel2.getComponentCount(); i++) {
					if (additionalServicesPanel2.getComponent(i) instanceof JCheckBox) {
						JCheckBox checkBox = (JCheckBox) additionalServicesPanel2.getComponent(i);

						if (checkBox.isSelected()) {
							selectedServiceNames2.add(checkBox.getText());
						}
					}
				}
				
				for (RoomAdditionalService service : hotelManager.getRoomAdditionalServiceManager().getRServiceList()
						.values()) {
					if (selectedServiceNames2.contains(service.getServiceName())) {
						adRoom.add(service);
						selectedServiceNames2.remove(service.getServiceName());
					}
				}
			int newRoom = hotelManager.getRoomManager().addRoom(rt, status, adRoom, false);
			addRowToRoomTable(hotelManager.getRoomManager().getRoom(newRoom));
			JOptionPane.showMessageDialog(window, "Soba " + newRoom + " je uspešno dodata.");
			hotelManager.getRoomManager().saveData();
			
        });
        
        JScrollPane roomComboBoxScrollPane = new JScrollPane(roomComboBox);
        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[]30[][][]30[][][][][][][][][]"));
        formPanel.add(form, "align left, span 2, wrap");
        formPanel.add(number, "align right");
        formPanel.add(number2, "wrap");
        formPanel.add(type, "align right");
        formPanel.add(roomComboBoxScrollPane, "wrap");
        formPanel.add(nameLabel, "align right");
        formPanel.add(stComboBox, "wrap");
        formPanel.add(additionalServicesPanel2, "span 2, wrap");
        
        formPanel.add(okButton, "align right, aligny bottom, span 2");
        formPanel.add(add, "align right, aligny bottom, span 1");
        return formPanel;
	}
	
	
	private void populateRoomTable(HotelManager hotelManager) {
        String[] columnNames = {"Broj sobe", "Tip","Status", "Dodatne usluge"};
        roomTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(roomTableModel);
        JPanel formPanel = createRoomFormPanel(hotelManager, table);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        populateRoomData(hotelManager);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel labeledTablePanel = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanel.add(scrollPane, "cell 0 0, grow");
        JLabel tableLabel = new JLabel("Sobe u hotelu");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        buttonAndTablesPanel.add(tableLabel, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanel, "grow, pushy");
        JButton deleteButton = new JButton("Obriši");
		deleteButton.addActionListener(e -> {
			int selectedRow;
			if (table.getSelectedRow() != -1) {
				selectedRow = table.getSelectedRow();
				roomTableModel.removeRow(selectedRow);
				roomTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
				hotelManager.getRoomManager().getRoom(selectedRow + 1).setDeleted(true);
				hotelManager.saveData();
				JOptionPane.showMessageDialog(window,
						"Soba " + hotelManager.getRoomManager().getRoom(selectedRow + 1).getRoomNumber()
								+ " je uspešno obrisana iz sistema.");

			} else {
				JOptionPane.showMessageDialog(window, "Izaberite sobu iz tabele koju želite da obrišete.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});
        buttonAndTablesPanel.add(deleteButton, "align left, wrap");
        JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
        tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);
        tablesAndFormPanel.add(formPanel, BorderLayout.EAST);
        JPanel page2 = (JPanel) tabPanel.getComponentAt(4);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
    }
	
	private void populateReservationTable(HotelManager hotelManager, RoomManager rm, GuestManager gm, AServiceManager sm, RoomAdditionalServiceManager rasm) {
        String[] columnNames = {"ID","Ime i prezime","Tip sobe", "Broj sobe","Broj gostiju", "Datum dolaska", "Datum odlaska", "Dodatne usluge", "Status", "Cena", "Dodatne sobne usluge"};
        hotelManager.getReservationManager().loadData(gm, rm, sm, rasm, hotelManager.getMaidManager());
        reservationTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(reservationTableModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        populateReservationData(hotelManager, rm, gm, sm, rasm);
        
//        JButton deleteButton = new JButton("Obriši");
//        JButton changeButton = new JButton("Izmeni");
//        deleteButton.addActionListener(e -> {
//        	int selectedRow;
//            if (table.getSelectedRow() != -1) {
//                selectedRow = table.getSelectedRow();
//                reservationTableModel.removeRow(selectedRow);
//                reservationTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
//                hotelManager.getStaffManager().getReceptionistManager().getGuestManager().getGuestByID(selectedRow).setDeleted(true);
//                hotelManager.getStaffManager().getReceptionistManager().getGuestManager().saveData();
//                JOptionPane.showMessageDialog(window, "Gost " + hotelManager.getStaffManager().getReceptionistManager().getGuestManager().getGuestByID(selectedRow).getFirstName() + hotelManager.getStaffManager().getReceptionistManager().getGuestManager().getGuestByID(selectedRow).getLastName() + " je uspešno obrisan iz sistema.");
//                
//            } else {
//                JOptionPane.showMessageDialog(window, "Izaberite gosta iz tabele kog želite da obrišete.", "Error", JOptionPane.ERROR_MESSAGE);
//                return; 
//            }
//        });

        JPanel buttonPanel = new JPanel(new MigLayout("wrap 2", "[]push[][]", ""));
//        buttonPanel.add(deleteButton, "grow");
//        buttonPanel.add(changeButton, "grow");
        
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel labeledTablePanel = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanel.add(scrollPane, "cell 0 0, grow");
        JLabel tableLabel = new JLabel("Rezervacije");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        buttonAndTablesPanel.add(tableLabel, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanel, "grow, pushy");
        buttonAndTablesPanel.add(buttonPanel, "growx");	
        JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
        tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);
        JPanel page4 = (JPanel) tabPanel.getComponentAt(3);
        page4.setLayout(new BorderLayout());
        page4.add(tablesAndFormPanel, BorderLayout.CENTER);
    }
	
	private void populateReservationData(HotelManager hotelManager, RoomManager rm, GuestManager gm, AServiceManager sm, RoomAdditionalServiceManager rasm) {
		hotelManager.getReservationManager().loadData(gm, rm, sm, rasm, hotelManager.getMaidManager());
		for (Reservation r : hotelManager.getReservationManager().getReservations().values()) {
			addRowToReservationTable(hotelManager, r);
        }
	
	}
	
	private boolean addRowToReservationTable(HotelManager hm, Reservation reservation) {
		String additionalServices = "";
		for (AService ad : reservation.getAd()) {
			if (ad == null) {
				continue;
			}
			AService service = hm.getServiceManager().getServiceList().get(ad.getId());
			if (service == null) {
				continue;
			}
			else {
			String name = service.getServiceName();
			additionalServices += name + ", ";
		}}
		String additionalServicesRoom = "";
	for (RoomAdditionalService ras : reservation.getAdRoom()) {
		if (ras == null) {
			continue;
		}
		RoomAdditionalService service = hm.getRoomAdditionalServiceManager().getRServiceList().get(ras.getId());
		if (service == null) {
			continue;
		} else {
			String name = service.getServiceName();
			additionalServicesRoom += name + ", ";
		}
	}
	Room room = reservation.getRoom();
    String roomStr = "";
	if (room == null) {
		roomStr = "";
	} else {
		roomStr = String.valueOf(room.getRoomNumber());
	}
		Object[] rowData = {reservation.getId(),
				reservation.getGuest().getUsername(),
				reservation.getRoomType().getServiceName(),
				roomStr,
				reservation.getNumOfGuest(), reservation.getCheckInDate().toString(),
				reservation.getCheckOutDate().toString(), additionalServices,
				reservation.getReservationStatus().toString()
				, reservation.getPrice(), additionalServicesRoom};
		reservationTableModel.addRow(rowData);
		return true;
	}
	
	private void addRowToPricelistsTable(Pricelist pricelist) {
		if (!pricelist.isDeleted()) {
		Object[] rowData = { pricelist.getId(), pricelist.getName(), pricelist.getDateFrom().toString(), pricelist.getDateTo().toString() };
		pricelistsTableModel.addRow(rowData);}
	}
	
	private JPanel createPricelistsForm(HotelManager hm, JTable table) {
		JLabel form = new JLabel("Forma za dodavanje i izmenu cenovnika");
        JLabel nameLabel = new JLabel("Naziv:");
        JTextField nameField = new JTextField(18);
        JLabel d1 = new JLabel("OD:");
        DatePicker odd = new DatePicker();
        JLabel d2 = new JLabel("DO:");
        DatePicker dod = new DatePicker();
        JButton addButton = new JButton("Dodaj");
        addButton.setPreferredSize(new Dimension(70, addButton.getPreferredSize().height));
        addButton.addActionListener(e -> {
			String name = nameField.getText();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite naziv.");
				return;
			}
			LocalDate date1 = odd.getDate();
			if (date1 == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite datum od.");
				return;
			}
			LocalDate date2 = dod.getDate();
			if (date2 == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite datum do.");
				return;
			}
			int priority = date1.until(date2).getDays();
			int p = hotelManager.getPricelistManager().generatePricelist(name, date1, date2, priority);
	        addRowToPricelistsTable(hm.getPricelistManager().getPricelistList().get(p));
			JOptionPane.showMessageDialog(window, "Cenovnik " + name + " je uspešno dodat.");
			hotelManager.getPricelistManager().saveData();
			nameField.setText("");
			odd.setText("");
			dod.setText("");
		});
        
        JButton okButton = new JButton("Izmeni");
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(window, "Molimo izaberite cenovnik.");
					return;
				}
				Pricelist pricelist = hm.getPricelistManager().getPricelistList()
						.get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
				nameField.setText(pricelist.getName());
				odd.setDate(pricelist.getDateFrom());
				dod.setDate(pricelist.getDateTo());
				okButton.setEnabled(true);
			}
		});
		okButton.addActionListener(e1 -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
			    JOptionPane.showMessageDialog(window, "Molimo izaberite cenovnik.");
                return;
		}
			if (nameField.getText().isEmpty()) {
				JOptionPane.showMessageDialog(window, "Molimo unesite naziv.");
				return;
			}
			if (odd.getDate() == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite datum od.");
				return;
			}
			if (dod.getDate() == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite datum do.");
				return;
			}
			
            Pricelist pricelist = hm.getPricelistManager().getPricelistList()
                    .get(Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
            pricelist.setName(nameField.getText());
            pricelist.setDateFrom(odd.getDate());
            pricelist.setDateTo(dod.getDate());
            
            table.setValueAt(pricelist.getName(), selectedRow, 1);
            table.setValueAt(pricelist.getDateFrom(), selectedRow, 2);
            table.setValueAt(pricelist.getDateTo(), selectedRow, 3);
            int priority = odd.getDate().until(dod.getDate()).getDays();
            pricelist.setPriority(priority);
            
            table.repaint();
            hm.getPricelistManager().saveData();
            JOptionPane.showMessageDialog(window, "Cenovnik " + pricelist.getName() + " je uspešno izmenjen.");
            populatePricelistTable(hotelManager);
		});
        
        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][]"));
        formPanel.add(form, "align left, span 2, wrap");
        formPanel.add(nameLabel, "align right");
        formPanel.add(nameField, "wrap");
        formPanel.add(d1, "align right");
        formPanel.add(odd, "wrap");
        formPanel.add(d2, "align right");
        formPanel.add(dod, "wrap");
        formPanel.add(okButton, "align right");
        formPanel.add(addButton, "align right");
        return formPanel;
	}
	
	private void populatePricelistsTable(HotelManager hm) {
        String[] columnNames = {"ID","Naziv","Datum od","Datum do"};
        pricelistsTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(pricelistsTableModel);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        hm.getPricelistManager().loadData();
		for (Pricelist pricelist : hm.getPricelistManager().getPricelistList().values()) {
			addRowToPricelistsTable(pricelist);
		}
		JPanel formPanel = createPricelistsForm(hm, table);
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel labeledTablePanel = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
		labeledTablePanel.add(scrollPane, "cell 0 0, grow");
		JLabel tableLabel = new JLabel("Cenovnici");
		tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
		buttonAndTablesPanel.add(tableLabel, "grow, pushy, center");
		buttonAndTablesPanel.add(labeledTablePanel, "grow, pushy");
		JButton deleteButton = new JButton("Obriši");
		buttonAndTablesPanel.add(deleteButton, "span 1, align left, wrap");
		deleteButton.addActionListener(e -> {
			int selectedRow;
			if (table.getSelectedRow() != -1) {
				selectedRow = table.getSelectedRow();
				pricelistsTableModel.removeRow(selectedRow);
				pricelistsTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
				hm.getPricelistManager().getPricelistList().get(selectedRow).setDeleted(true);
				hm.getPricelistManager().saveData();
				JOptionPane.showMessageDialog(window,
						"Cenovnik " + hm.getPricelistManager().getPricelistList().get(selectedRow).getName()
								+ " je uspešno obrisan iz sistema.");
			} else {
				JOptionPane.showMessageDialog(window, "Izaberite cenovnik iz tabele kog želite da obrišete.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});
		JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
		tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);
		tablesAndFormPanel.add(formPanel, BorderLayout.EAST);
		JPanel page3 = (JPanel) tabPanel.getComponentAt(5);
		page3.setLayout(new BorderLayout());
		page3.add(tablesAndFormPanel, BorderLayout.CENTER);
	}
	
	private void populateReportsTabPanel(HotelManager hm) {
		
	    JPanel page5 = (JPanel) tabPanel.getComponentAt(6);
	    page5.setLayout(new BorderLayout());
	    
	    JPanel panel = new JPanel(new BorderLayout());
	    JPanel formPanel = new JPanel(new MigLayout("", "30[]30[grow]", "30[][][][][][][][][][]20[][][][][]20[][]"));
	    DatePicker date1 = new DatePicker();
	    DatePicker date2 = new DatePicker();
	    JLabel d1 = new JLabel("OD:");
	    JLabel d2 = new JLabel("DO:");
	    JLabel d3 = new JLabel("Status:");
	    JComboBox<String> status = new JComboBox<>(new String[]{"", "CONFIRMED", "CANCELED", "REJECTED"});
	    JLabel d4 = new JLabel("Sobarica:");
	    JComboBox<String> maid = new JComboBox<>();
	    maid.addItem("");
        for (Staff staff : hm.getStaffManager().getMaidManager().getMaidList().values()) {
			if (staff.isDeleted()) {
				continue;
			}
			maid.addItem(staff.getUsername());
        }
	    JLabel d5 = new JLabel("Soba:");
	    JComboBox<String> room = new JComboBox<>();
	    room.addItem("");
	    for (Room room1 : hm.getRoomManager().getRoomList().values()) {
	        StringBuilder roomAd = new StringBuilder();
	        for (RoomAdditionalService ras : room1.getAdditionalServices()) {
	            if (ras == null) {
	                continue;
	            }
	            RoomAdditionalService service = hm.getRoomAdditionalServiceManager().getRServiceList().get(ras.getId());
	            if (service == null) {
	                continue;
	            } else {
	                String name = service.getServiceName();
	                roomAd.append(name).append(", ");
	            }
	        }
	        room.addItem(room1.getRoomNumber() + " - " + room1.getRoomType().getServiceName() + " - " + roomAd.toString());
	    }
	    
	    JButton show1 = new JButton("Prikaži prihode");
	    JButton show2 = new JButton("Prikaži statuse rezervacija");
	    JButton show3 = new JButton("Prikaži opterećenost sobarica");
	    JButton show4 = new JButton("Prikaži opterećenost sobe");
	    JButton show5 = new JButton("Prikaži prihode sobe");
	    
		show1.addActionListener(e -> {
			if (date1.getDate() == null || date2.getDate() == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite oba datuma.");
				return;
			}
			LocalDate dateFrom = date1.getDate();
			LocalDate dateTo = date2.getDate();
			double income = 0;
			for (Reservation r : hm.getReservationManager().getReservations().values()) {
				LocalDate date = r.getDate();
				if ((date.isAfter(dateFrom) || date.isEqual(dateFrom)) && (date.isBefore(dateTo) || date.isEqual(dateTo))) {
					income += r.getPrice();
				}
			}
			JOptionPane.showMessageDialog(window,
					"Prihod u periodu od " + dateFrom + " do " + dateTo + " je: " + income);
			room.setSelectedIndex(0);
	        maid.setSelectedIndex(0);
	        status.setSelectedIndex(0);
	        date1.setText("");
	        date2.setText("");
		});
	    
		show2.addActionListener(e -> {
			if (date1.getDate() == null || date2.getDate() == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite oba datuma.");
				return;
			}
			if (status.getSelectedItem().equals("")) {
				JOptionPane.showMessageDialog(window, "Molimo izaberite status rezervacije.");
				return;
			}
	        LocalDate dateFrom = date1.getDate();
	        LocalDate dateTo = date2.getDate();
	        String status1 = (String) status.getSelectedItem();

	        if (status1.isEmpty()) {
	            JOptionPane.showMessageDialog(window, "Izaberite status rezervacije.");
	            return;
	        }

	        hm.getReservationManager().loadData(hm.getReceptionistManager().getGuestManager(), hm.getRoomManager(),
	                hm.getServiceManager(), hm.getRoomAdditionalServiceManager(), hm.getMaidManager());

	        int confirmed = 0, cancelled = 0, rejected = 0;
	        for (Reservation r : hm.getReservationManager().getReservations().values()) {
	            if (r.getCheckInDate().isAfter(dateFrom) && r.getCheckOutDate().isBefore(dateTo)) {
	                switch (r.getReservationStatus().toString()) {
	                    case "CONFIRMED":
	                        confirmed++;
	                        break;
	                    case "CANCELED":
	                        cancelled++;
	                        break;
	                    case "REJECTED":
	                        rejected++;
	                        break;
	                }
	            }
	        }

	        int count = switch (status1) {
	            case "CONFIRMED" -> confirmed;
	            case "CANCELED" -> cancelled;
	            case "REJECTED" -> rejected;
	            default -> 0;
	        };

	        JOptionPane.showMessageDialog(window, "Broj rezervacija sa statusom " + status1 + " u periodu od "
	                + dateFrom + " do " + dateTo + " je: " + count);
	        room.setSelectedIndex(0);
	        maid.setSelectedIndex(0);
	        status.setSelectedIndex(0);
	        date1.setText("");
	        date2.setText("");
		}); 
		
	
	    show3.addActionListener(e -> {
	    	int count = 0;
	    	Maid maidd = null;
	    	String maid1 = (String) maid.getSelectedItem();
	    	LocalDate g1 = date1.getDate();
			LocalDate g2 = date2.getDate();
			if (maid1.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Izaberite sobaricu.");
				return;
			}
			if (date1.getDate() == null || date2.getDate() == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite oba datuma.");
				return;
			}
			
	    	for (Maid maid2 : hm.getStaffManager().getMaidManager().getMaidList().values()) {
				if (maid2.getUsername().equals(maid1)) {
					maidd = maid2;
				}
			}
			for (Reservation r : hm.getReservationManager().getReservations().values()) {
				LocalDate cout = r.getCheckOutDate2();
				
				
				if (r.getMaid() == null) {
					continue;
				}
				if (r.getMaid().getUsername().equals(maidd.getUsername())) {
					if (cout.isAfter(g1) && cout.isBefore(g2)) {
						count++;
					}
				}
			}
			JOptionPane.showMessageDialog(window, "Broj soba koje je sobarica " + maidd.getUsername() + " očistila u periodu od " + date1.getDate() + " do " + date2.getDate() + " je: " + count);
			room.setSelectedIndex(0);
	        maid.setSelectedIndex(0);
	        status.setSelectedIndex(0);
	        date1.setText("");
	        date2.setText("");
	    });
	   
	    show4.addActionListener(e -> {
	    	Room selectedRoom = null;
	        int count = 0;
	        
	        LocalDate g1 = date1.getDate();
	        LocalDate g2 = date2.getDate();
	        
	        if (date1.getDate() == null || date2.getDate() == null) {
                JOptionPane.showMessageDialog(window, "Molimo unesite oba datuma.");
                return;
	        }
			if (room.getSelectedItem().equals("")) {
				JOptionPane.showMessageDialog(window, "Izaberite sobu.");
				return;
			}
	        
	        String roomSelected = (String) room.getSelectedItem();
	        if (roomSelected != null && !roomSelected.isEmpty()) {
	            String[] parts = roomSelected.split(" - ");
	            int roomNumber = Integer.parseInt(parts[0]);
	            selectedRoom = hm.getRoomManager().getRoom(roomNumber);

	            for (Reservation r : hm.getReservationManager().getReservations().values()) {
	                LocalDate coutdate = r.getCheckOutDate2();
	                if (coutdate == null) {
                    	coutdate = LocalDate.now();
                    }
	                LocalDate cindate = r.getCheckInDate();
					if (r.getRoom() == null) {
						continue;
					}
	                if (r.getRoom().getRoomNumber() == selectedRoom.getRoomNumber() ) {
	                    for (LocalDate i = g1; i.isBefore(g2.plusDays(1)); i = i.plusDays(1)) {
	                        if ((cindate.isBefore(i) || cindate.isEqual(i)) && (coutdate.isAfter(i))) {
	                            count++;
	                        }
	                    }
	                }
	            }

	            JOptionPane.showMessageDialog(window, "Broj noćenja u sobi " + selectedRoom.getRoomNumber() + " u periodu od " + g1 + " do " + g2 + " je: " + count);
	            room.setSelectedIndex(0);
	            maid.setSelectedIndex(0);
	            status.setSelectedIndex(0);
	            date1.setText("");
	            date2.setText("");
	        } 
	    
	    });
	    
	    show5.addActionListener(e -> {
        double count = 0;
        LocalDate g1 = date1.getDate();
        LocalDate g2 = date2.getDate();
			if (date1.getDate() == null || date2.getDate() == null) {
				JOptionPane.showMessageDialog(window, "Molimo unesite oba datuma.");
				return;
			}
        String roomSelected = (String) room.getSelectedItem();
			if (roomSelected.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Izaberite sobu.");
				return;
			}
        
        String[] parts = roomSelected.split(" - ");
        int roomNumber;
        roomNumber = Integer.parseInt(parts[0]);
        
        Room selectedRoom = hm.getRoomManager().getRoom(roomNumber);
        
        for (Reservation r : hm.getReservationManager().getReservations().values()) {
        	if (r.getRoom()  == null) {
        		continue;
        	}
            if (r.getRoom().getRoomNumber() == selectedRoom.getRoomNumber() && !r.getReservationStatus().equals(ReservationStatus.REJECTED)) {
            	
                LocalDate date = r.getDate();
                
                if ((date.isAfter(g1) || date.isEqual(g1)) && (date.isBefore(g2) || date.isEqual(g2))) {
                    count += r.getPrice();
                }
            }
        }

        JOptionPane.showMessageDialog(window, "Prihod u sobi " + selectedRoom.getRoomNumber() + " u periodu od " + g1 + " do " + g2 + " je: " + count + " RSD");
        room.setSelectedIndex(0);
        maid.setSelectedIndex(0);
        status.setSelectedIndex(0);
        date1.setText("");
        date2.setText("");
        
    
});
	    JPanel panel2 = new JPanel(new MigLayout("", "[]", "[][]"));
	    JLabel label = new JLabel("Ukupan prihod: " + hm.getIncome());
		JLabel label2 = new JLabel("Ukupni mesečni rashodi: " + hm.getExpenses());
		panel2.add(label, "center");
		panel2.add(label2, "center");
		
		formPanel.add(d1, "align left");
		formPanel.add(date1, "wrap");
		formPanel.add(d2, "align left");
		formPanel.add(date2, "wrap");
		formPanel.add(d3, "align left");
		formPanel.add(status, "wrap");
		formPanel.add(d4, "align left");
		formPanel.add(maid, "wrap");
		formPanel.add(d5, "align left");
		formPanel.add(room, "wrap");
		formPanel.add(show1, "align left, wrap, span 2, growx");
		formPanel.add(show2, "align left, wrap, span 2, growx");
		formPanel.add(show3, "align left, wrap, span 2, growx");
		formPanel.add(show4, "align left, wrap, span 2, growx");
		formPanel.add(show5, "align left, wrap, span 2, growx");
		formPanel.add(label, "align left, wrap");
		formPanel.add(label2, "align left, wrap");
		panel.add(formPanel, BorderLayout.WEST);
		panel.add(panel2, BorderLayout.SOUTH);
		page5.add(panel, BorderLayout.CENTER);
		
	}



	private void populateChartTabPanel(HotelManager hm) {
		JPanel page7 = (JPanel) tabPanel.getComponentAt(7);
		page7.setLayout(new BorderLayout());
		JPanel panel00 = new JPanel();
		JPanel panel01 = new JPanel();
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		HashMap<String, Integer> data1 = new HashMap<>();
		for(Maid maid : hm.getMaidManager().getMaidList().values()) {
			data1.put(maid.getUsername(), 0);
		}
		
		for (Reservation r : hm.getReservationManager().getReservations().values()) {
			if(!r.getReservationStatus().equals(ReservationStatus.FINISHED))
				continue;
			
			LocalDate cout = r.getCheckOutDate2();
			LocalDate g1 = LocalDate.now().minusDays(30);
			LocalDate g2 = LocalDate.now();
			
			Maid maid = r.getMaid();
			
			if (cout.isAfter(g1) && (cout.isBefore(g2) || cout.isEqual(g2))) {
				int soFar = data1.get(maid.getUsername());
				soFar++;
				data1.put(maid.getUsername(), soFar);
			}
		}
		
		HashMap<String, Integer> data2 = new HashMap<>();
		for (ReservationStatus rs : ReservationStatus.values()) {
            data2.put(rs.toString(), 0);
        }
		for (Reservation r : hm.getReservationManager().getReservations().values()) {
			LocalDate g1 = LocalDate.now().minusDays(30);
			LocalDate g2 = LocalDate.now();
			LocalDate date = r.getDate();
			if (date.isAfter(g1) && (date.isBefore(g2) || date.isEqual(g2))) {
				int soFar = data2.get(r.getReservationStatus().toString());
				soFar++;
				data2.put(r.getReservationStatus().toString(), soFar);
			}
		}
		
		PieChart chart1 = createPieChart(data1, "Opterećenje sobarica u prethodnih 30 dana");
		JPanel chartPanel1 = new XChartPanel<>(chart1);
		panel1.add(chartPanel1);
		
		PieChart chart2 = createPieChart(data2, "Status rezervacija u prethodnih 30 dana");
		JPanel chartPanel2 = new XChartPanel<>(chart2);
		panel2.add(chartPanel2);
		
		CategoryChart chart = new CategoryChartBuilder()
                .title("Prihodi po tipu sobe")
                .xAxisTitle("Mesec")
                .yAxisTitle("Prihod")
                .height(350)
                .width(1150)
                .build();

		List<String> xData = new ArrayList<>();
		Map<String, List<Integer>> data3 = new HashMap<>();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.yy.");
		
		LocalDate now = LocalDate.now();
		for (LocalDate i = now.minusMonths(12); i.isBefore(now.plusDays(1)); i = i.plusMonths(1)) {
		    xData.add(i.format(formatter));
		}
		
		for (RoomType roomType : hm.getRoomManager().getRoomTypeList().values()) {
		    List<Integer> monthlyRevenues = new ArrayList<>();
		    
		    for (int i = 0; i < xData.size(); i++) {
		        monthlyRevenues.add(0);
		    }
		    data3.put(roomType.getServiceName(), monthlyRevenues);
		}
		data3.put("Ukupno", new ArrayList<>(Collections.nCopies(xData.size(), 0)));
		
		for (Reservation r : hm.getReservationManager().getReservations().values()) {
		    if (r.getReservationStatus().equals(ReservationStatus.REJECTED)) {
		        continue;
		    }
		    LocalDate date = r.getDate();
		    if (date.isAfter(now.minusMonths(12)) && (date.isBefore(now) || date.isEqual(now))) {
		        int monthIndex = (int) now.minusMonths(12).until(date, java.time.temporal.ChronoUnit.MONTHS);
		        String roomType = r.getRoomType().getServiceName();
		        List<Integer> revenues = data3.get(roomType);
		        revenues.set(monthIndex, revenues.get(monthIndex) + (int) r.getPrice());
		        data3.put(roomType, revenues);
		        List<Integer> totalRevenues = data3.get("Ukupno");
		        totalRevenues.set(monthIndex, totalRevenues.get(monthIndex) + (int) r.getPrice());
		        data3.put("Ukupno", totalRevenues);
		        
		    }
		}

		List<Integer> ukupnoRevenues = data3.remove("Ukupno");
		
		for (Map.Entry<String, List<Integer>> entry : data3.entrySet()) {
		    chart.addSeries(entry.getKey(), xData, entry.getValue());
		}
		
		chart.addSeries("Ukupno", xData, ukupnoRevenues);
		
		chart.getStyler().setLegendVisible(true);

        panel3.add(new XChartPanel<>(chart));
        panel3.setVisible(true);
		
		panel00.setLayout(new BorderLayout());
		panel00.add(panel1, BorderLayout.WEST);
		panel00.add(panel2, BorderLayout.EAST);
		panel01.add(panel3, BorderLayout.CENTER);
		page7.add(panel00, BorderLayout.NORTH);
		page7.add(panel01, BorderLayout.SOUTH);
	}
	
	private PieChart createPieChart(HashMap<String, Integer> data, String title) {
		PieChart chart = new PieChartBuilder().width(400).height(200).title(title).build();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            chart.addSeries(entry.getKey(), entry.getValue());
        }
        chart.getStyler().setLegendVisible(true);
        return chart;
	}
}
