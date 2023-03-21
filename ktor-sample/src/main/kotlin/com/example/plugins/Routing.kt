package com.example.plugins

import com.example.user.User
import com.example.user.UserService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.reflect.*
import org.kodein.di.DI
import org.kodein.di.instance

object Users {
    class Controller(override val di: DI) : KodeinController() {
        private val userService: UserService by di.instance()

        override fun Routing.registerRoutes() {
            resource<Routes.Users> {
                post {
                    val user = call.receive<User>()
                    val id = userService.create(user)
                    call.respond(HttpStatusCode.Created, id)
                }
                put {
                    val request = call.receive<User>()
                    val currentUser = userService.read(request.id)
                    val updatedUser = currentUser?.update(request.name, request.age) ?: throw NoSuchElementException("No User")
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
    }

    object Routes {
        @Resource("/users")
        object Users

        @Resource("/users/{id}")
        object User
    }
}
