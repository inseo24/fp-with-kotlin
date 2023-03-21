package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.Instance
import org.kodein.di.bind
import org.kodein.di.bindings.NoArgBindingDI
import org.kodein.di.instance
import org.kodein.di.singleton
import org.kodein.type.jvmType

fun Application.kodeinApplication(
    kodeinMapper: DI.MainBuilder.(Application) -> Unit = {}
) {
    val application = this

    application.install(Resources)

    val kodein = DI {
        bind<Application>() with instance(application)
        kodeinMapper(this, application)
    }

    routing {
        for (bind in kodein.container.tree.bindings) {
            val bindClass = bind.key.type.jvmType as? Class<*>?
            if (bindClass != null && KodeinController::class.java.isAssignableFrom(bindClass)) {
                val res by kodein.Instance(bind.key.type)
                println("Registering '$res' routes...")
                (res as KodeinController).apply { registerRoutes() }
            }
        }
    }
}

abstract class KodeinController : DIAware {
    val application: Application by instance()
    abstract fun Routing.registerRoutes()
}

inline fun <reified T : Any> DI.MainBuilder.bindSingleton(crossinline instanceProvider: NoArgBindingDI<Any>.() -> T) {
    bind<T>() with singleton { instanceProvider.invoke(this) }
}
