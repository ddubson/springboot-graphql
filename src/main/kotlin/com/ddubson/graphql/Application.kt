package com.ddubson.graphql

import graphql.GraphQL
import graphql.Scalars.GraphQLLong
import graphql.Scalars.GraphQLString
import graphql.schema.GraphQLFieldDefinition.newFieldDefinition
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLObjectType.newObject
import graphql.schema.GraphQLSchema
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class Application {
    @PostMapping(value = "/graphql", consumes = arrayOf("text/plain"))
    fun executeQuery(@RequestBody query: String): ResponseEntity<*> {
        return ok().body(graphQL().execute(query).getData<Any>())
    }

    @Bean
    fun personType(): GraphQLObjectType {
        return newObject()
                .name("Person")
                .field(newFieldDefinition()
                        .name("id")
                        .type(GraphQLLong)
                        .dataFetcher({ env -> 1L}))
                .field(newFieldDefinition()
                        .type(GraphQLString)
                        .name("firstName")
                        .dataFetcher({ env -> "John"}))
                .field(newFieldDefinition()
                        .type(GraphQLString)
                        .name("lastName")
                        .dataFetcher({ env -> "Doe"}))
                .build()
    }

    @Bean
    fun graphQL(): GraphQL {
        val schema = GraphQLSchema.newSchema()
                .query(personType()).build()
        return GraphQL.newGraphQL(schema).build()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}