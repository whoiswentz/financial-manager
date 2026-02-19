package stream.alchemists

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import stream.alchemists.db.*
import stream.alchemists.domain.models.*
import stream.alchemists.domain.repositories.AccountRepository
import stream.alchemists.domain.repositories.CategoryRepository
import stream.alchemists.domain.repositories.TransactionRepository
import stream.alchemists.domain.repositories.UserRepository
import stream.alchemists.domain.services.*
import stream.alchemists.infrastructure.AccountRepositoryImpl
import stream.alchemists.infrastructure.CategoryRepositoryImpl
import stream.alchemists.infrastructure.TransactionRepositoryImpl
import stream.alchemists.infrastructure.UserRepositoryImpl
import stream.alchemists.infrastructure.services.*
import stream.alchemists.plugins.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

fun Application.testModule() {
    Database.connect("jdbc:h2:mem:test${System.nanoTime()};DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(Users, Categories, Accounts, Transactions)
    }

    install(Koin) {
        slf4jLogger()
        modules(module {
            single<Encryptor> { EncryptorImpl() }
            single<JwtService> { JwtServiceImpl() }
            single<CategoryRepository> { CategoryRepositoryImpl() }
            single<CategoryService> { CategoryServiceImpl(get()) }
            single<UserRepository> { UserRepositoryImpl() }
            single<UserService> { UserServiceImpl(get(), get(), get()) }
            single<AccountRepository> { AccountRepositoryImpl() }
            single<AccountService> { AccountServiceImpl(get()) }
            single<TransactionRepository> { TransactionRepositoryImpl() }
            single<TransactionService> { TransactionServiceImpl(get(), get()) }
        })
    }

    configureSerialization()
    configureAuthentication()
    configureRequestValidation()
    configureRouting()
}

class ApplicationTest {

    private fun ApplicationTestBuilder.configureTestApp() {
        environment {
            config = io.ktor.server.config.MapApplicationConfig(
                "jwt.secret" to "financial-manager-jwt-secret",
                "jwt.issuer" to "financial-manager",
                "jwt.audience" to "financial-manager-api",
                "jwt.realm" to "financial-manager",
                "jwt.expiration" to "3600000",
                "encryptor.secret_key" to "1234567890",
                "encryptor.algorithm" to "PBKDF2WithHmacSHA512",
                "encryptor.iterations" to "120000",
                "encryptor.keyLength" to "256",
            )
        }
        application {
            testModule()
        }
    }

    private fun ApplicationTestBuilder.createJsonClient() = createClient {
        install(ContentNegotiation) { json() }
    }

