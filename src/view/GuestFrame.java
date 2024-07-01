package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;

import entity.Guest;
import entity.Pricelist;
import entity.Reservation;
import entity.Room;
import entity.RoomAdditionalService;
import entity.RoomType;
import entity.Service;
import entity.AService;
import entity.User;
import enums.ReservationStatus;
import enums.ServiceType;
import manage.GuestManager;
import manage.HotelManager;
import manage.PricelistManager;
import manage.RoomManager;
import manage.AServiceManager;
import net.miginfocom.swing.MigLayout;

public class GuestFrame extends JFrame {
	private JFrame window;
    private JTabbedPane tabPanel;
    private HotelManager hotelManager;
    private DefaultTableModel reservationTableModel;
    private User user;
    
    @Override
	public void dispose() {
		hotelManager.saveData();
		super.dispose();
	}
    
	public GuestFrame(User user, HotelManager hotelManager) {
		this.hotelManager = hotelManager;
		this.user = user;
		GuestManager gm = hotelManager.getGuestManager();
        RoomManager rm = hotelManager.getRoomManager();
        AServiceManager sm = hotelManager.getServiceManager();
        initializeWindow(user);
        addLogoutButton(user);
        initializeTabPanel();
        populateReservationTable(hotelManager, rm, gm, sm);
        window.setVisible(true);
        
        hotelManager.getRoomManager().loadData();
        hotelManager.getReceptionistManager().getGuestManager().loadData();
        hotelManager.getReservationManager().loadData(gm, rm, sm, hotelManager.getRoomAdditionalServiceManager(), hotelManager.getMaidManager());
	}
	
