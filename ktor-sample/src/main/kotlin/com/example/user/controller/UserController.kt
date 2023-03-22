package com.example.user.controller

import com.example.plugins.KodeinController
import com.example.user.application.UserService
import com.example.user.domain.User
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.instance

class UserController(override val di: DI) : KodeinController() {
    override fun Routing.registerRoutes() {
        val userService: UserService by di.instance()

        resource<Routes.Users> {
            post {
                val user = call.receive<User>()
                val id = userService.create(user)
                call.respond(HttpStatusCode.Created, id)
            }
            put {
                val request = call.receive<User>()
                val currentUser = userService.read(request.id)
                val updatedUser =
                    currentUser?.update(request.name, request.age) ?: throw NoSuchElementException("No User")
                userService.update(currentUser.id, updatedUser)
                call.respond(HttpStatusCode.OK)
            }
        }

        resource<Routes.User> {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
                userService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }

    object Routes {
        @Resource("/users")
        object Users

        @Resource("/users/{id}")
        object User
    }

}
