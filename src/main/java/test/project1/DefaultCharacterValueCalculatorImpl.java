package test.project1;

public class DefaultCharacterValueCalculatorImpl implements CharacterValueCalculator {

	private static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	@Override
	public int calculate(String word) {
		int count = 0;
		for (char letter: word.toCharArray()) {			
			if (Character.isDigit(letter)) {
				count += ((int)letter);
				continue;
			}
			
			for (int j=0; j < alphabet.length; j++) {
				if (alphabet[j] == letter) {
					int letterValue = j + 1;
					count += letterValue;
					break;
				}
			}
		}
		return count;
	}
}
