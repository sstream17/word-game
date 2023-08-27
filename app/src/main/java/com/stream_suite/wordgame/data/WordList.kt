package com.stream_suite.wordgame.data


const val WORD_LENGTH = 5

/**
 * Holds all valid words available to be guessed.
 *
 * Each word is sorted in alphabetical order and concatenated in this single string.
 */
val dictionary = AppResourceProvider.getDictionary()
val dictionarySize = dictionary.length / WORD_LENGTH

/**
 * A subset of [dictionary] which serves as the pool of words that can be selected as an answer.
 */
val possibleAnswers = AppResourceProvider.getPossibleAnswers()
val possibleAnswersSize = possibleAnswers.length / WORD_LENGTH