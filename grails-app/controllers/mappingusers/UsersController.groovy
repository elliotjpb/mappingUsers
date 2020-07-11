package mappingusers


import org.mappingusers.GUser
import org.mappingusers.MappingUsersService

class UsersController {

    MappingUsersService mappingUsersService

    def index() {
        List<GUser> user = mappingUsersService.user()
        respond([user: user])
        //System.out.println(user.getFirstName() + " " + user.getLastName() + " " + user.getEmail())
        //[user: user]
    }
}
