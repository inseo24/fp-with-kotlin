import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.some

class Example8 {
    fun example() {
        val some: Some<String> = Some("I am wrapped in something")
        val none: None = None

        val optionA: Option<String> = "I am wrapped in something".some()
        val optionB: Option<String> = none

//  some shouldBe optionA
//  none shouldBe optionB

        val some2: Option<String> = Option.fromNullable("Nullable string")
        val none2: Option<String> = Option.fromNullable(null)

//  "Nullable string".toOption() shouldBe some2
//  null.toOption<String>() shouldBe none2

//  Some("Found value").getOrNull() shouldBe "Found value"
//  None.getOrNull() shouldBe null

//  Some("Found value").getOrElse { "No value" } shouldBe "Found value"
//  None.getOrElse { "No value" } shouldBe "No value"

//  when (val value = 20.some()) {
//    is Some -> value.value shouldBe 20
//    None -> fail("$value should not be None")
//  }

//  when (val value = none<Int>()) {
//    is Some -> fail("$value should not be Some")
//    None -> value shouldBe None
//  }

//  Some("Hello, Arrow!").map(String::length) shouldBe Some(14)
//  None.map(String::length) shouldBe None
//
//  Some(10).fold({ "Value is None" }, { "Value is ${it * 2}" }) shouldBe "Value is 20"
//
//  Some("Hello, Arrow!").flatMap { it.toOption() } shouldBe Some("Hello, Arrow!")
//  None.flatMap { it.toOption() } shouldBe None
//
//  Some(10).isSome { it > 5 } shouldBe true
//  Some(10).isSome { it < 5 } shouldBe false
//  None.isSome { it > 5 } shouldBe false
    }
}
