package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import entity.Room;
import entity.User;
import enums.RoomStatus;
import manage.GuestManager;
import manage.HotelManager;
import manage.RoomManager;
import manage.AServiceManager;
import net.miginfocom.swing.MigLayout;

public class MaidFrame extends JFrame {
	private JFrame window;
    private JTabbedPane tabPanel;
    private HotelManager hotelManager;
    private DefaultTableModel roomTableModel;
    private User user;
    
    @Override
	public void dispose() {
		hotelManager.saveData();
		super.dispose();
	}
    
	public MaidFrame(User user, HotelManager hotelManager) {
		this.hotelManager = hotelManager;
		this.user = user;
        initializeWindow(user);
        addLogoutButton(user);
        initializeTabPanel();
        populateRoomTable(hotelManager);
        window.setVisible(true);
        hotelManager.getMaidManager().loadData(hotelManager.getRoomManager());
        hotelManager.getRoomManager().loadData();
	}
	
	private void initializeWindow(User user) {
        window = new JFrame("Maid Panel - " + user.getFirstName() + " " + user.getLastName());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(1200, 600);
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
        tabPanel.addTab("Sobe za spremanje", page1);
        window.add(tabPanel, "grow, push, wrap");
    }
    
    private void populateRoomTable(HotelManager hotelManager) {
        String[] columnNames = {"Broj sobe", "Tip","Status"};
        roomTableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(roomTableModel);
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
        
        JPanel tablesAndFormPanel = new JPanel(new BorderLayout());
        tablesAndFormPanel.add(buttonAndTablesPanel, BorderLayout.CENTER);

        JPanel page2 = (JPanel) tabPanel.getComponentAt(0);
        page2.setLayout(new BorderLayout());
        page2.add(tablesAndFormPanel, BorderLayout.CENTER);
        JButton clean = new JButton("Očišćeno");
		clean.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				return;
			}
			int roomNumber = (int) roomTableModel.getValueAt(selectedRow, 0);
			Room room = hotelManager.getRoomManager().getRoomList().get(roomNumber);
			room.setRoomStatus(RoomStatus.VACANT);
			int count = hotelManager.getMaidManager().getMaidList().get(user.getId()).getCount() + 1;
			hotelManager.getMaidManager().getMaidList().get(user.getId()).setCount(count);
			
			hotelManager.getMaidManager().saveData();
			hotelManager.getRoomManager().saveData();
			roomTableModel.removeRow(selectedRow);
			
		});
        page2.add(clean, BorderLayout.SOUTH);
    }
    
    private void addRowToRoomTable(Room room) {
		hotelManager.getRoomManager().loadRTData();
		hotelManager.getRoomManager().loadData();
		Object[] rowData = {
			room.getRoomNumber(),
			room.getRoomType().getServiceName(),
			room.getRoomStatus().toString()
        };
        roomTableModel.addRow(rowData);
	}
	
	private void populateRoomData(HotelManager hotelManager) {
		for (Room room : hotelManager.getRoomManager().getRoomList().values()) {
			if (room.getRoomStatus().equals(RoomStatus.CLEANING) && hotelManager.getMaidManager().getMaidList().get(user.getId()).getRoomListMaid().contains(room)) {
				addRowToRoomTable(room);
        }}
	
	}
}
