class UserEntity {

    data class User(
        val data: Data,
        val support: SupportEntity.Support
    )

    data class Data(
        val id: Int,
        val email: String,
        val first_name: String,
        val last_name: String,
        val avatar: String
    )


}