	private void initializeWindow(User user) {
        window = new JFrame("Guest Panel - " + user.getFirstName() + " " + user.getLastName());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        window.setSize(1200, 600);
        ImageIcon icon = new ImageIcon("../image/icon.png");
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
        tabPanel.addTab("Rezervacije", page1);
        window.add(tabPanel, "grow, push, wrap");
    }
	
    
    private double addRowToReservationTable(HotelManager hm, Reservation reservation, User user) {
    	
	        String additionalServices = "";
			for (AService ad : reservation.getAd()) {
				String name = "";
				if (ad == null) {
					continue;
				}
				AService service = hm.getServiceManager().getServiceList().get(ad.getId());
				if (service == null) {
					continue;
				}
				else {
				name = service.getServiceName();
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
		   
			
	        Object[] rowData = {
	        		reservation.getId(),
	        		reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName(),
	        		reservation.getRoomType().getServiceName(),
	                reservation.getNumOfGuest(),
	                reservation.getCheckInDate(),
	                reservation.getCheckOutDate(),
	                additionalServices,
	                reservation.getReservationStatus().toString(),
	                reservation.getPrice(),
	                roomAdditionalServices
	        };
	        
	        reservationTableModel.addRow(rowData);
	        return reservation.getPrice();
    }
	

    private double populateReservationData(HotelManager hotelManager, User user) {
        double ut = 0;
        for (Reservation r : hotelManager.getReservationManager().getReservations().values()) {
            if (r.getGuest().getUsername().equals(user.getUsername())) {
                addRowToReservationTable(hotelManager, r, user);
				ut += r.getPrice();
            }
            
        }
        return ut;
    }

	
	private JPanel createReservationFormPanel(HotelManager hotelManager, JTable table, JLabel utl) {
    	JLabel form = new JLabel("Forma za rezervacije");
        JLabel nameLabel = new JLabel("Korisničko ime gosta:");
        JLabel nameField = new JLabel(user.getUsername());
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
		
        

        JButton addButton = new JButton("Pošalji upit");
        addButton.setPreferredSize(new Dimension(70, addButton.getPreferredSize().height));
        addButton.addActionListener(e -> {
			String name = nameField.getText();
		
			Guest g = null;
			for (Guest guest : hotelManager.getReceptionistManager().getGuestManager().getGuestList().values()) {
				
				if (guest.getUsername().equals(name)) {
					g = guest;
					break;
				}
				
			}
			int roomType = (roomComboBox.getSelectedIndex() - 1);
			if (roomType == -1) {
				JOptionPane.showMessageDialog(window, "Izaberite tip sobe.");
				return;
			}
			RoomType rt = hotelManager.getRoomManager().getRoomTypeList().get(roomType);
			if (numOfGuestsField.getText().equals("")) {
				JOptionPane.showMessageDialog(window, "Unesite broj gostiju.");
				return;
			}
			int numOfGuests = Integer.parseInt(numOfGuestsField.getText());
			
			LocalDate checkInDate = checkInDateChooser.getDate();
			if (checkInDate == null) {
				JOptionPane.showMessageDialog(window, "Unesite datum dolaska.");
				return;
			}
			LocalDate checkOutDate = checkOutDateChooser.getDate();
			if (checkOutDate == null) {
				JOptionPane.showMessageDialog(window, "Unesite datum odlaska.");
				return;
			}
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
		PricelistManager pricelistManager = hotelManager.getPricelistManager();
			
		double price = calculateTotalPrice(checkInDate, checkOutDate, ad, numOfGuests, rt, pricelistManager, adRoom);
		
		int r = hotelManager.getReservationManager().addReservation(g, room, rt, numOfGuests, checkInDate, checkOutDate, ad, ReservationStatus.ONHOLD, price, adRoom, null, null);
		hotelManager.getReservationManager().getReservations().get(r).setNumOfGuest(numOfGuests);
		    
		  if (price > 0) {
			  addRowToReservationTable(hotelManager, hotelManager.getReservationManager().getReservations().get(r), user);
		   JOptionPane.showMessageDialog(window, "Rezervacija je uspešno poslata na upit.");
		   hotelManager.getGuestManager().getGuestByID(user.getId()).setExpenses(hotelManager.getGuestManager().getGuestByID(user.getId()).getExpenses() + price);
		   hotelManager.getGuestManager().saveData();
		   utl.setText("Ukupan trošak: " + hotelManager.getGuestManager().getGuestByID(user.getId()).getExpenses() + " RSD");
		   double income = hotelManager.getIncome();
		   hotelManager.setIncome(income + price);
		   
           reservationTableModel.fireTableDataChanged();
        
		   hotelManager.getReservationManager().saveData();}
		else {
			Reservation res = hotelManager.getReservationManager().getReservations().get(r);
			res.setPrice(0);
			res.setReservationStatus(ReservationStatus.REJECTED);
			   addRowToReservationTable(hotelManager, res, user);
			   
			   table.setValueAt(ReservationStatus.REJECTED.toString(), table.getRowCount() - 1, 7);
	           reservationTableModel.fireTableDataChanged();
	           hotelManager.getReservationManager().saveData();
		}
        });
        
        JButton deleteButton = new JButton("Otkaži rezervaciju");
        
        deleteButton.setPreferredSize(new Dimension(70, deleteButton.getPreferredSize().height));
		deleteButton.addActionListener(e -> {
			
			int selectedRow;
			if (table.getSelectedRow() != -1) {
				selectedRow = table.getSelectedRow();
				int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
				hotelManager.getReservationManager().getReservations().get(id).setReservationStatus(ReservationStatus.CANCELED);
				reservationTableModel.setValueAt(ReservationStatus.CANCELED.toString(), selectedRow, 7);
				JOptionPane.showMessageDialog(window, "Rezervacija je uspešno otkazana.");
				reservationTableModel.fireTableDataChanged();
			    hotelManager.getReservationManager().saveData();

			} else {
				JOptionPane.showMessageDialog(window, "Izaberite rezervaciju koju želite da otkažete.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});

        JPanel formPanel = new JPanel(new MigLayout("", "[][grow]", "[]30[][][]30[][][][][][][][][]"));
        formPanel.add(form, "align left, span 2, wrap");
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
        formPanel.add(addButton, "align right, aligny bottom");
        formPanel.add(deleteButton, "align right, aligny bottom");
        return formPanel;
	}

	private void populateReservationTable(HotelManager hotelManager, RoomManager rm, GuestManager gm, AServiceManager sm) {
        String[] columnNames = {"ID","Ime i prezime", "Tip sobe", "Broj gostiju", "Datum dolaska", "Datum odlaska", "Dodatne usluge", "Status", "Cena", "Dodatne sobne usluge"};
        hotelManager.getReservationManager().loadData(gm, rm, sm, hotelManager.getRoomAdditionalServiceManager(), hotelManager.getMaidManager());
        reservationTableModel = new DefaultTableModel(columnNames, 0);
        
        JTable table = new JTable(reservationTableModel);
        
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        
        populateReservationData(hotelManager, user);
        JLabel utl = new JLabel("Ukupan trošak: " + hotelManager.getGuestManager().getGuestByID(user.getId()).getExpenses() + " RSD");
        JPanel formPanel = createReservationFormPanel(hotelManager, table,utl);
        
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
        tablesAndFormPanel.add(formPanel, BorderLayout.EAST);

        JPanel page2 = (JPanel) tabPanel.getComponentAt(0);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
        page2.add(utl, BorderLayout.SOUTH);
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

}
