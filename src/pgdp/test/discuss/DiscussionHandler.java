package pgdp.test.discuss;

import java.io.IOException;

/**
 * Version 1.2
 * 1.1: fixed pattern in sum for negative ints
 * 1.2: fixed regex with possessive quantifiers
 * -----------
 * @author Florian Schmidt (ge75vob)
 * fs.schmidt@tum.de
 */
public class DiscussionHandler {
	/**
	 * Starts the server with a new discussion.
	 *
	 * @param args true / false, specifies whether Echtuin (true) or Falschuin (false)
	 * @throws IOException when errors occur with socket initialization
	 */
	public static void main(String[] args) throws IOException {
		boolean real;

		if (args.length != 1) {
			real = true;
		} else {
			real = Boolean.parseBoolean(args[0]);
		}

		Discussion discussion = new Discussion(real);
		discussion.handleIncomingCommunications();
	}
}
