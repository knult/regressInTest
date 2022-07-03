class ResourceEntity {

    data class Resource(
        val data: Data,
        val support: SupportEntity.Support
    )

    data class Data(
        val id: Int,
        val name: String,
        val year: Int,
        val color: String,
        val pantone_value: String
    )

}