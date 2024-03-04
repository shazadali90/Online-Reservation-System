package Oasis.OnlineReservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class OnlineReservationSystem {
	private static final int Min = 1000000000;
	private static final int Max = 999999999;

	public static class User {
		private String username;
		private String password;

		Scanner sc = new Scanner(System.in);

		public User() {

		}

		public String getUsername() {
			System.out.println("Enter Your User_Name : ");
			username = sc.nextLine();
			return username;
		}

		public String getPassword() {
			System.out.println("Enter Your Password : ");
			password = sc.nextLine();
			return password;
		}
	}

	public static class PnrRecord {
		private int pnrNumber;
		private String passengerName;
		private String trainNumber;
		private String classType;
		private String journeyDate;
		private String from;
		private String to;

		Scanner sc = new Scanner(System.in);

		public int getpnrNumber() {
			Random r = new Random();
			pnrNumber = r.nextInt(Max) + Min;
			return pnrNumber;
		}

		public String getPassengerName() {
			System.out.println("Enter the Passenger Name : ");
			passengerName = sc.nextLine();
			return passengerName;
		}

		public String gettrainNumber() {
			System.out.println("Enter the Train Number : ");
			trainNumber = sc.nextLine();
			return trainNumber;
		}

		public String getclassType() {
			System.out.println("Enter the Class Type : ");
			classType = sc.nextLine();
			return classType;
		}

		public String getjourneyDate() {
			System.out.println("Enter the Journey Date as 'YYYY-MM-DD' format");
			journeyDate = sc.nextLine();
			return journeyDate;
		}

		public String getfrom() {
			System.out.println("Enter the Starting Place : ");
			from = sc.nextLine();
			return from;
		}

		public String getto() {
			System.out.println("Enter the Destination Place :  ");
			to = sc.nextLine();
			return to;
		}
	}

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		User u1 = new User();
		String username = u1.getUsername();
		String password = u1.getPassword();

		String str = "jdbc:mysql://localhost:3306/online_reservation_system";
		try {
			Class.forName("com.mysql.jdbc.Driver");

			try (Connection connection = DriverManager.getConnection(str, username, password)) {
				System.out.println("User Connection Granted. ");
				while (true) {
					String InsertQuery = "Insert into reservations values (?, ?, ?, ?, ?, ?, ?)";
					String DeleteQuery = "Delete from reservations where pnr_number = ?";
					String ShowQuery = "Select * from reservations";

					System.out.println("Enter The Choice : ");
					System.out.println("------------------------");
					System.out.println("1. Insert Record. ");
					System.out.println("2. Delete Record. ");
					System.out.println("3. Show All Records. ");
					System.out.println("4. Exit.");
					int choice = sc.nextInt();

					if (choice == 1) {

						PnrRecord p1 = new PnrRecord();
						int pnr_number = p1.getpnrNumber();
						String passengerName = p1.getPassengerName();
						String trainNumber = p1.gettrainNumber();
						String classType = p1.getclassType();
						String journeyDate = p1.getjourneyDate();
						String getfrom = p1.getfrom();
						String getto = p1.getto();

						try (PreparedStatement preparedStatement = connection.prepareStatement(InsertQuery)) {
							preparedStatement.setInt(1, pnr_number);
							preparedStatement.setString(2, passengerName);
							preparedStatement.setString(3, trainNumber);
							preparedStatement.setString(4, classType);
							preparedStatement.setString(5, journeyDate);
							preparedStatement.setString(6, getfrom);
							preparedStatement.setString(7, getto);

							int rowsAffected = preparedStatement.executeUpdate();
							if (rowsAffected > 0) {
								System.out.println("Your Ticket is Booked Successfully.");
							}

							else {
								System.out.println("No records were added.");
							}
						}

						catch (SQLException e) {
							System.err.println("SQLException: " + e.getMessage());
						}

					}

					else if (choice == 2) {
						System.out.println("Enter the PNR Number to Delete : ");
						int pnrNumber = sc.nextInt();
						try (PreparedStatement preparedStatement = connection.prepareStatement(DeleteQuery)) {
							preparedStatement.setInt(1, pnrNumber);
							int rowsAffected = preparedStatement.executeUpdate();

							if (rowsAffected > 0) {
								System.out.println("-----------------------------");
								System.out.println("Record Deleted Successfully.");
							}

							else {
								System.out.println("No Records were Deleted.");
							}
						}

						catch (SQLException e) {
							System.err.println("SQLException: " + e.getMessage());
						}
					}

					else if (choice == 3) {
						try (PreparedStatement preparedStatement = connection.prepareStatement(ShowQuery);
								ResultSet resultSet = preparedStatement.executeQuery()) {
							System.out.println("Your All records printing");
							System.out.println("----------------------------");
							while (resultSet.next()) {
								String pnrNumber = resultSet.getString("pnr_number");
								String passengerName = resultSet.getString("passenger_name");
								String trainNumber = resultSet.getString("train_number");
								String classType = resultSet.getString("class_type");
								String journeyDate = resultSet.getString("journey_date");
								String fromLocation = resultSet.getString("from_location");
								String toLocation = resultSet.getString("to_location");

								System.out.println("PNR Number: " + pnrNumber);
								System.out.println("Passenger Name: " + passengerName);
								System.out.println("Train Number: " + trainNumber);
								System.out.println("Class Type: " + classType);
								System.out.println("Journey Date: " + journeyDate);
								System.out.println("From Location: " + fromLocation);
								System.out.println("To Location: " + toLocation);
								System.out.println("-----------------------");
							}
						} catch (SQLException e) {
							System.err.println("SQLException: " + e.getMessage());
						}
					}

					else if (choice == 4) {
						System.out.println("Exiting the program. ");
						break;
					}

					else {
						System.out.println("Invalid Choice Entered. ");
					}
				}

			}

			catch (SQLException e) {
				System.err.println("SQLException: " + e.getMessage());
			}
		}

		catch (ClassNotFoundException e) {
			System.err.println("Error loading JDBC driver: " + e.getMessage());
		}

		sc.close();
	}

}
