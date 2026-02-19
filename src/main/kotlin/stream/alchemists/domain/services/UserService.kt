package stream.alchemists.domain.services

import stream.alchemists.domain.models.AuthResponse
import stream.alchemists.domain.models.LoginRequest
import stream.alchemists.domain.models.RegisterRequest

interface UserService {
    suspend fun register(request: RegisterRequest): AuthResponse
    suspend fun login(request: LoginRequest): AuthResponse
}
