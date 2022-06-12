package io.hugo.wallet.api.model

import java.util.UUID

data class ResourceIdentity(
    val id: UUID? = null,
    val resourceType: ResourceType = ResourceType.ACCOUNT,
)
