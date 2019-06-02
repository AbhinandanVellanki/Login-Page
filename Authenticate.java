import java.security.*;
import java.io.*;
import java.text.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author abhin This program creates a database to store user records
 */
public class Authenticate {
	private static ArrayList<User> Database = new ArrayList<User>();

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException exception if the algorithm isnt valid
	 * @throws IOException              exception if there is an error encountered
	 *                                  during I/O
	 * @throws ParseException           exception if there is an error during
	 *                                  parsing
	 */
	public static void main(String args[]) throws NoSuchAlgorithmException, IOException, ParseException {
		Authenticate obj = new Authenticate();
		obj.execute();
	}

	/**
	 * @param a user object that is passed to the function
	 * @return boolean if user is successfully authenticated
	 * @throws NoSuchAlgorithmException if the algorithm is not valid
	 */
	private boolean authenti(User a) throws NoSuchAlgorithmException {
		Scanner sc = new Scanner(System.in);
		int rc = 0;// stores number of incorrect attempts
		boolean auth = false;
		if (!a.isLocked()) {// found the user account
			String ad = a.getUsername();
			do {
				System.out.print("Please enter your password: ");
				String p = sc.next();
				String sb = a.hashthis(p);
				String pa = a.getHpass();
				if (pa.equals(sb)) {// successful
					String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
					auth = true;
					a.setLalgin(date);
					a.setFailli(0);
					System.out.println("Login success! Hello " + ad + "!");
					return true;
				} else {// failed attempt
					rc++;
					a.setFailli(rc);
					System.out.println("Login Failed! ");
					if (a.getFailli() <= 3) {
						System.out.print("Please enter your username: ");
						ad = sc.next();
						while (!ad.equals(a.getUsername())) {
							System.out.println("Wrong username entered, please try again!");
							System.out.print("Please enter your username: ");
							ad = sc.next();
						}
					}

				}
			} while (a.getFailli() <= 3);
			if (a.getFailli() >= 3) {
				System.out.println("Login Failed! Your account has been locked!");
				a.setLocked(true);
				return false;
			}
		} else if (a.isLocked()) {
			System.out.println("This account is locked");
			return false;
		}
		return false;
	}

	/**
	 * @param a user object that is to be modified
	 * @throws NoSuchAlgorithmException if the algorithm is not valid
	 */
	private void modifyuser(User a) throws NoSuchAlgorithmException {
		Scanner sc = new Scanner(System.in);
		String p1;
		String p2;
		String na;
		String e;
		System.out.print("Please enter your new password: ");
		p1 = sc.next();
		boolean sl = false;
		boolean cl = false;
		boolean d = false;
		do {
			for (int i = 0; i < p1.length(); i++) {
				char ch = p1.charAt(i);
				if (Character.isLowerCase(ch))
					sl = true;
				else if (Character.isUpperCase(ch))
					cl = true;
				else if (Character.isDigit(ch))
					d = true;
			}
			if (!cl || !sl || !d) {
				System.out.println(
						"Your new password has to fulfill: at least 1 small letter, 1 capital letter, 1 digit!");
				System.out.print("Please enter your new password: ");
				p1 = sc.next();
			}
		} while (!sl || !cl || !d);
		System.out.print("Please re-enter your new password: ");
		p2 = sc.next();
		if (!p1.equals(p2)) {
			System.out.println("The passwords you have entered do not match!");
			do {
				System.out.print("Please re-enter your new password: ");
				p2 = sc.next();
			} while (!p1.equals(p2));
		}
		String sb = a.hashthis(p1);
		System.out.print("Please enter your new full name: ");
		na = sc.next();
		System.out.print("Please enter your new email address: ");
		e = sc.next();
		a.setEadd(e);
		a.setHpass(p1);
		a.setName(na);
		System.out.println("Record update successfully!");
	}

