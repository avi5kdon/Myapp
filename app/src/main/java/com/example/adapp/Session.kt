package com.example.adapp

data class Session(
    val centers: List<Center>? = null
)

data class Center(
    val address: String? = null,
    val block_name: String? = null,
    val center_id: Int? = null,
    val district_name: String? = null,
    val fee_type: String? = null,
    val from: String? = null,
    val lat: Int? = null,
    val long: Int? = null,
    val name: String? = null,
    val pincode: Int? = null,
    val sessions: List<SessionX>? = null,
    val state_name: String? = null,
    val to: String? = null
)

data class SessionX(
    val available_capacity: Int? = null,
    val available_capacity_dose1: Int? = null,
    val available_capacity_dose2: Int? = null,
    val date: String? = null,
    val min_age_limit: Int? = null,
    val session_id: String? = null,
    val slots: List<String>? = null,
    val vaccine: String? = null
)