package org.loopy.armeria

import org.jdbi.v3.sqlobject.SqlObject
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface UserDao : SqlObject {

    //language=MySQL
    @SqlQuery("SELECT id, firstName, lastName FROM user")
    fun list(): List<User>

}