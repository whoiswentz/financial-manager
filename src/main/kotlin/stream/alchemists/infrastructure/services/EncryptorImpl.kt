package stream.alchemists.infrastructure.services

import io.ktor.util.*
import stream.alchemists.AppConfiguration
import stream.alchemists.domain.services.Encryptor
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class EncryptorImpl : Encryptor {
    private val secretKey = AppConfiguration.secretKey
    private val algorithm = AppConfiguration.algorithm
    private val iterations = AppConfiguration.iterations
    private val keyLength = AppConfiguration.keyLength

    override fun encrypt(value: String): String {
        val salt = ByteArray(32)
        SecureRandom().nextBytes(salt)

        val combinedSalt = "$salt$secretKey".toByteArray()

        val factory = SecretKeyFactory.getInstance(algorithm)
        val spec = PBEKeySpec(value.toCharArray(), combinedSalt, iterations, keyLength)
        val key = factory.generateSecret(spec)
        val hash = key.encoded

        return hex(hash)
    }
}