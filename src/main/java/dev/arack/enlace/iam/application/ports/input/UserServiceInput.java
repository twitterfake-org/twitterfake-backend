package dev.arack.enlace.iam.application.ports.input;

import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;

import java.util.List;

public interface UserServiceInput {
    /**
     * Obtiene todos los usuarios.
     *
     * @return Lista de respuestas con los datos de todos los usuarios.
     */
    List<UserResponse> getAllUsers();

    /**
     * Obtiene un usuario por nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a obtener.
     * @return Respuesta con los datos del usuario.
     */
    UserResponse getUserByUsername(String username);

    /**
     * Actualiza un usuario existente.
     *
     * @param username Nombre de usuario del usuario a actualizar.
     * @param userRequest Solicitud con los nuevos datos del usuario.
     */
    void updateUser(String username, UserRequest userRequest);

    /**
     * Elimina un usuario por nombre de usuario.
     *
     * @param username Nombre de usuario del usuario a eliminar.
     */
    void deleteUser(String username);
}
