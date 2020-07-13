package org.mappingusers

import grails.gorm.transactions.Transactional
import org.apache.commons.collections.ListUtils

@Transactional
class GetAllUsersWithinDistanceAndInCityService {

    GetUsersInCityService getUsersInCityService
    GetUsersWithinDistanceService getUsersWithinDistanceService

    //Call each service and merge together
    List<User> getInCityAndWithinDistance() {
        List<User> usersInCity = getUsersInCityService.user()
        List<User> usersWithinDistance = getUsersWithinDistanceService.allUsers()

        final List<User> mergedLists = new ListUtils().union(usersInCity, usersWithinDistance)
        return mergedLists
    }
}
