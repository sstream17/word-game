package com.stream_suite.wordgame.util

/**
 * Recursive binary search function that searches through a concatenated
 * five-lettered words of a string.
 *
 * @param wordList is a string of concatenated five-lettered words.
 *         i.e. wordList of "hello", "world" and "happy" would be "helloworldhappy"
 * @param word is word to search in [wordlist]
 * @param lo low index
 * @param hi high index
 * @param wordLength length of word. For this game it's 5
 */
fun wordlistBinarySearch(
    wordList: String,
    word: String,
    lo: Int = 0,
    hi: Int,
    wordLength: Int
): Boolean {
    if (lo > hi) return false
    val mid = ((hi - lo) / 2) + lo
    val slice = wordLength * mid

    val midWord = wordList.slice(slice until slice + wordLength)

    return when {
        word < midWord -> wordlistBinarySearch(wordList, word, lo, mid - 1, wordLength)
        word == midWord -> true
        else -> wordlistBinarySearch(wordList, word, mid + 1, hi, wordLength)
    }
}