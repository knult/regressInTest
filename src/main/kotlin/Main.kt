import com.google.gson.Gson


fun main() {
    fun checkExpectedValue(val_name: String, actual_value: Any, expected_val: Any) {
        if (actual_value == expected_val) {
            println("Testing $val_name: pass")
        } else {
            println("Testing $val_name: fail")
            println("$actual_value != $expected_val")
        }
    }


    val request = Requests()
    val gson = Gson()
    var response: MyResponse

// подготовка тестовых данных
    val expectedSupportTag = SupportEntity.Support(
        "https://reqres.in/#support-heading",
        "To keep ReqRes free, contributions towards server costs are appreciated!"
    )
    val expectedPerPage = 6
    val expectedTotalPages = 2
    val expectedTotal = 12
    val expectedUserData =
        UserEntity.Data(3, "emma.wong@reqres.in", "Emma", "Wong", "https://reqres.in/img/faces/3-image.jpg")
    val expectedUser = UserEntity.User(expectedUserData, expectedSupportTag)
    val testPageNum = 2
    val expectedResourceData =
        ResourceEntity.Data(4, "aqua sky", 2003, "#7BC4C4", "14-4811")
    val expectedResource = ResourceEntity.Resource(expectedResourceData, expectedSupportTag)
    val testEmail = "eve.holt@reqres.in"
    val testEmailWrong = "abra-kadabra@mail.ru"
        val testPassword = "pistol"
    val expectedRegSuccess = "{\"id\":4,\"token\":\"QpwL5tke4Pnpja7X4\"}"
    val notFoundItem = 999
    val expectedRegUnsuccessEmail = "{\"error\":\"Missing email or username\"}"
    val expectedRegUnsuccessPassword = "{\"error\":\"Missing password\"}"
    val expectedRegUnsuccessEmailUndef = "{\"error\":\"Note: Only defined users succeed registration\"}"
    val patchData = mutableMapOf("new_param1" to "val1", "first_name" to "QA tester", "avatar" to "new_avatar")
    val putData = mutableMapOf("new_param1" to "val1", "first_name" to "QA tester", "avatar" to "new_avatar")
    val patchId = 4
    val putId = 5
    val delId = 6


    println("Start tests")

    println("Method GET: LIST USERS")
    response = request.listUsers()
    val listUsersObj1 = gson.fromJson(response.body, ListEntity.ListUsers::class.java)
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    checkExpectedValue("tag page", listUsersObj1.page, 1)
    checkExpectedValue("tag per_page", listUsersObj1.per_page, expectedPerPage)
    checkExpectedValue("tag total", listUsersObj1.total, expectedTotal)
    checkExpectedValue("tag total_pages", listUsersObj1.total_pages, expectedTotalPages)
    checkExpectedValue("tag support", listUsersObj1.support, expectedSupportTag)
    checkExpectedValue("selective user check", listUsersObj1.data[expectedUserData.id - 1], expectedUserData)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method GET: LIST USERS page=$testPageNum")
    response = request.listUsers(testPageNum)
    val listUsersObj2 = gson.fromJson(response.body, ListEntity.ListUsers::class.java)
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    checkExpectedValue("tag page", listUsersObj2.page, testPageNum)
    checkExpectedValue("tag per_page", listUsersObj2.per_page, expectedPerPage)
    checkExpectedValue("tag total", listUsersObj2.total, expectedTotal)
    checkExpectedValue("tag total_pages", listUsersObj2.total_pages, expectedTotalPages)
    checkExpectedValue("tag support", listUsersObj2.support, expectedSupportTag)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method GET: SINGLE USER")
    response = request.singleUser(expectedUser.data.id)
    val userObj = gson.fromJson(response.body, UserEntity.User::class.java)
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    checkExpectedValue("tag support", userObj.support, expectedSupportTag)
    checkExpectedValue("tag data", userObj.data, expectedUser.data)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method GET: LIST <RESOURCE>")
    response = request.listResource()
    val listResourceObj1 = gson.fromJson(response.body, ListEntity.ListResource::class.java)
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    checkExpectedValue("tag page", listResourceObj1.page, 1)
    checkExpectedValue("tag per_page", listResourceObj1.per_page, expectedPerPage)
    checkExpectedValue("tag total", listResourceObj1.total, expectedTotal)
    checkExpectedValue("tag total_pages", listResourceObj1.total_pages, expectedTotalPages)
    checkExpectedValue("tag support", listResourceObj1.support, expectedSupportTag)
    checkExpectedValue("selective resource check", listResourceObj1.data[expectedResourceData.id - 1], expectedResourceData)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")


    println("Method GET: LIST <RESOURCE> page=$testPageNum")
    response = request.listResource(testPageNum)
    val listResourceObj2 = gson.fromJson(response.body, ListEntity.ListResource::class.java)
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    checkExpectedValue("tag support", listResourceObj2.support, expectedSupportTag)
    checkExpectedValue("tag page", listResourceObj2.page, testPageNum)
    checkExpectedValue("tag per_page", listResourceObj2.per_page, expectedPerPage)
    checkExpectedValue("tag total", listResourceObj1.total, expectedTotal)
    checkExpectedValue("tag total_pages", listResourceObj1.total_pages, expectedTotalPages)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method GET: SINGLE <RESOURCE>")
    response = request.singleResource(expectedResource.data.id)
    val resourceObj = gson.fromJson(response.body, ResourceEntity.Resource::class.java)
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    checkExpectedValue("tag support", resourceObj.support, expectedSupportTag)
    checkExpectedValue("tag data", resourceObj.data, expectedResource.data)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method GET: SINGLE <RESOURCE> NOT FOUND")
    response = request.singleResource(notFoundItem)
    checkExpectedValue("HTTP_CODE", response.status_code, 404)
    checkExpectedValue("empty object in body", response.body, "{}")
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method GET: SINGLE USER NOT FOUND")
    response = request.singleUser(notFoundItem)
    checkExpectedValue("HTTP_CODE", response.status_code, 404)
    checkExpectedValue("empty object in body", response.body, "{}")
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method POST: REGISTER - SUCCESSFUL")
    response = request.register(testEmail, testPassword)
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    checkExpectedValue("body", response.body, expectedRegSuccess)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method POST: REGISTER - UNSUCCESSFUL email")
    response = request.register(password = testPassword)
    checkExpectedValue("HTTP_CODE", response.status_code, 400)
    checkExpectedValue("body", response.body, expectedRegUnsuccessEmail)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method POST: REGISTER - UNSUCCESSFUL password")
    response = request.register(testEmail)
    checkExpectedValue("HTTP_CODE", response.status_code, 400)
    checkExpectedValue("body", response.body, expectedRegUnsuccessPassword)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method POST: REGISTER - UNSUCCESSFUL all")
    response = request.register()
    checkExpectedValue("body", response.body, expectedRegUnsuccessEmail)
    checkExpectedValue("HTTP_CODE", response.status_code, 400)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method POST: REGISTER - UNSUCCESSFUL wrong email")
    response = request.register(testEmailWrong, testPassword)
    checkExpectedValue("HTTP_CODE", response.status_code, 400)
    checkExpectedValue("body", response.body, expectedRegUnsuccessEmailUndef)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method PATCH: UPDATE")
    response = request.patchUser(patchId, patchData)
    val patchObj = gson.fromJson(response.body, Map::class.java)

    for ((key, value) in patchData) {
        checkExpectedValue("tag $key", patchObj[key]!!, value)
    }
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method PUT: UPDATE")
    response = request.putUser(putId, putData)
    val putObj = gson.fromJson(response.body, Map::class.java)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    for ((key, value) in putData) {
        checkExpectedValue("tag $key", putObj[key]!!, value)
    }
    checkExpectedValue("HTTP_CODE", response.status_code, 200)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")

    println("Method DELETE: DELETE")
    response = request.delUser(delId)
    checkExpectedValue("empty body", response.body, "")
    checkExpectedValue("HTTP_CODE", response.status_code, 204)
    println("Elapsed: ${response.elapsed} ms")
    println("--------------")


}
