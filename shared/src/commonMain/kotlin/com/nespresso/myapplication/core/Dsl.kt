package com.nespresso.myapplication.core

abstract class Element(
    private val name: String,
    private val attributes: Arg.Dictionary = Arg.Dictionary(mapOf())
) {
    private val children = arrayListOf<Element>()

    protected fun <E : Element> initElement(element: E, init: E.() -> Unit): E {
        element.init()
        children.add(element)
        return element
    }

    protected open fun nameOfElement(builder: StringBuilder, indent: String) {
        builder.append("$indent$name ${renderAttributes()}")
    }

    protected open fun render(builder: StringBuilder, indent: String) {
        nameOfElement(builder, indent)
        if (children.isNotEmpty()) {
            builder.append(" { \n")
            for (c in children) {
                c.render(builder, "$indent ")
            }
            builder.append("$indent}")
        }
        builder.append("\n")
    }

    protected open fun renderAttributes(): String {
        val builder = StringBuilder()
        if (attributes.value.isNotEmpty()) {
            builder.append(
                "( ${
                    attributes.value.map { "${it.key}: ${it.value}" }.joinToString(", ")
                } )"
            )
        }
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

sealed class OperationType(val name: String) {
    object Query : OperationType("query")
    object Mutation : OperationType("mutation")
}

class Operation(
    name: String,
    attributes: Arg.Dictionary = Arg.Dictionary(mapOf())
) : QueryField(name, attributes)

abstract class QueryField(
    name: String,
    attributes: Arg.Dictionary = Arg.Dictionary(mapOf())
) : Element(name, attributes) {

    fun field(
        name: String,
        attributes: Arg.Dictionary = Arg.Dictionary(mapOf()),
        init: Field.() -> Unit = {}
    ): Field = initElement(Field(name, attributes), init)

    fun field(
        field: Field
    ): Field = initElement(field) {}

    fun inlineFragmentPartially(
        name: String,
        attributes: Arg.Dictionary = Arg.Dictionary(mapOf()),
        init: Fragment.() -> Unit
    ): Fragment = initElement(Fragment(name, attributes), init)

}

class Field(name: String, attributes: Arg.Dictionary = Arg.Dictionary(mapOf())) :
    QueryField(name, attributes)

class Fragment(name: String, attributes: Arg.Dictionary = Arg.Dictionary(mapOf())) :
    QueryField(name, attributes) {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent... on ")
        super.render(builder, indent)
    }
}

fun query(
    queryName: String,
    attributes: Arg.Dictionary = Arg.Dictionary(mapOf()),
    init: Operation.() -> Unit
): Operation {
    val query = Operation(queryName, attributes)
    query.init()
    return query
}

fun mutation(
    mutationName: String,
    attributes: Arg.Dictionary = Arg.Dictionary(mapOf()),
    init: Operation.() -> Unit = {}
): Operation {
    val mutation = Operation(mutationName, attributes)
    mutation.init()
    return mutation
}