	/**
	 * @param a       The user object that is to be added
	 * @param isAdmin if the user object to be added is the administrator
	 * @throws NoSuchAlgorithmException if the algorithm is not valid
	 */
	private void adduser(User a, boolean isAdmin) throws NoSuchAlgorithmException {
		Scanner sc = new Scanner(System.in);
		String u;
		String p1;
		String p2;
		String na;
		String e;
		Long ph;
		String keyword;
		if (!isAdmin) {
			System.out.print("Please enter your username: ");
			u = sc.next();
			keyword = "your";
		} else {
			u = "administartor";
			keyword = "the";
		}
		System.out.print("Please enter the password: ");
		p1 = sc.next();
		boolean sl = false;
		boolean cl = false;
		boolean d = false;
		do {
			for (int i = 0; i < p1.length(); i++) {
				char ch = p1.charAt(i);
				if (Character.isLowerCase(ch))
					sl = true;
				else if (Character.isUpperCase(ch))
					cl = true;
				else if (Character.isDigit(ch))
					d = true;
			}
			if (!cl || !sl || !d) {
				System.out.println("Your password has to fulfill: at least 1 small letter, 1 capital letter, 1 digit!");
				System.out.print("Please enter " + keyword + " password: ");
				p1 = sc.next();
			}
		} while (!sl || !cl || !d);
		System.out.print("Please re-enter " + keyword + " password: ");
		p2 = sc.next();
		if (!p1.equals(p2)) {
			System.out.println("The passwords you have entered do not match!");
			do {
				System.out.print("Please re-enter " + keyword + " password: ");
				p2 = sc.next();
			} while (!p1.equals(p2));
		}
		String sb = a.hashthis(p1);
		if (!isAdmin) {
			System.out.print("Please enter your full name: ");
			na = sc.next();
			System.out.print("Please enter your email address: ");
			e = sc.next();
			System.out.print("Please enter your phone number: ");
			ph = sc.nextLong();
			a = new User(u, sb, na, e, ph);
			Database.add(a);
			System.out.println("Record added successfully!");
		} else {
			User da = new User("administrator", p1, "admin@cs.hku.hk", "Administrator", 12345678);
			da.setHpass(da.hashthis(p1));
			Database.add(da);
			System.out.println("Administrator account created successfully!");
		}
	}

	/**
	 * @throws IOException              if there is an error during I/O
	 * @throws NoSuchAlgorithmException if the algorithm used is not valid
	 * @throws ParseException           if there is an error during parsing
	 */
	private void execute() throws IOException, NoSuchAlgorithmException, ParseException {
		File file = new File("./User.txt");
		try {
			JSONParser parser = new JSONParser();
			JSONObject o = (JSONObject) parser.parse(new FileReader(file));
			JSONArray jar = (JSONArray) o.get("user_array");
			Iterator<Object> iterator = jar.iterator();
			while (iterator.hasNext()) {
				Object obj = iterator.next();
				if (obj instanceof JSONObject) {
					String u;
					String p1;
					String na;
					String ea;
					Long ph;
					int w;
					String d;
					boolean l;
					JSONObject json = (JSONObject) obj;
					u = json.get("username").toString();
					p1 = json.get("hash_password").toString();
					na = json.get("Full Name").toString();
					ea = json.get("Email").toString();
					ph = Long.parseLong(json.get("Phone Number").toString());
					w = Integer.parseInt(json.get("Fail Count").toString());
					d = json.get("Last Login Date").toString();
					l = Boolean.parseBoolean(json.get("Account Locked").toString());
					User n = new User(u, p1, na, ea, ph, w, l, d);
					Database.add(n);
				}
			}
			start();
		} catch (FileNotFoundException e) {
			file.createNewFile();
			start();
		}
		PrintStream p = new PrintStream(file);
		JSONObject ob = new JSONObject();
		JSONArray ja = new JSONArray();
		for (int i = 0; i < Database.size(); i++) {
			User a = Database.get(i);
			JSONObject obj = new JSONObject();
			obj = CreateJSON(obj, a);
			ja.add(obj);
		}
		ob.put("user_array", ja);
		p.println(ob.toJSONString());
		p.close();
	}

	/**
	 * @param obj the JSON object to be created
	 * @param a   The user object that is to be used to create obj
	 * @return the JSON object after creation
	 */
	@SuppressWarnings("unchecked")
	private JSONObject CreateJSON(JSONObject obj, User a) {
		obj.put("username", a.getUsername());
		obj.put("hash_password", a.getHpass());
		obj.put("Full Name", a.getName());
		obj.put("Email", a.getEadd());
		obj.put("Phone Number", a.getPhno());
		obj.put("Fail Count", a.getFailli());
		obj.put("Last Login Date", a.getLalgin());
		obj.put("Account Locked", a.isLocked());
		return obj;
	}

