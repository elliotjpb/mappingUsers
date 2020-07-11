package org.mappingusers

import grails.gorm.transactions.Transactional
import org.apache.commons.collections.ListUtils

@Transactional
class GetAllUsersWithinDistanceAndInCityService {

    GetUsersInCityService getUsersInCityService
    GetUsersWithinDistanceService getUsersWithinDistanceService

    List<GUser> getInCityAndWithinDistance() {
        List<GUser> usersInCity = getUsersInCityService.user()
        List<GUser> usersWithinDistance = getUsersWithinDistanceService.allUsers()
        final List<GUser> mergedLists = new ListUtils().union(usersInCity, usersWithinDistance)
        return mergedLists
    }
}
