package cz.applifting.humansis.model.ui

/**
 * Created by Vaclav Legat <vaclav.legat@applifting.cz>
 * @since 12. 9. 2019
 */
// TODO remove this and instead use @Ingore in DB entity
data class ProjectModel(
    val id: Int,
    val name: String,
    val numberOfHouseholds: Int,
    val completed: Boolean
)