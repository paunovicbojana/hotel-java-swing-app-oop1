package view;

import java.awt.Font;

import javax.swing.*;

import entity.User;
import manage.HotelManager;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import entity.*;

public class LoginFrame extends JFrame {
	private HotelManager hotelManager;
	
	@Override
	public void dispose() {
		hotelManager.saveData();
		super.dispose();
	}

    public LoginFrame(HotelManager hotelManager) {
        this.hotelManager = hotelManager;
        LoginFrame loginFrame = this;
        setTitle("Prijava");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new MigLayout("wrap 2", "[][]", "[]5[]20[]10[]"));
        setResizable(false);
        ImageIcon icon = new ImageIcon("icon.png");
        setIconImage(icon.getImage());

        JLabel welcome = new JLabel("Dobrodošli u hotel! ");
        JLabel please = new JLabel("Molimo Vas da se ulogujete.");

        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD | Font.ITALIC, 14));
        please.setFont(please.getFont().deriveFont(Font.BOLD, 12));

        CC componentConstraints = new CC();
        componentConstraints.alignX("center").spanX();
        getContentPane().add(welcome, componentConstraints);
        getContentPane().add(please, componentConstraints);

        JTextField usernameField = new JTextField(20);
        JLabel usernameLabel = new JLabel("Korisničko ime: ");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setEchoChar('•');
        JLabel passwordLabel = new JLabel("Lozinka: ");
        JButton loginButton = new JButton("Prijavite se");
        loginButton.setFocusable(false);
        CC componentConstraints2 = new CC();
        componentConstraints2.alignX("right").spanX();

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
           

            User user = hotelManager.getLoginManager().login(username, password);
            if (user instanceof Administrator){
                loginFrame.dispose();
                new AdminFrame(user, hotelManager);
            } else if (user instanceof Receptionist) {
                loginFrame.dispose();
                new ReceptionistFrame(user, hotelManager);
            } else if (user instanceof Maid) {
                loginFrame.dispose();
                new MaidFrame(user, hotelManager);
            } else if (user instanceof Guest) {
            	loginFrame.dispose();
            	new GuestFrame(user, hotelManager);
            }
            else {
                JOptionPane.showMessageDialog(null, "Pogrešno korisničko ime ili lozinka. Pokušajte ponovo.");
            }
        });
        loginFrame.getRootPane().setDefaultButton(loginButton);
        getContentPane().add(usernameLabel);
        getContentPane().add(usernameField);
        getContentPane().add(passwordLabel);
        getContentPane().add(passwordField);
        getContentPane().add(loginButton, componentConstraints2);

        setVisible(true);
    }
}
