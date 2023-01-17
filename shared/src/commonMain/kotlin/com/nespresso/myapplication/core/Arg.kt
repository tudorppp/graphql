package com.nespresso.myapplication.core

sealed class Arg {
    data class Array(val value: List<Arg>) : Arg() {
        override fun toString(): kotlin.String {
            return "[${value.joinToString(",")}]"
        }
    }

    data class Int(val value: kotlin.Int) : Arg() {
        override fun toString(): kotlin.String {
            return "\"$value\""
        }
    }

    data class String(val value: kotlin.String) : Arg() {
        override fun toString(): kotlin.String {
            return "\"$value\""
        }
    }

    data class Bool(val value: Boolean) : Arg() {
        override fun toString(): kotlin.String {
            return "$value"
        }
    }

    data class Dictionary(val value: Map<kotlin.String, Arg>) : Arg() {
        override fun toString(): kotlin.String {
            return "{${value.map { "${it.key}:${it.value}" }.joinToString(", ")}}"
        }
    }
}

fun dictionaryOf(vararg pairs: Pair<String, Arg>) = Arg.Dictionary(mapOf(*pairs))