package app.controller

class Errors {
    class Error(f: String, msg: String?) {
        var field: String = f
        var message: String? = msg
    }

    var errors: MutableList<Error> = mutableListOf()

    fun addError(f: String, msg: String?) {
        errors.add(Error(f, msg))
    }
}