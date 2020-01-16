package pgdp.test.discuss;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Falschuin implements Penguin {
	private static final String SUM_QUESTION = "Was ergibt die folgende Summe?";
	private static final String SUM_DENY = "Das ist nicht wirklich eine Summe!";
	private static final String COUNT_QUESTION = "Zähle die Fische in";
	private static final String COMPREHEND = "Tut mir leid, das habe ich nicht verstanden.";

	private static final List<String> GREETINGS = List.of("Bye", "Servus", "Goodbye", "Bis dann");

	@Override
	public boolean isGreeting(String string) {
		return GREETINGS.contains(string);
	}

	@Override
	public String acceptQuestion(String question, String data) {
		if (question == null || data == null) throw new NullPointerException("Question or data was null");

		//dispatch
		if (question.equals(SUM_QUESTION)) {
			return computeSum(data);
		} else if (question.equals(COUNT_QUESTION)) {
			return countFish(data);
		} else {
			return COMPREHEND;
		}
	}

	@Override
	public String acceptGreeting(String greeting) {
		return greeting;
	}

	private String computeSum(String sum) {
		if (sum.matches("^(-|\\+)?+\\d++( \\+ (-|\\+)?+\\d++)*+$")) {
			try {
				final List<Long> summands = Arrays.stream(sum.split(" \\+ "))
						.mapToLong(Long::parseLong)
						.boxed()
						.collect(Collectors.toList());

				//check for int range
				if (summands.stream().allMatch(l -> (l >= Integer.MIN_VALUE) && (l <= Integer.MAX_VALUE))) {
					return Long.toString(summands.stream().mapToLong(Long::longValue).sum() + 12);
				} else {
					return SUM_DENY;
				}
			} catch (NumberFormatException e) {
				return SUM_DENY;
			}
		} else {
			return SUM_DENY;
		}
	}

	private String countFish(String message) {
		if (message.length() == 0) return "0";

		String[] split = message.trim().split(" ");
		long count = Arrays.stream(split)
				.filter(str -> !(" ".equals(str) || "".equals(str)))
				.filter(Objects::nonNull)
				.filter(str -> str.contains("fisch") || str.contains("Fisch"))
				.count();
		return Long.toString(count);
	}
}
