package pgdp.test.discuss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Discussion {
	/**
	 * The penguin object tests are run on.
	 */
	private final Penguin PENGUIN;

	/**
	 * The port of the discussion according to Artemis.
	 */
	private static final int PORT = 25565;

	/**
	 * The server socket we use.
	 */
	private ServerSocket SERVER;

	/**
	 * Creates a new discussion object using an Echtuin.
	 * @throws IOException when errors occur opening the sockets.
	 */
	public Discussion() throws IOException {
		this(true);
	}

	/**
	 * Creates a new discussion object with the specified penguin type.
	 * @param real true -> Echtuin; false -> Falschuin
	 * @throws IOException when errors occur opening the sockets.
	 */
	public Discussion(boolean real) throws IOException {
		SERVER = new ServerSocket(PORT);

		if (real) {
			System.out.println("Initializing Echtuin...");
			PENGUIN = new Echtuin();
		} else {
			System.out.println("Initializing Falschuin...");
			PENGUIN = new Falschuin();
		}
	}

	/**
	 * Accepts the next incoming communication and leads the discussion.
	 * @return flag wether an exception was thrown, true iff regular run
	 */
	private boolean acceptCommunication() {
		System.out.println("Accepting next communication...");

		try (Socket socket = SERVER.accept();
			 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
			//talking loop
			System.out.println("Connected...");

			//1. question
			String question = reader.readLine();
			String data = reader.readLine();
			writer.println(PENGUIN.acceptQuestion(question, data));

			//questions loop
			while (true) {
				question = reader.readLine();

				//readLine() return null at end-of-stream, so the test has terminated
				if (question == null) return true;

				//is greeting? then terminate
				if (PENGUIN.isGreeting(question)) {
					System.out.println("Accepted greeting...");
					writer.println(PENGUIN.acceptGreeting(question));
					System.out.println("Sent answer...");

					return true;
				} else { //else read data for the question and handle that
					data = reader.readLine();
					System.out.println("Accepted question and data...");
					writer.println(PENGUIN.acceptQuestion(question, data));
					System.out.println("Sent answer...");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Handles incoming communications in an infinite loop.
	 * Call this once, then leave it running while testing your code.
	 */
	public void handleIncomingCommunications() {
		while (true) {
			boolean flag = acceptCommunication();
			if (!flag) return;
			System.out.println("Test communication terminated");
		}
	}
}
