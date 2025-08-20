package com.example.apicalling

data class City(
    val name:String,
    val country :String
){
    override fun toString(): String {
        return "$name,$country"
    }
}