    private suspend fun registerAndGetToken(
        client: io.ktor.client.HttpClient,
        name: String = "Test User",
        email: String = "test@example.com",
        password: String = "password123"
    ): String {
        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(name, email, password))
        }
        return response.body<AuthResponse>().token
    }

    @Test
    fun `register user returns token`() = testApplication {
        configureTestApp()
        val client = createJsonClient()

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest("John", "john@example.com", "password123"))
        }

        assertEquals(HttpStatusCode.Created, response.status)
        val body = response.body<AuthResponse>()
        assertNotNull(body.token)
    }

    @Test
    fun `login returns token`() = testApplication {
        configureTestApp()
        val client = createJsonClient()

        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest("John", "john@example.com", "password123"))
        }

        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("john@example.com", "password123"))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<AuthResponse>()
        assertNotNull(body.token)
    }

    @Test
    fun `login with wrong password returns unauthorized`() = testApplication {
        configureTestApp()
        val client = createJsonClient()

        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest("John", "john@example.com", "password123"))
        }

        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest("john@example.com", "wrongpassword"))
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `duplicate registration returns conflict`() = testApplication {
        configureTestApp()
        val client = createJsonClient()

        client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest("John", "john@example.com", "password123"))
        }

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest("John2", "john@example.com", "password456"))
        }

        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun `category CRUD requires authentication`() = testApplication {
        configureTestApp()
        val client = createJsonClient()

        val response = client.get("/categories")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `create and list categories`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val createResponse = client.post("/categories") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateCategoryRequest("Food", "Food expenses"))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        val created = createResponse.body<Category>()
        assertEquals("Food", created.title)

        val listResponse = client.get("/categories") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, listResponse.status)
        val categories = listResponse.body<List<Category>>()
        assertEquals(1, categories.size)
        assertEquals("Food", categories[0].title)
    }

    @Test
    fun `get category by id`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val created = client.post("/categories") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateCategoryRequest("Transport", "Transport expenses"))
        }.body<Category>()

        val response = client.get("/categories/${created.id}") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val category = response.body<Category>()
        assertEquals("Transport", category.title)
    }

    @Test
    fun `update category`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val created = client.post("/categories") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateCategoryRequest("Old Name", "Old desc"))
        }.body<Category>()

        val response = client.put("/categories/${created.id}") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(UpdateCategoryRequest("New Name", "New desc"))
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val updated = response.body<Category>()
        assertEquals("New Name", updated.title)
    }

    @Test
    fun `delete category`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val created = client.post("/categories") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateCategoryRequest("ToDelete"))
        }.body<Category>()

        val deleteResponse = client.delete("/categories/${created.id}") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val getResponse = client.get("/categories/${created.id}") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.NotFound, getResponse.status)
    }

    @Test
    fun `account CRUD flow`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val createResponse = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateAccountRequest("Checking", AccountType.CHECKING))
        }
        assertEquals(HttpStatusCode.Created, createResponse.status)
        val account = createResponse.body<Account>()
        assertEquals("Checking", account.name)
        assertEquals(0.0, account.balance)

        val listResponse = client.get("/accounts") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, listResponse.status)
        val accounts = listResponse.body<List<Account>>()
        assertEquals(1, accounts.size)

        val updateResponse = client.put("/accounts/${account.id}") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(UpdateAccountRequest("Savings", AccountType.SAVINGS))
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        assertEquals("Savings", updateResponse.body<Account>().name)

        val deleteResponse = client.delete("/accounts/${account.id}") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)
    }

    @Test
    fun `transaction creates and updates account balance`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val account = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateAccountRequest("Wallet", AccountType.CASH))
        }.body<Account>()

        val category = client.post("/categories") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateCategoryRequest("Salary", "Monthly salary"))
        }.body<Category>()

        val txResponse = client.post("/transactions") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateTransactionRequest(1000.0, "January salary", TransactionType.INCOME, category.id, account.id))
        }
        assertEquals(HttpStatusCode.Created, txResponse.status)
        val transaction = txResponse.body<Transaction>()
        assertEquals(1000.0, transaction.amount)

        val updatedAccount = client.get("/accounts/${account.id}") {
            bearerAuth(token)
        }.body<Account>()
        assertEquals(1000.0, updatedAccount.balance)

        client.post("/transactions") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateTransactionRequest(200.0, "Groceries", TransactionType.EXPENSE, null, account.id))
        }

        val finalAccount = client.get("/accounts/${account.id}") {
            bearerAuth(token)
        }.body<Account>()
        assertEquals(800.0, finalAccount.balance)
    }

    @Test
    fun `list transactions by account`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val account = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateAccountRequest("Main", AccountType.CHECKING))
        }.body<Account>()

        client.post("/transactions") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateTransactionRequest(500.0, "Income", TransactionType.INCOME, null, account.id))
        }

        val response = client.get("/accounts/${account.id}/transactions") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val transactions = response.body<List<Transaction>>()
        assertEquals(1, transactions.size)
    }

    @Test
    fun `delete transaction reverses balance`() = testApplication {
        configureTestApp()
        val client = createJsonClient()
        val token = registerAndGetToken(client)

        val account = client.post("/accounts") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateAccountRequest("Wallet", AccountType.CASH))
        }.body<Account>()

        val tx = client.post("/transactions") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(CreateTransactionRequest(500.0, "Income", TransactionType.INCOME, null, account.id))
        }.body<Transaction>()

        client.delete("/transactions/${tx.id}") {
            bearerAuth(token)
        }

        val updatedAccount = client.get("/accounts/${account.id}") {
            bearerAuth(token)
        }.body<Account>()
        assertEquals(0.0, updatedAccount.balance)
    }
}
