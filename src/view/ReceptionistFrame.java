package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.github.lgooddatepicker.components.DatePicker;

import entity.Guest;
import entity.Maid;
import entity.Pricelist;
import entity.Reservation;
import entity.Room;
import entity.RoomAdditionalService;
import entity.RoomType;
import entity.Service;
import entity.AService;
import entity.User;
import enums.Gender;
import enums.ReservationStatus;
import enums.RoomStatus;
import enums.ServiceType;
import manage.GuestManager;
import manage.HotelManager;
import manage.MaidManager;
import manage.PricelistManager;
import manage.ReservationManager;
import manage.RoomAdditionalServiceManager;
import manage.RoomManager;
import manage.AServiceManager;
import net.miginfocom.swing.MigLayout;

public class ReceptionistFrame extends JFrame {
	private JFrame window;
    private JTabbedPane tabPanel;
    private HotelManager hotelManager;
    private DefaultTableModel guestTableModel;
    private DefaultTableModel pricelistTableModelR;
    private DefaultTableModel pricelistTableModelA;
    private DefaultTableModel reservationTableModel;
    private DefaultTableModel roomTableModel;
    private DefaultTableModel reservationTableModel1;
    private DefaultTableModel reservationTableModel2;
    //private DefaultTableModel reservationTableModel3;
    
    @Override
	public void dispose() {
		hotelManager.saveData();
		super.dispose();
	}
    
	public ReceptionistFrame(User user, HotelManager hotelManager) {
		this.hotelManager = hotelManager;
		RoomManager rm = hotelManager.getRoomManager();
		GuestManager gm = hotelManager.getReceptionistManager().getGuestManager();
		AServiceManager sm = hotelManager.getServiceManager();
        initializeWindow(user);
        addLogoutButton(user);
        initializeTabPanel();
        populateGuestTable(hotelManager);
        populatePricelistTable(hotelManager);
        populateReservationTable(hotelManager, rm, gm, sm, hotelManager.getRoomAdditionalServiceManager(), hotelManager.getMaidManager());
        populateRoomTable(hotelManager);
        populateDailyNewsTable(hotelManager);
        hotelManager.getReservationManager().loadData(gm, rm, sm, hotelManager.getRoomAdditionalServiceManager(), hotelManager.getMaidManager());
       
        window.setVisible(true);
	}
	
