package mappingusers


import org.mappingusers.GUser
import org.mappingusers.GetUsersWithinDistanceService
import org.mappingusers.GetUsersInCityService

class UsersController {

    GetUsersInCityService getUsersInCityService
    GetUsersWithinDistanceService getUsersWithinDistanceService

    def index() {
        List<GUser> user = getUsersInCityService.user()
        respond([user: user])

        List<GUser> all = getUsersWithinDistanceService.allUsers()
        respond([all: all])
    }
}
