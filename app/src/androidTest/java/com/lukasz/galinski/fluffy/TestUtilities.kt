package com.lukasz.galinski.fluffy

import com.lukasz.galinski.fluffy.model.UserModel

object TestUtilities {
    fun createTestUsers(testUsersLastIndex: Int): ArrayList<UserModel>{
        val usersList = arrayListOf<UserModel>()
        for (i in 0..testUsersLastIndex){
            usersList.add(UserModel("George", "emailExample$i@test.com", "test", "0000") )
        }
        return usersList
    }
}