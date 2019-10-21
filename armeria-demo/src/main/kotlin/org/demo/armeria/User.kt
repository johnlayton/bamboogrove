package org.demo.armeria

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class User(val id: Long, val firstName: String, val lastName: String)

/*
data class Thing(val id: Int, val name: String,
                 val nullable: String?,
                 val nullableDefaultedNull: String? = null,
                 val nullableDefaultedNotNull: String? = "not null",
                 val defaulted: String = "default value")
*/
