package edu.cmu.lti.deiis.hw5.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
//import java.util.Map;
import java.util.Set;

public class SetUtil {

	public static boolean calculauteIntersection(Set<String> wordList1,
			Set<String> wordList2) {
		Set<String> allWords = populateWordset(wordList1, wordList2);

		// int M11 = 0;

		Iterator<String> wordList = allWords.iterator();
		while (wordList.hasNext()) {
			String word = wordList.next();
			boolean freq1 = wordList1.contains(word);
			boolean freq2 = wordList2.contains(word);

			if (freq1 && freq2) {
				return true;
			}
		}

		return false;
	}

	public static Set<String> checkSet(Set<String> set) {

		if (set == null)
			set = new HashSet<String>();

		return set;
	}

	static Set<String> populateWordset(Set<String> wordList1,
			Set<String> wordList2) {
		Set<String> allWords = new HashSet<String>();

		Set<String> wordIterator = null;
		Iterator<String> iterator = null;

		// wordIterator = wordList1.keySet();
		iterator = wordList1.iterator();

		while (iterator.hasNext()) {

			allWords.add(iterator.next());

		}
		// wordIterator = wordList2.keySet();
		iterator = wordList2.iterator();
		while (iterator.hasNext()) {

			allWords.add(iterator.next());

		}

		return allWords;
	}

	public static Set<String> addStringArray(Set<String> set, String[] array) {
		set = checkSet(set);

		for (String string : array) {
			set.add(string);

		}

		return set;

	}

	public static Set<String> addStringList(Set<String> set, List<String> array) {
		set = checkSet(set);
		for (String string : array) {
			set.add(string);

		}

		return set;
	}

	public static List<String> setToList(Set<String> set)// List<String> array)
	{
		ArrayList<String> list = new ArrayList<String>();
		set = checkSet(set);
		for (String string : set) {
			list.add(string);

		}

		return list;
	}








}



