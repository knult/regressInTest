import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class MyResponse(
    val body: String,
    val status_code: Int,
    val elapsed: Long
)


class Requests {

    private val url = "https://reqres.in/api/"

    private fun sendRequest(request: HttpRequest): MyResponse {
        val client = HttpClient.newBuilder().build()
        val startTime = System.currentTimeMillis()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        val elapsedTime = System.currentTimeMillis() - startTime
        return MyResponse(response.body(), response.statusCode(), elapsedTime)
    }

    fun register(email: String? = null, password: String? = null): MyResponse {
        val values: MutableMap<String, String> = mutableMapOf()
        if (email != null) {
            values["email"] = email
        }
        if (password != null) {
            values["password"] = password
        }
        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(values)
        val request = HttpRequest.newBuilder().uri(URI.create("${url}register"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .header("Content-Type", "application/json").build()
        return sendRequest(request)
    }

    fun patchUser(id: Int, data: Map<String, Any>): MyResponse {
        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(data)
        val request = HttpRequest.newBuilder().uri(URI.create("${url}users/$id"))
            .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
            .header("Content-Type", "application/json").build()
        return sendRequest(request)
    }

    fun putUser(id: Int, data: Map<String, Any>): MyResponse {
        val objectMapper = ObjectMapper()
        val requestBody: String = objectMapper.writeValueAsString(data)
        val request = HttpRequest.newBuilder().uri(URI.create("${url}users/$id"))
            .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
            .header("Content-Type", "application/json").build()
        return sendRequest(request)
    }

    fun delUser(id: Int): MyResponse {
        val request = HttpRequest.newBuilder().uri(URI.create("${url}users/$id")).DELETE().build()
        return sendRequest(request)
    }

    fun singleUser(id: Int): MyResponse {
        val request = HttpRequest.newBuilder().uri(URI.create("${url}users/${id}")).build()
        return sendRequest(request)
    }

    fun listUsers(page: Int? = null): MyResponse {
        val uri: URI = if (page === null) {
            URI.create("${url}users")
        } else {
            URI.create("${url}users?page=${page}")
        }
        val request = HttpRequest.newBuilder().uri(uri).build()
        return sendRequest(request)
    }

    fun listResource(page: Int? = null): MyResponse {
        val uri: URI = if (page === null) {
            URI.create("${url}unknown")
        } else {
            URI.create("${url}unknown?page=${page}")
        }
        val request = HttpRequest.newBuilder().uri(uri).build()
        return sendRequest(request)
    }

    fun singleResource(id: Int): MyResponse {
        val request = HttpRequest.newBuilder().uri(URI.create("${url}unknown/${id}")).build()
        return sendRequest(request)
    }



}