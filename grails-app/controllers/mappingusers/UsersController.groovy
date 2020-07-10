package mappingusers

import org.mappingusers.UserdataService
import org.userdata.User

class UsersController {

    UserdataService userdataService

    def index() {
        User user = userdataService.user()
        System.out.println([user: user])
        [user: user]
    }
}
