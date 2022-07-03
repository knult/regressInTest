class ListEntity {

    data class ListUsers(
        val page: Int,
        val per_page: Int,
        val total: Int,
        val total_pages: Int,
        val data: Array<UserEntity.Data>,
        val support: SupportEntity.Support
        )

    data class ListResource(
        val page: Int,
        val per_page: Int,
        val total: Int,
        val total_pages: Int,
        val data: Array<ResourceEntity.Data>,
        val support: SupportEntity.Support
        )
}