	private void initializeWindow(User user) {
        window = new JFrame("Receptionist Panel - " + user.getFirstName() + " " + user.getLastName());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1200, 650);
        ImageIcon icon = new ImageIcon("icon.png");
        window.setIconImage(icon.getImage());
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setLayout(new MigLayout("", "[]", "[][grow]"));
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
        JPanel page2 = new JPanel();
        JPanel page3 = new JPanel();
        JPanel page4 = new JPanel();
        JPanel page5 = new JPanel();
        tabPanel.addTab("Gosti", page1);
        tabPanel.addTab("Rezervacije", page2);
        tabPanel.addTab("Sobe", page3);
        tabPanel.addTab("Cenovnik", page4);
        tabPanel.add("Dnevnik", page5);
        window.add(tabPanel, "grow, push, wrap");
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
			if (!guest.isDeleted())
			addRowToGuestTable(guest);
        }
	
	}
	
	private JPanel createGuestFormPanel(HotelManager hotelManager, JTable table) {
    	JLabel form = new JLabel("Forma za dodavanje gostiju");
        JLabel nameLabel = new JLabel("Ime:");
        JTextField nameField = new JTextField(18);
        JLabel lastNameLabel = new JLabel("Prezime:");
        JTextField lastNameField = new JTextField(18);
        JLabel genderLabel = new JLabel("Pol:");
        Gender[] genderValues = Gender.values();
        String[] genderOptions = new String[genderValues.length + 1];
        genderOptions[0] = "";
        for (int i = 0; i < genderValues.length; i++) {
            genderOptions[i + 1] = genderValues[i].toString();
        }
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
        

        JButton addButton = new JButton("Dodaj");
        JButton changeButton = new JButton("Izmeni");
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
        	int selectedRow = table.getSelectedRow();
        	if (selectedRow != -1) {
        		String name = guestTableModel.getValueAt(selectedRow, 0).toString();
        		String lastName = guestTableModel.getValueAt(selectedRow, 1).toString();
        		Gender gender = Gender.valueOf(guestTableModel.getValueAt(selectedRow, 2).toString());
        		LocalDate dob = LocalDate.parse(guestTableModel.getValueAt(selectedRow, 3).toString());
        		String phone = guestTableModel.getValueAt(selectedRow, 4).toString();
        		String address = guestTableModel.getValueAt(selectedRow, 5).toString();
        		String username = guestTableModel.getValueAt(selectedRow, 6).toString();
        		String password = guestTableModel.getValueAt(selectedRow, 7).toString();
        		
        		nameField.setText(name);
        		lastNameField.setText(lastName);
        		genderComboBox.setSelectedItem(gender.toString());
        		dobChooser.setDate(dob);
        		phoneField.setText(phone);
        		addressField.setText(address);
        		usernameField.setText(username);
        		passwordField.setText(password);
        		changeButton.setEnabled(true);
        				
        	}
        	            }
        });
        changeButton.addActionListener(e -> {
        	int selectedRow = table.getSelectedRow();
        	if (selectedRow != -1) {
        		String name = nameField.getText();
        		String lastName = lastNameField.getText();
        		Gender gender = Gender.valueOf(genderComboBox.getSelectedItem().toString());
        		LocalDate dob = dobChooser.getDate();
        		String phone = phoneField.getText();
        		String address = addressField.getText();
        		String username = usernameField.getText();
        		String password = new String(passwordField.getPassword());
        		
        		Guest guest = hotelManager.getReceptionistManager().getGuestManager().getGuestByID(selectedRow);
        		guest.setFirstName(name);
        		guest.setLastName(lastName);
        		guest.setGender(gender);
        		guest.setDateOfBirth(dob);
        		guest.setPhone(phone);
        		guest.setAddress(address);
        		guest.setUsername(username);
        		guest.setPassword(password);
        		hotelManager.getReceptionistManager().getGuestManager().saveData();
        		guestTableModel.setValueAt(name, selectedRow, 0);
        		guestTableModel.setValueAt(lastName, selectedRow, 1);
        		guestTableModel.setValueAt(gender.toString(), selectedRow, 2);
        		guestTableModel.setValueAt(dob.toString(), selectedRow, 3);
        		guestTableModel.setValueAt(phone, selectedRow, 4);
        		guestTableModel.setValueAt(address, selectedRow, 5);
        		guestTableModel.setValueAt(username, selectedRow, 6);
        		guestTableModel.setValueAt(password, selectedRow, 7);
        		JOptionPane.showMessageDialog(window, "Gost " + name + " " + lastName + " je uspešno izmenjen.");
        		nameField.setText("");
        		lastNameField.setText("");
        		genderComboBox.setSelectedIndex(0);
        		dobChooser.setDate(null);
        		phoneField.setText("");
        		addressField.setText("");
        		usernameField.setText("");
        		passwordField.setText("");
        		
        		
        	}
			else {
				JOptionPane.showMessageDialog(window, "Izaberite gosta iz tabele kog želite da izmenite.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
        	});
        addButton.setPreferredSize(new Dimension(70, addButton.getPreferredSize().height));
        addButton.addActionListener(e -> {
			String name = nameField.getText();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Unesite ime gosta.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String lastName = lastNameField.getText();
			if (lastName.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Unesite prezime gosta.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Gender gender = Gender.values()[genderComboBox.getSelectedIndex() - 1];
			if (gender == null) {
				JOptionPane.showMessageDialog(window, "Izaberite pol gosta.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			LocalDate dob = dobChooser.getDate();
			if (dob == null) {
				JOptionPane.showMessageDialog(window, "Izaberite datum rođenja gosta.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String phone = phoneField.getText();
			if (phone.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Unesite telefon gosta.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String address = addressField.getText();
			if (address.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Unesite adresu gosta.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String username = usernameField.getText();
			for (Guest guest : hotelManager.getReceptionistManager().getGuestManager().getGuestList().values()) {
				if (guest.getUsername().equals(username)) {
					JOptionPane.showMessageDialog(window, "Korisničko ime već postoji.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if (username.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Unesite korisničko ime gosta.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String password = new String(passwordField.getPassword());
			if (password.isEmpty()) {
				JOptionPane.showMessageDialog(window, "Unesite lozinku gosta.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
		    int g = hotelManager.getReceptionistManager().getGuestManager().addGuest(name, lastName, gender, dob, phone, address, username, password, false, 0.0);
			addRowToGuestTable(hotelManager.getReceptionistManager().getGuestManager().getGuestByID(g));
			hotelManager.getReceptionistManager().getGuestManager().saveData();
			JOptionPane.showMessageDialog(window, "Gost " + name + " " + lastName + " je uspešno dodat.");
			
			
        });

        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[]30[][][]30[][][][][][][][][]"));
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
        formPanel.add(changeButton, "align right");
        formPanel.add(addButton, "align right, aligny bottom");
        

        return formPanel;
    
        
	}

	private void populateGuestTable(HotelManager hotelManager) {
        String[] columnNames = {"Ime", "Prezime", "Pol", "Datum rođenja", "Telefon", "Adresa", "Korisničko ime", "Lozinka"};
        hotelManager.getStaffManager().getReceptionistManager().getGuestManager().loadData();
        guestTableModel = new DefaultTableModel(columnNames, 0);

        
        
        JTable table = new JTable(guestTableModel);
        JPanel formPanel = createGuestFormPanel(hotelManager, table);
        
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
       
        
        populateGuestData(hotelManager);
        
        JButton deleteButton = new JButton("Obriši");
        
        
        
        deleteButton.addActionListener(e -> {
        	int selectedRow;
            if (table.getSelectedRow() != -1) {
                selectedRow = table.getSelectedRow();
                guestTableModel.removeRow(selectedRow);
                guestTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                hotelManager.getStaffManager().getReceptionistManager().getGuestManager().getGuestByID(selectedRow).setDeleted(true);
                hotelManager.getStaffManager().getReceptionistManager().getGuestManager().saveData();
                JOptionPane.showMessageDialog(window, "Gost " + hotelManager.getStaffManager().getReceptionistManager().getGuestManager().getGuestByID(selectedRow).getFirstName() + " " + hotelManager.getStaffManager().getReceptionistManager().getGuestManager().getGuestByID(selectedRow).getLastName() + " je uspešno obrisan iz sistema.");
                
            } else {
                JOptionPane.showMessageDialog(window, "Izaberite gosta iz tabele kog želite da obrišete.", "Error", JOptionPane.ERROR_MESSAGE);
                return; 
            }
        });

        JPanel buttonPanel = new JPanel(new MigLayout("wrap 2", "[]push[][]", ""));
        buttonPanel.add(deleteButton, "align left, wrap");
        
        JScrollPane scrollPane = new JScrollPane(table);
        

        JPanel labeledTablePanel = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanel.add(scrollPane, "cell 0 0, grow");
        JLabel tableLabel = new JLabel("Gosti hotela");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        buttonAndTablesPanel.add(tableLabel, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanel, "grow, pushy");
        
        buttonAndTablesPanel.add(buttonPanel, "growx");	

        JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
        tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);
        tablesAndFormPanel.add(formPanel, BorderLayout.EAST);

        JPanel page2 = (JPanel) tabPanel.getComponentAt(0);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
    }

	private void populatePricelistTable(HotelManager hotelManager) {
	    String[] columnNames = {"Naziv","Cena","Od","Do"};
	    String[] columnNames2 = {"Tip sobe","Broj kreveta", "Kapacitet", "Cena","Od","Do"};
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
	
	    JPanel tablesAndFormPanel = new JPanel(new MigLayout("", "[grow][]", "[grow]"));
	    tablesAndFormPanel.add(buttonAndTablesPanel, "grow");
	    JPanel page1 = (JPanel) tabPanel.getComponentAt(3);
	    page1.setLayout(new BorderLayout());
	    page1.add(tablesAndFormPanel, BorderLayout.CENTER);
	}
	
	private void populatePricelistData(HotelManager hotelManager) {
		for (AService service : hotelManager.getServiceManager().getServiceList().values()) {
			if (service.isDeleted()) { continue; }
	        addRowToPricelistTableA(service);
	    }
		for (RoomAdditionalService room : hotelManager.getRoomAdditionalServiceManager().getRServiceList().values()) {
			if (room.isDeleted()) {
				continue;
			}
			addRowToPricelistTableRA(room);
		}
		for (RoomType room : hotelManager.getRoomManager().getRoomTypeList().values()) {
			if (room.isDeleted()) { continue; }
			addRowToPricelistTableR(room);
		}
	}
	
	private void addRowToPricelistTableA(AService service) {
		Object[] rowData = { service.getServiceName(),
				String.valueOf(service.getPrice()), hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getDateFrom(), hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getDateTo()};
		pricelistTableModelA.addRow(rowData);
	}
	
	private void addRowToPricelistTableRA(RoomAdditionalService service) {
		Object[] rowData = { service.getServiceName(),
				String.valueOf(service.getPrice()), hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getDateFrom(), hotelManager.getPricelistManager().getPricelistList().get(service.getPricelistID()).getDateTo()};
		pricelistTableModelA.addRow(rowData);
	}
	
	private void addRowToPricelistTableR(RoomType room) {
		Object[] rowData = { room.getServiceName(), room.getBeds(), room.getCapacity(),
				String.valueOf(room.getPrice()), hotelManager.getPricelistManager().getPricelistList().get(room.getPricelistID()).getDateFrom(), hotelManager.getPricelistManager().getPricelistList().get(room.getPricelistID()).getDateTo() };
		pricelistTableModelR.addRow(rowData);
	}

	
	private void addRRT(HotelManager hm, Reservation reservation) {
		
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
		
		String roomAdditionalServices = "";
		for (RoomAdditionalService ras : reservation.getAdRoom()) {
			if (ras == null) {
				continue;
			}
			RoomAdditionalService service = hm.getRoomAdditionalServiceManager().getService(ras.getId());
			if (service == null) {
				continue;
			} else {
				String name = service.getServiceName();
				roomAdditionalServices += name + ", ";
			}
		}
		
		Room room = reservation.getRoom();
        String roomStr = "";
		if (room == null) {
			roomStr = "";
		} else {
			roomStr = String.valueOf(room.getRoomNumber());
		}
		
		Object[] rowData = {
        		reservation.getId(),
        		reservation.getGuest().getUsername(),
                 reservation.getRoomType().getServiceName(),
                 roomStr,
                reservation.getNumOfGuest(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                additionalServices,
                reservation.getReservationStatus().toString(),
                reservation.getPrice(),
                roomAdditionalServices
                
                
        };
		reservationTableModel.addRow(rowData);
	}
	
	
	private boolean hasAvailableRooms(HotelManager hm, Reservation r) {
		int totalWithProperties = 0;
		RoomType roomType = r.getRoomType();
		LocalDate ourCheckInDate = r.getCheckInDate();
		LocalDate ourCheckOutDate = r.getCheckOutDate();
		ArrayList<RoomAdditionalService> rad = r.getAdRoom();
		
		for (Room room : hm.getRoomManager().getRoomList().values()) {
			if (room.getRoomType().equals(roomType)) {
				boolean containsAll = true;
				for (RoomAdditionalService ras : rad) {
					if (room.getAdditionalServices() == null) {
						containsAll = false;
						break;
					}
					containsAll = containsAll && room.getAdditionalServices().contains(ras);
				}
				if (containsAll) {
					totalWithProperties++;
				}
			}
		}
		
		int foundInReservations = 0;
		for (Reservation reservation : hm.getReservationManager().getReservations().values()) {
			LocalDate thisCheckIn = reservation.getCheckInDate();
			LocalDate thisCheckOut = reservation.getCheckOutDate();
			RoomType thisRoomType = reservation.getRoomType();
			ReservationStatus thisStatus = reservation.getReservationStatus();
			if ((thisRoomType == roomType) && (thisStatus.equals(ReservationStatus.CONFIRMED))) {
				if ((ourCheckInDate.isBefore(thisCheckOut)) && 
					(ourCheckOutDate.isAfter(thisCheckIn))
				) {
					ArrayList<RoomAdditionalService> thisRad = reservation.getAdRoom();
					boolean containsAll = true;
					if (thisRad.size() == 0 && rad.size() != 0) {
						containsAll = false;
					}
					for (RoomAdditionalService ourRad : rad) {
						containsAll = containsAll && thisRad.contains(ourRad);
					}
					if  (containsAll) {
						foundInReservations++;
					}
				}
			}
		}
//		if (hm.getRoomManager().getRoomTypeCount().get(roomType) == null) {
//			return false;
//		}
		int roomsRemaining = totalWithProperties - foundInReservations;
		return roomsRemaining > 0;
	}


	private void populateReservationData(HotelManager hotelManager, boolean isAll, int a) {
		for (Reservation r : hotelManager.getReservationManager().getReservations().values()) {
			if (isAll && (a == 2))
			addRRT(hotelManager, r);
			else {
				if (a == 0) {
					addRRT0(hotelManager, r, true);
				}
				else {
					addRRT0(hotelManager, r, false);
				}
			}
        }
	}
	
	private void addRRT0(HotelManager hm, Reservation reservation, boolean arrival) {
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
		
		String roomAdditionalServices = "";
		for (RoomAdditionalService ras : reservation.getAdRoom()) {
			if (ras == null) {
				continue;
			}
			RoomAdditionalService service = hm.getRoomAdditionalServiceManager().getService(ras.getId());
			if (service == null) {
				continue;
			} else {
				String name = service.getServiceName();
				roomAdditionalServices += name + ", ";
			}
		}
		
		Room room = reservation.getRoom();
        String roomStr = "";
		if (room == null) {
			roomStr = "";
		} else {
			roomStr = String.valueOf(room.getRoomNumber());
		}
		
		Object[] rowData = {
        		reservation.getId(),
        		reservation.getGuest().getUsername(),
                 reservation.getRoomType().getServiceName(),
                 roomStr,
                reservation.getNumOfGuest(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                additionalServices,
                reservation.getReservationStatus().toString(),
                reservation.getPrice(),
                roomAdditionalServices
                
                
        };
		if (reservation.getCheckInDate().isEqual(LocalDate.now()) && arrival && !reservation.getReservationStatus().equals(ReservationStatus.REJECTED))
		reservationTableModel1.addRow(rowData);
		if (reservation.getCheckOutDate().isEqual(LocalDate.now()) && !arrival && !reservation.getReservationStatus().equals(ReservationStatus.REJECTED))
			reservationTableModel2.addRow(rowData);
		
	}
	
	private JPanel createReservationFormPanel(HotelManager hotelManager, JTable table) {
    	JLabel form = new JLabel("Forma za rezervacije");
    	JLabel idLabel = new JLabel("ID rezervacije:");
    	JLabel id = new JLabel();
    	id.setText(hotelManager.getReservationManager().getReservations().size() + "");       
    	JLabel nameLabel = new JLabel("Korisničko ime gosta:");
        JTextField nameField = new JTextField(20);
        JLabel roomLabel = new JLabel("Soba:");
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
			if (roomOptions.contains(roomValues.get(i))) {
				continue;
			}
        	roomOptions.add(roomValues.get(i));
        }
        JComboBox<String> roomComboBox = new JComboBox<String>(roomOptions.toArray(new String[0]));
        JPanel additionalServicesPanel2 = new JPanel(new MigLayout()); 
		additionalServicesPanel2.setLayout(new MigLayout());
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
        
		JLabel numOfGuestsLabel = new JLabel("Broj gostiju:");
		JTextField numOfGuestsField = new JTextField(10);
		JLabel checkInDateLabel = new JLabel("Datum dolaska:");
		DatePicker checkInDateChooser = new DatePicker();
		JLabel checkOutDateLabel = new JLabel("Datum odlaska:");
		DatePicker checkOutDateChooser = new DatePicker();
		JPanel additionalServicesPanel = new JPanel(new MigLayout()); 
		additionalServicesPanel.setLayout(new MigLayout());

        Set<String> addedServiceNames = new HashSet<>();
        int checkboxCount = 0;

        for (AService service : hotelManager.getServiceManager().getServiceList().values()) {
            if (!service.isDeleted() && service.getServiceType() == ServiceType.ADDITIONAL_SERVICE) {
                String serviceName = service.getServiceName();

                if (!addedServiceNames.contains(serviceName)) {
                    JCheckBox checkBox = new JCheckBox(serviceName);
                    checkboxCount++;

                    if (checkboxCount % 3 == 0) {
                        additionalServicesPanel.add(checkBox, "wrap 1");
                    } else {
                        additionalServicesPanel.add(checkBox);
                    }

                    addedServiceNames.add(serviceName);
                }
            }
        }
        
        table.addMouseListener(new MouseAdapter() {
        	
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                
				if (selectedRow != -1) {
					id.setText(reservationTableModel.getValueAt(selectedRow, 0).toString());
					nameField.setText(reservationTableModel.getValueAt(selectedRow, 1).toString());
					roomComboBox.setSelectedItem(reservationTableModel.getValueAt(selectedRow, 2).toString());
					numOfGuestsField.setText(reservationTableModel.getValueAt(selectedRow, 4).toString());
					checkInDateChooser.setDate((LocalDate) reservationTableModel.getValueAt(selectedRow, 5));
					checkOutDateChooser.setDate((LocalDate) reservationTableModel.getValueAt(selectedRow, 6));
					for (int i = 0; i < additionalServicesPanel.getComponentCount(); i++) {
						JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(i);
						checkBox.setSelected(false);
					}
					String additionalServices = reservationTableModel.getValueAt(selectedRow, 7).toString();
					String[] additionalServicesArray = additionalServices.split(", ");
					for (int i = 0; i < additionalServicesArray.length; i++) {
						for (int j = 0; j < additionalServicesPanel.getComponentCount(); j++) {
							JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(j);
							if (checkBox.getText().equals(additionalServicesArray[i])) {
								checkBox.setSelected(true);
							}
						}
					
					}
					String additionalServices2 = reservationTableModel.getValueAt(selectedRow, 7).toString();
					String[] additionalServicesArray2 = additionalServices2.split(", ");
					for (int i = 0; i < additionalServicesArray2.length; i++) {
						for (int j = 0; j < additionalServicesPanel2.getComponentCount(); j++) {
							JCheckBox checkBox = (JCheckBox) additionalServicesPanel2.getComponent(j);
							if (checkBox.getText().equals(additionalServicesArray2[i])) {
								checkBox.setSelected(true);
							}
						}
					
					}
				}
            }
        });
        

        JButton addButton = new JButton("Pretraži");
        addButton.setPreferredSize(new Dimension(70, addButton.getPreferredSize().height));
        addButton.addActionListener(e -> {
        	boolean exists = false;
        	int id2 = Integer.parseInt(id.getText());
			for (Reservation reservation : hotelManager.getReservationManager().getReservations().values()) {
				if (reservation.getId() == id2) {
					JOptionPane.showMessageDialog(window, "Rezervacija " + reservation.getId() + " je pronađena.");
					if (!hasAvailableRooms(hotelManager, reservation)) {
						JOptionPane.showMessageDialog(window, "Tip sobe nije dostupan za dati period.");
						reservation.setReservationStatus(ReservationStatus.REJECTED);
						hotelManager.getReservationManager().getReservations().get(id2).setPrice(0);
						reservationTableModel.setValueAt(ReservationStatus.REJECTED.toString(), table.getSelectedRow(), 8);
						reservationTableModel.setValueAt(0, table.getSelectedRow(), 9);
						hotelManager.getReservationManager().saveData();
						reservationTableModel.fireTableDataChanged();
						exists = true;
						break;
					}
					else {
						JOptionPane.showMessageDialog(window, "Rezervacija " + reservation.getId() + " je potvrđena.");
						reservation.setReservationStatus(ReservationStatus.CONFIRMED);
						reservationTableModel.setValueAt(ReservationStatus.CONFIRMED.toString(), table.getSelectedRow(), 8);
						hotelManager.getReservationManager().saveData();
						reservationTableModel.fireTableDataChanged();
						exists = true;
						if (reservation.getCheckInDate().isEqual(LocalDate.now())) {
				        	   addRRT0(hotelManager, hotelManager.getReservationManager().getReservations().get(id2),true);
				        	   reservationTableModel1.fireTableDataChanged();}
						break;
				}
			}}
			if (!exists) {
			String name = nameField.getText();
			
			Guest g = null;
			for (Guest guest : hotelManager.getReceptionistManager().getGuestManager().getGuestList().values()) {
					if (guest.isDeleted()) {
						continue;
					}
				if (guest.getUsername().equals(name)) {
					JOptionPane.showMessageDialog(window,
							"Gost " + guest.getFirstName() + " " + guest.getLastName() + " je pronađen.");
					g = guest;
					break;
				}
				
			}
				if (g == null) {
					JOptionPane.showMessageDialog(window, "Gost nije pronađen.");
					nameField.setText("");
					roomComboBox.setSelectedIndex(0);
					numOfGuestsField.setText("");
					checkInDateChooser.setDate(null);
					checkOutDateChooser.setDate(null);
					for (int i = 0; i < additionalServicesPanel.getComponentCount(); i++) {
						JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(i);
						checkBox.setSelected(false);
					}
					for (int i = 0; i < additionalServicesPanel2.getComponentCount(); i++) {
						JCheckBox checkBox = (JCheckBox) additionalServicesPanel2.getComponent(i);
						checkBox.setSelected(false);
					}
					
					return;
				}
			
			
			int roomType = (roomComboBox.getSelectedIndex()-1);
			RoomType rt = hotelManager.getRoomManager().getRoomTypeList().get(roomType);
			int numOfGuests = Integer.parseInt(numOfGuestsField.getText());
			LocalDate checkInDate = checkInDateChooser.getDate();
			LocalDate checkOutDate = checkOutDateChooser.getDate();
			ArrayList<AService> ad = new ArrayList<AService>();

			Set<String> selectedServiceNames = new HashSet<>();

	        for (int i = 0; i < additionalServicesPanel.getComponentCount(); i++) {
	            if (additionalServicesPanel.getComponent(i) instanceof JCheckBox) {
	                JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(i);
	                
	                if (checkBox.isSelected()) {
	                    selectedServiceNames.add(checkBox.getText());
	                }
	            }
	        }

	        for (AService service : hotelManager.getServiceManager().getServiceList().values()) {
	            if (selectedServiceNames.contains(service.getServiceName())) {
	                ad.add(service);
	                selectedServiceNames.remove(service.getServiceName());
	            }
	        }
	        
	        
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
				
				if (numOfGuests < 1) {
					JOptionPane.showMessageDialog(window, "Broj gostiju mora biti veći od 0.");
					return;
				}
				if (numOfGuests > rt.getCapacity()) {
					JOptionPane.showMessageDialog(window, "Broj gostiju prelazi kapacitet sobe.");
					numOfGuestsField.setText("");
					return;
				}
		Room room = null;
		ReservationManager resM = hotelManager.getReservationManager();
		PricelistManager pricelistManager = hotelManager.getPricelistManager();

		double price = calculateTotalPrice(checkInDate, checkOutDate, ad, numOfGuests, rt, pricelistManager, adRoom);
		
		Reservation potential = new Reservation(id2, g, room, rt, numOfGuests, checkInDate, checkOutDate, ad, ReservationStatus.ONHOLD, price, LocalDate.now(), adRoom, null, null);
		
		boolean valid = hasAvailableRooms(hotelManager, potential);
		
		if (valid && price > 0) {
		
		potential.setReservationStatus(ReservationStatus.CONFIRMED);
		resM.addReservation(potential);
			
		  addRRT(hotelManager, hotelManager.getReservationManager().getReservations().get(id2));
		   
		   JOptionPane.showMessageDialog(window, "Rezervacija je uspešno dodata.");
		   double n = g.getExpenses();
		   g.setExpenses(n + price);
		   hotelManager.getReceptionistManager().getGuestManager().saveData();
		   double income = hotelManager.getIncome();
		   hotelManager.setIncome(income + price);
		   hotelManager.getReservationManager().saveData();
		   if (checkInDate.isEqual(LocalDate.now())) {
        	   addRRT0(hotelManager, hotelManager.getReservationManager().getReservations().get(id2),true);
        	   reservationTableModel1.fireTableDataChanged();}
		   
		   
		   reservationTableModel.fireTableDataChanged();
			}
		   else {
			   if (price < 0) {
				   resM.addReservation(potential);
	               hotelManager.getReservationManager().getReservations().get(id2).setReservationStatus(ReservationStatus.REJECTED);
	               hotelManager.getReservationManager().getReservations().get(id2).setPrice(0);
	               hotelManager.getReservationManager().saveData();
	               addRRT(hotelManager, hotelManager.getReservationManager().getReservations().get(id2));
	               
	               reservationTableModel.fireTableDataChanged();
				   
			   }
			   else {
               JOptionPane.showMessageDialog(window, "Tip sobe nije dostupan za dati period.");
               resM.addReservation(potential);
               hotelManager.getReservationManager().getReservations().get(id2).setReservationStatus(ReservationStatus.REJECTED);
               hotelManager.getReservationManager().getReservations().get(id2).setPrice(0);
               hotelManager.getReservationManager().saveData();
               addRRT(hotelManager, hotelManager.getReservationManager().getReservations().get(id2));
               
               
               reservationTableModel.fireTableDataChanged();}
               
		   }
			}
		
		id.setText(id2 + 1 + "");
		nameField.setText("");
		roomComboBox.setSelectedIndex(0);
		numOfGuestsField.setText("");
		checkInDateChooser.setDate(null);
		checkOutDateChooser.setDate(null);
		for (int i = 0; i < additionalServicesPanel.getComponentCount(); i++) {
				JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(i);
				checkBox.setSelected(false);
		}
		reservationTableModel.fireTableDataChanged();
           
                      
        });
        
        JButton change = new JButton("Izmeni");
		change.setPreferredSize(new Dimension(70, change.getPreferredSize().height));
		change.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
                JOptionPane.showMessageDialog(window, "Izaberite rezervaciju iz tabele koju želite da izmenite.");
                return;
            }
			
			
			ArrayList<AService> ad = new ArrayList<AService>();
			for (int i = 0; i < additionalServicesPanel.getComponentCount(); i++) {
				JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(i);
				if (checkBox.isSelected()) {
					for (AService service : hotelManager.getServiceManager().getServiceList().values()) {
						if (ad.contains(service)) {
							continue;
						}
						if (service.getServiceName().equals(checkBox.getText())) {
							ad.add(service);
							
						}
					}
				}
			}
			String name = nameField.getText();
			Guest g = null;
			for (Guest guest : hotelManager.getReceptionistManager().getGuestManager().getGuestList().values()) {
				
				if (guest.getUsername().equals(name)) {
					JOptionPane.showMessageDialog(window,
							"Gost " + guest.getFirstName() + " " + guest.getLastName() + " je pronađen.");
					g = guest;
					break;
				}
				
			}
			Reservation reservation = hotelManager.getReservationManager().getReservations().get(selectedRow);
			reservation.setAd(ad);
			double income = hotelManager.getIncome();
			hotelManager.setIncome(income - reservation.getPrice());
			double price = 0;
			RoomType rt = reservation.getRoomType();
			LocalDate checkInDate = reservation.getCheckInDate();
			LocalDate checkOutDate = reservation.getCheckOutDate();
			PricelistManager pricelistManager = hotelManager.getPricelistManager();
			LocalDate pricelistDateFrom = pricelistManager.getPricelistList().get(rt.getPricelistID()).getDateFrom();
			LocalDate pricelistDateTo = pricelistManager.getPricelistList().get(rt.getPricelistID()).getDateTo();
			if (checkInDate.isAfter(pricelistDateFrom) && checkOutDate.isBefore(pricelistDateTo)) {
				price = rt.getPrice() * (checkOutDate.getDayOfYear() - checkInDate.getDayOfYear());
				double add = (ad.stream().mapToDouble(AService::getPrice).sum()
						* (checkOutDate.getDayOfYear() - checkInDate.getDayOfYear()));
				price += add;
			}
			reservation.setPrice(price);
			g.setExpenses(g.getExpenses() + reservation.getPrice());
			double income2 = hotelManager.getIncome();
			hotelManager.setIncome(income2 + price);
			
			hotelManager.getReservationManager().saveData();
			reservationTableModel.setValueAt(price, selectedRow, 9);
			
			StringBuilder additionalServices = new StringBuilder();
			
			Set<String> selectedServiceNames2 = new HashSet<>();
	        for (AService ad2 : reservation.getAd()) {
	            AService service = hotelManager.getServiceManager().getServiceList().get(ad2.getId());
	            if (service != null) {
	            	if (!selectedServiceNames2.contains(service.getServiceName())) {
	                additionalServices.append(service.getServiceName()).append(", ");
	                selectedServiceNames2.add(service.getServiceName());
	            	}
	            }
	        

	        
				reservationTableModel.setValueAt(additionalServices.toString(), selectedRow, 7);
			}
			reservationTableModel.fireTableDataChanged();
			hotelManager.getReservationManager().saveData();
			
			
		});
		
		JButton getRoom = new JButton("Check in");
		getRoom.setPreferredSize(new Dimension(70, getRoom.getPreferredSize().height));
		getRoom.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(window, "Izaberite rezervaciju iz tabele kojoj želite da dodelite sobu.");
				return;
			}
			Reservation reservation = hotelManager.getReservationManager().getReservations().get(selectedRow);
			if (giveFreeRoom(hotelManager, reservation)) {
				JOptionPane.showMessageDialog(window, "Soba je uspešno dodeljena.");
				reservationTableModel.setValueAt(reservation.getRoom().getRoomNumber(), selectedRow, 3);
				hotelManager.getReservationManager().saveData();
				reservationTableModel.fireTableDataChanged();
				roomTableModel.setValueAt(RoomStatus.OCCUPIED, reservation.getRoom().getRoomNumber()-1, 2);
				roomTableModel.fireTableDataChanged();
				hotelManager.getRoomManager().getRoomList().get(reservation.getRoom().getRoomNumber()).setRoomStatus(RoomStatus.OCCUPIED);
				hotelManager.getRoomManager().saveData();
				
				id.setText(reservation.getId() + 1 + "");
				nameField.setText("");
				roomComboBox.setSelectedIndex(0);
				for (int i = 0; i < additionalServicesPanel2.getComponentCount(); i++) {
					JCheckBox checkBox = (JCheckBox) additionalServicesPanel2.getComponent(i);
					checkBox.setSelected(false);
				}
				numOfGuestsField.setText("");
				checkInDateChooser.setDate(null);
				checkOutDateChooser.setDate(null);
				for (int i = 0; i < additionalServicesPanel.getComponentCount(); i++) {
					JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(i);
					checkBox.setSelected(false);
				}
				
				 
			} else {
				JOptionPane.showMessageDialog(window, "Nije moguće dodeliti slobodnu sobu.");
			}
		});
		
		JButton checkOutButton = new JButton("Check out");
		checkOutButton.setPreferredSize(new Dimension(70, checkOutButton.getPreferredSize().height));
		checkOutButton.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(window, "Izaberite rezervaciju iz tabele koju želite da završite.");
				return;
			}
			Reservation reservation = hotelManager.getReservationManager().getReservations().get(selectedRow);
			if (reservation.getRoom() == null) {
				JOptionPane.showMessageDialog(window, "Rezervacija nema dodeljenu sobu.");
				return;
			}
			reservation.getRoom().setRoomStatus(RoomStatus.CLEANING);
			Maid maid = hotelManager.getMaidManager().assignMaidToRoom(reservation.getRoom());
			reservation.setMaid(maid);
			reservation.setCheckOutDate2(LocalDate.now());
			hotelManager.getMaidManager().saveData();
			reservation.setReservationStatus(ReservationStatus.FINISHED);
			JOptionPane.showMessageDialog(window, "Check out uspešno završen.");
			
			reservationTableModel.setValueAt(ReservationStatus.FINISHED, selectedRow, 8);
			roomTableModel.setValueAt(RoomStatus.CLEANING, reservation.getRoom().getRoomNumber()-1, 2);
			roomTableModel.fireTableDataChanged();
			hotelManager.getReservationManager().saveData();
			reservationTableModel.fireTableDataChanged();
			hotelManager.getRoomManager().getRoomList().get(reservation.getRoom().getRoomNumber()).setRoomStatus(RoomStatus.CLEANING);
			hotelManager.getRoomManager().saveData();
			
			reservationTableModel.fireTableDataChanged();
			
			
		});
		
		

        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[][][][][][][][][][][][][]"));
        formPanel.add(form, "align left, span 2, wrap");
        formPanel.add(idLabel, "align right");
        formPanel.add(id, "wrap");
        formPanel.add(nameLabel, "align right");
        formPanel.add(nameField, "wrap");
        formPanel.add(roomLabel, "align right");
        formPanel.add(roomComboBox, "wrap");
        formPanel.add(additionalServicesPanel2, "span 2, grow, wrap");
        formPanel.add(numOfGuestsLabel, "align right");
        formPanel.add(numOfGuestsField, "wrap");
        formPanel.add(checkInDateLabel, "align right");
        formPanel.add(checkInDateChooser, "wrap");
        formPanel.add(checkOutDateLabel, "align right");
        formPanel.add(checkOutDateChooser, "wrap");
        formPanel.add(additionalServicesPanel, "span 2, grow, wrap");
        
        formPanel.add(addButton, "align right, aligny bottom, span 1");
        formPanel.add(change, "align right, aligny bottom, span 1, wrap");
        formPanel.add(getRoom, "align right, aligny bottom, span 1");
        formPanel.add(checkOutButton, "align right, aligny bottom, span 1");

        return formPanel;
        
	}
	
	

	private void populateReservationTable(HotelManager hotelManager, RoomManager rm, GuestManager gm, AServiceManager sm, RoomAdditionalServiceManager asm, MaidManager mm) {
        String[] columnNames = {"ID","Ime i prezime", "Tip sobe", "Broj sobe", "Broj gostiju", "Datum dolaska", "Datum odlaska", "Dodatne usluge", "Status", "Cena", "Dodatne usluge sobe"};
        hotelManager.getReservationManager().loadData(gm, rm, sm, asm, mm);
        reservationTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(reservationTableModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(reservationTableModel);
        table.setRowSorter(sorter);
        JPanel formPanel = createReservationFormPanel(hotelManager, table);
        
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
       
        populateReservationData(hotelManager, true, 2);
        
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
        JButton filterButton = new JButton("Filter");
		filterButton.addActionListener(e -> {
			JDialog filterDialog = new JDialog(window, "Filter", true);
			filterDialog.setSize(450, 300);
			filterDialog.setLocationRelativeTo(null);
			filterDialog.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
			filterDialog.setResizable(false);
			filterDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			// filtracija po tipu sobe, broju sobe, ceni i dodaatnim uslugama
			JLabel roomTypeLabel = new JLabel("Tip sobe:");
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
				if (roomOptions.contains(roomValues.get(i))) {
					continue;
				}
				roomOptions.add(roomValues.get(i));
			}
			JComboBox<String> roomComboBox = new JComboBox<String>(roomOptions.toArray(new String[0]));
			JLabel roomNumberLabel = new JLabel("Broj sobe:");
			JTextField roomNumberField = new JTextField(10);
			JLabel priceLabel = new JLabel("Cena");
			JLabel priceL1 = new JLabel("Od:");
			JTextField priceField = new JTextField(10);
			JLabel priceL2 = new JLabel("Do:");
			JTextField priceField2 = new JTextField(10);
			JLabel additionalServicesLabel = new JLabel("Dodatne usluge:");
			JPanel additionalServicesPanel = new JPanel(new MigLayout());
			additionalServicesPanel.setLayout(new MigLayout());
			Set<String> addedServiceNames = new HashSet<>();
			int checkboxCount = 0;
			for (AService service : hotelManager.getServiceManager().getServiceList().values()) {
				if (!service.isDeleted() && service.getServiceType() == ServiceType.ADDITIONAL_SERVICE) {
					String serviceName = service.getServiceName();
					if (!addedServiceNames.contains(serviceName)) {
						JCheckBox checkBox = new JCheckBox(serviceName);
						checkboxCount++;
						if (checkboxCount % 3 == 0) {
							additionalServicesPanel.add(checkBox, "wrap 1");
						} else {
							additionalServicesPanel.add(checkBox);
						}
						addedServiceNames.add(serviceName);
					}
				}
			}
			
			JButton filterButton2 = new JButton("Pretraži");
			filterButton2.addActionListener(e2 -> {
				TableRowSorter<DefaultTableModel> sorter2 = new TableRowSorter<>(reservationTableModel);
                table.setRowSorter(sorter2);
                List<RowFilter<Object, Object>> filters = new ArrayList<>();
                if (roomComboBox.getSelectedIndex() != 0) {
                    filters.add(RowFilter.regexFilter(roomComboBox.getSelectedItem().toString(), 2));
                }
                if (!roomNumberField.getText().equals("")) {
                    filters.add(RowFilter.regexFilter(roomNumberField.getText(), 3));
                }
                if (!priceField.getText().equals("")) {
                    filters.add(RowFilter.numberFilter(ComparisonType.AFTER, Integer.parseInt(priceField.getText()), 9));
                }
                if (!priceField2.getText().equals("")) {
                    filters.add(RowFilter.numberFilter(ComparisonType.BEFORE, Integer.parseInt(priceField2.getText()), 9));
                }
                Set<String> selectedServiceNames = new HashSet<>();
                for (int i = 0; i < additionalServicesPanel.getComponentCount(); i++) {
                    JCheckBox checkBox = (JCheckBox) additionalServicesPanel.getComponent(i);
                    if (checkBox.isSelected()) {
                        selectedServiceNames.add(checkBox.getText());
                    }
                }
                if (!selectedServiceNames.isEmpty()) {
                    filters.add(new RowFilter<Object, Object>() {
                        @Override
                        public boolean include(Entry<? extends Object, ? extends Object> entry) {
                            String additionalServices = entry.getStringValue(7);
                            String[] additionalServicesArray = additionalServices.split(", ");
                            for (String selectedServiceName : selectedServiceNames) {
                                boolean found = false;
                                for (String additionalService : additionalServicesArray) {
                                    if (additionalService.equals(selectedServiceName)) {
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    });
                }
                sorter2.setRowFilter(RowFilter.andFilter(filters));
                filterDialog.dispose();
            });
			filterDialog.add(roomTypeLabel, "align left");
			filterDialog.add(roomComboBox, "wrap");
			filterDialog.add(roomNumberLabel, "align left");
			filterDialog.add(roomNumberField, "wrap");
			filterDialog.add(priceLabel, "align left, wrap");
			filterDialog.add(priceL1, "align right");
			filterDialog.add(priceField, "wrap");
			filterDialog.add(priceL2, "align right");
			filterDialog.add(priceField2, "wrap");
			filterDialog.add(additionalServicesLabel, "align left");
			filterDialog.add(additionalServicesPanel, "span 2, grow, wrap");
			filterDialog.add(filterButton2, "align right, aligny bottom, span 2");
			filterDialog.setVisible(true);
			
				
			});
			
		JButton clearButton = new JButton("Očisti filter");
		clearButton.addActionListener(e -> {
			TableRowSorter<DefaultTableModel> sorter2 = new TableRowSorter<>(reservationTableModel);
            table.setRowSorter(sorter2);
            sorter2.setRowFilter(null);
		});
			
        buttonPanel.add(filterButton, "grow");
        buttonPanel.add(clearButton, "grow");
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
        tablesAndFormPanel.add(formPanel, BorderLayout.EAST);
        

        JPanel page2 = (JPanel) tabPanel.getComponentAt(1);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
    }
	
	public boolean giveFreeRoom(HotelManager hm, Reservation r) {
	    RoomType roomType = r.getRoomType();
	    LocalDate checkInDate = r.getCheckInDate();
	    List<Room> suitableRooms = new ArrayList<>();
	    
	    for (Room room : hm.getRoomManager().getRoomList().values()) {
	        if (room.getRoomType().equals(roomType) && room.getRoomStatus() == RoomStatus.VACANT && checkInDate.isEqual(LocalDate.now())) {
	            suitableRooms.add(room);
	        }
	    }
	    
	    ArrayList<RoomAdditionalService> requiredServices = r.getAdRoom();
	    Room exactMatch = null;
	    Room moreThanRequired = null;
	    
	    for (Room room : suitableRooms) {
	        ArrayList<RoomAdditionalService> roomServices = room.getAdditionalServices();
	        if (roomServices.containsAll(requiredServices)) {
	            if (roomServices.size() == requiredServices.size()) {
	                exactMatch = room;
	                break; 
	            } else if (moreThanRequired == null || roomServices.size() < moreThanRequired.getAdditionalServices().size()) {
	                moreThanRequired = room; 
	            }
	        }
	    }
	    
	    Room chosenRoom = exactMatch != null ? exactMatch : moreThanRequired;
	    
	    if (chosenRoom != null) {
	        r.setRoom(chosenRoom);
	        chosenRoom.setRoomStatus(RoomStatus.OCCUPIED);
	        return true;
	    }
	    
	    return false;
	}

	
	private void addRowToRoomTable(Room room) {
		hotelManager.getRoomManager().loadRTData();
		hotelManager.getRoomManager().loadData();
		String roomAdditionalServices = "";
		if (room.getAdditionalServices() == null) {
			roomAdditionalServices = "";
		} else 
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
		}
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
	
	private void populateRoomTable(HotelManager hotelManager) {
        String[] columnNames = {"Broj sobe", "Tip","Status", "Dodatne usluge"};
        roomTableModel = new DefaultTableModel(columnNames, 0);

        
        
        JTable table = new JTable(roomTableModel);
        
//        String[] columnNames1 = {"ID", "Ime i prezime", "Tip sobe", "Broj sobe", "Broj gostiju", "Datum dolaska", "Datum odlaska", "Dodatne usluge", "Status", "Cena", "Dodatne usluge sobe"};
//
//        reservationTableModel3 = new DefaultTableModel(columnNames1, 0);
//
//        JTable table1 = new JTable(reservationTableModel3);
        
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        
//        table1.setRowSelectionAllowed(true);
//        table1.setColumnSelectionAllowed(false);
//        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        table1.setDefaultEditor(Object.class, null);
//        table1.getTableHeader().setReorderingAllowed(false);
       
        
        populateRoomData(hotelManager);
        
//        table.addMouseListener(new MouseAdapter() {
//        	
//            @Override
//            public void mousePressed(MouseEvent e) {
//                int selectedRow = table1.getSelectedRow();
//				if (selectedRow != -1) {
//					int id = Integer.parseInt(roomTableModel.getValueAt(selectedRow, 0).toString());
//					for (Reservation reservation : hotelManager.getReservationManager().getReservations().values()) {
//						if (reservation.getRoom().getRoomNumber() == id) {
//							String additionalServices = "";
//							for (AService ad : reservation.getAd()) {
//								if (ad == null) {
//									continue;
//								}
//								AService service = hotelManager.getServiceManager().getServiceList().get(ad.getId());
//								if (service == null) {
//									continue;
//								} else {
//									String name = service.getServiceName();
//									additionalServices += name + ", ";
//								}
//							}
//							String roomAdditionalServices = "";
//							for (RoomAdditionalService ras : reservation.getAdRoom()) {
//								if (ras == null) {
//									continue;
//								}
//								RoomAdditionalService service = hotelManager.getRoomAdditionalServiceManager()
//										.getService(ras.getId());
//								if (service == null) {
//									continue;
//								} else {
//									String name = service.getServiceName();
//									roomAdditionalServices += name + ", ";
//								}
//							}
//							Object[] r =  { reservation.getId(), reservation.getGuest().getUsername(),
//									reservation.getRoomType().getServiceName(), reservation.getRoom().getRoomNumber(),
//									reservation.getNumOfGuest(), reservation.getCheckInDate(),
//									reservation.getCheckOutDate(), additionalServices,
//									reservation.getReservationStatus(), reservation.getPrice(),
//									roomAdditionalServices };
//							reservationTableModel3.addRow(r);
//						}
//					}
//				}
//            }
//                
//            });
        
        JScrollPane scrollPane = new JScrollPane(table);
     //   JScrollPane scrollPane1 = new JScrollPane(table1);
        

        JPanel labeledTablePanel = new JPanel(new MigLayout("", "[grow]", "[grow][]"));
        labeledTablePanel.add(scrollPane, "cell 0 0, grow");
        JLabel tableLabel = new JLabel("Sobe u hotelu");
       // JLabel tableLabel1 = new JLabel("Rezervacije za izabranu sobu");
        tableLabel.setHorizontalAlignment(SwingConstants.CENTER);
       
        //tableLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonAndTablesPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        buttonAndTablesPanel.add(tableLabel, "grow, pushy, center");
        buttonAndTablesPanel.add(labeledTablePanel, "grow, pushy");
//        buttonAndTablesPanel.add(tableLabel1, "grow, pushy, center");
//        buttonAndTablesPanel.add(scrollPane1, "grow, pushy");
        
        JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
        tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);

        JPanel page2 = (JPanel) tabPanel.getComponentAt(2);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
    }

	public double calculateTotalPrice(LocalDate checkInDate, LocalDate checkOutDate, List<AService> ad, int numOfGuests, Service rt, PricelistManager pricelistManager, List<RoomAdditionalService> adRoom) {
        double totalPrice = 0;

        for (LocalDate date : pricelistManager.getDatesBetween(checkInDate, checkOutDate)) {
            for (AService service : ad) {
            	
                double bestPrice = getBestPriceForService(service, date, pricelistManager);
				if (bestPrice == -1) {
					return -1;
				}
                totalPrice += bestPrice * numOfGuests;}
            	
            
            
            double bestPriceForRt = getBestPriceForService(rt, date, pricelistManager);
            totalPrice += bestPriceForRt;
        }
        
        for (LocalDate date : pricelistManager.getDatesBetween(checkInDate, checkOutDate)) {
            for (RoomAdditionalService service : adRoom) {
            	
                double bestPrice = getBestPriceForService(service, date, pricelistManager);
                if (bestPrice == -1) {
                	return -1;
                }
                totalPrice += bestPrice;
            }
        }

        return totalPrice;
    }

    private double getBestPriceForService(Service service, LocalDate date, PricelistManager pricelistManager) {
        int bestPriority = Integer.MAX_VALUE;
        double bestPrice = -1;

        for (Pricelist pricelist : pricelistManager.getPricelistList().values()) {
        	
        	LocalDate pricelistDateFrom = pricelist.getDateFrom();
            LocalDate pricelistDateTo = pricelist.getDateTo();
        	if ((date.isAfter(pricelistDateFrom) || date.isEqual(pricelistDateFrom)) &&
                  (date.isBefore(pricelistDateTo) || date.isEqual(pricelistDateTo)) && (pricelist.getPriority() <= bestPriority)) { 
        		bestPriority = pricelist.getPriority();
	        	for (Service s : pricelist.getServices().values()) {
					if (s == null) {
						continue;
					}
	        		if (!s.isDeleted() && s.getServiceName().equals(service.getServiceName())) {
	        			bestPrice = s.getPrice();
	        			break;
	        		}
	        	}
            }
			
        }
		if (bestPrice == -1) {
			JOptionPane.showMessageDialog(window,
					"Usluga " + service.getServiceName() + " nije dostupna u ovom periodu.");
			return -1;
		}
        return bestPrice;
    }
    
    public void populateDailyNewsTable(HotelManager hm) {
        String[] columnNames = {"ID", "Ime i prezime", "Tip sobe", "Broj sobe", "Broj gostiju", "Datum dolaska", "Datum odlaska", "Dodatne usluge", "Status", "Cena", "Dodatne usluge sobe"};

        reservationTableModel1 = new DefaultTableModel(columnNames, 0);

        JTable table1 = new JTable(reservationTableModel1);
        reservationTableModel2 = new DefaultTableModel(columnNames, 0);

        JTable table2 = new JTable(reservationTableModel2);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(reservationTableModel1);
        table1.setRowSorter(sorter);
        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(false);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setDefaultEditor(Object.class, null);
        table1.getTableHeader().setReorderingAllowed(false);
        
        TableRowSorter<DefaultTableModel> sorter1 = new TableRowSorter<>(reservationTableModel2);
        table2.setRowSorter(sorter1);
        table2.setRowSelectionAllowed(true);
        table2.setColumnSelectionAllowed(false);
        table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table2.setDefaultEditor(Object.class, null);
        table2.getTableHeader().setReorderingAllowed(false);

        populateReservationData(hm, false, 0);  
        populateReservationData(hm, false, 1); 

        JScrollPane scrollPaneArrivals = new JScrollPane(table1);
        JScrollPane scrollPaneDepartures = new JScrollPane(table2);

        JPanel panelArrivals = createLabeledPanel(scrollPaneArrivals, "Dnevni dolasci");
        JPanel panelDepartures = createLabeledPanel(scrollPaneDepartures, "Dnevni odlasci");

        JPanel combinedPanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow][]"));
        combinedPanel.add(panelArrivals, "grow, pushy");
        combinedPanel.add(panelDepartures, "grow, pushy");

        JPanel page2 = (JPanel) tabPanel.getComponentAt(4);
        page2.setLayout(new BorderLayout());
        page2.add(combinedPanel, BorderLayout.CENTER);
    }

    
    private JPanel createLabeledPanel(JScrollPane scrollPane, String labelText) {
        JPanel labeledTablePanel = new JPanel(new MigLayout("wrap 1", "[grow]", "[][grow]"));
        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        labeledTablePanel.add(label, "growx");
        labeledTablePanel.add(scrollPane, "grow");
        return labeledTablePanel;
    }


}