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
            post { call.receive<User>().let { call.respond(HttpStatusCode.Created, userService.create(it)) } }
            put {
                call.receive<User>().let { request ->
                    userService.read(request.id)?.also { userService.update(it.id, it.update(request)) }
                        ?: throw NoSuchElementException("No User")
                }
                call.respond(HttpStatusCode.OK)
            }
        }

        resource<Routes.User> {
            get {
                call.parameters["id"]?.toIntOrNull()?.let {
                    userService.read(it)?.let { user -> call.respond(HttpStatusCode.OK, user) }
                        ?: call.respond(HttpStatusCode.NotFound)
                } ?: throw IllegalArgumentException("Invalid ID")
            }

            delete {
                call.parameters["id"]?.toIntOrNull()?.let {
                    userService.delete(it)
                    call.respond(HttpStatusCode.OK)
                } ?: throw IllegalArgumentException("Invalid ID")
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