	/**
	 * @param a the user object to be reset
	 * @throws NoSuchAlgorithmException if the algorithm used is not valid
	 */
	private void reset(User a) throws NoSuchAlgorithmException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Please enter the new password: ");
		String p1 = sc.next();
		boolean sl = false;
		boolean cl = false;
		boolean d = false;
		do {
			for (int i = 0; i < p1.length(); i++) {
				char ch = p1.charAt(i);
				if (Character.isLowerCase(ch))
					sl = true;
				else if (Character.isUpperCase(ch))
					cl = true;
				else if (Character.isDigit(ch))
					d = true;
			}
			if (!cl || !sl || !d) {
				System.out.println("Your password has to fulfill: at least 1 small letter, 1 capital letter, 1 digit!");
				System.out.print("Please enter the new password: ");
				p1 = sc.next();
			}
		} while (!sl || !cl || !d);
		System.out.print("Please re-enter the password: ");
		String p2 = sc.next();
		if (!p1.equals(p2)) {
			System.out.println("The passwords you have entered do not match!");
			do {
				System.out.print("Please re-enter the password: ");
				p2 = sc.next();
			} while (!p1.equals(p2));
		}
		String sb = a.hashthis(p1);
		a.setHpass(sb);
		a.setFailli(0);
		a.setLocked(false);
		System.out.println("Password update successfully!");
	}

	/**
	 * @throws NoSuchAlgorithmException if the algorithm entered is not valid
	 */
	private void start() throws NoSuchAlgorithmException {
		boolean e = false;
		Scanner sc = new Scanner(System.in);
		System.out.print(
				"Welcome to the COMP2396 Authentication System! \n1. Authenticate User \n2. Add User Record \n3. Edit user record \n4. Reset user password \nWhat would you like to perform? \nPlease enter your command (1-4, or 0 to terminate the system) : ");
		int n;
		do {
			n = sc.nextInt();
			switch (n) {
			case 1: {
				if (Database.size() == 0) {
					System.out.println("No user accounts exist");
					break;
				} else {
					System.out.print("Please enter your username: ");
					String u = sc.next();
					User a = Finduser(u);
					if (a.getUsername().equals("notfound")) {
						System.out.println("Wrong Username!");
						break;
					}
					authenti(a);
				}
				break;
			}
			case 2: {
				User a = new User("", "", "", "", 0);
				adduser(a, false);
				break;
			}
			case 3: {
				if (Database.size() == 0) {
					System.out.println("No user accounts exist");
					break;
				} else {
					System.out.print("Please enter your username: ");
					String u = sc.next();
					User a = Finduser(u);
					if (a.getUsername().equals("notfound")) {
						System.out.println("Wrong Username!");
						break;
					}
					if (authenti(a))
						modifyuser(a);
				}
				break;
			}
			case 4: {
				if (Database.size() == 0) {
					System.out.println("No user accounts exist");
					break;
				}
				User a = Finduser("administrator");
				if (a.getUsername().equals("notfound")) {
					System.out.println(
							"Administrator account not exist, please create the administrator account by setting up a password for it");
					User c = new User("administrator", "", "", "", 0);
					adduser(c, true);
					break;
				}
				System.out.print("Please enter the password of the administrator: ");
				String p = a.hashthis(sc.next());
				if (p.equals(a.getHpass())) {
					System.out.print("Please enter the user account need to reset: ");
					String u = sc.next();
					User b = Finduser(u);
					reset(b);
					break;
				}
				break;
			}
			case 0:
				e = true;
				break;

			default: {
				System.out.println("Wrong Choice entered");
				break;
			}
			}
			if (!e)
				System.out.print("Please enter your command (1-4, or 0 to terminate the system) : ");
		} while (n != 0);

	}

	/**
	 * @param u the username of the user to be found
	 * @return the user if found or a dummy user if not found
	 */
	private User Finduser(String u) {
		for (int i = 0; i < Database.size(); i++) {
			User a = Database.get(i);
			if (a.getUsername().equals(u)) {
				return a;
			}
		}
		User c = new User("notfound", "", "", "", 0);
		return c;
	}
}
