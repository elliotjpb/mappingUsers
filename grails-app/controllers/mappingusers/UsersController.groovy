package mappingusers

import groovy.json.JsonBuilder
import org.mappingusers.User
import org.mappingusers.GetAllUsersWithinDistanceAndInCityService

class UsersController {

    GetAllUsersWithinDistanceAndInCityService getAllUsersWithinDistanceAndInCityService

    def index() {
        List<User> aggregateUsers = getAllUsersWithinDistanceAndInCityService.getInCityAndWithinDistance()
        System.out.println("aggregateUsers: " + new JsonBuilder( aggregateUsers ).toPrettyString())
        render(view: "index", model: [aggregateUsers: new JsonBuilder( aggregateUsers ).toPrettyString()])
    }
}
