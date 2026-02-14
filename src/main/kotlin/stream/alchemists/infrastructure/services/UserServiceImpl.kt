package stream.alchemists.infrastructure.services

import stream.alchemists.domain.exceptions.ConflictException
import stream.alchemists.domain.exceptions.UnauthorizedException
import stream.alchemists.domain.models.AuthResponse
import stream.alchemists.domain.models.LoginRequest
import stream.alchemists.domain.models.RegisterRequest
import stream.alchemists.domain.repositories.UserRepository
import stream.alchemists.domain.services.Encryptor
import stream.alchemists.domain.services.JwtService
import stream.alchemists.domain.services.UserService

class UserServiceImpl(
    private val userRepository: UserRepository,
    private val encryptor: Encryptor,
    private val jwtService: JwtService,
) : UserService {
    override suspend fun register(request: RegisterRequest): AuthResponse {
        val existing = userRepository.findByEmail(request.email)
        if (existing != null) {
            throw ConflictException("user with email ${request.email} already exists")
        }

        val hashedPassword = encryptor.encrypt(request.password)
        val user = userRepository.create(request.name, request.email, hashedPassword)
        val token = jwtService.generateToken(user.id, user.email)
        return AuthResponse(token)
    }

    override suspend fun login(request: LoginRequest): AuthResponse {
        val (user, hashedPassword) = userRepository.findByEmail(request.email)
            ?: throw UnauthorizedException("invalid credentials")

        if (!encryptor.verify(request.password, hashedPassword)) {
            throw UnauthorizedException("invalid credentials")
        }

        val token = jwtService.generateToken(user.id, user.email)
        return AuthResponse(token)
    }
}
