package io.hugo.wallet.api.model

import java.util.UUID

data class ResourceIdentity(
    val id: UUID = UUID(0, 0),
    val resourceType: ResourceType = ResourceType.ACCOUNT,
)
